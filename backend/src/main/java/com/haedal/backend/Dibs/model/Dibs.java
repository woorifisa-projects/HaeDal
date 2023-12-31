package com.haedal.backend.Dibs.model;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.product.model.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name= "dibs")
public class Dibs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="dibs_id")
    private long dibsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="dibs_date")
    private LocalDate dibsDate;

    @Builder
    public Dibs(User user, Product product, LocalDate dibsDate) {
        this.user = user;
        this.product = product;
        this.dibsDate = dibsDate;
    }

}
