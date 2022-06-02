package iskake.jgbe.pair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import iskake.pair.Pair;
import iskake.pair.Pairs;
import iskake.pair.SimplePair;
import iskake.pair.SimplePairs;

public class PairTest {
    @Test
    void pairTest() {
        Pair<String, String> stringPair = new SimplePair<>("Hello", "World");
        assertEquals("Hello", stringPair.getFirst());
        assertEquals("World", stringPair.getSecond());

        Pair<String, String> equalsPair = new SimplePair<>("Hello", "World");
        assertFalse(stringPair == equalsPair);
        assertTrue(stringPair.equals(equalsPair));

        Pair<Integer,String> intPair = new SimplePair<>(123456789, "This is a string");
        assertEquals(123456789, intPair.getFirst());
        assertEquals("This is a string", intPair.getSecond());
    }

    @Test
    void pairsTest() {
        Pairs<String, Integer> stringIntegerPairs = new SimplePairs<>();
        stringIntegerPairs.add(new SimplePair<String,Integer>("Zero", 0));
        assertEquals(new SimplePair<>("Zero", 0), stringIntegerPairs.get(0));
        stringIntegerPairs.remove(0);
        assertThrows(IndexOutOfBoundsException.class, () -> stringIntegerPairs.get(0));

        Pair<String, Integer> intStringPair = new SimplePair<>("Some string", 567);
        stringIntegerPairs.add(intStringPair);
        stringIntegerPairs.add(intStringPair);
        assertTrue(stringIntegerPairs.get(0) == stringIntegerPairs.get(1));
    }
}
