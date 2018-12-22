package aoc2018;

import aoc.Day;
import aoc2018.helper.Instruction;
import aoc2018.helper.Operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Explanation for future:
 * For part 1 we use instructions 17 - 26 to calculate a value of 835 for R[3]
 * For part 2, since R[0] == 1, we skip instruction 26, resulting in instructions 27 - 35 calculating a value of 10161475 for R[3]
 *
 * After that both parts continue from instructions 1
 *
 * The program then basically behaves like this
 * 1. We loop through the instructions 3 - 11, always skipping instruction 7 and in instruction 8 increasing R[2] by 1
 * 2. Instruction 4 evaluates R[1] == R[3]. (R[1] = R[2] * [R5] from instruction 3)
 * 4. Onl if this is true we execute instruction 7 which adds the value of R[5] to R[0]
 * 5. After that the loop continues until R[2] is greater than R[3]
 * 6. When this is true we skip instruction 11 and increase R[5] by 1
 * 7. As long as R[5] is not greater as R[3] we start again at 1
 * 8. As soon as R[5] is greater than R[3] we skip instruction 15
 * 9. instruction 16 ends the program
 *
 * This means that to reach the end condition we have to loop R[3]^2. This is okay for part1, but not for part2
 *
 * So we are basically running this loop
 *
 * for R[5] in range(1, R[3] + 1):
 *   for R[2] in range(1, R[3] + 1):
 *      if (R[2] * R[5] == R[3])
 *          R[0] += R[5]
 *
 * So we are adding up all factors of R[3] to R[0], which we can calculate with only one loop
 *
 * for x in range(1, R[3] + 1):
 *    if (R[3] % x == 0)
 *      R[0] += R[5]
 *
 */
public class Day19 extends Day {

    public Day19(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        int startRegister = Integer.parseInt(inputs.get(0).replaceAll("#ip (\\d)", "$1"));

        List<Instruction> instructions = getInstructions(inputs);

        int[] register = new int[6];
        int solution = runSubTask(startRegister, instructions, register);

        printSolution(1, solution);
    }

    @Override
    protected void part2(List<String> inputs) {
        int startRegister = Integer.parseInt(inputs.get(0).replaceAll("#ip (\\d)", "$1"));

        List<Instruction> instructions = getInstructions(inputs);

        int[] register = new int[6];
        register[0] = 1;
        int solution = runSubTask(startRegister, instructions, register);

        printSolution(2, solution);
    }

    private int runSubTask(int startRegister, List<Instruction> instructions, int[] register) {
        int instructionPointer = register[startRegister];

        int solution = 0;
        while (instructionPointer >= 0 && instructionPointer < instructions.size()) {

            if (instructionPointer == 1) {
                // when we get here the value that should be factorized is initialized.
                // It's unclear if this would be correct for every input value
                int initializedValue = Arrays.stream(register).max().orElse(0);

                for (int i = 1; i < initializedValue + 1; i++) {
                    if (initializedValue % i == 0) {
                        solution += i;
                    }
                }

                break;
            }

            Instruction instruction = instructions.get(instructionPointer);
            register = Operations.execute(instruction.getOpName(), instruction, register);
            //printRegister(register);
            instructionPointer = register[startRegister];
            instructionPointer++;

            if (instructionPointer < instructions.size()) {
                register[startRegister] = instructionPointer;
            }
        }

        return solution;
    }

    private void printRegister(int[] register) {
        for (int i : register) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    private List<Instruction> getInstructions(List<String> inputs) {
        List<Instruction> instructions = new ArrayList<>();
        for (String instructionString : inputs.subList(1, inputs.size())) {
            instructions.add(getInstruction(instructionString));
        }
        return instructions;
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
