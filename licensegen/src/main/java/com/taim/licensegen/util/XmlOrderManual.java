package com.taim.licensegen.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * XmlOrderManual is used to represent the order of input for generating license signature.
 * This annotation should only be used when the order of the signature input is not the
 * same as the order of xml elements in the license file. One example is license-trial.dat with
 * ela-required field. When ela-required is introduced in the license-trial.dat, the ela-required
 * is placed before cov-4 field in the license-trial.dat, however, it appears as the last element
 * when generating the signature for the trial license.
 * Created by tjin on 2017-03-14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XmlOrderManual {
    //order for the current class
    String[] order();
    //order for the parent class
    String[] orderSup();
}
