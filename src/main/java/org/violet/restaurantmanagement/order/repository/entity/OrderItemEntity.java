package org.violet.restaurantmanagement.order.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.violet.restaurantmanagement.common.repository.entity.BaseEntity;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rm_order_item")
public class OrderItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private OrderEntity order;

    @Column(name = "order_id")
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductEntity product;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "price")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_item_status", length = 10)
    private OrderItemStatus status;

}
