package com.example.capstone.Entity;

import com.example.capstone.PlantType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private PlantType plantType;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("time DESC")
    @Column
    private List<SensorData> landMoisture;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("time DESC")
    @Column
    private List<SensorData> temperature;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("time DESC")
    @Column
    private List<SensorData> moisture;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("time DESC")
    @Column
    private List<SensorData> light;

    @Column
    private boolean led;

    @Column
    private boolean pump;

    @Column
    private boolean fan;

    @Column
    private boolean ledAuto;

    @Column
    private boolean pumpAuto;

    @Column
    private boolean fanAuto;
}
