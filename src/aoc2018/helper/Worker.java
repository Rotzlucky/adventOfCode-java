package aoc2018.helper;

import java.util.ArrayList;
import java.util.List;

public class Worker {
    private int offset;
    private int workingTime = -1;
    private String assignedStep;

    public Worker(int offset) {
        this.offset = offset;
    }

    public boolean isWorking() {
        return workingTime > 0;
    }

    public void setAssignedStep(String step) {
        assignedStep = step;
        workingTime = offset + (int) step.toCharArray()[0] - 64;
    }

    public int work() {
        if (workingTime >= 0) {
            workingTime--;
        }

        return workingTime;
    }

    public static List<Worker> getWorkers(int amount, int offset) {
        List<Worker> workers = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            workers.add(new Worker(offset));
        }

        return workers;
    }

    public String getAssignedStep() {
        return assignedStep;
    }
}