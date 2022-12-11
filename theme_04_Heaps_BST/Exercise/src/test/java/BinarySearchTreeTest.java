import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BinarySearchTreeTest {
    private BinarySearchTree<Integer> bst;

    @Before
    public void setUp() {
        bst = new BinarySearchTree<>(5);
    }

    @Test
    public void testCreate() {
        assertEquals(Integer.valueOf(5), bst.getRoot().getValue());
    }

    @Test
    public void testInsert() {
        insertElements();

        assertEquals(Integer.valueOf(3), bst.getRoot().getLeft().getValue());
        assertEquals(Integer.valueOf(7), bst.getRoot().getRight().getValue());
        assertEquals(Integer.valueOf(6), bst.getRoot().getRight().getLeft().getValue());
    }

    @Test
    public void testEachInOrder() {
        insertElements();

        List<Integer> elements = new ArrayList<>();
        bst.eachInOrder(elements::add);

        List<Integer> expected = new ArrayList<>(
                Arrays.asList(1, 3, 5, 6, 7, 17)
        );

        assertEquals(expected.size(), elements.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), elements.get(i));
        }
    }

    @Test
    public void testContainsTrue() {
        insertElements();
        assertTrue(bst.contains(6));
    }

    @Test
    public void testContainsFalse() {
        insertElements();
        assertFalse(bst.contains(8));
    }

    @Test
    public void testSearchTrue() {
        insertElements();
        BinarySearchTree<Integer> search = bst.search(7);

        bst.insert(8);

        assertEquals(Integer.valueOf(7), search.getRoot().getValue());
        assertEquals(Integer.valueOf(6), search.getRoot().getLeft().getValue());
        assertEquals(Integer.valueOf(17), search.getRoot().getRight().getValue());

        assertTrue(bst.contains(8));
        assertFalse(search.contains(8));


    }

    @Test
    public void testRange() {
        insertElements();
        List<Integer> expected = Arrays.asList(3, 5, 6, 7);
        List<Integer> range = bst.range(3, 7);

        assertEquals(4, range.size());

        for (Integer value : range) {
            assertTrue(expected.contains(value));
        }
    }

    @Test
    public void tesDeleteMin() {
        insertElements();
        assertTrue(bst.contains(1));
        bst.deleteMin();
        assertFalse(bst.contains(1));
    }

    @Test
    public void tesDeleteMax() {
        insertElements();
        assertTrue(bst.contains(17));
        bst.deleteMax();
        assertFalse(bst.contains(17));
    }

    @Test
    public void testCount() {
        insertElements();
        assertEquals(6, bst.count());
    }

    @Test
    public void testCountAfterInsert() {
        insertElements();
        bst.insert(11);
        assertEquals(7, bst.count());
    }

    @Test
    public void testCountAfterDeleteMin() {
        insertElements();
        bst.deleteMin();
        assertEquals(5, bst.count());
    }

    @Test
    public void testCountAfterDeleteMax() {
        insertElements();
        bst.deleteMax();
        assertEquals(5, bst.count());
    }

    @Test
    public void testRank() {
        insertElements();
        assertEquals(4, bst.rank(7));
    }

    @Test
    public void testRankMinElement() {
        insertElements();
        assertEquals(0, bst.rank(-1));
    }

    @Test
    public void testRankEmptyTree() {
        assertEquals(0, new BinarySearchTree<Integer>().rank(1));
    }

    @Test
    public void testFloor() {
        insertElements();
        assertEquals(Integer.valueOf(6), bst.floor(7));
    }

    @Test
    public void testEmptyFloor() {
        insertElements();
        assertNull(bst.floor(-1));
    }

    @Test
    public void testCeil() {
        insertElements();
        assertEquals(Integer.valueOf(7), bst.ceil(6));
    }

    @Test
    public void testEmptyCeil() {
        insertElements();
        assertNull(bst.ceil(20));
    }

    private void insertElements() {
        bst.insert(3);
        bst.insert(7);
        bst.insert(6);
        bst.insert(1);
        bst.insert(17);
    }


}