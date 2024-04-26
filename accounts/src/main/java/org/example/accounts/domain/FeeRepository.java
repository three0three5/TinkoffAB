package org.example.accounts.domain;

import org.example.accounts.domain.entity.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface FeeRepository extends JpaRepository<FeeEntity, String> {
    @Query(value =
            """
            INSERT INTO fee (id, value, updated_at)
            VALUES (:id, :value, CURRENT_TIMESTAMP)
            ON CONFLICT (id) DO
            UPDATE SET value=:value, updated_at=CURRENT_TIMESTAMP;
            """, nativeQuery = true)
    @Modifying
    @Transactional
    void createOrUpdate(BigDecimal value, String id);
}
