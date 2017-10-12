package com.taimsoft.licensetools.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * XmlProperty is an annotation used to map specified fields in LicenseFile.FileAttributes
 * and LicenseFile.LicenseAttributes to LicenseXml.License.
 * The value of the key in LicenseXml.License will be the annotated value of name
 * The key's value in LicenseXml.License is the related value of the annotated field
 * Created by tjin on 2016-12-07.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlProperty {
    String name() default "";
}
