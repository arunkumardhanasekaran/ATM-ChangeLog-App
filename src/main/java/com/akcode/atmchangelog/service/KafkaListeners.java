package com.akcode.atmchangelog.service;

import com.akcode.atmchangelog.entity.ChangeLog;
import com.akcode.atmchangelog.repository.ChangeLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Slf4j
@Component
public class KafkaListeners {

    @Autowired
    private ChangeLogRepository changeLogRepository;

    @KafkaListener(topics = "account-changes", groupId = "group_id")
    public void listen(String message) {
        log.info("Kafka message: {}", message);
        String[] values = message.split("::");
        ChangeLog changeLog = new ChangeLog();
        changeLog.setChangeType(values[0].trim());
        changeLog.setAccountId(Long.parseLong(values[1].trim()));
        changeLog.setAmount(new BigDecimal(values[2].trim()));
        changeLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
        changeLogRepository.save(changeLog);
        log.debug("Transaction saved");
    }
}
