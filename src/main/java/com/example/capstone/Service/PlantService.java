package com.example.capstone.Service;

import com.example.capstone.Entity.Plant;
import com.example.capstone.Entity.PlantAppropriateValue;
import com.example.capstone.Entity.SensorData;
import com.example.capstone.PlantType;
import com.example.capstone.Repository.PlantAppropriateValueRepository;
import com.example.capstone.Repository.PlantRepository;
import com.example.capstone.Repository.SensorDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlantService {

    private final PlantRepository plantRepository;
    private final SensorDataRepository sensorDataRepository;
    private final PlantAppropriateValueRepository plantAppropriateValueRepository;

    public Long newPlant(String name, String plantString) {

        PlantType plantType = null;
        if(name == null && plantString == null) {
            throw new IllegalArgumentException("name과 plantType을 입력해주세요!");
        }
        try {
            plantType = PlantType.valueOf(plantString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("식물이름이 올바르지 않거나 해당 식물을 지원하지 않습니다!");
        }
        return plantRepository.save(Plant.builder()
                .name(name)
                .plantType(plantType)
                .led(false)
                .pump(false)
                .fan(false)
                .build()).getId();
    }

    public Plant editPlant(Long id, PlantType plantType, String name) {
        if(plantRepository.existsById(id)) {
            Plant plant = plantRepository.findById(id).get();
            return plantRepository.save(Plant.builder()
                    .id(id)
                    .name(name==null? plant.getName() : name)
                    .plantType(plantType==null? plant.getPlantType() : plantType)
                    .landMoisture(plant.getLandMoisture())
                    .temperature(plant.getTemperature())
                    .moisture(plant.getMoisture())
                    .light(plant.getLight())
                    .build());
        } else {
            throw new IllegalArgumentException("해당 ID로된 식물이 없습니다!");
        }
    }

    public boolean existsPlant(Long id) {

        return plantRepository.existsById(id);
    }

    public void addSensorData(Long id, float landMoisture, float temperature, float moisture, float light, boolean led, boolean pump, boolean fan ) {
        Plant target = plantRepository.findById(id).get();
        List<SensorData> landMoistures = target.getLandMoisture();
        List<SensorData> temperatures = target.getTemperature();
        List<SensorData> moistures = target.getMoisture();
        List<SensorData> lights = target.getLight();
        LocalDateTime time = LocalDateTime.now();
        landMoistures.add(sensorDataRepository.save(SensorData.builder()
                .time(time)
                .value(landMoisture)
                .build()));
        temperatures.add(sensorDataRepository.save(SensorData.builder()
                .time(time)
                .value(temperature)
                .build()));
        moistures.add(sensorDataRepository.save(SensorData.builder()
                .time(time)
                .value(moisture)
                .build()));
        lights.add(sensorDataRepository.save(SensorData.builder()
                .time(time)
                .value(light)
                .build()));
        plantRepository.save(Plant.builder()
                .id(target.getId())
                .name(target.getName())
                .plantType(target.getPlantType())
                .landMoisture(landMoistures)
                .temperature(temperatures)
                .moisture(moistures)
                .light(lights)
                .led(led)
                .pump(pump)
                .fan(fan)
                .build());
    }

    public PlantAppropriateValue getAppropriateValue(PlantType plantType) {
        return plantAppropriateValueRepository.findByPlantType(plantType).get();
    }

    public void deviceToggle(Long id, String deviceName, boolean state) {
        if(plantRepository.existsById(id)) {
            Plant plant = plantRepository.findById(id).get();
            switch(deviceName) {
                case "pump":
                    plantRepository.save(Plant.builder()
                            .id(plant.getId())
                            .name(plant.getName())
                            .plantType(plant.getPlantType())
                            .landMoisture(plant.getLandMoisture())
                            .temperature(plant.getTemperature())
                            .moisture(plant.getMoisture())
                            .light(plant.getLight())
                            .fan(plant.isFan())
                            .led(plant.isLed())
                            .pump(state)
                            .build());
                    break;
                case "fan":
                    plantRepository.save(Plant.builder()
                            .id(plant.getId())
                            .name(plant.getName())
                            .plantType(plant.getPlantType())
                            .landMoisture(plant.getLandMoisture())
                            .temperature(plant.getTemperature())
                            .moisture(plant.getMoisture())
                            .light(plant.getLight())
                            .fan(state)
                            .led(plant.isLed())
                            .pump(plant.isPump())
                            .build());
                    break;
                case "LED":
                    plantRepository.save(Plant.builder()
                            .id(plant.getId())
                            .name(plant.getName())
                            .plantType(plant.getPlantType())
                            .landMoisture(plant.getLandMoisture())
                            .temperature(plant.getTemperature())
                            .moisture(plant.getMoisture())
                            .light(plant.getLight())
                            .fan(plant.isFan())
                            .led(state)
                            .pump(plant.isPump())
                            .build());
                    break;
                default:
                    throw new IllegalArgumentException("장치종류가 올바르지 않습니다!");
            }
        }
    }
}
