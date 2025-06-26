package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.dto.order.OrderItemDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.model.shipping.DeliveryInfo;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.PaymentTransactionRepository;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;
import com.hustict.aims.utils.mapper.DeliveryInfoMapper;
import com.hustict.aims.utils.mapper.InvoiceMapper;
import com.hustict.aims.utils.mapper.OrderMapper;
import com.hustict.aims.utils.mapper.PaymentTransactionMapper;
import com.hustict.aims.repository.DeliveryInfoRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class SaveOrderService {

    private final OrderRepository orderRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final InvoiceRepository invoiceRepository;
    private final OrderMapper orderMapper;
    private final DeliveryInfoMapper deliveryInfoMapper;
    private final PaymentTransactionMapper paymentTxnMapper;
    private final InvoiceMapper invoiceMapper;
    private final SessionValidatorService sessionValidatorService;
    private final DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    public SaveOrderService(
        OrderRepository orderRepository,
        PaymentTransactionRepository transactionRepository,
        InvoiceRepository invoiceRepository,
        OrderMapper orderMapper,
        DeliveryInfoMapper deliveryInfoMapper,
        PaymentTransactionMapper paymentTxnMapper,
        InvoiceMapper invoiceMapper,
        SessionValidatorService sessionValidatorService,
        DeliveryInfoRepository deliveryInfoRepository
    ) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.invoiceRepository = invoiceRepository;
        this.orderMapper = orderMapper;
        this.deliveryInfoMapper = deliveryInfoMapper;
        this.paymentTxnMapper = paymentTxnMapper;
        this.invoiceMapper = invoiceMapper;
        this.sessionValidatorService = sessionValidatorService;
        this.deliveryInfoRepository = deliveryInfoRepository;
    }

    @Transactional
    public OrderInformationDTO saveAll(HttpSession session) {
        CartRequestDTO cartDTO = (CartRequestDTO) session.getAttribute("cartRequested");
        InvoiceDTO invoiceDTO = (InvoiceDTO) session.getAttribute("invoice");
        DeliveryFormDTO deliveryFormDTO = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        PaymentTransactionDTO paymentDTO = (PaymentTransactionDTO) session.getAttribute("paymentTransaction");
        
        // Lưu delivery information trước
        if (deliveryFormDTO == null) {
            throw new IllegalArgumentException("DeliveryFormDTO không được null");
        }
        DeliveryInfo deliveryEntity = deliveryInfoMapper.toEntity(deliveryFormDTO);
        
        if (deliveryEntity == null) {
            throw new IllegalArgumentException("Mapping DeliveryFormDTO sang DeliveryInfo trả về null");
        }
        DeliveryInfo savedDelivery = deliveryInfoRepository.save(deliveryEntity);
        
        if (savedDelivery == null || savedDelivery.getId() == null) {
            throw new IllegalStateException("Lưu DeliveryInfo thất bại, entity hoặc id trả về null");
        }

        // Lưu Invoice trước
        if (invoiceDTO == null) {
            throw new IllegalArgumentException("InvoiceDTO không được null");
        }
        Invoice invoiceEntity = invoiceMapper.toEntity(invoiceDTO);
        if (invoiceEntity == null) {
            throw new IllegalArgumentException("Mapping InvoiceDTO sang Invoice trả về null");
        }
        Invoice savedInvoice = invoiceRepository.save(invoiceEntity);
        if (savedInvoice.getId() == null) {
            throw new IllegalStateException("Lưu Invoice thất bại, id trả về null");
        }

        OrderInformationDTO orderInfoDTO = new OrderInformationDTO();
        List<CartItemRequestDTO> items = cartDTO.getProductList();
        orderInfoDTO.setProductList(items);

        orderInfoDTO.setTotalPriceExVAT(invoiceDTO.getProductPriceExVAT());
        orderInfoDTO.setTotalPriceInVAT(invoiceDTO.getProductPriceIncVAT());
        orderInfoDTO.setShippingFee(invoiceDTO.getShippingFee());
        orderInfoDTO.setRushOrder(false);
        orderInfoDTO.setTotalAmount(invoiceDTO.getTotalAmount());
        orderInfoDTO.setCurrency(cartDTO.getCurrency());
        // thời gian đang set now
        orderInfoDTO.setOrderDate(LocalDateTime.now());
        orderInfoDTO.setDeliveryInfoId(savedDelivery.getId());
        orderInfoDTO.setInvoiceId(savedInvoice.getId());

        session.setAttribute("orderInformation", orderInfoDTO);

  
        sessionValidatorService.validateAfterPayment(session);

        // Order order = orderMapper.toEntity(orderInfoDTO, deliveryFormDTO);
        // Order savedOrder = orderRepository.save(order);

        // // 5. Lưu PaymentTransaction
        // PaymentTransaction txn = paymentTxnMapper.toEntity(paymentDTO);
        // txn.setOrder(savedOrder);
        // transactionRepository.save(txn);

        // // 6. Lưu Invoice
        // Invoice inv = invoiceMapper.toEntity(invoiceDTO);
        // inv.setOrder(savedOrder);
        // invoiceRepository.save(inv);

        // // 7. Dọn session
        // session.removeAttribute("cartRequested");
        // session.removeAttribute("invoice");
        // session.removeAttribute("deliveryForm");
        // session.removeAttribute("paymentTransaction");
        // session.removeAttribute("orderInformation");

        // // 8. Trả về DTO kết quả
        // return orderMapper.toDTO(savedOrder);



        // xóa
        System.out.println("Order Information:");
        System.out.println("Order Date: " + orderInfoDTO.getOrderDate());
        System.out.println("Total Price Ex VAT: " + orderInfoDTO.getTotalPriceExVAT());
        System.out.println("Total Price In VAT: " + orderInfoDTO.getTotalPriceInVAT());
        System.out.println("Shipping Fee: " + orderInfoDTO.getShippingFee());
        System.out.println("Total Amount: " + orderInfoDTO.getTotalAmount());
        System.out.println("Currency: " + orderInfoDTO.getCurrency());
        System.out.println("Rush Order: " + orderInfoDTO.isRushOrder());
        System.out.println("Delivery Info ID: " + orderInfoDTO.getDeliveryInfoId());
        System.out.println("Invoice ID: " + orderInfoDTO.getInvoiceId());

        System.out.println("Products:");
        for (CartItemRequestDTO item : orderInfoDTO.getProductList()) {
            System.out.println("- Product ID: " + item.getProductID()
                            + ", Quantity: " + item.getQuantity()
                            + ", Price: " + item.getPrice());
        }
        return orderInfoDTO;
    }
}

