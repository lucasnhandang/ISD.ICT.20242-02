package com.hustict.aims.repository;

import com.hustict.aims.model.shipping.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Long> {
} 