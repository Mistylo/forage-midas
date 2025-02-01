package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.persistence.spi.PersistenceProviderResolverHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaTransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTransactionListener.class);

    private final TransactionService transactionService;

    @Autowired
    public KafkaTransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(Transaction transaction) {
        try {
            logger.info("Received Transaction: {}", transaction);
            transactionService.processTransaction(transaction);
            logger.info("Processed Transaction: {}", transaction);
        } catch (Exception e) {
            logger.error("Error processing transaction: {}", e.getMessage());
        }
    }
}
