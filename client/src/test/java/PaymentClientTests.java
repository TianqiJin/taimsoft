import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taim.client.PaymentClient;
import com.taim.client.StaffClient;
import com.taim.client.TransactionClient;
import com.taim.client.VendorClient;
import com.taim.dto.*;
import com.taim.model.Payment;
import com.taim.model.Staff;
import com.taim.model.Transaction;
import com.taim.model.basemodels.UserBaseModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class PaymentClientTests {
    private static TransactionClient transactionClient = new TransactionClient();
    private static VendorClient vendorClient = new VendorClient();
    private static StaffClient staffClient = new StaffClient();
    private static PaymentClient paymentClient = new PaymentClient();
    private static TransactionDTO transaction;
    private static VendorDTO vendor;
    private static StaffDTO staff;
    private PaymentDTO payment;

    @BeforeClass
    public static void setup(){
//        vendor = new VendorDTO();
//        staff = new StaffDTO();
        transaction = new TransactionDTO();

//        vendor.setDateCreated(DateTime.now());
//        vendor.setDateModified(DateTime.now());
//        vendor.setUserType(UserBaseModel.UserType.INDIVIDUAL);
//
//        staff.setDateCreated(DateTime.now());
//        staff.setDateModified(DateTime.now());
//        staff.setUserName("admin");
//        staff.setPosition(Staff.Position.MANAGER);
//        staff.setPassword("admin");

        transaction.setDateCreated(DateTime.now());
        transaction.setDateModified(DateTime.now());
        transaction.setTransactionType(Transaction.TransactionType.INVOICE);
        transaction.setTransactionCategory(Transaction.TransactionCategory.OUT);

//        vendorClient.add(vendor);
//        staffClient.add(staff);
    }

    @Test
    public void testAddPayment() throws IOException {
        VendorDTO vendorDTO = vendorClient.getById(1L);
        StaffDTO staffDTO = staffClient.getById(1L);
        transaction.setStaff(staffDTO);
        transaction.setVendor(vendorDTO);
        transactionClient.add(transaction);
        TransactionDTO transactionDTO = transactionClient.getById(1L);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setUserID(vendorDTO.getId());
        paymentDTO.setStaffID(staffDTO.getId());
        paymentDTO.setDateCreated(DateTime.now());
        paymentDTO.setDateModified(DateTime.now());
        paymentDTO.setPaymentType(Payment.PaymentType.VENDOR_PAYMENT);
        PaymentDetailDTO paymentDetailDTO = new PaymentDetailDTO();
        paymentDetailDTO.setTransaction(transactionDTO);
        paymentDetailDTO.setAmount(100);
        paymentDetailDTO.setDeposit(false);
        paymentDetailDTO.setDateCreated(DateTime.now());
        paymentDetailDTO.setDateModified(DateTime.now());
        paymentDTO.getPaymentDetails().add(paymentDetailDTO);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.readValue(objectMapper.writeValueAsString(paymentDTO), PaymentDTO.class);

        paymentClient.add(paymentDTO);
        System.out.println("HERE");

    }
}
