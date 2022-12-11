package solutions;

import java.util.*;
import java.util.stream.Collectors;

public class BinaryTree {
    private int value;
    private BinaryTree left;
    private BinaryTree right;

    public BinaryTree(int key, BinaryTree first, BinaryTree second) {
        this.value = key;
        this.left = first;
        this.right = second;
    }

    public Integer findLowestCommonAncestor(int first, int second) {
        List<Integer> firstPath = findPath(first);
        List<Integer> secondPath = findPath(second);

        int smallerSize = Math.min(firstPath.size(), secondPath.size());

        int i = 0;
        for (; i < smallerSize; i++) {
            if (!firstPath.get(i).equals(secondPath.get(i))) {
                break;
            }

        }

        return firstPath.get(i - 1);
    }

    private List<Integer> findPath(int element) {
        List<Integer> currentPath = new ArrayList<>();
        findNodePath(this, element, currentPath);
        return currentPath;
    }

    private boolean findNodePath(BinaryTree node, int element, List<Integer> currentPath) {
        if (node == null) {
            return false;
        }

        if (node.value == element) {
            return true;
        }

        currentPath.add(node.value);
        boolean leftResult = findNodePath(node.left, element, currentPath);
        if (leftResult) {
            return true;
        }

        boolean rightResult = findNodePath(node.right, element, currentPath);
        if (rightResult) {
            return true;
        }

        currentPath.remove(Integer.valueOf(node.value));

        return false;
    }

    public List<Integer> topView() {
        Map<Integer, Pair<Integer, Integer>> offsetToValueLevel = new TreeMap<>();
        //Map<Offset, Pair <Value, Level>>>
        traverseTree(this, 0, 1, offsetToValueLevel);

        return offsetToValueLevel
                .values()
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toList());

    }

    private void traverseTree(BinaryTree binaryTree, int offset, int level,
                              Map<Integer, Pair<Integer, Integer>> offsetToValueLevel) {
        if (binaryTree == null) {
            return;
        }

        Pair<Integer, Integer> currentValueLevel = offsetToValueLevel.get(offset);

        if (currentValueLevel == null || level < currentValueLevel.getValue()) {
            offsetToValueLevel.put(offset, new Pair<>(binaryTree.value, level));
        }

        traverseTree(binaryTree.left, offset - 1, level + 1, offsetToValueLevel);
        traverseTree(binaryTree.right, offset + 1, level + 1, offsetToValueLevel);
    }
}
