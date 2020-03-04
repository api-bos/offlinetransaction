package com.bos.offlinetransaction.service;

import bca.bit.proj.library.base.ResultEntity;
import bca.bit.proj.library.enums.ErrorCode;
import com.bos.offlinetransaction.model.RequestData;
import com.bos.offlinetransaction.model.Transaction;
import com.bos.offlinetransaction.model.TransactionDetail;
import com.bos.offlinetransaction.repository.ProductRepository;
import com.bos.offlinetransaction.repository.TransactionDetailRepository;
import com.bos.offlinetransaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class OfflineTransactionService {
    @Autowired
    TransactionRepository g_transactionRepository;

    @Autowired
    TransactionDetailRepository g_transactionDetailRepository;

    @Autowired
    ProductRepository g_productRepository;

    public ResultEntity addOfflineTransaction(RequestData p_requestData){
        ResultEntity l_output = null;

        try{
            //Save data to transaction table
            Transaction tmp_transaction = new Transaction();
            Timestamp l_timestamp = new Timestamp(System.currentTimeMillis());
            tmp_transaction.setOrder_time(l_timestamp);
            tmp_transaction.setId_seller(p_requestData.getId_seller());
            tmp_transaction.setStatus(3);
            tmp_transaction.setTotal_payment(p_requestData.getTotal_payment());
            g_transactionRepository.save(tmp_transaction);

            //Get id_transaction from transaction table
            int tmp_transactionId = g_transactionRepository.getTransactionIdByOrderTime(l_timestamp);

            //Save data as much as product purchased to transaction_detail table
            for (int i = 0; i<p_requestData.getProduct().size(); i++){
                int l_productId = p_requestData.getProduct().get(i).getId_product();

                //Decrease stock
                int tmp_quantity = p_requestData.getProduct().get(i).getQuantity();
                int tmp_stock = g_productRepository.getStockByProductId(l_productId);
                System.out.println(tmp_stock);
                int tmp_currentStock = tmp_stock - tmp_quantity;
                g_productRepository.updateStockByProductId(tmp_currentStock, l_productId);

                //Save data to transaction_detail table
                TransactionDetail tmp_transactionDetail = new TransactionDetail();
                tmp_transactionDetail.setId_transaction(tmp_transactionId);
                tmp_transactionDetail.setId_product(l_productId);
                tmp_transactionDetail.setQuantity(p_requestData.getProduct().get(i).getQuantity());
                tmp_transactionDetail.setSell_price(p_requestData.getProduct().get(i).getSell_price());
                g_transactionDetailRepository.save(tmp_transactionDetail);

                l_output = new ResultEntity("Y", ErrorCode.B000);
            }

        }catch (Exception e){
            e.printStackTrace();
            l_output = new ResultEntity(e.toString(), ErrorCode.B999);
        }

        return l_output;
    }
}
