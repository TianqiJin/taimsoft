package com.taim.dto;

import com.taim.dto.basedtos.UserBaseModelDTO;

import java.util.ArrayList;
import java.util.List;

public class VendorDTO extends UserBaseModelDTO {
    private List<TransactionDTO> transactionList;

    public VendorDTO(){
        transactionList = new ArrayList<>();
    }

    public List<TransactionDTO> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionDTO> transactionList) {
        this.transactionList = transactionList;
    }
}
