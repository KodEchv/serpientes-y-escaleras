package co.edu.unbosque.utils.structure;

public class AVLTree<T> {

    private AVLNode<T> root;

    private static class AVLNode<T> {
        int key;
        T value;
        int height;
        AVLNode<T> left;
        AVLNode<T> right;

        AVLNode(int key, T value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    public AVLTree() {
        this.root = null;
    }

    private int getHeight(AVLNode<T> node) {
        if (node == null) return 0;
        return node.height;
    }

    private int getBalance(AVLNode<T> node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private int maxHeight(int a, int b) {
        return (a > b) ? a : b;
    }

    private AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = 1 + maxHeight(getHeight(y.left), getHeight(y.right));
        x.height = 1 + maxHeight(getHeight(x.left), getHeight(x.right));
        return x;
    }

    private AVLNode<T> rotateLeft(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = 1 + maxHeight(getHeight(x.left), getHeight(x.right));
        y.height = 1 + maxHeight(getHeight(y.left), getHeight(y.right));
        return y;
    }

    private AVLNode<T> balance(AVLNode<T> node, int key) {
        node.height = 1 + maxHeight(getHeight(node.left), getHeight(node.right));
        int bal = getBalance(node);
        if (bal > 1 && key < node.left.key) return rotateRight(node);
        if (bal < -1 && key > node.right.key) return rotateLeft(node);
        if (bal > 1 && key > node.left.key) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bal < -1 && key < node.right.key) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private AVLNode<T> insertRec(AVLNode<T> node, int key, T value) {
        if (node == null) return new AVLNode<>(key, value);
        if (key < node.key) {
            node.left = insertRec(node.left, key, value);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }
        return balance(node, key);
    }

    public void insert(int key, T value) {
        root = insertRec(root, key, value);
    }

    private T searchRec(AVLNode<T> node, int key) {
        if (node == null) return null;
        if (key == node.key) return node.value;
        if (key < node.key) return searchRec(node.left, key);
        return searchRec(node.right, key);
    }

    public T search(int key) {
        return searchRec(root, key);
    }

    private AVLNode<T> minNode(AVLNode<T> node) {
        if (node.left == null) return node;
        return minNode(node.left);
    }

    private AVLNode<T> deleteRec(AVLNode<T> node, int key) {
        if (node == null) return null;
        if (key < node.key) {
            node.left = deleteRec(node.left, key);
        } else if (key > node.key) {
            node.right = deleteRec(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            AVLNode<T> successor = minNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = deleteRec(node.right, successor.key);
        }
        return balance(node, node.key);
    }

    public void delete(int key) {
        root = deleteRec(root, key);
    }

    private int countRec(AVLNode<T> node) {
        if (node == null) return 0;
        return 1 + countRec(node.left) + countRec(node.right);
    }

    private void inorderRec(AVLNode<T> node, Object[] result, int[] idx) {
        if (node == null) return;
        inorderRec(node.left, result, idx);
        result[idx[0]] = node.value;
        idx[0] = idx[0] + 1;
        inorderRec(node.right, result, idx);
    }

    public Object[] inorderArray() {
        int count = countRec(root);
        Object[] result = new Object[count];
        int[] idx = { 0 };
        inorderRec(root, result, idx);
        return result;
    }

    public String inorderString() {
        return inorderStringRec(root, "");
    }

    private String inorderStringRec(AVLNode<T> node, String acc) {
        if (node == null) return acc;
        String leftResult = inorderStringRec(node.left, acc);
        String withSelf = (leftResult.isEmpty() ? "" : leftResult + "\n") + node.value.toString();
        return inorderStringRec(node.right, withSelf);
    }

    public boolean isEmpty() {
        return root == null;
    }
}
