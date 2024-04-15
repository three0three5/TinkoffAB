package org.example.accounts.domain;

import org.example.accounts.domain.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByAccount_AccountNumberOrderByCreatedAtDesc(int accountNumber);
}
