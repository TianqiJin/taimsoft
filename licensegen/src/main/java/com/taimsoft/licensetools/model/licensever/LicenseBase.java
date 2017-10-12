package com.taimsoft.licensetools.model.licensever;

import com.taimsoft.licensetools.model.LicenseFile;
import com.taimsoft.licensetools.model.LicenseXML;
import com.taimsoft.licensetools.util.XmlProperty;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.taimsoft.licensetools.util.XmlOrderManual;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjin on 2017-02-09.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"version", "product", "validUntil", "customer", "project"})
@XmlJavaTypeAdapter(License.LicenseAdaptor.class)
public class LicenseBase {

    @XmlProperty(name = "max-version")
    @XmlElement(name = "version")
    private String version;
    @XmlProperty(name = "product")
    @XmlElement(name = "product")
    private String product;
    @XmlElement(name = "valid-until")
    @XmlProperty(name = "valid-until")
    private String validUntil;
    @XmlProperty(name = "customer")
    @XmlElement(name = "customer")
    private String customer;
    @XmlProperty(name = "project")
    @XmlElement(name = "project")
    private String project;
    @XmlProperty(name = "comment")
    @XmlTransient
    private String comment;

    public LicenseBase(){}

    /**
     * Map each key-pair LicenseXML.LicenseAttrCollection to the specified fields of Class c
     * It will loop through every field in fields JSON array and find the corresponding field in Class c.
     * If the field of Class c is annotated by XmlProperty, then this function will look for
     * the key name as the value of XmlProperty and set the field value
     * @param object
     * @param c
     * @param attrCollection
     * @param fields
     * @throws LicenseFile.LicenseException
     * @throws IllegalAccessException
     */
    public static void mapFieldsFromLicenseAttrCollection(Object object, Class c, LicenseXML.LicenseAttrCollection attrCollection, JSONArray fields) throws LicenseFile.LicenseException, IllegalAccessException {
        //For Legacy Licenses
        if(fields != null){
            for(Object o: fields){
                Class cReal = c;
                boolean flag = false;
                while(!cReal.isAssignableFrom(Object.class)){
                    for(Field field : cReal.getDeclaredFields()){
                        field.setAccessible(true);
                        if(field.isAnnotationPresent(XmlProperty.class)){
                            XmlProperty xmlProperty = field.getAnnotation(XmlProperty.class);
                            if(xmlProperty.name().equals(String.valueOf(o))) {
                                field.set(object, attrCollection.getAttributes().get(xmlProperty.name()));
                                flag = true;
                                break;
                            }
                        }
                    }
                    cReal = cReal.getSuperclass();
                }
                if(!flag)
                    throw new LicenseFile.LicenseException("Unable to find " + String.valueOf(o) + " in the LicenseAttributeCollection");
            }
        }//For V8 License
        else{
            while(true){
                for(Field field : c.getDeclaredFields()){
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(XmlProperty.class)){
                        XmlProperty xmlProperty = field.getAnnotation(XmlProperty.class);
                        field.set(object, attrCollection.getAttributes().get(xmlProperty.name()));
                    }
                }
                c = c.getSuperclass();
                if(c.isAssignableFrom(Object.class))
                    break;
            }
        }
    }

    /**
     *Map each key-pair LicenseXML.LicenseAttrCollection to the specified fields of Class c
     * If the field of Class c is annotated by XmlProperty, then this function will look for
     * the key name as the value of XmlProperty and set the field value if such key is found
     * @param object
     * @param c
     * @param attrCollection
     * @throws LicenseFile.LicenseException
     * @throws IllegalAccessException
     */
    public static void mapFieldsFromLicenseAttrCollection(Object object, Class c, LicenseXML.LicenseAttrCollection attrCollection) throws LicenseFile.LicenseException, IllegalAccessException {
        mapFieldsFromLicenseAttrCollection(object, c, attrCollection, null);
    }

    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }

    public String getProduct() {
        return product;
    }


    public void setProduct(String product) {
        this.product = product;
    }


    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getCustomer() {
        return customer;
    }


    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProject() {
        return project;
    }


    public void setProject(String project) {
        this.project = project;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Generate the input for license signature generation. The rules of the constructing the input is as followed:
     * Add the name of the tag, followed by a 0 byte
     * Depending of the type of the tag (as indicated in the specification), do the following:
     * If the tag is an integer or ASCII string, add the string
     * If the tag is binary, add the Base64 encoding of the binary (with no whitespace)
     * If the tag contains subtags, recursively add all the subtags
     * Add a 0 byte
     * For instance if you have something like this:
     * <A>
     *  <B>foo</B>
     *  <C>
     *      <D>bar</D>
     *  </C>
     * </A>
     * We would sign:
     * A\0B\0foo\0C\0D\0bar\0\0\0
     * Note the succession of 0 bytes which indicates nesting. This fully encodes the XML structure (without those closing tags).
     * Also note that when reading, if any fields doesn't correspond exactly to the expected format, the license must be rejected.
     * @return List<String>
     * @throws LicenseFile.LicenseException
     */
    public List<String> generateSignatureInput() throws LicenseFile.LicenseException {
        List<String> results = new ArrayList<>();
        //Construct the signature input based on XmlOrderManual. The reason is that ela-required in the license-trial.dat
        //is not put into the correct order when generating signature in the legacy code.
        if(this.getClass().isAnnotationPresent(XmlOrderManual.class)){
            XmlOrderManual xmlOrderManual = this.getClass().getAnnotation(XmlOrderManual.class);
            //Construct signature input for orderSup String array
            for(String s: xmlOrderManual.orderSup())
                constructXmlOrderManualList(results, this.getClass().getSuperclass(), s);
            //Construct signature input for order String array
            for(String s: xmlOrderManual.order())
                constructXmlOrderManualList(results, this.getClass(), s);
            return results;
        }else{
            //Construct the signature input based on the JAXB marshalling result
            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                JAXBContext jaxbContext;
                try {
                    jaxbContext = JAXBContext.newInstance(this.getClass());
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    jaxbMarshaller.marshal(this, document);
                    if(document.getFirstChild().hasChildNodes()){
                        loopThroughDocNode(document.getFirstChild().getChildNodes(), results);
                        return results;
                    }else{
                        throw new LicenseFile.LicenseException("license document is empty");
                    }
                } catch (JAXBException e) {
                    throw new LicenseFile.LicenseException(e.getMessage(), e);
                }
            } catch (ParserConfigurationException e) {
                throw new LicenseFile.LicenseException(e.getMessage(), e);
            }
        }
    }

    //Loop through the XML Document nodes and generate license signature output
    private void loopThroughDocNode(NodeList list, List<String> results){
        for(int i = 0; i < list.getLength(); i++){
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                if(node.hasChildNodes() && node.getFirstChild().getNodeType() != Node.TEXT_NODE){
                    if(node.getNodeName().equals("options"))
                        loopThroughDocNodeForV8Options(node.getChildNodes(), results);
                    else{
                        results.add(node.getNodeName());
                        results.add("\0");
                        loopThroughDocNode(node.getChildNodes(), results);
                        results.add("\0");
                    }
                }else{
                    results.add(node.getNodeName());
                    results.add("\0");
                    results.add(node.getTextContent());
                    results.add("\0");
                }
            }
        }
    }

    //Loop through the XML Document nodes for license v8 options tag group
    private void loopThroughDocNodeForV8Options(NodeList list, List<String> results){
        for(int i = 0; i < list.getLength(); i++){
            Node node = list.item(i);
            if(node.hasChildNodes() && node.getNodeType() != Node.TEXT_NODE){
                loopThroughDocNodeForV8Options(node.getChildNodes(), results);
            }else{
                results.add(node.getNodeValue());
                results.add("\0");
            }
        }
    }

    //Construct the signature input based on XmlOrderManual annotation
    private void constructXmlOrderManualList(List<String> results, Class type, String name) throws LicenseFile.LicenseException{
        try{
            Field field = type.getDeclaredField(name);
            field.setAccessible(true);
            if(field.get(this) != null){
                XmlElement xmlElement = field.getAnnotation(XmlElement.class);
                results.add(xmlElement.name());
                results.add("\0");
                results.add((String)field.get(this));
                results.add("\0");
            }

        }catch (IllegalAccessException | NoSuchFieldException e) {
            throw new LicenseFile.LicenseException(e.getMessage(), e);
        }
    }
}
