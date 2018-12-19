package aoc2018.helper;

public class Operations {


    public static int testAddRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (registersBefore[instruction.getA()] + registersBefore[instruction.getB()] == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testAddImmediate(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (registersBefore[instruction.getA()] + instruction.getB() == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testMultiplyRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (registersBefore[instruction.getA()] * registersBefore[instruction.getB()] == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testMultiplyImmediate(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (registersBefore[instruction.getA()] * instruction.getB() == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testBitwiseAndRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        int bitwise = registersBefore[instruction.getA()] & registersBefore[instruction.getB()];
        if ( bitwise == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testBitwiseAndImmediate(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        int bitwise = registersBefore[instruction.getA()] & instruction.getB();
        if ( bitwise == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testBitwiseOrRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        int bitwise = registersBefore[instruction.getA()] | registersBefore[instruction.getB()];
        if ( bitwise == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testBitwiseOrImmediate(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        int bitwise = registersBefore[instruction.getA()] | instruction.getB();
        if ( bitwise == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testSetRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if ( registersBefore[instruction.getA()] == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testSetImmediate(RegisterSample sample) {
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if ( instruction.getA() == registersAfter[instruction.getC()]) {
            return 1;
        }

        return 0;
    }

    public static int testGreaterThanImmediateRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (instruction.getA() > registersBefore[instruction.getB()] && registersAfter[instruction.getC()] == 1) {
            return 1;
        } else if (instruction.getA() <= registersBefore[instruction.getB()] && registersAfter[instruction.getC()] == 0) {
            return 1;
        }

        return 0;
    }

    public static int testGreaterThanRegisterImmediate(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (registersBefore[instruction.getA()] > instruction.getB() && registersAfter[instruction.getC()] == 1) {
            return 1;
        } else if (registersBefore[instruction.getA()] <= instruction.getB() && registersAfter[instruction.getC()] == 0) {
            return 1;
        }

        return 0;
    }

    public static int testGreaterThanRegisterRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (registersBefore[instruction.getA()] > registersBefore[instruction.getB()] && registersAfter[instruction.getC()] == 1) {
            return 1;
        } else if (registersBefore[instruction.getA()] <= registersBefore[instruction.getB()] && registersAfter[instruction.getC()] == 0) {
            return 1;
        }

        return 0;
    }

    public static int testEqualImmediateRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (instruction.getA() == registersBefore[instruction.getB()] && registersAfter[instruction.getC()] == 1) {
            return 1;
        } else if (instruction.getA() != registersBefore[instruction.getB()] && registersAfter[instruction.getC()] == 0) {
            return 1;
        }

        return 0;
    }

    public static int testEqualRegisterImmediate(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (registersBefore[instruction.getA()] == instruction.getB() && registersAfter[instruction.getC()] == 1) {
            return 1;
        } else if (registersBefore[instruction.getA()] != instruction.getB() && registersAfter[instruction.getC()] == 0) {
            return 1;
        }

        return 0;
    }

    public static int testEqualRegisterRegister(RegisterSample sample) {
        int[] registersBefore = sample.getRegistersBefore();
        int[] registersAfter = sample.getRegistersAfter();
        Instruction instruction = sample.getInstruction();

        if (registersBefore[instruction.getA()] == registersBefore[instruction.getB()] && registersAfter[instruction.getC()] == 1) {
            return 1;
        } else if (registersBefore[instruction.getA()] != registersBefore[instruction.getB()] && registersAfter[instruction.getC()] == 0) {
            return 1;
        }

        return 0;
    }

    public static int[] execute(String opCodeString, Instruction instruction, int[] register) {
        switch (opCodeString) {
            case "addr":
                register[instruction.getC()] = register[instruction.getA()] + register[instruction.getB()];
                break;
            case "addi":
                register[instruction.getC()] = register[instruction.getA()] + instruction.getB();
                break;
            case "mulr":
                register[instruction.getC()] = register[instruction.getA()] * register[instruction.getB()];
                break;
            case "muli":
                register[instruction.getC()] = register[instruction.getA()] * instruction.getB();
                break;
            case "banr":
                register[instruction.getC()] = register[instruction.getA()] & register[instruction.getB()];
                break;
            case "bani":
                register[instruction.getC()] = register[instruction.getA()] & instruction.getB();
                break;
            case "borr":
                register[instruction.getC()] = register[instruction.getA()] | register[instruction.getB()];
                break;
            case "bori":
                register[instruction.getC()] = register[instruction.getA()] | instruction.getB();
                break;
            case "setr":
                register[instruction.getC()] = register[instruction.getA()];
                break;
            case "seti":
                register[instruction.getC()] = instruction.getA();
                break;
            case "gtir":
                register[instruction.getC()] = instruction.getA() > register[instruction.getB()] ? 1 : 0;
                break;
            case "gtri":
                register[instruction.getC()] = register[instruction.getA()] > instruction.getB() ? 1 : 0;
                break;
            case "gtrr":
                register[instruction.getC()] = register[instruction.getA()] > register[instruction.getB()] ? 1 : 0;
                break;
            case "eqir":
                register[instruction.getC()] = instruction.getA() == register[instruction.getB()] ? 1 : 0;
                break;
            case "eqri":
                register[instruction.getC()] = register[instruction.getA()] == instruction.getB() ? 1 : 0;
                break;
            case "eqrr":
                register[instruction.getC()] = register[instruction.getA()] == register[instruction.getB()] ? 1 : 0;
                break;
        }

        return register;
    }
}
