package tland.emu.cpu.inst;

/**
 * Exception for illegal instructions.
 * 
 * @author Tarjei Landøy
 */
public class IllegalInstructionException extends RuntimeException {
    public IllegalInstructionException() {}
    public IllegalInstructionException(String msg) {}
}
