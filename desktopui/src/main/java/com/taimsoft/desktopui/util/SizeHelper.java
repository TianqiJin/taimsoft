package com.taimsoft.desktopui.util;

import com.taim.dto.ProductDTO;

/**
 * Created by jiawei.liu on 10/5/17.
 */
public class SizeHelper {

    public static String getSizeString(ProductDTO productDTO){
        return productDTO.getLength()+"x"+productDTO.getWidth()+"x"+productDTO.getHeight();
    }

    public static double getSizeValue(ProductDTO productDTO){
        return productDTO.getLength()*productDTO.getWidth()*productDTO.getHeight();
    }
}

