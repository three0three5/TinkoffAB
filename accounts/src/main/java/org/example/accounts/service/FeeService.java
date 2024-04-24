package org.example.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.domain.FeeRepository;
import org.example.accounts.domain.entity.FeeEntity;
import org.example.accounts.dto.FeeDto;
import org.example.accounts.dto.messages.FeeUpdateMessage;
import org.example.accounts.utils.Constants;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeeService {
    private final FeeRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CacheManager cacheManager;

    @Transactional
    public FeeDto update(FeeDto feeDto) {
        log.info("update with {}", feeDto);
        repository.createOrUpdate(feeDto.getFee(), Constants.DEFAULT_FEE_ENTITY_ID);
        sendToKafka(new FeeUpdateMessage());
        return feeDto;
    }

    @Cacheable(value = "fee")
    public BigDecimal getFee() {
        return repository.findById(Constants.DEFAULT_FEE_ENTITY_ID).map(FeeEntity::getValue).orElse(BigDecimal.ZERO);
    }

    @KafkaListener(topics = "#{'${spring.kafka.template.default-topic}'.split(',')}")
    public void consumeUpdateMessage(FeeUpdateMessage message) {
        log.info("Received message: {}", message);
        if (!FeeUpdateMessage.UpdateAction.UPDATE_FEE.equals(message.getAction())) return;
        BigDecimal result = getFee();
        cacheManager.getCache("fee").clear();
        log.info("put {} in cache", result);
    }

    @Transactional("kafkaTransactionManager")
    public void sendToKafka(Object msg) {
        kafkaTemplate.sendDefault(msg);
    }
}
