package Tree;

import java.util.*;

public class AVLTree {
    public AVLNode root = null;

    public static void main(String[] args) {
        List<AVLNode> arrayNode = getArrayNode();
        AVLTree avl = new AVLTree();

        // 进行二叉排序
        for (AVLNode n : arrayNode) avl.addNode(n);

        // 中序输出
        System.out.println("##########二叉排序树##############");
        avl.infixOrder(avl.root);

        // 获取高度
        System.out.println("##########高度##############");
        System.out.println("树的高度："+avl.getHeight(avl.root));
        System.out.println("左子树的高度："+avl.getLeftHeight(avl.root));
        System.out.println("右子树的高度："+avl.getRightHeight(avl.root));

        // 验证
        System.out.println("##########正确AVL后验证##############");
        System.out.println(avl.root.left);
        System.out.println(avl.root.left.left);
        System.out.println(avl.root.right);


    }

    public int getLeftHeight(AVLNode node){
        if(node.left != null) return getHeight(node.left);
        else return 0;
    }

    public int getRightHeight(AVLNode node){
        if(node.right != null) return getHeight(node.right);
        else return 0;
    }

    public int getHeight(AVLNode node){
        return Math.max(node.left == null ? 0 : getHeight(node.left), node.right == null ? 0 : getHeight(node.right)) + 1;
    }

    public static List<AVLNode> getArrayNode() {
        int length = 10;
        List<AVLNode> nodes = new ArrayList<>();
        int[] arr = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
        // arr = new int[]{4,3,6,5,7,8};
        arr = new int[]{10,11,7,6,8,9};
        for (int i : arr) {
            nodes.add(new AVLNode(i));
        }
        return nodes;
    }

    public void addNode(AVLNode node) {
        if (root == null) root = node;
        else addNode(root, node);
    }

    public void infixOrder(AVLNode node) {
        if (node != null) {
            infixOrder(node.left);
            System.out.println(node);
            infixOrder(node.right);
        }
    }

    public Map<String, AVLNode> getNode(int value) {
        Map<String, AVLNode> result = new HashMap<>();
        AVLNode targetNode = getTargetNode(value, root);
        AVLNode parentNode = getTargetParentNode(value, root);
        result.put("tn", targetNode);
        result.put("pn", parentNode);
        return result;
    }

    public void deleteNode(int value){
        Map<String, AVLNode> node = getNode(value);
        AVLNode targetNode = node.get("tn");
        AVLNode parentNode = node.get("pn");

        if(root == null || targetNode == null) return;

        if(root.right == null && root.left == null && root.value == value) {root = null; return;}
        // 删除叶子节点
        if(targetNode.right == null && targetNode.left == null) {
            if(parentNode.left == targetNode) parentNode.left = null;
            else parentNode.right = null;
            // 删除有俩个子树的节点
        } else if(targetNode.right != null && targetNode.left != null) {
            targetNode.value = getMin(targetNode);
            // 删除只有一个子树的节点
        } else {
            if(parentNode != null) {
                if(parentNode.left == targetNode) {
                    parentNode.left = Objects.requireNonNullElseGet(targetNode.left, () -> targetNode.right);
                }
                else {
                    if(targetNode.left != null) parentNode.right = targetNode.left;
                    else parentNode.right = targetNode.right;
                }
            } else {
                root = targetNode;
            }
        }
    }

    public int getMin(AVLNode target){
        AVLNode node = target.right;
        while(node.left != null) node = node.left;
        deleteNode(node.value);
        return node.value;
    }

    public void leftRotate(AVLNode r){
        // 值变换
        AVLNode node = new AVLNode(r.value); // 将当前节点值赋给新节点
        r.value = r.right.value; // 将当前节点的右节点值赋给当前节点
        // 左节点变换
        node.left = r.left; // 将当前节点的左节点赋给新节点
        r.left = node; // 将当前节点的左节点指向新节点
        // 右节点变换
        node.right = r.right.left; // 将当前节点的右节点的左节点赋给新节点的右节点
        r.right = r.right.right; // 将当前节点的右节点的右节点赋给当前节点的右节点
    }

    public void rightRotate(AVLNode r) {
        AVLNode node = new AVLNode(r.value);
        r.value = r.left.value;
        node.right = r.right;
        r.right = node;
        node.left = r.left.right;
        r.left = r.left.left;
    }

    public void addNode(AVLNode parent, AVLNode node) {
        if (node != null) {
            if (node.value > parent.value) {
                if (parent.right == null) parent.right = node;
                else addNode(parent.right, node);
            } else {
                if (parent.left == null) parent.left = node;
                else addNode(parent.left, node);
            }
        }
        // 添加一个节点后判断是否需要旋转
        // 如果右子树高度大于左子树高度
        if(getRightHeight(parent) - getLeftHeight(parent)> 1) {
            // 判断该节点的子树高度是否符合
            if(parent.right != null && getLeftHeight(parent.right) > getRightHeight(parent.left))
                rightRotate(parent.right);
            leftRotate(parent);
            return;
        }
        if(getLeftHeight(parent) - getRightHeight(parent)> 1) {
            if(parent.left != null && getRightHeight(parent.left) > getLeftHeight(parent.left))
                leftRotate(parent.left);
            rightRotate(parent);
        }
    }

    public AVLNode getTargetNode(int value, AVLNode node) {
        if (value == node.value) return node;
        else if (value > node.value && node.right != null) return getTargetNode(value, node.right);
        else if (value < node.value && node.left != null) return getTargetNode(value, node.left);
        else return null;
    }

    public AVLNode getTargetParentNode(int value, AVLNode node) {
        // 需要判断左右节点不为空，否则查找不存在的数时回报空指针异常
        if((node.right != null && value == node.right.value) || (node.left != null && value == node.left.value)) return node;
        else if(value > node.value && node.right != null) return getTargetParentNode(value, node.right);
        else if(value < node.value && node.left != null) return getTargetParentNode(value, node.left);
        else return null;
    }
}

class AVLNode {
    public int value;
    public AVLNode left;
    public AVLNode right;

    public AVLNode(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AVLNode{" +
                "value=" + value +
                '}';
    }
}