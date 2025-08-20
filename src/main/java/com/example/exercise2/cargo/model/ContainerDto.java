package com.example.exercise2.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContainerDto {
    private String containerNumber;
    private String origin;
    private String destination;
    private double weight;

}
