package com.example.capstone.Network.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DeviceToggleRequest {

    private Long plantId;

    private String device;

    private boolean newState;
}
