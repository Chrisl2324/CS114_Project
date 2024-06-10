package edu.njit.cs114;

import java.util.*;

/**
 * Author: Ravi Varadarajan
 * Date created: 4/1/2024
 */
public class BinarySearchTree<K extends Comparable<K>,V> {

    private BSTNode<K,V> root;
    private int size;

    public static class BSTNodeData<K extends Comparable<K>,V> {
        private K key;
        private V value;

        public BSTNodeData(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key + "," + value;
        }
    }

    public static class BSTNode<K extends Comparable<K>,V> implements
                            BinTreeNode<BSTNodeData<K,V>> {

        private BSTNodeData<K,V> nodeData;
        private int height;
        // number of keys (including the key in this node) in the subtree rooted at this node
        private int size;
        private BSTNode<K, V> left, right;

        public BSTNode(K key, V value, BSTNode<K, V> left, BSTNode<K, V> right) {
            this.nodeData = new BSTNodeData<>(key, value);
            this.left = left;
            this.right = right;
            /**
             * Complete code to store height and size  (for the lab)
             */
            this.setAugmentedInfo();
        }

        public void setAugmentedInfo() {
            int leftHeight = (left == null ? 0: left.height);
            int rightHeight = (right == null ? 0: right.height);
            this.height = 1 + Math.max(leftHeight, rightHeight);

            int leftSize = (left == null ? 0: left.size);
            int rightSize = (right == null ? 0: right.size);
            this.size = 1 + leftSize + rightSize;
        }

        public BSTNode(K key, V value) {
            this(key, value, null, null);
        }

        public K getKey() {
            return nodeData.key;
        }

        public V getValue() {
            return nodeData.value;
        }

        @Override
        public BSTNodeData<K, V> element() {
            return nodeData;
        }

        @Override
        public BSTNodeData<K, V> setElement(BSTNodeData<K, V> element) {
            BSTNodeData<K, V> oldValue = nodeData;
            this.nodeData = new BSTNodeData<>(element.key, element.value);
            return oldValue;
        }

        @Override
        public BSTNode<K, V> leftChild() {
            return left;
        }

        @Override
        public BSTNode<K, V> rightChild() {
            return right;
        }

        @Override
        public boolean isLeaf() {
            return (left == null && right == null);
        }

        private void setLeftChild(BSTNode<K, V> node) {
            this.left = node;
            this.setAugmentedInfo();

            /**
             * Complete code to store height and size  (for the lab)
             */
        }

        private void setRightChild(BSTNode<K, V> node) {
            this.right = node;
            this.setAugmentedInfo();
            /**
             * Complete code to store height and size  (for the lab)
             */
        }

        private void setValue(V value) { this.nodeData.value = value; };

        /**
         * Returns height of right subtree - height of left subtree
         * @return
         */
        @Override
        public int balanceFactor() {
            int leftHeight = (left == null ? 0: left.height);
            int rightHeight = (right == null ? 0: right.height);
            return rightHeight - leftHeight;
            /**
             * Complete code for the lab
             *
             */
        }

    }

    public BSTNode<K,V> getRoot() {
        return root;
    }

    private V getValueAux(BSTNode<K,V> localRoot, K key) {
        if (localRoot == null) {
            return null;
        }
        int cmpResult = key.compareTo(localRoot.getKey());

        if (cmpResult < 0) {
            return getValueAux(localRoot.leftChild(), key);
        }
        else if (cmpResult > 0) {
            return getValueAux(localRoot.rightChild(), key);
        }
        else {
            return localRoot.getValue();
        }
    }


    /**
     * Get value for the key
     * @param key
     * @return value, null if key does not exist
     */
    public V getValue(K key) {
        return getValueAux(root, key);
    }

    /**
     * Rotate left or right the child node depending on whether child is a right or
     * left child of localRoot
     * @param localRoot root of subtree involved in rotation
     * @param child child of localRoot
     * @return the new root of the subtree
     */
    private BSTNode<K,V> singleRotate(BSTNode<K,V> localRoot, BSTNode<K,V> child) {
        if(child == localRoot.leftChild()){//right rotation
            localRoot.setLeftChild(child.rightChild());
            child.setRightChild(localRoot);
            return child;
        }
        if(child == localRoot.rightChild()){ //left rotation
            localRoot.setRightChild(child.leftChild());
            child.setLeftChild(localRoot);
            return child;
        }
        /**
         * Complete code that will be useful for homeowork
         */
        return localRoot;
    }

