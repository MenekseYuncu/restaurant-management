package org.violet.restaurantmanagement.payment.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.violet.restaurantmanagement.common.repository.entity.BaseEntity;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rm_payment_item")
public class PaymentItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Column(name = "payment_id", insertable = false, updatable = false)
    private String paymentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItemEntity orderItem;

    @Column(name = "order_item_id", insertable = false, updatable = false)
    private String orderItemId;

    @Column(name = "amount", precision = 50, scale = 8, nullable = false)
    private BigDecimal amount;

    public static PaymentItemEntity of(PaymentEntity payment, OrderItemEntity orderItem) {
        BigDecimal amount = orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        return PaymentItemEntity.builder()
                .payment(payment)
                .orderItem(orderItem)
                .amount(amount)
                .build();
    }

}
