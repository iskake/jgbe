package iskake.pair;

import java.util.Objects;

/**
 * Simple class representing a immutable pair of two elements.
 */
public class SimplePair<T1, T2> implements Pair<T1, T2> {
    private final T1 t1;
    private final T2 t2;

    public SimplePair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public T1 getFirst() {
        return t1;
    }

    @Override
    public T2 getSecond() {
        return t2;
    }

    @Override
    public String toString() {
        return "SimplePair { " + t1.toString() + ", " + t2.toString() + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePair<?, ?> that = (SimplePair<?, ?>) o;
        return Objects.equals(t1, that.t1) && Objects.equals(t2, that.t2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t1, t2);
    }
}
