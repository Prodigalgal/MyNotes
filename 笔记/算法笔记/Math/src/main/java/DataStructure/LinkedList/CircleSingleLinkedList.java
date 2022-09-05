package DataStructure.LinkedList;

public class CircleSingleLinkedList {
    public static void main(String[] args) {
        CircleSingleLinkedList csll = new CircleSingleLinkedList();
        csll.createCircle(5);
        System.out.println("##########原序列##########");
        csll.show();
        System.out.println("##########约瑟夫序列##########");
        csll.showByMK(2,2);

    }

    NodeCircle first = null;

    public void createCircle(int num) {
        NodeCircle tp = null;
        for (int i = 0; i < num; i++) {
            if(i == 0) {
                first = new NodeCircle(i);
                first.next = first;
                tp = first;
            } else {
                NodeCircle cacheNode = new NodeCircle(i);
                cacheNode.next = tp.next;
                tp.next = cacheNode;
                tp = cacheNode;
            }
        }
    }

    public void show(){
        NodeCircle tp = first;
        if(tp == null) {
            System.out.println("循环链表为空！");
        } else {
            do {
                System.out.println(tp);
                tp = tp.next;
            } while (tp != first);
        }
    }

    public NodeCircle getLastNode() {
        NodeCircle tp = first.next;
        while(tp.next != first) {
            tp = tp.next;
        }
        return tp;
    }

    public void showByMK(int k, int m) {
        NodeCircle lastNode = getLastNode();
        for (int i = 0; i < k-1; i++) {
            first = first.next;
            lastNode = lastNode.next;
        }

        while(first != lastNode) {
            for (int i = 0; i < m-1; i++) {
                first = first.next;
                lastNode = lastNode.next;
            }
            NodeCircle cacheNode = first;
            first = first.next;
            lastNode.next = first;
            System.out.println(cacheNode);
        }
        System.out.println(first);
    }
}

class NodeCircle {
    public int no;
    public NodeCircle next;

    public NodeCircle(int no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "NodeCircle{" +
                "no=" + no +
                '}';
    }
}
