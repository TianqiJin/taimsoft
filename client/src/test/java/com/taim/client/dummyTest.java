package com.taim.client;

import org.junit.Test;

/**
 * Created by jiawei.liu on 11/25/17.
 */
public class dummyTest {
    @Test
    public void testIt()throws Exception{
        ProductClient client = new ProductClient();

        System.out.println("!!!!!!!"+client.getList().size());
    }

}