    /**
     * Rotate grandchild node left and then right if child is left child of localRoot and grandChild is
     * right child of child.
     * Rotate grandchild node right and then left if child is right child of localRoot and grandChild is
     * left child of child.
     * @param localRoot root of subtree involved in rotatio
     * @param child child node of localRoot
     * @param grandChild child node of child
     * @return the new root of the subtree
     */
    private BSTNode<K,V> doubleRotate(BSTNode<K,V> localRoot, BSTNode<K,V> child,
                                      BSTNode<K,V> grandChild) {
        if (child != null && grandChild != null) {
            if (child == localRoot.leftChild() && grandChild == child.rightChild()) {
                child.setRightChild(grandChild.leftChild());
                localRoot.setLeftChild(grandChild.rightChild());
                grandChild.setLeftChild(child);
                grandChild.setRightChild(localRoot);
                localRoot = grandChild;
                grandChild.setAugmentedInfo();
                child.setAugmentedInfo();
                localRoot.setAugmentedInfo();
            }

            if (child == localRoot.rightChild() && grandChild == child.leftChild()) {
                child.setLeftChild(grandChild.rightChild());
                localRoot.setRightChild(grandChild.leftChild());
                grandChild.setRightChild(child);
                grandChild.setLeftChild(localRoot);
                localRoot = grandChild;
                child.setAugmentedInfo();
                grandChild.setAugmentedInfo();
                localRoot.setAugmentedInfo();
            }
            /**
             * Complete code that will be useful for homeowork
             */
        }return localRoot;
    }
    private BSTNode<K,V> balance(BSTNode<K,V> localRoot) {
        int balance = localRoot.balanceFactor(); // get balance factor of localRoot

        if (balance < -1) { // Check left
            if (localRoot.leftChild() != null) {
                if (localRoot.leftChild().balanceFactor() > 0) {
                    localRoot.setLeftChild(singleRotate(localRoot.leftChild(), localRoot.leftChild().rightChild()));
                }
                return singleRotate(localRoot, localRoot.leftChild());
            }
        } else if (balance > 1) { // Right Check
            if (localRoot.rightChild() != null) {
                if (localRoot.rightChild().balanceFactor() < 0) {
                    localRoot.setRightChild(singleRotate(localRoot.rightChild(), localRoot.rightChild().leftChild()));
                }
                return singleRotate(localRoot, localRoot.rightChild());
            }
        }

        return localRoot;
    }


    public BSTNode<K,V> insertAux(BSTNode<K,V> localRoot, K key, V value) {//helper method
        if (localRoot == null) {
            return new BSTNode<K,V>(key, value);
        }
        int result = key.compareTo(localRoot.nodeData.key);
        if (result < 0) {
            localRoot.setLeftChild(insertAux(localRoot.left, key, value));
        } else if (result > 0){
            localRoot.setRightChild(insertAux(localRoot.right, key, value));
        } else {
            localRoot.setValue(value);
        }
        localRoot.setAugmentedInfo();
        /**
         * Complete code to set height and size of localRoot for the lab
         */
         // Balance the tree if necessary
        return balance(localRoot);
    }

    /**
     * Insert/Replace (key,value) association or mapping in the tree
     * @param key
     * @param value value to insert or replace
     */
    public void insert(K key, V value) {
        this.root = insertAux(root, key, value);
    }

    // Extra credit problem for homework
    /**
     * Delete the value associated with the key if it exists
     * Note you need to set height and size properly
     * in the nodes of the subtrees affected and also balance the tree
     * @param key
     * @return value deleted if key exists else null
     */
    public V delete(K key) {
        return null;
    }

    public int height() {
        return (root == null ? 0 : root.height);
    }

    public int size() {
        return (root == null ? 0 : root.size);
    }

    private boolean isBalanced(BSTNode<K,V> localRoot) {
        if(localRoot == null){
            return true;
        }
        int leftHeight = localRoot.left.height;
        int rightHeight = localRoot.right.height;
        if(leftHeight - rightHeight > 1 || rightHeight - leftHeight > 1){
            return false;
        }
        /**
         * Complete code here for the lab
         */
         return true;
    }

