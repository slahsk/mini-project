package com.kkulkkeog.order.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TB_ORDER_MENU")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"order"})
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOrderMenu")
    private Long orderMenuNo;

    @Column(nullable = false)
    private long menuNo;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_no")
    private Order order;

}
