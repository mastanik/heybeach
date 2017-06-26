package com.daimler.heybeach.backend.autocomplete;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PrefixTree {

    private Node root;

    public PrefixTree() {
    }

    public void add(String value) {
        root = put(root, value, 0);
    }

    public List<Entry> list(String value) {
        return list(root, value);
    }

    private List<Entry> list(Node node, String value) {
        List<Entry> values = new LinkedList<>();
        Node startNode = get(node, value, 0);
        if (startNode != null) {
            Queue<Node> nodeQueue = new LinkedList<>();

            if (startNode.value != null) {
                values.add(new Entry(startNode.value, startNode.count));
            }
            nodeQueue.add(startNode.center);
            while (!nodeQueue.isEmpty()) {
                Node savedNode = nodeQueue.poll();
                if (savedNode != null) {
                    if (savedNode.value != null) {
                        values.add(new Entry(savedNode.value, savedNode.count));
                    }
                    nodeQueue.add(savedNode.left);
                    nodeQueue.add(savedNode.center);
                    nodeQueue.add(savedNode.right);
                }
            }
        }
        return values;
    }

    private Node get(Node node, String value, int index) {
        if (node == null) {
            return node;
        }
        char symbol = value.charAt(index);
        if (symbol < node.symbol) {
            return get(node.left, value, index);
        } else if (symbol > node.symbol) {
            return get(node.right, value, index);
        } else if (index < value.length() - 1) {
            return get(node.center, value, index + 1);
        } else {
            return node;
        }
    }

    private Node put(Node node, String value, int index) {
        char symbol = value.charAt(index);
        if (node == null) {
            node = new Node();
            node.symbol = symbol;
        }

        if (symbol < node.symbol) {
            node.left = put(node.left, value, index);
        } else if (symbol > node.symbol) {
            node.right = put(node.right, value, index);
        } else if (index < value.length() - 1) {
            node.center = put(node.center, value, index + 1);
        } else {
            if (node.value != null) {
                node.count++;
            } else {
                node.value = value;
            }
        }
        return node;
    }

    class Node {
        private char symbol;
        private String value;
        private Node left, center, right;
        private long count;

        public Node() {
            count = 1L;
        }
    }

    public static class Entry {
        private String value;
        private Long count;

        public Entry(String value, Long count) {
            this.value = value;
            this.count = count;
        }

        public String getValue() {
            return value;
        }

        public Long getCount() {
            return count;
        }
    }
}
