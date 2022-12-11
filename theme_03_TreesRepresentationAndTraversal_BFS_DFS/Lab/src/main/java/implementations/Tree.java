package implementations;

import interfaces.AbstractTree;

import java.util.*;

public class Tree<E> implements AbstractTree<E> {
    private E value;
    private Tree<E> parent;
    private List<Tree<E>> children;

    public Tree(E value, Tree<E>... subtrees) {
        this.value = value;
        this.parent = null;
        this.children = new ArrayList<>();

        for (Tree<E> subtree : subtrees) {
            this.children.add(subtree);
            subtree.parent = this;
        }
    }

    private void doDfs(Tree<E> node, List<E> result) {
        for (Tree<E> child : node.children) {
            doDfs(child, result);
        }

        result.add(node.value);
    }

    private Tree<E> findBfs(E nodeValue) {
        Deque<Tree<E>> childrenQueue = new ArrayDeque<>();

        childrenQueue.offer(this);

        while (!childrenQueue.isEmpty()) {
            Tree<E> current = childrenQueue.poll();

            if (current.value.equals(nodeValue)) {
                return current;
            }

            for (Tree<E> child : current.children) {
                childrenQueue.offer(child);
            }
        }

        return null;

    }


    private Tree<E> findRecursive(Tree<E> current, E nodeValue) {
        if (current.value.equals(nodeValue)) {
            return current;
        }

        for (Tree<E> child : current.children) {
            Tree<E> found = this.findRecursive(child, nodeValue);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    private void isExist(Tree<E>... nodes) {
        for (Tree<E> node : nodes) {
            if (node == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public List<E> orderBfs() {
        List<E> result = new ArrayList<>();

        if (this.value == null) {
            return result;
        }

        Deque<Tree<E>> childrenQueue = new ArrayDeque<>();

        childrenQueue.offer(this);

        while (!childrenQueue.isEmpty()) {

            Tree<E> current = childrenQueue.poll();

            result.add(current.value);

            for (Tree<E> child : current.children) {
                childrenQueue.offer(child);
            }
        }

        return result;
    }

    @Override
    public List<E> orderDfs() {
        List<E> result = new ArrayList<>();

        this.doDfs(this, result); //with recursion

        //with stack (not working):
//        Deque<Tree<E>> toTraverse = new ArrayDeque<>();
//        toTraverse.push(this);
//
//        while (!toTraverse.isEmpty()) {
//            Tree<E> current = toTraverse.pop();
//
//            for (Tree<E> node : current.children) {
//                toTraverse.push(node);
//            }
//
//            result.add(current.value);
//        }

        return result;
    }

    @Override
    public void addChild(E parentKey, Tree<E> child) {
        Tree<E> search = findBfs(parentKey);

        isExist(search);

        search.children.add(child);
        child.parent = search;
    }

    @Override
    public void removeNode(E nodeKey) {
        Tree<E> toRemove = findBfs(nodeKey);

        isExist(toRemove);

        for (Tree<E> child : toRemove.children) {
            child.parent = null;
        }

        toRemove.children.clear();

        Tree<E> parent = toRemove.parent;

        if (parent != null) {
            parent.children.remove(toRemove);
        }

        toRemove.value = null;
    }

    @Override
    public void swap(E firstKey, E secondKey) {

        Tree<E> firstNode = findBfs(firstKey);
        Tree<E> secondNode = findBfs(secondKey);

        isExist(firstNode, secondNode);

        Tree<E> firstParent = firstNode.parent;
        Tree<E> secondParent = secondNode.parent;

        if (firstParent == null) {
            swapRoot(secondNode);
            return;
        }

        if (secondParent == null) {
            swapRoot(firstNode);
            return;
        }

        firstNode.parent = secondParent;
        secondNode.parent = firstParent;

        int firstIndex = firstParent.children.indexOf(firstNode);
        int secondIndex = secondParent.children.indexOf(secondNode);

        firstParent.children.set(firstIndex, secondNode);
        secondParent.children.set(secondIndex, firstNode);

    }

    private void swapRoot(Tree<E> newRoot) {
        this.value = newRoot.value;
        this.parent = null;
        this.children = newRoot.children;
        newRoot.parent = null;
    }
}



