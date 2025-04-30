package com.example.capstone.Network.Response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoadPlantResponse {

    private int type;

    private int moisture;

    private int temperature;

    private int light;

    private boolean ledAuto;

    private boolean pumpAuto;

    private boolean fanAuto;
}
