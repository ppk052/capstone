package com.example.capstone.Entity;

import com.example.capstone.PlantType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlantAppropriateValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private PlantType plantType;

    @Column
    private int moisture;

    @Column
    private int temperature;

    @Column
    private int light;
}
