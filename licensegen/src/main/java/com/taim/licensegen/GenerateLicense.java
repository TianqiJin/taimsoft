package com.taim.licensegen;

import com.taim.licensegen.model.licensever.License;
import com.taim.licensegen.util.ToolCommandLineParser;
import com.taim.licensegen.util.XmlProperty;
import com.taim.licensegen.model.CryptKey;
import com.taim.licensegen.model.LicenseFile;
import com.taim.licensegen.model.LicenseXML;
import com.taim.licensegen.model.XMLWriter;
import com.taim.licensegen.model.licensever.LicenseV1;
import com.taim.licensegen.model.licensever.LicenseBase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64;

/**
 * Created by tjin on 2016-12-02.
 */
public class GenerateLicense {
    private static final String UNLIMITED = "unlimited";
    private static final String TAIM_PRODUCTS_FILE = "taim_products";
    private static final String COVERITY_PRODUCTS_V3_FILE = "coverity_products_v3";
    private static final String LICENSE_REFERENCE_DATE = "2005-03-13 00:00:00";
    private static final String PST_TIMEZONE = "America/Los_Angeles";
    private static final String START_ANNOATION = StringUtils.repeat("#", 15);
    private List<String> taimProductList;
    private File tempDir;
    private File workingDir;
    private File targetFile;
    private File privateKeyFile;
    private File publicKeyFile;
    private LicenseXML licenseXML;
    private License licenses;
    private String fileComment;
    private File tempAuthPasswordFile;

    public static class GenerateLicenseException extends Exception {
        public GenerateLicenseException(String s) {
            super(s);
        }
        public GenerateLicenseException(String s, Exception x) {
            super(s, x);
        }
    }

    public GenerateLicense(){
        tempDir = new File(System.getProperty("java.io.tmpdir"));
        workingDir = new File(System.getProperty("user.dir"));

        targetFile = new File(tempDir, "license.dat");
        privateKeyFile = new File(workingDir, "private.key");

        licenseXML = new LicenseXML();
        licenses = new License();
    }

    /**
     * Initialize License from the licenseAttrCollection JSON request. It converts the JSON request
     * to LicenseFile first, then map from LicenseFile to LicenseXml object.
     * Meanwhile, it validates whether the attributes stored in LicenseXml are valid.
     * After the validation, it generates cov0 field based on valid-from and duration/valid-until fields.
     * Then it generates license version based on the existing license attributes and generates trial information if needed
     * Finally, it generate license instance and store it into license and licenseTrial objects
     *
     * @param request
     * @throws Exception
     */
    public GenerateLicense initializeLicense(JSONObject request) throws GenerateLicenseException {
        try {
            return initializeLicense(new LicenseFile(request));
        } catch (Exception e) {
            throw new GenerateLicenseException(e.getMessage(), e);
        }
    }

    /**
     * Initialize License from the licenseAttrCollection JSON request. It maps the <code>LicenseFile</code> object obtained
     * from JSON license request, to LicenseXml object.
     * Meanwhile, it validates whether the attributes stored in LicenseXml are valid.
     * After the validation, it generates cov0 field based on valid-from and duration/valid-until fields.
     * Then it generates license version based on the existing license attributes and generates trial information if needed
     * Finally, it generate license instance and store it into license and licenseTrial objects
     *
     * @param licenseFile
     * @throws Exception
     */

