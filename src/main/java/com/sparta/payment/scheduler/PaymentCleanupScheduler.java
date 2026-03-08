package com.sparta.payment.scheduler;

import com.sparta.payment.entity.Payment;
import com.sparta.payment.entity.PaymentRepository;
import com.sparta.payment.entity.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCleanupScheduler {

    private final PaymentRepository paymentRepository;

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void cleanupAbandonedPayments() {
        log.info("유령 결제건 처리 스케줄러 실행");

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);

        List<Payment> abandonedPayments = paymentRepository.findAllByStatusAndCreatedAtBefore(PaymentStatus.PENDING, threshold);

        if (abandonedPayments.isEmpty()) {
            log.info("유령 결제건이 없습니다");
            return;
        }

        log.info("{}건의 유령 결제 발견", abandonedPayments.size());

        for (Payment payment : abandonedPayments) {
            payment.expire();
            log.info("결제 만료 처리: {}", payment.getMerchantUid());
        }

        log.info("유령 결제건 처리 완료");
    }
}
