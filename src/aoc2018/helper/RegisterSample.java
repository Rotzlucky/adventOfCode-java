package aoc2018.helper;

public class RegisterSample {
    private int[] registersBefore;
    private Instruction instruction;
    private int[] registersAfter;

    public int[] getRegistersBefore() {
        return registersBefore;
    }

    public void setRegistersBefore(int[] registersBefore) {
        this.registersBefore = registersBefore;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public int[] getRegistersAfter() {
        return registersAfter;
    }

    public void setRegistersAfter(int[] registersAfter) {
        this.registersAfter = registersAfter;
    }
}