    public GenerateLicense initializeLicense(LicenseFile licenseFile) throws GenerateLicenseException{
        try{
            System.out.println(START_ANNOATION + "Initialize license generation" + START_ANNOATION);
            System.out.println("Validate License request is not empty");
            validateLicenseFile(licenseFile);

            System.out.println("Mapping file-attributes, license-attributes, and license into LicenseAttrCollection Object");
            for(LicenseFile.LicenseRequest licenseRequest: licenseFile.getLicenses()){
                LicenseXML.LicenseAttrCollection licenseAttrCollection = new LicenseXML.LicenseAttrCollection();
                //Map all the fields in LicenseRequest
                for(String name : JSONObject.getNames(licenseRequest.getJson())) {
                    String value = licenseRequest.getJson().get(name).toString();
                    licenseAttrCollection.getAttributes().put(name, value);
                }
                System.out.println("Map all the annotated fields in FileAttributes ");
                //Map all the annotated fields in FileAttributes
                mapFromJSONToAttrCollection(licenseFile.getFileAttributes(), LicenseFile.FileAttributes.class, licenseAttrCollection);
                //Map all the annotated fields in LicenseAttributes
                System.out.println("Map all the annotated fields in LicenseAttributes ");
                mapFromJSONToAttrCollection(licenseFile.getLicenseAttributes(), LicenseFile.LicenseAttributes.class, licenseAttrCollection);
                licenseXML.getLicenses().add(licenseAttrCollection);
            }
            for(LicenseXML.LicenseAttrCollection licenseAttrCollection : licenseXML.getLicenses()){
                //Validate license attributes defined in licenseAttrCollection
                validateLicenseXml(licenseAttrCollection);
                //Generate final taim-0 and valid-until fields based on valid-from, duration, and valid-until fields
                System.out.println("Generate taim-0 and valid-until fields");
                generateTaim0(licenseAttrCollection);
                //Generate license version based on the given license attributes
                System.out.println("Generate license version");
                //Remove file comment from the licenseAttrCollection in case that it is mapped into Options for LicenseV1
                System.out.println("Remove file-comment from the licenseAttrCollection");
                this.fileComment = String.valueOf(licenseAttrCollection.getAttributes().get("file-comment"));
                licenseAttrCollection.getAttributes().remove("file-comment");

                //Generate License instances
                generateLicenseInstance(licenseAttrCollection);
            }
            return this;

        }catch (Exception e){
            throw new GenerateLicenseException(e.getMessage(), e);
        }
    }



    /**
     * Initialize License signature cov1 and cov5 fields for all licenses stored in
     * <code>this.getLicenses()</code> and <code>this.getLicensesTrial()</code>
     * @throws CryptKey.CryptKeyException
     * @throws GenerateLicenseException
     */
    public GenerateLicense initializeSignature() throws CryptKey.CryptKeyException, GenerateLicenseException {
        String symmetricKeyPw = null;
        try{
            if(!StringUtils.isBlank(System.getenv("TAIM_AUTHORIZATION_PASSPHRASE")))
                symmetricKeyPw = System.getenv("TAIM_AUTHORIZATION_PASSPHRASE");
            else if(this.getTempAuthPasswordFile() != null && this.getTempAuthPasswordFile().exists())
                symmetricKeyPw = Files.readAllLines(this.getTempAuthPasswordFile().toPath()).get(0);
        }catch (IOException e){
            throw new GenerateLicenseException(e.getMessage(), e);
        }
        CryptKey<PrivateKey> privateKey = CryptKey.decryptTaimMasterKey(this.getPrivateKeyFile(), symmetricKeyPw);

        for(LicenseBase license: this.getLicenses().getLicense()){
            String sig = generateLicenseSignature(privateKey, license);
            if(license.getClass().isAssignableFrom(LicenseV1.class)){
                LicenseV1 licenseV1 = (LicenseV1)license;
                licenseV1.setSignature(sig);
            }
        }

        return this;
    }

