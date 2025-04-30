package com.example.capstone.Entity;

import com.example.capstone.Constant.DataType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plant plant;

    @Column
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Column
    private LocalDateTime time;

    @Column
    private float value;
}
