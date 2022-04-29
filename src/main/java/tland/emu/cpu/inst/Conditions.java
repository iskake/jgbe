package tland.emu.cpu.inst;

import tland.emu.Registers;
import tland.emu.Registers.Flags;

/**
 * Conditions for {@code JP}, {@code JR}, {@code CALL}, and {@code RET}
 * instuctions.
 * 
 * @author Tarjei Landøy
 */
public enum Conditions {
    /** No condition (condition always satisfied). */
    NONE,
    /** Zero condition, if {@code Flags.Z} is set. */
    Z,
    /** Not zero condition, if {@code Flags.Z} is not set. */
    NZ,
    /** Carry condition, if {@code Flags.C} is set. */
    C,
    /** Not carry condition, if {@code Flags.C} not is set. */
    NC;

    /**
     * Check if the specified condition is satisfied.
     * 
     * @param reg       The registers to check the flags of.
     * @param condition The condition to check for.
     * @return The result for the given condition.
     */
    public static boolean conditionSatisfied(Registers reg, Conditions condition) {
        boolean result = switch (condition) {
            case NONE -> true;
            case Z -> reg.isFlagSet(Flags.Z);
            case NZ -> !reg.isFlagSet(Flags.Z);
            case C -> reg.isFlagSet(Flags.C);
            case NC -> !reg.isFlagSet(Flags.C);
        };

        return result;
    }
}
