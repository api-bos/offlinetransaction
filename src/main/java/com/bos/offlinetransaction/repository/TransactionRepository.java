package com.bos.offlinetransaction.repository;

import com.bos.offlinetransaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT id_transaction FROM transaction WHERE order_time = :order_time", nativeQuery = true)
    int getTransactionIdByOrderTime(@Param("order_time") Timestamp order_time);
}
