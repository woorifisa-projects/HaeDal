package com.haedal.backend.product.model;

import com.haedal.backend.profile.model.Account;
import com.haedal.backend.profile.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.security.auth.login.AccountException;

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
    @JoinColumn(name = "account_id")
    private Account account;





}