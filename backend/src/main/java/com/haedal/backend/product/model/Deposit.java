package com.haedal.backend.product.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

//TODO : 상속 관련 설정
@DiscriminatorValue("D")
@Entity
@Table(name = "Deposit")
public class Deposit extends Product {


}
