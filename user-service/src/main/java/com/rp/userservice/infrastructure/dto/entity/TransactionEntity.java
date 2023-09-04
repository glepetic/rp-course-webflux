package com.rp.userservice.infrastructure.dto.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Builder
@Table("user_transaction")
public record TransactionEntity(@Id long id,
                                long userId,
                                int amount,
                                @Column("transaction_date") LocalDateTime dateTime) {

}
