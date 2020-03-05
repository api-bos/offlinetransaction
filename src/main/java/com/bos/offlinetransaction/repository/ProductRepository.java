package com.bos.offlinetransaction.repository;

import com.bos.offlinetransaction.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE product SET stock = :stock WHERE id_product = :id_product", nativeQuery = true)
    void updateStockByProductId(@Param("stock") int stock, @Param("id_product") int id_product);

    @Query(value = "SELECT stock FROM product WHERE id_product = :id_product", nativeQuery = true)
    int getStockByProductId(@Param("id_product") int id_product);

    @Query(value = "SELECT product_name FROM product WHERE id_product = :id_product", nativeQuery = true)
    String getProductNameByProductId(@Param("id_product") int id_product);
}
