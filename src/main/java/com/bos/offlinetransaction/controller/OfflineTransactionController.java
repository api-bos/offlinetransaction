package com.bos.offlinetransaction.controller;

import bca.bit.proj.library.base.ResultEntity;
import com.bos.offlinetransaction.model.RequestData;
import com.bos.offlinetransaction.service.OfflineTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/bos", produces = "application/json")
@CrossOrigin(origins = {"*"})
public class OfflineTransactionController {
    @Autowired
    OfflineTransactionService g_offlineTransactionService;

    @PostMapping(value = "/offlineTransaction", consumes = "application/json")
    public ResultEntity addOfflineTransaction(@RequestBody RequestData p_requestData){
        return g_offlineTransactionService.addOfflineTransaction(p_requestData);
    }
}
