package com.hustict.aims.repository;

import com.hustict.aims.model.shipping.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Long> {

    @Query("SELECT d FROM DeliveryInfo d WHERE d.phoneNumber = :phoneNumber")
    Optional<DeliveryInfo> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE DeliveryInfo d SET d.address = :address, d.province = :province, d.shippingInstruction = :instruction WHERE d.id = :id")
    void updateDeliveryDetailsById(
        @Param("id") Long id,
        @Param("address") String address,
        @Param("province") String province,
        @Param("instruction") String instruction
    );
}
