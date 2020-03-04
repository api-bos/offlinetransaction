package com.bos.offlinetransaction.repository;

import com.bos.offlinetransaction.model.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Integer> {
}