    /**
     * Initialize Taim0 tag for the corresponding licenseAttrCollection. Cov0 tag describes the total seconds between the reference date
     * (2005-03-13 00:00:00 UTC) to the valid-until date. The valid-until date is calculated based on UTC TimeZone.
     * If "duration" is found in the given licenseAttrCollection object, then it will compute the valid-until date based on the duration
     * and optional valid-from date. Otherwise, the valid-until date will be parsed based on the "valid-until" in the
     * given licenseAttrCollection object.
     *
     * If neither duration nor valid-until is found, then it will throw an exception
     * @param licenseAttrCollection
     * @throws GenerateLicenseException
     */
    public void generateTaim0(LicenseXML.LicenseAttrCollection licenseAttrCollection) throws GenerateLicenseException{
        DateTime expireDate;
        if(licenseAttrCollection.isKeyPresented("duration")){
            String validFromDate = null;
            if(licenseAttrCollection.isKeyPresented("valid-from"))
                validFromDate = String.valueOf(licenseAttrCollection.getAttributes().get("valid-from"));
            expireDate = parseDuration(String.valueOf(licenseAttrCollection.getAttributes().get("duration")), validFromDate);
        }else if(licenseAttrCollection.isKeyPresented("valid-until")){
            expireDate = parseAbsoluteDate(String.valueOf(licenseAttrCollection.getAttributes().get("valid-until")));
        }else{
            throw new GenerateLicenseException("Either duration or valid-until must be presented in the given licenseAttrCollection");
        }
        DateTime referenceTime = DateTime.parse(LICENSE_REFERENCE_DATE, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).withZoneRetainFields(DateTimeZone.UTC);

        //Calculate the total seconds between the reference time and valid-until date and fulfill cov-0 and valid-until
        //fields in the given licenseAttrCollection object
        Seconds seconds = Seconds.secondsBetween(referenceTime, expireDate);
        licenseAttrCollection.getAttributes().put("taim-0", seconds.getSeconds());
        licenseAttrCollection.getAttributes().put("valid-until", expireDate.toString("yyyy-MMM-dd HH:mm:ss") + " UTC");
        //Remove duration if existed since it is not supposed to be existed
        licenseAttrCollection.getAttributes().remove("duration");
    }

    /**
     * Generate non-trial license instance
     * If version is 1, it will generate LicenseV1 instances.
     * @param licenseAttrCollection
     * @return
     * @throws LicenseFile.LicenseException
     * @throws IllegalAccessException
     */
    public void generateLicenseInstance(LicenseXML.LicenseAttrCollection licenseAttrCollection) throws LicenseFile.LicenseException, IllegalAccessException {
        //Generate license instance and add it into license list
        int version = Integer.parseInt((String) licenseAttrCollection.getAttributes().get("max-version"));
        System.out.println("Start generating version " + version + " license");
        if(version == 1){
            this.licenses.addSubLicense(licenseAttrCollection, new LicenseV1.LicenseV1Factory());
        }
    }

    /**
     * Marshall <code>this.getLicenses()</code> into <code>this.getTargetFile()</code>
     * Marshall <code>this.getLicensesTrial()</code> into <code>this.getTrialTargetFile()</code>
     * @throws XMLWriter.XMLWriterException
     */
    public GenerateLicense marshall() throws XMLWriter.XMLWriterException{
        try {
            System.out.println("Start writing licenses into " + this.getTargetFile().getName());
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(this.getTargetFile().getPath()));
            xmlWriter.marshall(this.getLicenses(), XMLWriter.Mode.NON_TRIAL, this.getFileComment());
        } catch ( FileNotFoundException e) {
            throw new XMLWriter.XMLWriterException(e.getMessage(), e);
        }

