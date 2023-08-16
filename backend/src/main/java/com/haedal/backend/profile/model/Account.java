package com.haedal.backend.profile.model;

import com.haedal.backend.product.model.Subscribe;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AccountId; // 계좌 번호 입력

    @Column(name = "asset")
    private Long asset; // 자산

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account")
    private List<Subscribe> subscribes;


}
