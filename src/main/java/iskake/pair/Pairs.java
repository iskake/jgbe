package iskake.pair;

/**
 * Interface containing multiple pairs.
 */
public interface Pairs<T1, T2> extends Iterable<Pair<T1, T2>> {
    /**
     * Add a new Pair to the pairs.
     * 
     * @param pair The pair to add.
     */
    void add(Pair<T1, T2> pair);

    /**
     * Remove the pair at the specified index.
     * 
     * @param index The index of the pair to remove.
     */
    void remove(int index);

    /**
     * Remove the specified pair.
     * 
     * @param pair The pair to remove.
     */
    void remove(Pair<T1, T2> pair);

    /**
     * Get the pair at the specified index.
     * 
     * @param index The index to get the element from.
     * @return The index stored at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    Pair<T1, T2> get(int index) throws IndexOutOfBoundsException;
}
