package com.coverity.licensetools.model.licensever;
import com.coverity.licensetools.model.LicenseFile;
import org.eclipse.persistence.jaxb.JAXBContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjin on 2017-02-09.
 */

@XmlRootElement(name = "taim")
public class License{
    @XmlAnyElement
    private List<LicenseBase> license;

    public License(){
        this.license = new ArrayList<>();
    }

    public void addSubLicense(Object o, LicenseVersionFactory<? extends LicenseBase> factory) throws LicenseFile.LicenseException, IllegalAccessException {
        this.license.add(factory.create(o));
    }

    public List<LicenseBase> getLicense() {
        return license;
    }

    public void setLicense(List<LicenseBase> license) {
        this.license = license;
    }

    public LicenseBase getLastElement(){
        if(this.license.size() != 0)
            return this.license.get(this.license.size() - 1);
        return null;
    }

    /**
     * XmlAdaptor used by JAXB to customize marshalling for <code>License</code> object
     */
    public static class LicenseAdaptor extends XmlAdapter<Element, LicenseBase> {

        @Override
        public LicenseBase unmarshal(Element v) throws Exception {
            if(v == null)
                return null;
            Node versionNode = v.getFirstChild();
            if(v.getNodeName().equals("license")){
                if(versionNode.getNodeName().equals("version")){
                    if(versionNode.getTextContent().equals("1")){
                        Unmarshaller unmarshaller = JAXBContext.newInstance(LicenseV1.class).createUnmarshaller();
                        return (LicenseV1) unmarshaller.unmarshal(v);
                    }
                }
            }

            return null;
        }

        @Override
        public Element marshal(LicenseBase object) throws Exception {
            if(object == null)
                return null;

            Class type = object.getClass();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            //Every subclass of LicenseBase starts with license / license-trial tag
            String licenseRoot = "license";
            JAXBElement jaxbElement = new JAXBElement(new QName(licenseRoot), object.getClass(), object);
            Marshaller marshaller = JAXBContext.newInstance(type).createMarshaller();
            marshaller.marshal(jaxbElement, document);
            //JAXB always ignores the first node if the node is a comment node. Therefore, put it write beside the root
            //tag node. This is a little bit different from the original license, but I think it is fine
            if(object.getComment() != null)
                document.getDocumentElement().insertBefore(document.createComment(object.getComment()), document.getDocumentElement().getFirstChild());

            return document.getDocumentElement();
        }
    }

}
