package com.sparta.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @Id
    private String merchantUid;
    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Payment(String merchantUid, Long amount) {
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public void expire() {
        if(this.status == PaymentStatus.PENDING) {
            this.status = PaymentStatus.FAILED;
        }
    }

    public void approve() {
        if (this.status != PaymentStatus.PENDING) throw new IllegalStateException("PENDING 상태만 승인 가능");
        this.status = PaymentStatus.APPROVED;
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }
}

