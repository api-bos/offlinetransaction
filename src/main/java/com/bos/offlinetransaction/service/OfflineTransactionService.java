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
import java.util.ArrayList;

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
        int l_stock;
        int l_quantity;
        int l_productId;
        ArrayList<String> l_arrayErrorMessage = new ArrayList<>();
        boolean l_transactionOccured = false;

        try{
            for (int i = 0; i<p_requestData.getProduct().size(); i++){
                l_productId = p_requestData.getProduct().get(i).getId_product();
                l_stock = g_productRepository.getStockByProductId(l_productId);
                l_quantity = p_requestData.getProduct().get(i).getQuantity();

                if (l_stock < l_quantity){
                    String tmp_productName = g_productRepository.getProductNameByProductId(l_productId);
                    String tmp_errorMessage = "Stok produk " + tmp_productName + " tidak mencukupi";

                    l_arrayErrorMessage.add(tmp_errorMessage);

                    l_transactionOccured = true;
                }
            }

            if (!l_transactionOccured){
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
                    l_productId = p_requestData.getProduct().get(i).getId_product();

                    //Decrease stock
                    l_quantity = p_requestData.getProduct().get(i).getQuantity();
                    l_stock = g_productRepository.getStockByProductId(l_productId);
                    int tmp_currentStock = l_stock - l_quantity;
                    g_productRepository.updateStockByProductId(tmp_currentStock, l_productId);

                    //Save data to transaction_detail table
                    TransactionDetail tmp_transactionDetail = new TransactionDetail();
                    tmp_transactionDetail.setId_transaction(tmp_transactionId);
                    tmp_transactionDetail.setId_product(l_productId);
                    tmp_transactionDetail.setQuantity(l_quantity);
                    tmp_transactionDetail.setSell_price(p_requestData.getProduct().get(i).getSell_price());
                    g_transactionDetailRepository.save(tmp_transactionDetail);

                    l_output = new ResultEntity("Y", ErrorCode.B000);
                }

            }else {
                l_output = new ResultEntity(l_arrayErrorMessage, ErrorCode.B999);
            }

        }catch (Exception e){
            e.printStackTrace();
            l_output = new ResultEntity(e.toString(), ErrorCode.B999);
        }

        return l_output;
    }
}
