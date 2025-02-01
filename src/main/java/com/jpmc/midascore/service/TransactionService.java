package com.jpmc.midascore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);

        if (sender == null || recipient == null) {
            logger.warn("Invalid transaction");
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Sender does not have enough balance");
            return;
        }

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        TransactionRecord transactionRecord = new TransactionRecord(transaction.getAmount(), sender, recipient);
        transactionRecordRepository.save(transactionRecord);

        userRepository.save(sender);
        userRepository.save(recipient);

        logger.info("Transaction completed for sender: {}, recipient: {}", sender.getId(), recipient.getId());

        logger.info("Transaction processed successfully");
    }
}
