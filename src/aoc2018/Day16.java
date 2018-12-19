package aoc2018;

import aoc.Day;
import aoc2018.helper.Instruction;
import aoc2018.helper.Operations;
import aoc2018.helper.RegisterSample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 extends Day {
    public Day16(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        final List<RegisterSample> samples = preparePart1(inputs.subList(0, 3260));

        long sampleCount = samples.stream().filter(sample -> getPossibleOperations(sample).size() >= 3).count();

        printSolution(1, sampleCount);
    }

    @Override
    protected void part2(List<String> inputs) {
        final List<RegisterSample> samples = preparePart1(inputs.subList(0, 3260));

        Map<Integer, String> opCodeMapping = identifyOpCodes(samples);

        List<Instruction> instructions = preparePart2(inputs.subList(3260, inputs.size()));

        int[] register = new int[4];

        for (Instruction instruction : instructions) {
            String opCodeString = opCodeMapping.get(instruction.getOpCode());
            register = Operations.execute(opCodeString, instruction, register);
        }

        printSolution(2, register[0]);
    }

    private Map<Integer, String> identifyOpCodes(List<RegisterSample> samples) {

        Map<Integer, String> opCodeMap = new HashMap<>();

        List<String> ignoreOperations = new ArrayList<>();

        for (RegisterSample sample : samples) {
            int opCode = sample.getInstruction().getOpCode();

            List<String> possibleOperations = getPossibleOperations(sample, ignoreOperations);

            if (possibleOperations.size() == 1) {
                ignoreOperations.addAll(possibleOperations);
                opCodeMap.put(opCode, possibleOperations.get(0));
            }
        }

        return opCodeMap;
    }

    private List<String> getPossibleOperations(RegisterSample sample) {
        return getPossibleOperations(sample, Collections.emptyList());
    }

    private List<String> getPossibleOperations(RegisterSample sample, List<String> ignoreOperations) {
        List<String> operations = new ArrayList<>();
        if (Operations.testAddRegister(sample) == 1 && !ignoreOperations.contains("addr")) {
            operations.add("addr");
        }
        if (Operations.testAddImmediate(sample) == 1 && !ignoreOperations.contains("addi")) {
            operations.add("addi");
        }
        if (Operations.testMultiplyRegister(sample) == 1 && !ignoreOperations.contains("mulr")) {
            operations.add("mulr");
        }
        if (Operations.testMultiplyImmediate(sample) == 1 && !ignoreOperations.contains("muli")) {
            operations.add("muli");
        }
        if (Operations.testBitwiseAndRegister(sample) == 1 && !ignoreOperations.contains("banr")) {
            operations.add("banr");
        }
        if (Operations.testBitwiseAndImmediate(sample) == 1 && !ignoreOperations.contains("bani")) {
            operations.add("bani");
        }
        if (Operations.testBitwiseOrRegister(sample) == 1 && !ignoreOperations.contains("borr")) {
            operations.add("borr");
        }
        if (Operations.testBitwiseOrImmediate(sample) == 1 && !ignoreOperations.contains("bori")) {
            operations.add("bori");
        }
        if (Operations.testSetRegister(sample) == 1 && !ignoreOperations.contains("setr")) {
            operations.add("setr");
        }
        if (Operations.testSetImmediate(sample) == 1 && !ignoreOperations.contains("seti")) {
            operations.add("seti");
        }
        if (Operations.testGreaterThanImmediateRegister(sample) == 1 && !ignoreOperations.contains("gtir")) {
            operations.add("gtir");
        }
        if (Operations.testGreaterThanRegisterImmediate(sample) == 1 && !ignoreOperations.contains("gtri")) {
            operations.add("gtri");
        }
        if (Operations.testGreaterThanRegisterRegister(sample) == 1 && !ignoreOperations.contains("gtrr")) {
            operations.add("gtrr");
        }
        if (Operations.testEqualImmediateRegister(sample) == 1 && !ignoreOperations.contains("eqir")) {
            operations.add("eqir");
        }
        if (Operations.testEqualRegisterImmediate(sample) == 1 && !ignoreOperations.contains("eqri")) {
            operations.add("eqri");
        }
        if (Operations.testEqualRegisterRegister(sample) == 1 && !ignoreOperations.contains("eqrr")) {
            operations.add("eqrr");
        }

        return operations;
    }

    private List<RegisterSample> preparePart1(List<String> inputs) {
        List<RegisterSample> samples = new ArrayList<>();

        for (int i = 0; i < inputs.size(); i += 4) {
            RegisterSample sample = new RegisterSample();
            sample.setRegistersBefore(getRegisters(inputs.get(i)));
            sample.setInstruction(getInstruction(inputs.get(i + 1)));
            sample.setRegistersAfter(getRegisters(inputs.get(i + 2)));
            samples.add(sample);
        }

        return samples;
    }

    private List<Instruction> preparePart2(List<String> inputs) {
        List<Instruction> instructions = new ArrayList<>();

        for (String input : inputs) {
            if (input.isEmpty()) {
                continue;
            }

            instructions.add(getInstruction(input));
        }

        return instructions;
    }

    private Instruction getInstruction(String instructionString) {
        Instruction instruction = new Instruction();

        String[] split = instructionString.split(" ");

        instruction.setOpCode(Integer.parseInt(split[0]));
        instruction.setA(Integer.parseInt(split[1]));
        instruction.setB(Integer.parseInt(split[2]));
        instruction.setC(Integer.parseInt(split[3]));

        return instruction;
    }

    private int[] getRegisters(String registers) {
        int[] register = new int[4];

        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(registers);

        matcher.find();
        register[0] = Integer.parseInt(matcher.group(0));

        matcher.find();
        register[1] = Integer.parseInt(matcher.group(0));

        matcher.find();
        register[2] = Integer.parseInt(matcher.group(0));

        matcher.find();
        register[3] = Integer.parseInt(matcher.group(0));

        return register;
    }
}
