package DataStructure.Tree;

import java.util.*;

public class BinarySortTree {
    public BSNode root = null;

    public static void main(String[] args) {
        List<BSNode> arrayNode = getArrayNode();
        BinarySortTree bst = new BinarySortTree();

        // 进行二叉排序
        for (BSNode n : arrayNode) bst.addNode(n);

        // 中序输出
        System.out.println("##########二叉排序树##############");
        bst.infixOrder(bst.root);

        // 查找要删除的节点
        System.out.println("##########要删除的节点及其父节点##############");
        Map<String, BSNode> nodeMap = bst.getNode(55);
        System.out.println(nodeMap.get("tn"));
        System.out.println(nodeMap.get("pn"));
        // 查找要删除的节点
        System.out.println("##########删除节点##############");
        // 7, 3, 10, 12, 5, 1, 9
        bst.deleteNode(7);
        bst.deleteNode(3);
        bst.deleteNode(10);
        bst.deleteNode(12);
        bst.deleteNode(5);
        bst.deleteNode(1);
        bst.deleteNode(9);


        bst.infixOrder(bst.root);

    }

    public static List<BSNode> getArrayNode() {
        int length = 10;
        List<BSNode> nodes = new ArrayList<>();
        int[] arr = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
        arr = new int[]{7, 3, 10, 12, 5, 1, 9};
        for (int i : arr) {
            nodes.add(new BSNode(i));
        }
        return nodes;
    }

    public void addNode(BSNode node) {
        if (root == null) root = node;
        else root.addNode(node);
    }

    public void infixOrder(BSNode node) {
        if (node != null) {
            infixOrder(node.left);
            System.out.println(node);
            infixOrder(node.right);
        }
    }

    public Map<String, BSNode> getNode(int value) {
        Map<String, BSNode> result = new HashMap<>();
        BSNode targetNode = root.getTargetNode(value);
        BSNode parentNode = root.getTargetParentNode(value);
        result.put("tn", targetNode);
        result.put("pn", parentNode);
        return result;
    }

    public void deleteNode(int value){
        Map<String, BSNode> node = getNode(value);
        BSNode targetNode = node.get("tn");
        BSNode parentNode = node.get("pn");

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

    public int getMin(BSNode target){
        BSNode node = target.right;
        while(node.left != null) node = node.left;
        deleteNode(node.value);
        return node.value;
    }
}

class BSNode {
    public int value;
    public BSNode left;
    public BSNode right;

    public BSNode() {
    }

    public BSNode(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BSNode{" +
                "value=" + value +
                '}';
    }

    public void addNode(BSNode node) {
        if (node != null) {
            if (node.value > this.value) {
                if (this.right == null) this.right = node;
                else this.right.addNode(node);
            } else {
                if (this.left == null) this.left = node;
                else this.left.addNode(node);
            }
        }
    }

    public BSNode getTargetNode(int value) {
        if (value == this.value) return this;
        else if (value > this.value && this.right != null) return this.right.getTargetNode(value);
        else if (value < this.value && this.left != null) return this.left.getTargetNode(value);
        else return null;
    }

    public BSNode getTargetParentNode(int value) {
        // 需要判断左右节点不为空，否则查找不存在的数时回报空指针异常
        if((this.right != null && value == this.right.value) || (this.left != null && value == this.left.value)) return this;
        else if(value > this.value && this.right != null) return this.right.getTargetParentNode(value);
        else if(value < this.value && this.left != null) return this.left.getTargetParentNode(value);
        else return null;
    }


}
