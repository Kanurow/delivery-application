package com.rowland.engineering.byteworks.repository;

import com.rowland.engineering.byteworks.model.DeliveryGenerated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryGeneratedRepository extends JpaRepository<DeliveryGenerated, Long> {
}
