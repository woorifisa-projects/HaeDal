package com.haedal.backend.product.model;

import com.haedal.backend.profile.model.User;
import lombok.Getter;

import javax.persistence.*;

@Getter
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





}