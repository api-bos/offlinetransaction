package com.bos.offlinetransaction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transaction_detail")
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_transaction_detail;
    private int id_transaction;
    private int id_product;
    private int quantity;
    private double sell_price;

}