    /**
     * Is the tree balanced ?
     * For every node, height of left and right subtrees differ by at most 1
     * @return
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    /**
     * Get level ordering of nodes; keys in a level must be in descending order
     * @return a map which associates a level with list of nodes at that level
     */
    public Map<Integer, List<BSTNode<K,V>>> getNodeLevels() {
        Map<Integer, List<BSTNode<K, V>>> nodeLevels = new HashMap<>();
        if (root == null) {
            return nodeLevels;
        }

        int level = 0;
        List<BSTNode<K, V>> nodeList = new ArrayList<>();
        nodeList.add(root);
        nodeLevels.put(level, nodeList);

        while (true) {
            List<BSTNode<K, V>> childNodeList = new ArrayList<>();

            for (BSTNode<K, V> node : nodeList) {

                if (node.right != null) {
                childNodeList.add(node.right);
            }

                if (node.left != null) {
                    childNodeList.add(node.left);
                }

            }
            if (childNodeList.isEmpty()) {
                break;
            }
                nodeList = childNodeList;
                level++;

                nodeLevels.put(level, nodeList);
            }
        /**{
         * Complete code for the lab
         */
        return nodeLevels;
    }


    /**
     * Return list of nodes whose keys are greater than or equal to key1
     *   and smaller than or equal to key2
     * @param key1
     * @param key2
     * @return
     */
    public List<BSTNode<K,V>> getRange(K key1, K key2) {
        /**
         * Complete code for homework (define a recursive aux function to be called from here)
         */
        return rangeAux(root, key1, key2);
    }
    private List<BSTNode<K,V>> rangeAux(BSTNode<K,V> localRoot, K key1, K key2) {
        List<BSTNode<K,V>> nodeList = new ArrayList<>();

        // Base case: If localRoot is null, return an empty list
        if (localRoot == null) {
            return nodeList;
        }

        // Compare key2 with localRoot's key
        int cmpToKey2 = key2.compareTo(localRoot.getKey());

        if (cmpToKey2 < 0) {
            nodeList.addAll(rangeAux(localRoot.left, key1, key2));
        } else if (cmpToKey2 == 0) {
            nodeList.addAll(rangeAux(localRoot.left, key1, key2));
            nodeList.add(localRoot);
        } else { // cmpToKey2 > 0
            int cmpToKey1 = key1.compareTo(localRoot.getKey());

            // If key1 is equal to localRoot's key
            if (cmpToKey1 == 0) {
                nodeList.add(localRoot);
                nodeList.addAll(rangeAux(localRoot.right, key1, key2));
            } else if (cmpToKey1 > 0) {
                nodeList.addAll(rangeAux(localRoot.right, key1, key2));
            } else { // cmpToKey1 < 0
                nodeList.addAll(rangeAux(localRoot.left, key1, key2));
                nodeList.add(localRoot);
                nodeList.addAll(rangeAux(localRoot.right, key1, key2));
            }
        }

        return nodeList;
    }


    /**
     * Find number of keys smaller than or equal to the specified key
     * @param key
     * @return
     */
    public int rank(K key) {
        return rankAux(root, key);
        /**
         * Complete code for homework (define a recursive aux function to be called from here)
         */
    }
    private int rankAux(BSTNode<K, V> localRoot, K key) {
        // Base case
        if (localRoot == null) {
            return 0;
        }

        // Compare the key with the key at the current node
        int cmpResult = key.compareTo(localRoot.getKey());

        if (cmpResult < 0) {
            return rankAux(localRoot.left, key);
        } else {
            int nKeys = 0;


            int leftSize = (localRoot.left != null) ? localRoot.left.size : 0;
            nKeys += leftSize + 1; // Include the current node itself

            if (cmpResult > 0) {
                nKeys += rankAux(localRoot.right, key);
            }

            // Return the total number of keys found
            return nKeys;
        }
    }



