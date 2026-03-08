package com.sparta.payment.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findAllByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime createdAt);
}
