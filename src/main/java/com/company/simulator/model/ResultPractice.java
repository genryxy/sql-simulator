package com.company.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultPractice {
    private Practice practice;

    private int total;

    private int score;
}
