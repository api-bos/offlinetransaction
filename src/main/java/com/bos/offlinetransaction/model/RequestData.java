package com.bos.offlinetransaction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class RequestData {
    private int id_seller;
    private double total_payment;
    private ArrayList<ProductPurchased> product;
}
