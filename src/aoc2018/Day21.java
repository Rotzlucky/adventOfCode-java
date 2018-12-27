package aoc2018;

import aoc.Day;
import aoc2018.helper.Instruction;
import aoc2018.helper.Operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day21 extends Day {

    public Day21(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        int startRegister = Integer.parseInt(inputs.get(0).replaceAll("#ip (\\d)", "$1"));

        List<Instruction> instructions = Instruction.getInstructions(inputs);

        int[] register = new int[6];
        printSolution(1, runSubTask(startRegister, instructions, register, true));
    }

    @Override
    protected void part2(List<String> inputs) {
        int startRegister = Integer.parseInt(inputs.get(0).replaceAll("#ip (\\d)", "$1"));

        List<Instruction> instructions = Instruction.getInstructions(inputs);

        int[] register = new int[6];
        printSolution(1, runSubTask(startRegister, instructions, register, false));
    }

    private int runSubTask(int startRegister, List<Instruction> instructions, int[] register, boolean returnOnFirst) {
        int instructionPointer = register[startRegister];

        Set<Integer> seenValues = new HashSet<>();
        int value = 0;

        while (instructionPointer >= 0 && instructionPointer < instructions.size()) {

            if (instructionPointer == 28) {

                //part 1
                if (returnOnFirst) {
                    return register[2];
                }
                //part 2
                else {
                    if (!seenValues.contains(register[2])) {
                        seenValues.add(register[2]);
                        value = register[2];
                    } else {
                        return value;
                    }
                }
            }

            Instruction instruction = instructions.get(instructionPointer);
            register = Operations.execute(instruction.getOpName(), instruction, register);
            instructionPointer = register[startRegister];

            instructionPointer++;

            if (instructionPointer < instructions.size()) {
                register[startRegister] = instructionPointer;
            }

        }

        return 0;
    }
}
