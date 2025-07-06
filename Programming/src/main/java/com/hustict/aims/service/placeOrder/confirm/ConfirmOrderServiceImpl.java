package com.hustict.aims.service.placeOrder.confirm;

import com.hustict.aims.dto.order.ConfirmOrderRequestDTO;
import com.hustict.aims.service.reservation.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConfirmOrderServiceImpl implements ConfirmOrderService {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmOrderServiceImpl.class);

    private final CartCleanupService cartCleanupService;
    private final ReservationService reservationService;

    public ConfirmOrderServiceImpl(CartCleanupService cartCleanupService, ReservationService reservationService) {
        this.cartCleanupService = cartCleanupService;
        this.reservationService = reservationService;
    }

    @Override
    public void confirmOrder(HttpSession session, ConfirmOrderRequestDTO confirmOrderRequestDTO) {
        logger.info("Success or not?: " + confirmOrderRequestDTO.isSuccess());

        if (confirmOrderRequestDTO.isSuccess()) {
            Long orderId = confirmOrderRequestDTO.getOrderId();

            logger.info("Remove cart for order " + orderId);
            cartCleanupService.removePurchasedItems(session, orderId);

            logger.info("Confirm reservation for order " + orderId);
            reservationService.confirmReservation(session.getId(), orderId);

        } else {
            logger.info("Order was not successful: " + confirmOrderRequestDTO.getOrderId());
        }
    }
}
