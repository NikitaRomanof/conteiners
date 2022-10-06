public class List<T> {
    private long size;
    private Node<T> head;
    private Node<T> tail;

    public List() {
        this.size = 0;
        this.head = null;
        this.tail = null;

    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setHead(Node<T> head) {
        this.head = head;
    }

    public void setTail(Node<T> tail) {
        this.tail = tail;
    }

    public long getSize() {
        return size;
    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    private class Node<T> {
        public T data;
        public Node<T> prevNode;
        public Node<T> nextNode;

        public Node(T data, Node<T> prevNode, Node<T> nextNode) {
            this.data = data;
            this.prevNode = prevNode;
            this.nextNode = nextNode;
        }
    }

    public T front() {
        if (head != null) {
            return head.data;
        } else {
            return null;
        }
    }

    public T back() {
        if (tail != null) {
            return tail.data;
        } else {
            return null;
        }
    }

    public void pushBack(T value) {
        if (head == null) {
            head = new Node<T>(value,null,null);
            tail = head;
        } else if (head == tail) {
            tail = new Node<T>(value, head, null);
            head.nextNode = tail;
        } else {
            Node<T> curNode = new Node<T>(value, tail, null);
            tail.nextNode = curNode;
            tail = curNode;
        }
        ++size;
    }

    public T popBack() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("List is empty");
        }
        --size;
        T tmp = tail.data;
        if (tail == head) {
            tail = null;
            head = null;
        } else {
            tail = tail.prevNode;
            tail.nextNode = null;
        }
        return tmp;
    }

    public void pushFront(T value) {
        if (head == null) {
            head = new Node<T>(value, null, null);
            tail = head;
        } else {
            if (tail == head) {
                head = new Node<T>(value, null, tail);
                tail.prevNode = head;
            } else {
                Node<T> curNode = new Node<T>(value, null, head);
                head.prevNode = curNode;
                head = curNode;
            }
        }
        ++size;
    }

    public T popFront() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("List is empty");
        }
        --size;
        T tmp = head.data;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.nextNode;
            head.prevNode = null;
        }
        return tmp;
    }

    public void erase(long pos) {
        if (pos > size - 1 || pos < 0) {
            throw new IndexOutOfBoundsException("Position out of range");
        }
        Node<T> tmp = head;
        for (long i = 0; i < pos; ++i) {
            tmp = tmp.nextNode;
        }
        if (tmp == head) {
            this.popFront();
        } else if (tmp == tail) {
            this.popBack();
        } else {
            tmp.prevNode.nextNode = tmp.nextNode;
            tmp.nextNode.prevNode = tmp.prevNode;
            --size;
        }
    }

    public void insert(long pos, T value) {

    }

    public void printList() {
        Node<T> tmp = head;
        while (tmp != null) {
            System.out.print(tmp.data);
            tmp = tmp.nextNode;
            if (tmp != null) {
                System.out.print("->");
            } else {
                System.out.println("");
            }
        }
        if (size == 0) {
            System.out.println("List is empty");
        }
        System.out.println("Size: " + size);
    }
}
