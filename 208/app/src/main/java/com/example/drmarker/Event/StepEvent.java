package com.example.drmarker.Event;

public class StepEvent {
    private Long stepNum;

    public void setStepNum(Long stepNum) {
        this.stepNum = stepNum;
    }

    public Long getStepNum() {
        return stepNum;
    }
    public StepEvent(Long num){
        stepNum = num;
    }
}
