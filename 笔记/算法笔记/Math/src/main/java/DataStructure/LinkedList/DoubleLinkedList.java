package DataStructure.LinkedList;

public class DoubleLinkedList {
    NodePlus head;


    public DoubleLinkedList() {
        this.head = new NodePlus(0, "双向头节点");
    }

    public static void main(String[] args) {
        NodePlus n0 = new NodePlus(1, "张三");
        NodePlus n1 = new NodePlus(2, "李四");
        NodePlus n2 = new NodePlus(3, "王五");
        NodePlus n3 = new NodePlus(4, "赵六");
        DoubleLinkedList dll0 = new DoubleLinkedList();

        System.out.println("############无序插入############");
        dll0.add(n3);
        dll0.add(n1);
        dll0.add(n0);
        dll0.add(n2);
        dll0.show();

        System.out.println("############有序插入############");
        NodePlus n4 = new NodePlus(1, "张三");
        NodePlus n5 = new NodePlus(2, "李四");
        NodePlus n6 = new NodePlus(3, "王五");
        NodePlus n7 = new NodePlus(4, "赵六");
        DoubleLinkedList dll1 = new DoubleLinkedList();
        dll1.addByOrder(n7);
        dll1.addByOrder(n4);
        dll1.addByOrder(n6);
        dll1.addByOrder(n5);
        dll1.show();
        System.out.println("-------------------------------");
        NodePlus n8 = new NodePlus(3, "额外");
        dll1.addByOrder(n8);
        NodePlus n9 = new NodePlus(4, "额外");
        dll1.addByOrder(n9);
        dll1.show();

        System.out.println("############删除节点############");
        dll1.delete(4);
        dll1.delete(4);
        dll1.delete(1);
        dll1.show();

        System.out.println("############更新节点############");
        dll1.update(new NodePlus(3, "嘎嘎"));
        dll1.show();
    }

    public void add(NodePlus node) {
        NodePlus tp = head;
        while (tp.next != null) {
            tp = tp.next;
        }
        tp.next = node;
        node.pre = tp;
    }

    public void addByOrder(NodePlus node) {
        NodePlus tp = head;
        while (tp.next != null) {
            if (tp.no >= node.no) {
                break;
            }
            tp = tp.next;
        }
        if (tp.next == null && tp.no < node.no) {
            tp.next = node;
            node.pre = tp;
        } else {
            node.pre = tp.pre;
            node.next = tp;
            tp.pre.next = node;
            tp.pre = node;
        }
    }

    public void delete(int no) {
        boolean deleted = false;
        NodePlus tp = head;
        if (tp.next == null) {
            System.out.println("链表为空！");
        } else {
            while (tp != null) {
                if (tp.no == no) {
                    deleted = true;
                    break;
                }
                tp = tp.next;
            }
            if (deleted) {
                if(tp.next == null) {
                    tp.pre.next = null;
                } else {
                    tp.pre.next = tp.next;
                    tp.next.pre = tp.pre;
                }
            } else {
                System.out.println("未找到该节点！");
            }
        }
    }

    public void update(NodePlus node) {
        NodePlus tp = head;
        boolean updated = false;
        if (tp.next == null) {
            System.out.println("链表为空！");
        } else {
            while (tp.next != null) {
                if (tp.no == node.no) {
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
    }

    public void show() {
        NodePlus tp = head;
        if (tp.next == null) {
            System.out.println("链表为空！");
        } else {
            tp = tp.next;
            while (tp != null) {
                System.out.println(tp);
                tp = tp.next;
            }
        }
    }

}

class NodePlus {
    public int no;
    public String name;
    public NodePlus next = null;
    public NodePlus pre = null;

    public NodePlus(int no, String name) {
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
