package com.cricket.pointstable.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Score {
    private int runs;
    private int balls;
    private int wickets;
    private int points;
    private int target;
    private int totalBalls;
    private boolean allOut;
}
