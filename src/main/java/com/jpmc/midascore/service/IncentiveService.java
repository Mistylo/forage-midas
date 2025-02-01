package com.jpmc.midascore.service;

import com.jpmc.midascore.api.Incentive;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    public void processIncentive(TransactionRecord transaction) {
        String url = "http://localhost:8080/incentive";
        Incentive incentive = restTemplate.postForObject(url, transaction, Incentive.class);

        if (incentive != null && transaction.getRecipient() != null) {
            UserRecord recipient = transaction.getRecipient();
            recipient.setBalance(recipient.getBalance() + incentive.getAmount());
            userRepository.save(recipient);
        }
    }
}
