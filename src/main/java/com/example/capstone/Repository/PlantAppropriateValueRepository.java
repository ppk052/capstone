package com.example.capstone.Repository;

import com.example.capstone.Entity.PlantAppropriateValue;
import com.example.capstone.PlantType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlantAppropriateValueRepository extends JpaRepository<PlantAppropriateValue, Long> {

    Optional<PlantAppropriateValue> findByPlantType(PlantType plantType);
}
