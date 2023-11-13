package com.rowland.engineering.byteworks.repository;

import com.rowland.engineering.byteworks.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findByLocation(String location);
}
