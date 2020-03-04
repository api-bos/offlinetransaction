package com.bos.offlinetransaction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductPurchased {
    private int id_product;
    private int quantity;
    private double sell_price;
}
