package com.hustict.aims.service.payment;

import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.model.order.Order;

import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.repository.PaymentTransactionRepository;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.utils.mapper.PaymentTransactionMapper;
import org.springframework.beans.factory.annotation.Autowired; // Import @Autowired
import org.springframework.stereotype.Service; // Import @Service
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service // Đảm bảo đây là một service được quản lý bởi Spring
public class SavePaymentTransaction {

    private static final Logger logger = LoggerFactory.getLogger(SavePaymentTransaction.class);

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    // Phương thức save
    public void save(PaymentTransactionDTO paymentTransaction, String orderInfo) {
        // Chuyển PaymentTransactionDTO thành PaymentTransaction entity
        PaymentTransaction txn = paymentTransactionMapper.toEntity(paymentTransaction);
        if (txn == null) {
            throw new IllegalArgumentException("Không mapping từ paymentDTO qua payment được không được null");
        }
        txn.setContent(orderInfo);

        // Lưu PaymentTransaction vào cơ sở dữ liệu
        PaymentTransaction paymentTransactionEntity = paymentTransactionRepository.save(txn);
        
        if (paymentTransactionEntity.getId() == null) {
            throw new IllegalArgumentException("Lưu paymentTransaction thất bại");
        }

        // Lấy ID từ orderInfo (phần sau dấu cách cuối cùng)
        int lastSpaceIndex = orderInfo.lastIndexOf(" ");
        String idStr = (lastSpaceIndex != -1) ? orderInfo.substring(lastSpaceIndex + 1).trim() : null;
        Long id = null;

        try {
            if (idStr != null) {
                id = Long.parseLong(idStr);
            }
        } catch (NumberFormatException e) {
            logger.error("ID không hợp lệ: " + idStr);
            throw new IllegalArgumentException("ID không hợp lệ: " + idStr);
        }

        if (id == null) {
            logger.error("ID không có giá trị hợp lệ.");
            throw new IllegalArgumentException("ID không có giá trị hợp lệ.");
        }

        // Tìm order tương ứng với id
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            logger.error("Không tìm thấy order với ID: " + id);
            throw new IllegalArgumentException("Không tìm thấy order với ID: " + id);
        }

        Order order = orderOptional.get();

        // Lấy invoiceId từ order
        Long invoiceId = order.getInvoice().getId();
        if (invoiceId == null) {
            logger.error("Không có invoiceId trong order với ID: " + id);
            throw new IllegalArgumentException("Không có invoiceId trong order với ID: " + id);
        }

        // Tìm invoice tương ứng với invoiceId
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (!invoiceOptional.isPresent()) {
            logger.error("Không tìm thấy invoice với ID: " + invoiceId);
            throw new IllegalArgumentException("Không tìm thấy invoice với ID: " + invoiceId);
        }

        
        Invoice invoice = invoiceOptional.get();

        invoice.setPaymentTransaction(paymentTransactionEntity);  // Gán PaymentTransaction ID vào Invoice

        invoiceRepository.save(invoice);

        logger.info("Payment transaction saved successfully with ID: " + paymentTransactionEntity.getId() + "to invoice " + invoice.getId());
    }
}
