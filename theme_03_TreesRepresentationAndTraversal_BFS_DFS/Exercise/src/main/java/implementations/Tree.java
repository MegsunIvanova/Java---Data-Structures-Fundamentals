package implementations;

import interfaces.AbstractTree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Tree<E> implements AbstractTree<E> {
    private E key;
    private Tree<E> parent;
    private List<Tree<E>> children;

    public Tree() {
        this.children = new ArrayList<>();
    }

    public Tree(E key) {
        this.key = key;
        this.children = new ArrayList<>();
//        for (Tree<E> child : children) {
//            this.addChild(child);
//            child.setParent(this);
//        }
    }

    @Override
    public void setParent(Tree<E> parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(Tree<E> child) {
        this.children.add(child);
    }

    @Override
    public Tree<E> getParent() {
        return this.parent;
    }

    @Override
    public E getKey() {
        return this.key;
    }

    @Override
    public String getAsString() {
        StringBuilder builder = new StringBuilder();

        traverseTreeWithRecurrence(builder, 0, this);

        return builder.toString().trim();
    }

    public List<Tree<E>> traverseWithBFS() {
        List<Tree<E>> allNodes = new ArrayList<>();

        Deque<Tree<E>> queue = new ArrayDeque<>();

        queue.offer(this);

        while (!queue.isEmpty()) {
            Tree<E> tree = queue.poll();
            allNodes.add(tree);

            for (Tree<E> child : tree.children) {
                queue.offer(child);
            }

        }

        return allNodes;
    }

    private void traverseWithDFS(List<Tree<E>> collection, Tree<E> tree) {

        collection.add(tree);

        for (Tree<E> child : tree.children) {
            traverseWithDFS(collection, child);
        }

    }

    private void traverseTreeWithRecurrence(StringBuilder builder, int indent, Tree<E> tree) {

        builder.append(getPadding(indent))
                .append(tree.getKey())
                .append(System.lineSeparator());

        for (Tree<E> child : tree.children) {
            traverseTreeWithRecurrence(builder, indent + 2, child);
        }

    }

    private String getPadding(int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }


    @Override
    public List<E> getLeafKeys() {
        return traverseWithBFS().stream()
                .filter(tree -> tree.children.size() == 0)
                .map(Tree::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<E> getMiddleKeys() {
        List<Tree<E>> allNodes = new ArrayList<>();
        this.traverseWithDFS(allNodes, this);

        return allNodes.stream()
                .filter(tree -> tree.getParent() != null && tree.children.size() > 0)
                .map(Tree::getKey)
                .collect(Collectors.toList());
    }

//    @Override
//    public Tree<E> getDeepestLeftmostNode() {
//
//        List<Tree<E>> trees = this.traverseWithBFS();
//
//        int maxPath = 0;
//        Tree<E> deepestLeftMostNode = null;
//
//        for (Tree<E> tree : trees) {
//            if (tree.isLeaf()) {
//                int currentPath = getStepsFromLeafToRoot(tree);
//                if (currentPath > maxPath) {
//                    maxPath = currentPath;
//                    deepestLeftMostNode = tree;
//                }
//            }
//        }
//
//        return deepestLeftMostNode;
//    }

    @Override
    public Tree<E> getDeepestLeftmostNode() {

        List<Tree<E>> deepestLeftMostNode = new ArrayList<>();
        int[] maxPath = new int[1];
        int max = 0;

        deepestLeftMostNode.add(new Tree<E>());

        findDeepestNodeDFS(deepestLeftMostNode, maxPath, max, this);

        return deepestLeftMostNode.get(0);
    }

    private void findDeepestNodeDFS(List<Tree<E>> deepestLeftMostNode, int[] maxPath, int max, Tree<E> tree) {

        if (max > maxPath[0]) {
            maxPath[0] = max;
            deepestLeftMostNode.set(0, tree);
        }

        for (Tree<E> child : tree.children) {
            findDeepestNodeDFS(deepestLeftMostNode, maxPath, max + 1, child);
        }
    }

    @Override
    public List<E> getLongestPath() {
        List<E> longestPathKeys = new ArrayList<>();
        List<Tree<E>> currentPath = new ArrayList<>();
        findLongestPath(longestPathKeys, currentPath, this);

        return longestPathKeys;
    }

    private void findLongestPath(List<E> longestPathKeys, List<Tree<E>> currentPath, Tree<E> tree) {

        currentPath.add(tree);

        if (currentPath.size() > longestPathKeys.size()) {
            longestPathKeys.clear();
            for (Tree<E> node : currentPath) {
                longestPathKeys.add(node.key);
            }
        }

        for (Tree<E> child : tree.children) {
            findLongestPath(longestPathKeys, currentPath, child);
            currentPath.remove(currentPath.size() - 1);
        }

    }

    private int getStepsFromLeafToRoot(Tree<E> tree) {
        int counter = 0;
        Tree<E> current = tree;

        while (current.parent != null) {
            counter++;
            current = current.parent;
        }

        return counter;
    }

    private boolean isLeaf() {
        return this.getParent() != null && this.children.size() == 0;
    }

    @Override
    public List<List<E>> pathsWithGivenSum(int sum) {

        List<List<E>> pathsWithGivenSum = new ArrayList<>();

        List<E> currentPathKeys = new ArrayList<>();

        findPathsWithGivenSum(sum, currentPathKeys, pathsWithGivenSum, this);

        return pathsWithGivenSum;
    }

    private void findPathsWithGivenSum(int sum, List<E> currentPathKeys, List<List<E>> pathsWithGivenSum, Tree<E> tree) {
        E element = tree.key;
        currentPathKeys.add(element);

        int currentSum = currentPathKeys.stream()
                .mapToInt(key -> Integer.parseInt(String.valueOf(key)))
                .sum();

        if (currentSum > sum) {
            return;
        }

        if (currentSum == sum) {
            pathsWithGivenSum.add(new ArrayList<>(currentPathKeys));
        }

        for (Tree<E> child : tree.children) {
            findPathsWithGivenSum(sum, currentPathKeys, pathsWithGivenSum, child);
            currentPathKeys.remove(currentPathKeys.size() - 1);
        }

    }

    @Override
    public List<Tree<E>> subTreesWithGivenSum(int sum) {
        List<Tree<E>> subTreesList = new ArrayList<>();

        for (Tree<E> tree : traverseWithBFS()) {
            int currentSum = getSumOfTreeELElements(tree);
            if (currentSum == sum) {
                subTreesList.add(tree);
            }
        }

        return subTreesList;
    }

    private int getSumOfTreeELElements(Tree<E> tree) {

        int sum = 0;

        Deque<Tree<E>> queueTrees = new ArrayDeque<>();

        queueTrees.offer(tree);

        while (!queueTrees.isEmpty()) {
            Tree<E> nextTree = queueTrees.poll();
            int key = Integer.parseInt(String.valueOf(nextTree.key));
            sum += key;

            for (Tree<E> child : nextTree.children) {
                queueTrees.offer(child);
            }
        }

        return sum;
    }
}


