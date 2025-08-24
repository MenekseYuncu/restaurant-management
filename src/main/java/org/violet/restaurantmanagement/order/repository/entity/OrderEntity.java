package org.violet.restaurantmanagement.order.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.violet.restaurantmanagement.common.repository.entity.BaseEntity;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.order.exceptions.OrderUpdateNotAllowedException;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rm_order")
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "dining_table_merge_id")
    private String mergeId;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItemEntity> items = new ArrayList<>();

    public void cancel() {
        this.status = OrderStatus.CANCELED;
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }

    public boolean allItemsPaid() {
        return getItems() != null && !getItems().isEmpty()
                && getItems().stream().allMatch(i -> i.getStatus() == OrderItemStatus.PAID);
    }

    public void inProgress() {
        this.status = OrderStatus.IN_PROGRESS;
    }

    public boolean isNotUpdatable() {
        return this.status == OrderStatus.CANCELED || this.status == OrderStatus.COMPLETED;
    }

    public BigDecimal getTotalAmount() {
        if (this.totalAmount == null) {
            return BigDecimal.ZERO;
        }
        return this.totalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void addItems(List<OrderItemEntity> newItems, List<DiningTableEntity> tables) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        tables.forEach(DiningTableEntity::takingOrders);
        this.items.addAll(newItems);
        this.totalAmount = calculateTotalPrice(this.items);
    }

    public BigDecimal calculateTotalPrice(final List<OrderItemEntity> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void updateItems(List<OrderItemEntity> newItems) {
        if (isNotUpdatable()) {
            throw new OrderUpdateNotAllowedException();
        }

        if (newItems == null || newItems.isEmpty()) {
            throw new ProductNotFoundException();
        }
        this.items.addAll(newItems);
        this.inProgress();
        this.totalAmount = calculateTotalPrice(this.items);
    }

    public void removeItem(String productId, int quantity) {
        OrderItemEntity item = this.items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(ProductNotFoundException::new);

        if (item.getQuantity() <= quantity) {
            this.items.remove(item);
        } else {
            item.setQuantity(item.getQuantity() - quantity);
        }

        this.totalAmount = calculateTotalPrice(this.items);

        if (this.items.isEmpty()) {
            this.cancel();
        }
    }

}
