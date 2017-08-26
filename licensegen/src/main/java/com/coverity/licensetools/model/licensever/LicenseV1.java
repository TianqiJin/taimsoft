package com.coverity.licensetools.model.licensever;

import com.coverity.licensetools.model.LicenseFile;
import com.coverity.licensetools.model.LicenseXML;
import org.eclipse.persistence.jaxb.JAXBContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.coverity.licensetools.util.XmlProperty;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by tjin on 2017-02-09.
 */
@XmlRootElement(name = "license")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"totalSeconds", "options", "signature"})
public class LicenseV1 extends LicenseBase{

    public static class LicenseV1Factory implements LicenseVersionFactory<LicenseV1>{
        @Override
        public LicenseV1 create(Object o) throws LicenseFile.LicenseException, IllegalAccessException {
            LicenseXML.LicenseAttrCollection attrCollection = (LicenseXML.LicenseAttrCollection) o;
            LicenseV1 license = new LicenseV1();
            LicenseBase.mapFieldsFromLicenseAttrCollection(license, LicenseV1.class, attrCollection);
            List<String> fieldAnnotatedList = new ArrayList<>();
            Class licenseV1Class = LicenseV1.class;
            while(!licenseV1Class.isAssignableFrom(Object.class)){
                for(Field field : licenseV1Class.getDeclaredFields()){
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(XmlProperty.class)){
                        XmlProperty xmlProperty = field.getAnnotation(XmlProperty.class);
                        fieldAnnotatedList.add(xmlProperty.name());
                    }
                }
                licenseV1Class = licenseV1Class.getSuperclass();
            }
            attrCollection.getAttributes().keySet().forEach(k -> {
                //All fields in LicenseBase and the "trial" field should not be existed in the Option list
                if(!fieldAnnotatedList.contains(k))
                    license.getOptions().add(new Option(k, attrCollection.getAttributes().get(k)));
            });

            return license;
        }
    }

    /**
     * Manually construct XML Element for Value field in LicenseV1.Option class.
     */
    public static class OptionAdapter extends XmlAdapter<Element, Object>{

        @Override
        public Object unmarshal(Element v) throws Exception {
            if(v == null)
                return null;
            return v.getTextContent();
        }

        @Override
        public Element marshal(Object object) throws Exception {
            if(object == null)
                return null;
            Class type = object.getClass();
            JAXBElement jaxbElement = new JAXBElement(new QName("value"), object.getClass(), object);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Marshaller marshaller = JAXBContext.newInstance(type).createMarshaller();
            marshaller.marshal(jaxbElement, document);
            return document.getDocumentElement();
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(propOrder = {"name", "value"})
    public static class Option{
        @XmlElement
        private String name;
        @XmlJavaTypeAdapter(OptionAdapter.class)
        @XmlAnyElement
        private Object value;

        public Option(){}

        public Option(String name, Object value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    @XmlProperty(name = "taim-0")
    @XmlElement(name = "taim-0")
    private Integer totalSeconds;
    @XmlElement(name = "taim-1")
    private String signature;
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    private Set<Option> options;

    public LicenseV1(){
        options = new TreeSet<>(new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public Integer getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(Integer totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
