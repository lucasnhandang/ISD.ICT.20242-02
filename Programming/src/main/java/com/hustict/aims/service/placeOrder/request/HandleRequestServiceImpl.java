package com.hustict.aims.service.placeOrder.request;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.reservation.ReservationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HandleRequestServiceImpl implements HandleRequestService {

    private static final Logger logger = LoggerFactory.getLogger(HandleRequestServiceImpl.class);

    private final ProductAvailabilityService productAvailabilityService;
    private final ReservationService reservationService;

    public HandleRequestServiceImpl(ProductAvailabilityService productAvailabilityService,
                                    ReservationService reservationService) {
        this.productAvailabilityService = productAvailabilityService;
        this.reservationService = reservationService;
    }

    @Override
    public void requestToPlaceOrder(CartRequestDTO cart, String sessionId) {
        logger.info("Processing cart request for sessionId: {}", sessionId);

        if (cart.getProductQuantity() == null || cart.getProductQuantity().isEmpty()) {
            throw new IllegalArgumentException("Empty cart: no products found.");
        }

        productAvailabilityService.checkCartAvailability(cart);
        logger.info("1. Check product availability successfully");
        
        reservationService.createReservation(cart, sessionId);
        logger.info("2. Create reservation successfully");
    }
}
