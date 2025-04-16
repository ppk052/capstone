package com.example.capstone.Service;

import com.example.capstone.Entity.Plant;
import com.example.capstone.Entity.PlantAppropriateValue;
import com.example.capstone.Repository.PlantAppropriateValueRepository;
import com.example.capstone.Repository.PlantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorService {

    private final PlantRepository plantRepository;
    private final PlantAppropriateValueRepository plantAppropriateValueRepository;

    public Map<String, Object> getSonsorData(Long id) {
        if(id == 0) {
            Map<String, Object> sensorData = new HashMap<>();
            sensorData.put("landMoisture", 0);
            sensorData.put("temperature", 0);
            sensorData.put("light", 0);
            sensorData.put("LED" , false);
            sensorData.put("pump", false);
            sensorData.put("fan", false);
            sensorData.put("landMoistureAppropriate", 0);
            sensorData.put("temperatureAppropriate", 0);
            sensorData.put("lightAppropriate", 0);
            return sensorData;
        } else {
            Optional<Plant> targetPlant = plantRepository.findById(id);
            if(targetPlant.isPresent() && plantAppropriateValueRepository.findByPlantType(targetPlant.get().getPlantType()).isPresent()) {
                PlantAppropriateValue plantValue = plantAppropriateValueRepository.findByPlantType(targetPlant.get().getPlantType()).get();
                Map<String, Object> sensorData = new HashMap<>();
                sensorData.put("landMoisture", targetPlant.get().getLandMoisture().get(0).getValue());
                sensorData.put("temperature", targetPlant.get().getTemperature().get(0).getValue());
                sensorData.put("light", targetPlant.get().getLight().get(0).getValue());
                sensorData.put("LED" , targetPlant.get().isLed());
                sensorData.put("pump", targetPlant.get().isPump());
                sensorData.put("fan", targetPlant.get().isFan());
                sensorData.put("landMoistureAppropriate",plantValue.getMoisture());
                sensorData.put("temperatureAppropriate",plantValue.getTemperature());
                sensorData.put("lightAppropriate",plantValue.getLight());
                sensorData.put("LEDAuto",targetPlant.get().isLedAuto());
                sensorData.put("pumpAuto",targetPlant.get().isPumpAuto());
                sensorData.put("fanAuto",targetPlant.get().isFanAuto());
                return sensorData;
            } else {
                throw new IllegalArgumentException("id가 올바르지 않습니다!");
            }
        }
    }

    public PlantAppropriateValue getAppropriate(Long id) {
        Optional<Plant> targetPlant = plantRepository.findById(id);
        if(targetPlant.isPresent()) {
            Optional<PlantAppropriateValue> targetValue = plantAppropriateValueRepository.findByPlantType(targetPlant.get().getPlantType());
            if(targetValue.isPresent()) {
                return targetValue.get();
            } else {
                throw new IllegalArgumentException("해당 식물의 적정 값이 없습니다!");
            }
        } else {
            throw new IllegalArgumentException("id가 올바르지 않습니다!");
        }
    }
}
