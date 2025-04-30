package com.example.capstone.Service;

import com.example.capstone.Constant.DataType;
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
                .led(true)
                .pumpAuto(true)
                .fanAuto(true)
                .ledAuto(true)
                .build()).getId();
    }

    public boolean existsPlant(Long id) {

        return plantRepository.existsById(id);
    }

    public void addSensorData(Long id, float landMoisture, float temperature, float moisture, float light) {
        //log.warn("센서데이터추가 : 토양 "+landMoisture + ", 온도 " + temperature + ", 조도" + light);
        Plant target = plantRepository.findById(id).get();
        LocalDateTime time = LocalDateTime.now();
        sensorDataRepository.save(SensorData.builder()
                .plant(target)
                .dataType(DataType.LandMoisture)
                .time(time)
                .value(landMoisture)
                .build());
        sensorDataRepository.save(SensorData.builder()
                .plant(target)
                .dataType(DataType.Temperature)
                .time(time)
                .value(temperature)
                .build());
        sensorDataRepository.save(SensorData.builder()
                .plant(target)
                .dataType(DataType.Moisture)
                .time(time)
                .value(moisture)
                .build());
        sensorDataRepository.save(SensorData.builder()
                .plant(target)
                .dataType(DataType.Light)
                .time(time)
                .value(light)
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
                            .fan(plant.isFan())
                            .led(plant.isLed())
                            .pump(state)
                            .fanAuto(plant.isFanAuto())
                            .ledAuto(plant.isLedAuto())
                            .pumpAuto(plant.isPumpAuto())
                            .build());
                    break;
                case "fan":
                    plantRepository.save(Plant.builder()
                            .id(plant.getId())
                            .name(plant.getName())
                            .plantType(plant.getPlantType())
                            .fan(state)
                            .led(plant.isLed())
                            .pump(plant.isPump())
                            .fanAuto(plant.isFanAuto())
                            .ledAuto(plant.isLedAuto())
                            .pumpAuto(plant.isPumpAuto())
                            .build());
                    break;
                case "LED":
                    plantRepository.save(Plant.builder()
                            .id(plant.getId())
                            .name(plant.getName())
                            .plantType(plant.getPlantType())
                            .fan(plant.isFan())
                            .led(state)
                            .pump(plant.isPump())
                            .fanAuto(plant.isFanAuto())
                            .ledAuto(plant.isLedAuto())
                            .pumpAuto(plant.isPumpAuto())
                            .build());
                    break;
                default:
                    throw new IllegalArgumentException("장치종류가 올바르지 않습니다!");
            }
        }
    }

    //장치자동온오프
    public void deviceAuto(Long id, String deviceName, boolean state) {
        if(plantRepository.existsById(id)) {
            Plant plant = plantRepository.findById(id).get();
            switch(deviceName) {
                case "pump":
                    plantRepository.save(Plant.builder()
                            .id(plant.getId())
                            .name(plant.getName())
                            .plantType(plant.getPlantType())
                            .fan(plant.isFan())
                            .led(plant.isLed())
                            .pump(plant.isLed())
                            .fanAuto(plant.isFanAuto())
                            .ledAuto(plant.isLedAuto())
                            .pumpAuto(state)
                            .build());
                    break;
                case "fan":
                    plantRepository.save(Plant.builder()
                            .id(plant.getId())
                            .name(plant.getName())
                            .plantType(plant.getPlantType())
                            .led(plant.isLed())
                            .pump(plant.isPump())
                            .fanAuto(state)
                            .ledAuto(plant.isLedAuto())
                            .pumpAuto(plant.isPumpAuto())
                            .build());
                    break;
                case "LED":
                    plantRepository.save(Plant.builder()
                            .id(plant.getId())
                            .name(plant.getName())
                            .plantType(plant.getPlantType())
                            .fan(plant.isFan())
                            .led(plant.isLed())
                            .pump(plant.isPump())
                            .fanAuto(plant.isFanAuto())
                            .ledAuto(state)
                            .pumpAuto(plant.isPumpAuto())
                            .build());
                    break;
                default:
                    break;
            }
        }
    }
}
