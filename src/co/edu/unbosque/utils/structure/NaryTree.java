package co.edu.unbosque.utils.structure;

public class NaryTree<T> {

    private NaryNode<T> root;
    private final int maxChildren;

    public static class NaryNode<T> {
        T value;
        NaryNode<T>[] children;
        int childCount;

        @SuppressWarnings("unchecked")
        NaryNode(T value, int maxChildren) {
            this.value = value;
            this.children = (NaryNode<T>[]) new NaryNode[maxChildren];
            this.childCount = 0;
        }

        public T getValue() { return value; }
        public NaryNode<T>[] getChildren() { return children; }
        public int getChildCount() { return childCount; }
    }

    public NaryTree(int maxChildren) {
        this.maxChildren = maxChildren;
        this.root = null;
    }

    public void setRoot(T value) {
        root = new NaryNode<>(value, maxChildren);
    }

    public NaryNode<T> getRoot() {
        return root;
    }

    public NaryNode<T> insertChild(NaryNode<T> parent, T value) {
        if (parent == null || parent.childCount >= maxChildren) return null;
        NaryNode<T> child = new NaryNode<>(value, maxChildren);
        parent.children[parent.childCount] = child;
        parent.childCount = parent.childCount + 1;
        return child;
    }

    public NaryNode<T> search(T value) {
        return searchRec(root, value);
    }

    private NaryNode<T> searchRec(NaryNode<T> node, T value) {
        if (node == null) return null;
        if (node.value.equals(value)) return node;
        return searchInChildren(node, value, 0);
    }

    private NaryNode<T> searchInChildren(NaryNode<T> parent, T value, int idx) {
        if (idx >= parent.childCount) return null;
        NaryNode<T> found = searchRec(parent.children[idx], value);
        if (found != null) return found;
        return searchInChildren(parent, value, idx + 1);
    }

    public String traversePreorder() {
        return preorderRec(root, "");
    }

    private String preorderRec(NaryNode<T> node, String acc) {
        if (node == null) return acc;
        String current = (acc.isEmpty() ? "" : acc + " -> ") + node.value.toString();
        return preorderChildren(node, current, 0);
    }

    private String preorderChildren(NaryNode<T> parent, String acc, int idx) {
        if (idx >= parent.childCount) return acc;
        String result = preorderRec(parent.children[idx], acc);
        return preorderChildren(parent, result, idx + 1);
    }

    public String traversePostorder() {
        return postorderRec(root, "");
    }

    private String postorderRec(NaryNode<T> node, String acc) {
        if (node == null) return acc;
        String childrenResult = postorderChildren(node, acc, 0);
        return (childrenResult.isEmpty() ? "" : childrenResult + " -> ") + node.value.toString();
    }

    private String postorderChildren(NaryNode<T> parent, String acc, int idx) {
        if (idx >= parent.childCount) return acc;
        String result = postorderRec(parent.children[idx], acc);
        return postorderChildren(parent, result, idx + 1);
    }

    public boolean isEmpty() {
        return root == null;
    }
}
