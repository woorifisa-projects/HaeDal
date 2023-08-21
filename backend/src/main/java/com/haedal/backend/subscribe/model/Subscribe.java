package com.haedal.backend.subscribe.model;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Subscribe")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private Long subscribeId;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    //입력받을 시작 금액
    @Column(name = "user_start_money")
    private Long startMoney;

    //투자 상품에 대한 현재 금액
    @Column(name = "user_present_money")
    private Long presentMoney;

    @Column(name = "user_subscribe_date")
    private LocalDate subscribeDate;

    public Subscribe(User user, Product product, Long startMoney, Long presentMoney, LocalDate subscribeDate) {
        this.user = user;
        this.product = product;
        this.startMoney = startMoney;
        this.presentMoney = presentMoney;
        this.subscribeDate = subscribeDate;
    }
}