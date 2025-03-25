package com.example.capstone.Websocket;

import com.example.capstone.Entity.Plant;
import com.example.capstone.Entity.PlantAppropriateValue;
import com.example.capstone.Entity.SensorData;
import com.example.capstone.Network.Request.PlantRequest;
import com.example.capstone.Network.Response.LoadPlantResponse;
import com.example.capstone.Network.Response.NewPlantResponse;
import com.example.capstone.PlantType;
import com.example.capstone.Repository.PlantRepository;
import com.example.capstone.Service.PlantService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {
    // 세션을 관리하는 ConcurrentHashMap (key: ID, value: WebSocketSession)
    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final PlantService plantService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        log.info("웹소켓 연결 종료됨: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        PlantRequest request = convertToObject(payload);
        PlantAppropriateValue appropriateValue = plantService.getAppropriateValue(PlantType.valueOf(request.getPlantType()));
        log.info("웹소켓 연결됨 : " + session.getId());
        switch(request.getType()) {
            //새식물
            case 0:
                Long newPlantId = plantService.newPlant(request.getName(),request.getPlantType());
                sessions.put(newPlantId,session);
                session.sendMessage(new TextMessage(convertToJson(NewPlantResponse.builder()
                        .id(newPlantId)
                        .type(0)
                        .moisture(appropriateValue.getMoisture())
                        .temperature(appropriateValue.getTemperature())
                        .light(appropriateValue.getLight())
                        .build())));
                break;
            //식물재연결
            case 1:
                if(sessions.containsKey(request.getId()) && plantService.existsPlant(request.getId())) {
                    sessions.remove(request.getId());
                }
                sessions.put(request.getId(), session);
                session.sendMessage(new TextMessage(convertToJson(LoadPlantResponse.builder()
                        .type(3)
                        .build())));
                break;
            //센서데이터전송
            case 2:
                plantService.addSensorData(request.getId(), request.getLandMoisture(), request.getTemperature(), request.getMoisture(), request.getLight());
                break;
            default:
                break;
        }
    }

    // ✅ 서버에서 특정 클라이언트에게 메시지 보내기
    public void sendMessageToClient(Long id, String message) {
        WebSocketSession session = sessions.get(id);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                log.info("메시지 전송: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private PlantRequest convertToObject(String json) throws IOException{
        return objectMapper.readValue(json, PlantRequest.class);
    }

    private String convertToJson(Object response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response);
    }
}
