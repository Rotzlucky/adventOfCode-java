package aoc2018;

import aoc.Day;
import aoc2018.helper.Instruction;
import aoc2018.helper.Operations;

import java.util.ArrayList;
import java.util.List;

public class Day19 extends Day {
    
    public Day19(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        int startRegister = Integer.parseInt(inputs.get(0).replaceAll("#ip (\\d)", "$1"));

        List<Instruction> instructions = getInstructions(inputs);

        int[] register = new int[6];
        int instructionPointer = register[startRegister];

        while (instructionPointer >= 0 && instructionPointer < instructions.size()) {
            Instruction instruction = instructions.get(instructionPointer);
            register = Operations.execute(instruction.getOpName(), instruction, register);
            instructionPointer = register[startRegister];
            instructionPointer++;

            if (instructionPointer < instructions.size()) {
                register[startRegister] = instructionPointer;
            }
        }

        printSolution(1, register[0]);
    }

    private List<Instruction> getInstructions(List<String> inputs) {
        List<Instruction> instructions = new ArrayList<>();
        for (String instructionString : inputs.subList(1, inputs.size())) {
            instructions.add(getInstruction(instructionString));
        }
        return instructions;
    }

    @Override
    protected void part2(List<String> inputs) {

    }

    private Instruction getInstruction(String instructionString) {
        Instruction instruction = new Instruction();

        String[] split = instructionString.split(" ");

        instruction.setOpName(split[0]);
        instruction.setA(Integer.parseInt(split[1]));
        instruction.setB(Integer.parseInt(split[2]));
        instruction.setC(Integer.parseInt(split[3]));

        return instruction;
    }
}
