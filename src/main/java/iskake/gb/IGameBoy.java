package iskake.gb;

import iskake.gb.pointer.ProgramCounter;
import iskake.gb.pointer.StackPointer;

/**
 * Game Boy interface.
 * <p>
 * Should be used instead of the Game Boy class (unless special access is needed
 * (for example, Interpreter or Debugger vs. Instruction.))
 */
public interface IGameBoy {
    /**
     * Read the next byte value in memory.
     * 
     * @return The next byte in memory.
     */
    byte readNextByte();

    /**
     * Read the next short value in memory, consisting of the two next bytes in
     * memory combined (little-endian).
     * 
     * @return The next short in memory.
     */
    short readNextShort();

    /**
     * Read the byte at the specified memory address.
     * 
     * @return The byte stored at the specified memory address.
     */
    byte readMemoryAddress(short address);

    /**
     * Read the byte at the specified memory address, without increasing the cycle
     * count.
     * 
     * @return The byte stored at the specified memory address.
     */
    byte readMemoryNoCycle(short address);

    /**
     * Write the specified value to the specified memory address.
     * 
     * @param address The address to write to.
     * @param value   The value to write to the memory address.
     */
    void writeMemoryAddress(short address, byte value);

    /**
     * Write the specified value to the specified memory address, without increasing
     * the cycle count.
     * 
     * @param address The address to write to.
     * @param value   The value to write to the memory address.
     */
    void writeMemoryNoCycle(short address, byte value);

    /**
     * Stop opcode all operations of the GameBoy.
     * <p>
     * Note that the stop instruction is actually 2 bytes long, with the second byte
     * being ignored.
     */
    void stop();

    /**
     * Halt CPU instruction execution.
     */
    void halt();

    /**
     * Get the ProgramCounter stored in the IGameBoy object.
     * 
     * @return The program counter object.
     */
    ProgramCounter pc();

    /**
     * Get the StackPointer stored in the IGameBoy object.
     * 
     * @return The stack pointer object.
     */
    StackPointer sp();

    /**
     * Get the Registers stored in the IGameBoy object.
     * 
     * @return The registers object.
     */
    Registers reg();
}