    public static void main(String [] args) {
        BinarySearchTree<Integer, String> bst = new BinarySearchTree<>();
        bst.insert(25,"a");
        bst.insert(15,"b");
        bst.insert(30,"c");
        bst.insert(5,"d");
        bst.insert(27,"e");
        bst.insert(36,"f");
        bst.insert(40,"g");
        bst.insert(10,"k");
        bst.insert(52,"l");
        System.out.println("Printing tree bst..");
        new BinTreeInorderNavigator<BSTNodeData<Integer,String>>().visit(bst.getRoot());
        int key = 36;
        String value = bst.getValue(key);
        if (value != null) {
            System.out.println("Value for key "+ key + "=" + value);
        } else {
            System.out.println("Key " + key + " does not exist");
        }
        key = 20;
        value = bst.getValue(key);
        if (value != null) {
            System.out.println("Value for key "+ key + "=" + value);
        } else {
            System.out.println("Key " + key + " does not exist");
        }
        bst.insert(40,"m");
        System.out.println("Printing tree bst..");
        new BinTreeInorderNavigator<BSTNodeData<Integer,String>>().visit(bst.getRoot());
//        System.out.println("Value for deleted key 5 = " + bst.delete(5));
//        System.out.println("Printing tree bst..");
//        new BinTreeInorderNavigator<BSTNodeData<Integer,String>>().visit(bst.getRoot());
//        System.out.println("Value for deleted key 30 = " + bst.delete(30));
//        System.out.println("Printing tree bst..");
//        new BinTreeInorderNavigator<BSTNodeData<Integer,String>>().visit(bst.getRoot());
        System.out.println("size of bst=" + bst.size());
        System.out.println("height of bst=" + bst.height());
        System.out.println("Is bst an AVL tree ? " + bst.isBalanced());
        Map<Integer, List<BSTNode<Integer,String>>> nodeLevels = bst.getNodeLevels();
        for (int level : nodeLevels.keySet()) {
            System.out.print("Keys at level " + level + " :");
            for (BSTNode<Integer,String> node : nodeLevels.get(level)) {
                System.out.print(" " + node.getKey());
            }
            System.out.println("");
        }
        BinarySearchTree<Integer, Integer> bst1 = new BinarySearchTree<>();
        bst1.insert(44,1);
        bst1.insert(17,2);
        bst1.insert(78,3);
        bst1.insert(50,4);
        bst1.insert(62,5);
        bst1.insert(88,6);
        bst1.insert(48,7);
        bst1.insert(32,8);
        System.out.println("Printing tree bst1..");
        new BinTreeInorderNavigator<BSTNodeData<Integer,Integer>>().visit(bst1.getRoot());
        System.out.println("size of bst1=" + bst1.size());
        System.out.println("height of bst1=" + bst1.height());
        System.out.println("Is bst1 an AVL tree ? " + bst1.isBalanced());
        Map<Integer, List<BSTNode<Integer,Integer>>> nodeLevels1 = bst1.getNodeLevels();
        for (int level : nodeLevels1.keySet()) {
            System.out.print("Keys at level " + level + " :");
            for (BSTNode<Integer,Integer> node : nodeLevels1.get(level)) {
                System.out.print(" " + node.getKey());
            }
            System.out.println("");
        }
//        System.out.println("rank of key 10 in bst=" + bst.rank(10)); // should be 2
//        System.out.println("rank of key 30 in bst=" + bst.rank(30)); // should be 6
//        System.out.println("rank of key 3 in bst=" + bst.rank(3)); // should be 0
//        System.out.println("rank of key 55 in bst=" + bst.rank(55)); // should be 9
//        List<BSTNode<Integer,Integer>> rangeNodes = bst1.getRange(32,62);
//        System.out.print("Keys in the range : [32,62] are:");
//        // should get 32,44,48,50,62,
//        for (BSTNode<Integer,Integer> node : rangeNodes) {
//            System.out.print(node.getKey() + ",");
//        }
//        System.out.println("");
//        rangeNodes = bst1.getRange(10,50);
//        System.out.print("Keys in the range : [10,50] are:");
//        // should get 17,32,44,48,50,
//        for (BSTNode<Integer,Integer> node : rangeNodes) {
//            System.out.print(node.getKey() + ",");
//        }
//        System.out.println("");
//        rangeNodes = bst1.getRange(90,100);
//        System.out.print("Keys in the range : [90,100] are:");
//        // should get empty list
//        for (BSTNode<Integer,Integer> node : rangeNodes) {
//            System.out.print(node.getKey() + ",");
//        }
//        System.out.println("");
    }

}
