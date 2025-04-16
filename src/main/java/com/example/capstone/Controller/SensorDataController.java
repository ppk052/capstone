package com.example.capstone.Controller;

import com.example.capstone.Network.Request.DeviceToggleRequest;
import com.example.capstone.Service.PlantService;
import com.example.capstone.Service.SensorService;
import com.example.capstone.Websocket.MyWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorService sensorService;
    private final MyWebSocketHandler myWebSocketHandler;
    private final PlantService plantService;

    //각종제어장치 넣어야됨
    @GetMapping("/api/sensor/{id}")
    public ResponseEntity<Map<String, Object>> sendSensorData(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sensorService.getSonsorData(id));
    }

    //장치제어온오프
    @PostMapping("/api/device")
    public ResponseEntity<Void> deviceToggle(@RequestBody DeviceToggleRequest request) {
        log.info("온오프요청 : " + request.toString());
        plantService.deviceToggle(request.getPlantId(), request.getDevice(), request.isNewState());
        String message = switch (request.getDevice()) {
            case "pump" -> "{\"type\":2, \"switch\":" + request.isNewState() + "}";
            case "fan" -> "{\"type\":4, \"switch\":" + request.isNewState() + "}";
            case "LED" -> "{\"type\":1, \"switch\":" + request.isNewState() + "}";
            default -> "";
        };
        myWebSocketHandler.sendMessageToClient(request.getPlantId(), message);
        return ResponseEntity.ok().build();
    }

    //장치자동수동
    @PostMapping("/api/device/auto")
    public ResponseEntity<Void> autoToggle(@RequestBody DeviceToggleRequest request) {
        plantService.deviceAuto(request.getPlantId(),request.getDevice(),request.isNewState());
        String message = "{\"type: 5, \"device\": " + request.getDevice() + "\"switch\": " + request.isNewState() + "}";
        myWebSocketHandler.sendMessageToClient(request.getPlantId(), message);
        return ResponseEntity.ok().build();
    }

    //js파일
    @GetMapping("/js/senor.js")
    public ResponseEntity<Resource> getJs() {
        Resource resource = new ClassPathResource("js/sensor.js");
        return ResponseEntity.ok(resource);
    }
}
