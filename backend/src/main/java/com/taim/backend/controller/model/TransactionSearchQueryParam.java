package com.taim.backend.controller.model;

import com.taim.model.Transaction;
import org.joda.time.DateTime;

public class TransactionSearchQueryParam {
    private Transaction.TransactionType type;
    private Transaction.TransactionCategory category;
    private DateTime fromDate;
    private DateTime toDate;

}
