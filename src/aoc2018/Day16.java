package aoc2018;

import aoc.Day;
import aoc2018.helper.Instruction;
import aoc2018.helper.Operations;
import aoc2018.helper.RegisterSample;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 extends Day {
    public Day16(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        final List<RegisterSample> samples = preparePart1(inputs.subList(0, 3260));

        long sampleCount = samples.stream().filter(sample -> checkSample(sample) >= 3).count();

        printSolution(1, sampleCount);
    }

    private int checkSample(RegisterSample sample) {

        int passedTests = 0;

        passedTests += Operations.testAddRegister(sample);
        passedTests += Operations.testAddImmediate(sample);
        passedTests += Operations.testMultiplyRegister(sample);
        passedTests += Operations.testMultiplyImmediate(sample);
        passedTests += Operations.testBitwiseAndRegister(sample);
        passedTests += Operations.testBitwiseAndImmediate(sample);
        passedTests += Operations.testBitwiseOrRegister(sample);
        passedTests += Operations.testBitwiseOrImmediate(sample);
        passedTests += Operations.testSetRegister(sample);
        passedTests += Operations.testSetImmediate(sample);
        passedTests += Operations.testGreaterThanImmediateRegister(sample);
        passedTests += Operations.testGreaterThanRegisterImmediate(sample);
        passedTests += Operations.testGreaterThanRegisterRegister(sample);
        passedTests += Operations.testEqualImmediateRegister(sample);
        passedTests += Operations.testEqualRegisterImmediate(sample);
        passedTests += Operations.testEqualRegisterRegister(sample);

        return passedTests;
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

    @Override
    protected void part2(List<String> inputs) {

    }
}
