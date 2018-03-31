package com.taim.model.basemodels;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class TransactionBaseModel extends BaseModel {
    @Column(name = "present_id")
    private String presentId;

    public TransactionBaseModel(){}

    public String getPresentId() {
        return presentId;
    }

    public void setPresentId(String presentId) {
        this.presentId = presentId;
    }
}
