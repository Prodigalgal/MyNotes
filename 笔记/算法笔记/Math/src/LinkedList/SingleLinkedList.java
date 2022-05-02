package LinkedList;

import java.util.Stack;

public class SingleLinkedList {
    public Node head;

    public SingleLinkedList() {
        this.head = new Node(0, "我是头节点");
    }

    public static void main(String[] args) {

        Node n0 = new Node(1, "张三");
        Node n1 = new Node(2, "李四");
        Node n2 = new Node(3, "王五");
        Node n3 = new Node(4, "赵六");

        System.out.println("##########无序插入##########");
        SingleLinkedList sll0 = new SingleLinkedList();
        sll0.addNode(n0);
        sll0.addNode(n2);
        sll0.addNode(n3);
        sll0.addNode(n1);
        sll0.show();

        System.out.println("##########有序插入##########");
        SingleLinkedList sll1 = new SingleLinkedList();
        sll1.addNodeByOrder(n0);
        sll1.addNodeByOrder(n2);
        sll1.addNodeByOrder(n3);
        sll1.addNodeByOrder(n1);
        sll1.show();

        System.out.println("##########头部节点##########");
        System.out.println(sll1.getHead());

        System.out.println("##########更新节点##########");
        sll1.updateNode(new Node(1, "嘎嘎"));
        sll1.show();
        sll1.updateNode(new Node(10, "嘎嘎"));

        System.out.println("##########删除节点##########");
        sll1.deleteNode(new Node(1, "嘎嘎"));
        sll1.show();
        sll1.deleteNode(new Node(10, "嘎嘎"));

        System.out.println("##########链表长度##########");
        SingleLinkedList sll2 = new SingleLinkedList();
        sll2.addNodeByOrder(n0);
        sll2.addNodeByOrder(n2);
        sll2.addNodeByOrder(n3);
        sll2.addNodeByOrder(n1);
        System.out.println("链表长度:" + sll2.getLength());

        System.out.println("##########倒数第X个节点##########");
        System.out.println(sll2.getNodeByLastIndex(4));
        System.out.println(sll2.getNodeByLastIndex(1));
        System.out.println(sll2.getNodeByLastIndex(5));
        SingleLinkedList sll3 = new SingleLinkedList();
        System.out.println(sll3.getNodeByLastIndex(1));

        System.out.println("##########反转链表##########");
        SingleLinkedList sll4 = new SingleLinkedList();
        Node n4 = new Node(1, "张三");
        Node n5 = new Node(2, "李四");
        Node n6 = new Node(3, "王五");
        Node n7 = new Node(4, "赵六");
        sll4.addNodeByOrder(n4);
        sll4.addNodeByOrder(n5);
        sll4.addNodeByOrder(n6);
        sll4.addNodeByOrder(n7);
        sll4.reverse();
        sll4.show();
        SingleLinkedList sll5 = new SingleLinkedList();
        sll5.reverse();

        System.out.println("##########逆序打印##########");
        SingleLinkedList ssl6 = new SingleLinkedList();
        ssl6.addNodeByOrder(n4);
        ssl6.addNodeByOrder(n5);
        ssl6.addNodeByOrder(n6);
        ssl6.addNodeByOrder(n7);
        ssl6.reversePrint();

        System.out.println("##########合并链表##########");
        SingleLinkedList ssl7 = new SingleLinkedList();
        SingleLinkedList ssl8 = new SingleLinkedList();
        Node n10 = new Node(10, "张三");
        Node n11 = new Node(11, "李四");
        Node n12 = new Node(12, "王五");
        Node n13 = new Node(13, "赵六");
        ssl7.addNodeByOrder(n11);
        ssl7.addNodeByOrder(n13);
        ssl7.addNodeByOrder(n12);
        ssl7.addNodeByOrder(n10);
        Node n14 = new Node(14, "张三");
        Node n15 = new Node(15, "李四");
        Node n16 = new Node(16, "王五");
        Node n17 = new Node(17, "赵六");
        ssl8.addNodeByOrder(n14);
        ssl8.addNodeByOrder(n16);
        ssl8.addNodeByOrder(n17);
        ssl8.addNodeByOrder(n15);
        Node node = mergeByOrder(ssl7.getHead(), ssl8.getHead());
        Node tp = node.next;
        while(tp != null) {
            System.out.println(tp);
            tp = tp.next;
        }
    }

