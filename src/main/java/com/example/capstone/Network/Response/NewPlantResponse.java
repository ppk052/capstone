package com.example.capstone.Network.Response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewPlantResponse {

    private Long id;

    private int type;

    private int moisture;

    private int temperature;

    private int light;
}
