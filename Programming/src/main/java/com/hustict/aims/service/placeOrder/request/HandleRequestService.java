package com.hustict.aims.service.placeOrder.request;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.placeOrder.ProductAvailabilityService;
import com.hustict.aims.service.reservation.ReservationService;


@Service
public class HandleRequestService {
   
    private final ProductAvailabilityService productAvailabilityService;
    private final ReservationService reservationService;
    
    public HandleRequestService(ProductAvailabilityService productAvailabilityService, 
                                ReservationService reservationService) {
        this.productAvailabilityService = productAvailabilityService;
        this.reservationService = reservationService;
    }

    public void requestToPlaceOrder(CartRequestDTO cart,String sessionId) {
        // xóa
        System.out.println("Processing cart request...");
        
        if (cart.getProductQuantity() == null || cart.getProductQuantity().isEmpty()) {
            throw new IllegalArgumentException("Empty cart: no products found.");
        }

        productAvailabilityService.checkCartAvailability(cart);

        // xóa
        System.out.println("1. Check product availability successfully");
        
        reservationService.createReservation(cart,sessionId);

        // xóa
        System.out.println("2.CreateReservationSuccessfully");
    }
}