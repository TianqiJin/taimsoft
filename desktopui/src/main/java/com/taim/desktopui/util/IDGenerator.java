package com.taim.desktopui.util;

import java.util.Date;
import java.util.Random;

public class IDGenerator {
    private static Random dice = null;

    public static synchronized String createQuotationID(){
        return getUniqueIdentifier("Q");
    }

    public static synchronized String createContractID(){
        return getUniqueIdentifier("C");
    }

    public static synchronized String createInvoiceID(){
        return getUniqueIdentifier("I");
    }

    public static synchronized String createPaymentID(){
        return getUniqueIdentifier("P");
    }

    private static synchronized String getUniqueIdentifier(String type) {
        if (dice == null) {
            dice = new Random();
        }
        int random = Math.abs(dice.nextInt());

        if (random == Integer.MIN_VALUE) {
            random = 42;
        }

        return type + "-" + String.format("%1$ty%1$tm%1$td-%1$tH%1$tM-%2$s", new Date(), Integer.toString(random, 32));
    }
}
