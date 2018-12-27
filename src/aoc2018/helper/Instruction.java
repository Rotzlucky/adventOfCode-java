package aoc2018.helper;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
    private String opName;
    private int opCode;
    private int A;
    private int B;
    private int C;

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public static List<Instruction> getInstructions(List<String> inputs) {
        List<Instruction> instructions = new ArrayList<>();
        for (String instructionString : inputs.subList(1, inputs.size())) {
            instructions.add(getInstruction(instructionString));
        }
        return instructions;
    }

    public static Instruction getInstruction(String instructionString) {
        Instruction instruction = new Instruction();

        String[] split = instructionString.split(" ");

        instruction.setOpName(split[0]);
        instruction.setA(Integer.parseInt(split[1]));
        instruction.setB(Integer.parseInt(split[2]));
        instruction.setC(Integer.parseInt(split[3]));

        return instruction;
    }
}
