package tland.emu;

/**
 * Emulator interface.
 */
public interface IEmulator {
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
     * Wrire the specified value to the specified memory address.
     * 
     * @param address The address to write to.
     * @param value   The value to write to the memory address.
     */
    void writeMemoryAddress(short address, byte value);

    /**
     * Halt ('stop') all operations of the emulator.
     * <p>
     * Note that the stop instruction is actually 2 bytes long, with the second byte
     * being ignored.
     */
    void stop();

    /**
     * Halt CPU instruction execution for some time in milliseconds.
     * 
     * @param millis The time (in ms) to halt the CPU for.
     */
    void halt(short millis);

    /**
     * Undecided
     */
    void undecidedDI();

    /**
     * Undecided
     */
    void undecidedEI();

    /**
     * Get the ProgramCounter stored in the IEmulator object.
     * 
     * @return The program counter object.
     */
    ProgramCounter pc();

    /**
     * Get the StackPointer stored in the IEmulator object.
     * 
     * @return The stack pointer object.
     */
    StackPointer sp();

    /**
     * Get the Registers stored in the IEmulator object.
     * 
     * @return The registers object.
     */
    Registers reg();
}
