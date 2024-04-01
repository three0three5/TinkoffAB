package org.example.accounts.domain;

import org.example.accounts.domain.entity.OutboxCustomerUpdateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxCustomerUpdateRepository extends JpaRepository<OutboxCustomerUpdateEntity, Long> {
    @Query(value = """
            SELECT * FROM outbox_updates
            ORDER BY created_at DESC
            FOR UPDATE SKIP LOCKED
            LIMIT :batchSize""", nativeQuery = true)
    List<OutboxCustomerUpdateEntity> fetchMessages(int batchSize);
}
