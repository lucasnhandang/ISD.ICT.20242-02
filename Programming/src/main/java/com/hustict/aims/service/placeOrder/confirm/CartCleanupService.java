package com.hustict.aims.service.placeOrder.confirm;

import jakarta.servlet.http.HttpSession;



public interface CartCleanupService {
  public void removePurchasedItems(HttpSession session, Long orderid);
    
}
