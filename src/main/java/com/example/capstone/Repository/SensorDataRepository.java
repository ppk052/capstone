package com.example.capstone.Repository;

import com.example.capstone.Constant.DataType;
import com.example.capstone.Entity.Plant;
import com.example.capstone.Entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    SensorData findTopByPlantAndDataTypeOrderByTimeDesc(Plant plant, DataType dataType);
}
