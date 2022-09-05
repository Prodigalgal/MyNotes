package DataStructure.Tree;

public class BinaryTree {

    int preCount = 0;
    int infixCount = 0;
    int postCount = 0;
    int[] a = {1, 2, 3, 4, 5, 6, 7};
    Node pre = null;

    public static void main(String[] args) {
        Node n0 = new Node(0, "张三");
        Node n1 = new Node(1, "李四");
        Node n2 = new Node(2, "王五");
        Node n3 = new Node(3, "赵六");
        Node n4 = new Node(4, "嘎嘎");

        n0.left = n1;
        n0.right = n2;
        n1.left = n3;
        n2.right = n4;

        BinaryTree bt = new BinaryTree();
        System.out.println("#########前序#########");
        bt.preOrder(n0);
        System.out.println("#########中序#########");
        bt.infixOrder(n0);
        System.out.println("#########后序#########");
        bt.postOrder(n0);
        System.out.println("#########前序查找#########");
        System.out.println(bt.findPreOrder(n0, 4));
        System.out.println("前序遍历对比次数：" + bt.preCount);
        System.out.println("#########中序查找#########");
        System.out.println(bt.findInfixOrder(n0, 4));
        System.out.println("中序遍历对比次数：" + bt.infixCount);
        System.out.println("#########后序查找#########");
        System.out.println(bt.findPostOrder(n0, 4));
        System.out.println("后序遍历对比次数：" + bt.postCount);

        // System.out.println("#########删除节点#########");
        // bt.delete(n0, 0);
        // bt.preOrder(n0);

        System.out.println("#########顺序存储二叉树前序遍历#########");
        bt.preOrderByArray(0);
        System.out.println();
        System.out.println("#########顺序存储二叉树中序遍历#########");
        bt.infixOrderByArray(0);
        System.out.println();
        System.out.println("#########顺序存储二叉树后序遍历#########");
        bt.postOrderByArray(0);
        System.out.println();

        System.out.println("#########线索二叉树中序线索化#########");
        bt.threaded(n0);
        System.out.println(n3.right);
        System.out.println(n4.left);
        System.out.println("#########线索二叉树中序遍历#########");
        bt.threadedOrder(n0);
    }

    public void threadedOrder(Node root){
        Node tp = root;

        while (tp != null) {
            while (!tp.leftType) {
                tp = tp.left;
            }

            System.out.println(tp);

            while (tp.rightType) {
                tp = tp.right;
                System.out.println(tp);
            }
            tp = tp.right;
        }
    }

    public void threaded(Node root){
        if(root == null) return;

        threaded(root.left);

        if(root.left == null) {
            root.left = pre;
            root.leftType = true;
        }

        if(pre != null && pre.right == null) {
            pre.right = root;
            pre.rightType = true;
        }

        pre = root;
        threaded(root.right);
    }

    public void preOrderByArray(int index) {
        System.out.print(a[index] + " ");
        if ((index * 2) + 1 < a.length) preOrderByArray((index * 2) + 1);
        if ((index * 2) + 2 < a.length) preOrderByArray((index * 2) + 2);
    }

    public void infixOrderByArray(int index) {
        if ((index * 2) + 1 < a.length) infixOrderByArray((index * 2) + 1);
        System.out.print(a[index] + " ");
        if ((index * 2) + 2 < a.length) infixOrderByArray((index * 2) + 2);
    }

    public void postOrderByArray(int index) {
        if ((index * 2) + 1 < a.length) postOrderByArray((index * 2) + 1);
        if ((index * 2) + 2 < a.length) postOrderByArray((index * 2) + 2);
        System.out.print(a[index] + " ");
    }

    public void delete(Node root, int no) {
        if (root == null || (root.left == null && root.right == null && root.no == no) || root.no == no) {
            if (root != null) {
                System.out.println("被删除节点=" + root);
                root.right = null;
                root.left = null;
            }
            return;
        }
        if (root.right != null) {
            if (root.right.no == no) {
                System.out.println("被删除节点=" + root.right);
                root.right = null;
                return;
            } else {
                delete(root.right, no);
            }
        }
        if (root.left != null) {
            if (root.left.no == no) {
                System.out.println("被删除节点=" + root.left);
                root.left = null;
            } else {
                delete(root.left, no);
            }
        }
    }

    public String findPreOrder(Node root, int no) {
        preCount++;
        String cache = "没有找到";
        if (root != null) {
            if (root.no == no) return root.name;

            cache = findPreOrder(root.left, no);

            if (cache.equals("没有找到"))
                cache = findPreOrder(root.right, no);
        }
        return cache;
    }

    public String findInfixOrder(Node root, int no) {

        String cache = "没有找到";
        if (root != null) {
            cache = findInfixOrder(root.left, no);

            infixCount++;
            if (root.no == no) return root.name;

            if (cache.equals("没有找到"))
                cache = findInfixOrder(root.right, no);
        }
        return cache;
    }

    public String findPostOrder(Node root, int no) {

        String cache = "没有找到";
        if (root != null) {
            cache = findPostOrder(root.left, no);

            if (cache.equals("没有找到"))
                cache = findPostOrder(root.right, no);

            postCount++;
            if (root.no == no) return root.name;
        }
        return cache;
    }

    public void preOrder(Node root) {
        if (root != null) {
            System.out.println(root);
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    public void infixOrder(Node root) {
        if (root != null) {
            infixOrder(root.left);
            System.out.println(root);
            infixOrder(root.right);
        }
    }

    public void postOrder(Node root) {
        if (root != null) {
            postOrder(root.left);
            postOrder(root.right);
            System.out.println(root);
        }
    }
}

class Node {

    public int no;
    public String name;
    public Node left;
    public Node right;

    public boolean leftType = false;
    public boolean rightType = false;

    public Node(int no, String name) {
        this.no = no;
        this.name = name;
    }

    public Node() {
    }

    @Override
    public String toString() {
        return "Node{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }
}
