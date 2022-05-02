package ArrayQueue;

public class CircleArrayQueue {

    public int maxSize;
    public int rear = 0;
    public int front = 0;
    public int[] arr;

    public CircleArrayQueue(int maxSize) {
        this.maxSize = maxSize+1;
        this.arr = new int[this.maxSize];
    }

    public boolean isEmpty() {
        return front == rear;
    }

    public boolean isFull() {
        return (rear + 1) % maxSize == front;
    }

    public int size(){
        return (rear + maxSize - front) % maxSize;
    }

    public void get() {
        if (isEmpty()) {
            System.out.println("队列为空，无法取数据");
        } else {
            int value = arr[front];
            front = (front + 1) % maxSize;
            System.out.println("获取的元素为 = "+value);
        }
    }


    public void add(int num) {
        if(isFull()) {
            System.out.println("队列已满，无法添加数据");
        } else {
            arr[rear] = num;
            rear = (rear + 1) % maxSize;
        }
    }

    public void show() {
        for (int i = front; i < front + size(); i++) {
            System.out.printf("arr[%d] = %d\n", i % maxSize, arr[i % maxSize]);
        }
        System.out.println("front = " + front);
        System.out.println("rear = " + rear);
    }

    public void peek(){
        if(isEmpty()){
            System.out.println("队列为空，无法取出数据");
        } else {
            System.out.println("头部元素为 = "+arr[front]);
        }
    }



    public static void main(String[] args) {
        CircleArrayQueue caq = new CircleArrayQueue(5);

        caq.add(1);
        caq.add(2);
        caq.add(3);
        caq.add(4);
        caq.show();

        caq.get();
        caq.add(5);
        caq.show();

        caq.peek();
        caq.get();
        caq.add(6);
        caq.show();

        caq.add(7);
        caq .show();
    }


}
