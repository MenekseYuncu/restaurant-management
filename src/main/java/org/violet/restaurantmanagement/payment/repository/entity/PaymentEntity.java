package org.violet.restaurantmanagement.payment.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.violet.restaurantmanagement.common.repository.entity.BaseEntity;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;
import org.violet.restaurantmanagement.payment.model.PaymentStatus;
import org.violet.restaurantmanagement.payment.model.PaymentType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rm_payment")
public class PaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "order_id", insertable = false, updatable = false)
    private String orderId;

    @Column(name = "amount", precision = 50, scale = 8, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PaymentStatus status;

    @Column(name = "payment_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentItemEntity> paymentItems;

    public static PaymentEntity buildPayment(OrderEntity order) {
        return PaymentEntity.builder()
                .order(order)
                .totalAmount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .paymentType(PaymentType.CASH)
                .build();
    }

    public boolean isCompleted() {
        return this.status == PaymentStatus.COMPLETED;
    }

    public boolean hasPaidItemId(String orderItemId) {
        if (paymentItems == null) return false;
        return paymentItems.stream()
                .anyMatch(pi -> Objects.equals(pi.getOrderItemId(), orderItemId));
    }

    /** OrderItem için ödeme satırı oluşturur ve toplamı günceller. */
    public void addPaymentFor(OrderItemEntity orderItem) {
        if (this.paymentItems == null) this.paymentItems = new ArrayList<>();
        PaymentItemEntity item = PaymentItemEntity.of(this, orderItem);
        this.paymentItems.add(item);
        this.totalAmount = this.totalAmount.add(item.getAmount());
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
    }

}
