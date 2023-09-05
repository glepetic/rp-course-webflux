package com.rp.orderservice.infrastructure.repository.dao;

import com.rp.orderservice.infrastructure.dto.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEntityDao extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByUserId(long userId);
}
