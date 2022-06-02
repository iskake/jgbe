package iskake.pair;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Simple class containing multiple pairs.
 */
public class SimplePairs<T1, T2> implements Pairs<T1, T2> {
    ArrayList<Pair<T1, T2>> pairs = new ArrayList<>();

    @Override
    public void add(Pair<T1, T2> pair) {
        pairs.add(pair);
    }

    @Override
    public void remove(int index) {
        pairs.remove(index);
    }

    @Override
    public void remove(Pair<T1, T2> pair) {
        pairs.remove(pair);
    }

    @Override
    public Pair<T1, T2> get(int index) throws IndexOutOfBoundsException {
        return pairs.get(index);
    }

    @Override
    public Iterator<Pair<T1, T2>> iterator() {
        return pairs.iterator();
    }

}
