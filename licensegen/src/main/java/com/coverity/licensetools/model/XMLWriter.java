package com.coverity.licensetools.model;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import com.coverity.licensetools.model.licensever.License;
import com.coverity.licensetools.model.licensever.LicenseBase;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.*;
import java.io.OutputStream;

/**
 * Created by tjin on 2017-03-06.
 */
public class XMLWriter {

    //space value used when wrapping up xml values
    public enum XMLValueSpaces{
        TRIAL_PUB(12),
        SIGNATURE(8),
        TRIAL_PRIV_V8(16),
        TRIAL_PRIV_LEGACY(8),
        TRIAL_DEBUG_LEGACY(12);

        XMLValueSpaces(int space){
            this.space = space;
        }
        private int space;

        public int getSpace() {
            return space;
        }
    }

    public enum Mode{
        NON_TRIAL,
        TRIAL;
    }

    public static class XMLWriterException extends Exception {
        public XMLWriterException(String s) {
            super(s);
        }
        public XMLWriterException(String s, Exception x) {
            super(s, x);
        }
    }

    private final String VERSION = "1.0";
    private final String ENCODING = "UTF-8";
    private final String DO_NOT_MODIFY_MSG= "DO NOT MODIFY THE CONTENTS OF THIS FILE";
    private final String SYNOPSYS_LEGAL_STATEMENT = "The information contained in this file is the proprietary and confidential\n" +
            "information of Taim Software. and its licensors, and is supplied subject to,\n" +
            "and may be used only by Taim Software customers in accordance with the terms and\n" +
            "conditions of a previously executed license agreement between Taim Software and that\n" +
            "customer.  Customer acknowledges that the use of Taim Software Licensed Product is\n" +
            "enabled by authorization keys supplied by Taim Software for a limited licensed period.\n" +
            "At the end of this period, the authorization key will expire.  You agree not to\n" +
            "take any action to work around or override these license restrictions or use\n" +
            "the Licensed Product beyond the licensed period.  Any attempt to do so will be\n" +
            "considered an infringement of intellectual property rights that may be subject\n" +
            "to legal action.";

    private IndentingXMLStreamWriter writer;

    public XMLWriter(OutputStream outputStream) throws XMLWriterException{
        XMLOutputFactory xof = XMLOutputFactory.newFactory();
        try {
            this.writer = new IndentingXMLStreamWriter(xof.createXMLStreamWriter(outputStream));
            this.writer.setIndentStep("    ");
            this.writer.writeStartDocument(ENCODING, VERSION);
            generateNewLines(1);
            this.writer.writeComment(DO_NOT_MODIFY_MSG);
            generateNewLines(1);
            this.writer.writeComment(SYNOPSYS_LEGAL_STATEMENT);
            generateNewLines(2);

        } catch (XMLStreamException e) {
            throw new XMLWriterException(e.getMessage(), e);
        }
    }

    /**
     * Marshall <code>License</code> object to <code>IndentingXMLStreamWriter</code> with file comment
     * @param license
     * @param fileComment
     * @throws XMLWriterException
     */
    public void marshall(License license, Mode mode, String fileComment) throws XMLWriterException{
        try {
            if(fileComment != null){
                this.writer.writeComment(fileComment);
                generateNewLines(1);
            }
            marshall(license, mode);
        } catch (XMLStreamException e) {
            throw new XMLWriterException(e.getMessage(), e);
        }
    }

    /**
     * Marshall <code>License</code> object to <code>IndentingXMLStreamWriter</code>
     * @param license
     * @throws XMLWriterException
     * @throws XMLStreamException
     */
    public void marshall(License license, Mode mode) throws XMLWriterException, XMLStreamException {
        try {
            if(mode == Mode.NON_TRIAL)
                this.writer.writeDTD("<!DOCTYPE taim SYSTEM \"license.dtd\">");
            else
                this.writer.writeDTD("<!DOCTYPE taim SYSTEM \"license-trial.dtd\">");
            generateNewLines(1);

            JAXBContext jaxbContext = JAXBContext.newInstance(license.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            jaxbMarshaller.marshal(license, this.writer);
        } catch (JAXBException e) {
            throw new XMLWriterException(e.getMessage(), e);
        }
    }

    /**
     * Wrap up xml text value with specified <code>xmlValueSpace</code>
     * Each wrapped up value starts with <code>INDENT1 + INDENT2</code>.
     * The whole input string is split up by chunk size 50. At the end
     * of each chunk, INDENT_1 is appended. If the chunk is not the final
     * chunk, then INDENT_2 is appended
     * @param value
     * @param xmlValueSpace
     * @return
     */
    public static String wrapValue(final String value, int xmlValueSpace){
        if(StringUtils.isBlank(value))
            return null;

        final String INDENT_1 = "\n" + StringUtils.repeat(" ", xmlValueSpace);
        final String INDENT_2 = StringUtils.repeat(" ", 4);
        final int CHUNK_SIZE = 50;
        int offset = 0;

        StringBuilder wrappedValueBuilder = new StringBuilder(INDENT_1 + INDENT_2);
        while(true){
            if(offset + CHUNK_SIZE >= value.length()){
                wrappedValueBuilder.append(value.substring(offset));
                wrappedValueBuilder.append(INDENT_1);
                break;
            }else {
                wrappedValueBuilder.append(value.substring(offset, offset + CHUNK_SIZE));
                wrappedValueBuilder.append(INDENT_1);
                offset += CHUNK_SIZE;
            }
            wrappedValueBuilder.append(INDENT_2);
        }
        return wrappedValueBuilder.toString();
    }

    //Manually generate line breaks in the xml file
    private void generateNewLines(int times) throws XMLWriterException{
        for(int i = 0; i < times; i++)
            try {
                this.writer.writeCharacters("\n");
            } catch (XMLStreamException e) {
                throw new XMLWriterException(e.getMessage(), e);
            }
    }
}