    public Node getHead() {
        return head;
    }

    public void addNode(Node node) {
        Node tp = head;
        while (tp.next != null) {
            tp = tp.next;
        }
        tp.next = node;
    }

    public void addNodeByOrder(Node node) {
        Node tp = head;
        while (true) {
            if (tp.next == null) {
                break;
            } else if (tp.next.no >= node.no) {
                break;
            }
            tp = tp.next;
        }
        node.next = tp.next;
        tp.next = node;
    }

    public void updateNode(Node node) {
        Node tp = head;
        boolean updated = false;
        while (true) {
            if (tp.next == null) {
                break;
            } else if (tp.no == node.no) {
                updated = true;
                break;
            }
            tp = tp.next;
        }
        if (updated) {
            tp.name = node.name;
        } else {
            System.out.println("未找到该节点！");
        }
    }

    public void deleteNode(Node node) {
        Node tp = head;
        boolean deleted = false;
        while (true) {
            if (tp.next == null) {
                break;
            } else if (tp.next.no == node.no) {
                deleted = true;
                break;
            }
            tp = tp.next;
        }
        if (deleted) {
            tp.next = tp.next.next;
        } else {
            System.out.println("找不到该节点！");
        }
    }

    public void show() {
        Node tp = head.next;
        if (tp.next == null) {
            System.out.println("链表为空");
        }
        while (tp != null) {
            System.out.println(tp);
            tp = tp.next;
        }
    }

    public int getLength() {
        Node tp = head;
        int count = 0;
        while(tp.next != null) {
            count++;
            tp = tp.next;
        }
        return count;
    }

    public Node getNodeByLastIndex(int index){
        Node tp = head;
        if(tp.next == null) {
            System.out.println("链表为空");
            return null;
        } else {
            int len = getLength();
            int needIndex = len - index + 1;
            if(needIndex < 1) {
                System.out.println("超出链表");
                return null;
            }
            for (int i = 0; i < needIndex; i++) {
                tp = tp.next;
            }
        }
        return tp;
    }

    public void reverse() {
        Node reverseHead = new Node(0,"反转的头节点");
        Node tp = head.next;
        if(tp == null) {
            System.out.println("链表为空！");
        } else {
            while(tp != null) {
                Node cacheNode = tp.next;
                tp.next = reverseHead.next;
                reverseHead.next = tp;
                tp = cacheNode;
            }
            head.next = reverseHead.next;
        }
    }

    public void reversePrint(){
        Stack<Node> stack = new Stack<>();
        Node tp = head;
        if(tp.next == null) {
            System.out.println("链表为空！");
        } else {
            while(tp.next != null) {
                tp = tp.next;
                stack.add(tp);
            }
            while (!stack.isEmpty()) {
                System.out.println(stack.pop());
            }
        }

    }

    public static Node mergeByOrder(Node node1, Node node2) {
        Node nodeHead = new Node(0, "合并后的头节点");
        Node source = nodeHead;
        Node tp1 = node1.next;
        Node tp2 = node2.next;
        while (tp1 != null && tp2 != null) {
            if(tp1.no <= tp2.no) {
                source.next = tp1;
                tp1 = tp1.next;
            } else {
                source.next = tp2;
                tp2 = tp2.next;
            }
            source = source.next;
        }
        source.next = tp1 != null ? tp1 : tp2;
        return nodeHead;
    }
}

class Node {
    public int no;
    public String name;
    public Node next = null;

    public Node(int no, String name) {
        this.no = no;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Node{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }
}