        return this;
    }

    /**
     * Unmarshall <code>this.getTargetFile()</code> into License object <code>this.getLicense()</code>
     * If <code>this.getTrialTargetFile()</code> exists, then unmarshall <code>this.getTrialTargetFile()</code>
     * into <code>this.getLicensesTrial()</code>
     * @throws LicenseFile.LicenseException
     */
    public void unmarshall() throws LicenseFile.LicenseException{
        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader xsr;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(License.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            if(this.getTargetFile().exists()){
                xsr = xif.createXMLStreamReader(new StreamSource(this.getTargetFile()));
                this.setLicenses((License)jaxbUnmarshaller.unmarshal(xsr));
            }
        } catch (JAXBException | XMLStreamException e) {
            throw new LicenseFile.LicenseException(e.getMessage(), e);
        }
    }

    public File getTempDir() {
        return tempDir;
    }

    public void setTempDir(File tempDir) {
        this.tempDir = tempDir;
    }

    public File getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(File workingDir) {
        this.workingDir = workingDir;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public File getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(File privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public File getPublicKeyFile() {
        return publicKeyFile;
    }

    public void setPublicKeyFile(File publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public LicenseXML getLicenseXML() {
        return licenseXML;
    }

    public String getFileComment() { return fileComment; }

    public void setFileComment(String fileComment) {
        this.fileComment = fileComment;
    }

    public File getTempAuthPasswordFile() {
        return tempAuthPasswordFile;
    }

    public void setTempAuthPasswordFile(File tempAuthPasswordFile) {
        this.tempAuthPasswordFile = tempAuthPasswordFile;
    }

    private void validateLicenseFile(LicenseFile licenseFile) throws LicenseFile.LicenseException{
        //Check licenseAttrCollection array non-empty
        if(licenseFile.getLicenses().size() == 0){
            throw new LicenseFile.LicenseException("licenseAttrCollection array is empty");
        }
    }

    private void validateLicenseXml(LicenseXML.LicenseAttrCollection licenseAttrCollection) throws LicenseFile.LicenseException, IOException{
        //Check licenseAttrCollection has customer key and its value is not null
        if(!licenseAttrCollection.isKeyPresented("customer") || licenseAttrCollection.isKeyValueNull("customer")){
            throw new LicenseFile.LicenseException("customer field is empty");
        }
        //Check licenseAttrCollection has valid-until key and its value is not null
        if(!licenseAttrCollection.isKeyPresented("valid-until") || licenseAttrCollection.isKeyValueNull("valid-until")){
            throw new LicenseFile.LicenseException("valid-until field is empty");
        }
        //Check licenseAttrCollection has project key and its value is not null
        if(!licenseAttrCollection.isKeyPresented("project") || StringUtils.isBlank(String.valueOf(licenseAttrCollection.getAttributes().get("project")))){
            throw new LicenseFile.LicenseException("project field is empty");
        }
        //Check licenseAttrCollection has product key and its value is not null and its value is in the taim_products.properties
        if(!licenseAttrCollection.isKeyPresented("product") || licenseAttrCollection.isKeyValueNull("product")){
            throw new LicenseFile.LicenseException("product field is empty");
        }else{
            try(InputStream inputStream = GenerateLicense.class.getClassLoader().getResourceAsStream(TAIM_PRODUCTS_FILE)) {
                taimProductList = IOUtils.readLines(inputStream, StandardCharsets.UTF_8.name());
            }
            if(!taimProductList.contains(licenseAttrCollection.getAttributes().get("product"))){
                throw new LicenseFile.LicenseException("product is invalid");
            }
        }
        //Check record-id is present
        if(!licenseAttrCollection.isKeyPresented("record-id")){
            throw new LicenseFile.LicenseException("record-id is missing");
        }
        //Check max-transactions is present and valid
        if(licenseAttrCollection.isKeyPresented("max-transactions")){
            String value = String.valueOf(licenseAttrCollection.getAttributes().get("max-transactions"));
            if(!isMaxTransactionUserCountsValid(value)){
                throw new LicenseFile.LicenseException(value + " is not a valid max-transactions");
            }
        }else{
            throw new LicenseFile.LicenseException("max-transactions is not presented");
        }
        //Check user-count is present and valid
        if(licenseAttrCollection.isKeyPresented("user-count")){
            String value = String.valueOf(licenseAttrCollection.getAttributes().get("user-count"));
            if(!isMaxTransactionUserCountsValid(value)){
                throw new LicenseFile.LicenseException(value + " is not a valid user-count");
            }
        }else{
            throw new LicenseFile.LicenseException("user-count is not presented");
        }
        //Check the value of max-version (max-version is only for internal usage)
        if(licenseAttrCollection.isKeyPresented("max-version")){
            int version = Integer.parseInt((String) licenseAttrCollection.getAttributes().get("max-version"));
            if(version < 1){
                throw new LicenseFile.LicenseException("license version must be greater than 1");
            }
        }
    }

    //Check whether max-transactions or user-count is valid
    private boolean isMaxTransactionUserCountsValid(String value){
        if(value.equals(UNLIMITED)){
            return true;
        }else{
            try{
                int intValue = Integer.valueOf(value);
                if(intValue <= 0)
                    return false;
            }catch (NumberFormatException ex){
                return false;
            }
        }
        return true;
    }

    //map LicenseAttributes and FileAttributes to LicenseXml.LicenseAttrCollection
    private void mapFromJSONToAttrCollection(Object object, Class c, LicenseXML.LicenseAttrCollection attrCollection) throws LicenseFile.LicenseException{
        for(Field field : c.getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(XmlProperty.class)){
                XmlProperty xmlProperty = field.getAnnotation(XmlProperty.class);
                try {
                    attrCollection.getAttributes().put(xmlProperty.name(), field.get(object));
                } catch (IllegalAccessException e) {
                    throw new LicenseFile.LicenseException("Unable to map " + c.getName() + " to licenseAttrCollection", e);
                }
            }
        }
    }

    //parse date in the format of "yyyyMMdd" and return DateTime object
    private DateTime parseAbsoluteDate(String dateString) throws GenerateLicenseException{
        final String INPUT_TIME_FORMAT="yyyyMMdd";
        if(StringUtils.isBlank(dateString)){
            throw new GenerateLicenseException("A date of the form yyymmdd is missing");
        }else{
            try{
                int intValue = Integer.valueOf(dateString);
                if(intValue < 10000000 || intValue > 30000000){
                    throw new GenerateLicenseException("The given date is not of the form yyyymmdd");
                }
            }catch(NumberFormatException e){
                throw new GenerateLicenseException(e.getMessage(), e);
            }
        }
        try{
            //Set the time to the start of the given date in the time zone of PST, then convert it to time zone UTC
            //For example, if the given date is 20170101, then it should be converted to 2017-01-01 08:00:00 UTC
            DateTime dateTime = new DateTime(new SimpleDateFormat(INPUT_TIME_FORMAT).parse(dateString));
            dateTime = dateTime.withZoneRetainFields(DateTimeZone.forID(PST_TIMEZONE)).withTimeAtStartOfDay().withZone(DateTimeZone.UTC);
            //If we are int the PST zone DST, then we need to add 1 hour to the expire time
            if(TimeZone.getTimeZone(PST_TIMEZONE).inDaylightTime(dateTime.toDate()))
                dateTime = dateTime.plusHours(1);
            return dateTime;
        }catch(ParseException e){
            throw new GenerateLicenseException(e.getMessage());
        }
    }

    //parse the duration and the optional valid-from and return DateTime object
    private DateTime parseDuration(String duration, String validFromString) throws GenerateLicenseException{
        final String INPUT_TIME_FORMAT="yyyyMMdd";
        int digitDuration;
        DateTime validFromDate = null;

        if(StringUtils.isBlank(duration)){
            throw new GenerateLicenseException("The value of duration is missing");
        }

        //The duration should be in the format of "<digits>[yds]"
        //For example: 1s, 2d, 30y
        String unit = duration.substring(duration.length() - 1);
        duration = duration.substring(0, duration.length() - 1);

        if(!unit.matches("[yds]")){
            throw new GenerateLicenseException("Invalid duration unit, must be y, d or s, but the unit is " + unit);
        }else{
            try{
                digitDuration = Integer.valueOf(duration);
            }catch(NumberFormatException e){
                throw new GenerateLicenseException("Invalid duration amount, must be an integer, but the amount is " + duration);
            }
        }

        if(StringUtils.isNotBlank(validFromString)){
            try{
                Integer.valueOf(validFromString);
                //If valid-from is given, then set the time to the start of the given date in the time zone of PST,
                //then convert it to time zone UTC
                //For example, if the given date is 20170101, then it should be converted to 2017-01-01 08:00:00 UTC
                validFromDate = DateTime.parse(validFromString, DateTimeFormat.forPattern(INPUT_TIME_FORMAT))
                        .withTimeAtStartOfDay()
                        .withZoneRetainFields(DateTimeZone.forID(PST_TIMEZONE))
                        .withZone(DateTimeZone.UTC);
            }catch(NumberFormatException e){
                throw new GenerateLicenseException("The value of valid-from, must be an integer, but it is " + validFromString);
            }
        }

        if(validFromDate == null){
            if(unit.equals("s"))
                //If valid-from is not given and duration unit is "s", then set the time to the current time in the time zone of PST,
                //then convert it to time zone UTC
                validFromDate = DateTime.now(DateTimeZone.forID(PST_TIMEZONE)).withZone(DateTimeZone.UTC);
            else{
                //If valid-from is not given and duration unit is not "s", then set the time to the start of the given
                //date and plus 1 day in the time zone of PST,
                //then convert it to time zone UTC
                DateTime dateTime = DateTime.now(DateTimeZone.forID(PST_TIMEZONE))
                        .withTimeAtStartOfDay()
                        .withZone(DateTimeZone.UTC);
                validFromDate = dateTime.plusDays(1);
            }
        }
        //If we are int the PST zone DST, then we need to add 1 hour to the expire time
        if(TimeZone.getTimeZone(PST_TIMEZONE).inDaylightTime(validFromDate.toDate()))
            validFromDate = validFromDate.plusHours(1);

        switch(unit){
            case "s":
                validFromDate = validFromDate.plusSeconds(digitDuration);
                break;
            case "d":
                validFromDate =  validFromDate.plusDays(digitDuration);
                break;
            case "y":
                validFromDate = validFromDate.plusYears(digitDuration);
                break;
            default:
                break;
        }

        return validFromDate;
    }

    /**
     * Generate ela-required field for all trial license
     * For v8 license, if trial is presented and ela-required is not found,
     * we need to manually add ela-required into licenseAttrCollection and set it to false.
     * If ela-required is set to false, then set the value to no
     * If ela-required is set to true, then set the value to yes
     * @param licenseAttrCollection
     */
    private void generateElaRequiredField(LicenseXML.LicenseAttrCollection licenseAttrCollection){
        //For v8 license, if trial is presented and ela-required is not found,
        // we need to manually add ela-required into licenseAttrCollection
        // and set it to false
        if(Integer.valueOf(licenseAttrCollection.getAttributes().get("max-version").toString()) == 8 ||
                Integer.valueOf(licenseAttrCollection.getAttributes().get("max-version").toString()) == 7){
            if(!licenseAttrCollection.isKeyPresented("ela-required"))
                licenseAttrCollection.getAttributes().put("ela-required", false);
        }
        //If ela-required is set to false, then set the value to no
        //If ela-required is set to true, then set the value to yes
        if(licenseAttrCollection.isKeyPresented("ela-required")){
            String value =
                    Boolean.valueOf(licenseAttrCollection.getAttributes().get("ela-required").toString())
                            .equals(true)? "yes" : "no";
            licenseAttrCollection.getAttributes().put("ela-required", value);
        }
    }

    //generate license signature
    private String generateLicenseSignature(CryptKey<PrivateKey> privateKeyCryptKey, LicenseBase license) throws GenerateLicenseException{
        try{
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKeyCryptKey.getKeyData());
            List<String> inputs = license.generateSignatureInput();
            for(String s: inputs)
                signature.update(s.getBytes(StandardCharsets.UTF_8));
            byte[] finalOut = signature.sign();
            return Base64.getEncoder().encodeToString(finalOut);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | LicenseFile.LicenseException e) {
            throw new GenerateLicenseException(e.getMessage(), e);
        }
    }

    public void verifyLicense(File licenseFile, String product) throws GenerateLicenseException {
        try{
            publicKeyFile = Files.createTempFile("tmpPublicKey", null).toFile();
            Files.copy(getClass().getClassLoader().getResourceAsStream("taimPublicKey"), publicKeyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            setTargetFile(licenseFile);
            unmarshall();

            byte[] tmpPublicKeyBytes = Files.readAllBytes(getPublicKeyFile().toPath());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(tmpPublicKeyBytes));
            CryptKey<PublicKey> publicKeyCryptKey = new CryptKey<>();
            publicKeyCryptKey.setKeyData(publicKey);
            boolean isProductFound = false;
            for(LicenseBase licenseBase: getLicenses().getLicense()){
                LicenseV1 license = (LicenseV1) licenseBase;
                if(license.getProduct().equals(product)){
                    isProductFound = true;
                    String taim1 = license.getSignature();
                    license.setSignature(null);
                    Signature signature = Signature.getInstance("SHA1withRSA");
                    signature.initVerify(publicKeyCryptKey.getKeyData());
                    List<String> inputs = license.generateSignatureInput();
                    for(String s: inputs)
                        signature.update(s.getBytes(StandardCharsets.UTF_8));

                    //Check signature
                    if(!signature.verify(Base64.getDecoder().decode(taim1))){
                        throw new GenerateLicenseException("License authorization failure. The signature is invalid");
                    }
                    //Check valid-from field
                    boolean isValidFromFound = false;
                    DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
                    for(LicenseV1.Option option: license.getOptions()){
                        if(option.getName().equals("valid-from")){
                            isValidFromFound = true;
                            if(dtf.parseDateTime(String.valueOf(option.getValue())).isAfter(DateTime.now())){
                                throw new GenerateLicenseException("License starts in the future");
                            }
                        }
                    }

                    //Check valid-until field
                    dtf = DateTimeFormat.forPattern("yyyy-MMM-dd HH:mm:ss z");
                    if(DateTime.now().withZoneRetainFields(DateTimeZone.UTC).isAfter(dtf.parseDateTime(license.getValidUntil()))){
                        throw new GenerateLicenseException("License is expired");
                    }

                    if(!isValidFromFound){
                        throw new GenerateLicenseException("Field valid-from is not found in the license");
                    }

                }
            }
            if(!isProductFound){ throw new GenerateLicenseException("License for product - " + product+ " is not found");
            }
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException | IOException | LicenseFile.LicenseException e) {
            throw new GenerateLicenseException(e.getMessage(), e);
        }finally {
            if(publicKeyFile.exists()){
                publicKeyFile.delete();
            }
        }
    }

    public static void main(String[] args){
//        GenerateLicense generateLicense = new GenerateLicense();
//        try {
//            generateLicense.verifyLicense(new File("license.dat"), "Taim Desktop");
//        } catch (GenerateLicenseException e) {
//            e.printStackTrace();
//        }

        ToolCommandLineParser commandLineParser = new ToolCommandLineParser();
        switch(commandLineParser.parse(args)){
            case success:
                GenerateLicense generateLicense = new GenerateLicense();
                String securityFile = commandLineParser.getStringValue(ToolCommandLineParser.SECURITY_FILE);
                String privateKeyFile = commandLineParser.getStringValue(ToolCommandLineParser.PRIVATE_KEY_FILE);
                String requestFile = commandLineParser.getStringValue(ToolCommandLineParser.LICENSE_REQUEST);

                generateLicense.setTargetFile(new File(securityFile));
                generateLicense.setPrivateKeyFile(new File(privateKeyFile));

                try {
                    JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(requestFile)), StandardCharsets.UTF_8));
                    generateLicense.initializeLicense(jsonObject).initializeSignature().marshall();

                    generateLicense.setLicenses(null);
                    generateLicense.verifyLicense(new File("license.dat"), "Taim Desktop");
                } catch (IOException | XMLWriter.XMLWriterException | GenerateLicenseException | CryptKey.CryptKeyException  e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                break;
            case failFriendly:
                System.exit(1);
            case failBadly:
                System.exit(1);
        }
    }

    public License getLicenses() {
        return licenses;
    }

    public void setLicenses(License licenses) {
        this.licenses = licenses;
    }

}
