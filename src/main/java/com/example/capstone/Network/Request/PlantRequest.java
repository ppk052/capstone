package com.example.capstone.Network.Request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlantRequest {

    private String name;

    private String plantType;

    private Long id;

    private float landMoisture;

    private float temperature;

    private float moisture;

    private float light;

    private int type;
}
