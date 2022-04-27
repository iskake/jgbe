package tland.pair;

/**
 * Interface representing a pair of two elements.
 */
public interface Pair<T1, T2> {
    /**
     * Get the first element of the pair.
     * 
     * @return The first element in the pair.
     */
    T1 getFirst();

    /**
     * Get the second element of the pair.
     * 
     * @return The second element in the pair.
     */
    T2 getSecond();
}
