
import org.jetbrains.annotations.NotNull;
import java.security.InvalidParameterException;

public class Map<K extends Comparable<K>, V > {
    private Node<K, V> head;
    private long size;

    public Map() {
        this.head = null;
        this.size = 0;
    }

    public class IteratorMap {
        public Node<K, V> curParent;
        public Node<K, V> curIt;

        public K getKey() {
            return curIt.key;
        }

        public V getValue() {
            return curIt.value;
        }

        public IteratorMap() {
            curParent = null;
            curIt = null;
        }

        public IteratorMap(@NotNull IteratorMap other) {
            curParent = other.curParent;
            curIt = other.curIt;
        }

        public IteratorMap(@NotNull Map<K, V> map) {
            curParent = null;
            curIt = map.head;
        }


        public void changeItPos(@NotNull Node<K, V> other) {
            curParent = other.parent;
            curIt = other;
        }

        public @NotNull IteratorMap begin() {
            IteratorMap tmpIt = new IteratorMap(Map.this);
            if (head != null) {
                while (tmpIt.curIt.left != null) {
                    tmpIt.curIt = tmpIt.curIt.left;
                }
                tmpIt.curParent = tmpIt.curIt.parent;
            }
            return tmpIt;
        }

        public @NotNull IteratorMap end() {
            IteratorMap tmpIt = new IteratorMap(Map.this);
            if (head != null) {
                while (tmpIt.curIt.right != null) {
                    tmpIt.curIt = tmpIt.curIt.right;
                }
                tmpIt.curParent = tmpIt.curIt;
                tmpIt.curIt = null;
            }
            return tmpIt;
        }

        public void iteratorIncrement() {
            if (curIt == null && curParent == null) {
                throw new InvalidParameterException("Increment is not possible, iterator is nullptr");
            }
            if (curParent != null) {
                if (curIt == null) {
                    curIt = curParent;
                    curParent = curIt.parent;
                } else if (curParent.left == curIt && curIt.right == null) {
                    curIt = curParent;
                    curParent = curParent.parent;
                } else if (curIt.right != null) {
                    curIt = curIt.right;
                    while (curIt.left != null) {
                        curIt = curIt.left;
                    }
                    curParent = curIt.parent;
                } else {
                    while (curIt.parent != null &&
                            curIt != curIt.parent.left) {
                        curIt = curIt.parent;
                    }
                    if (curIt.parent == null) {
                        curIt = null;
                        curParent = curParent.right;
                    } else {
                        curIt = curIt.parent;
                        curParent = curIt.parent;
                    }
                }
            } else {
                curParent = curIt;
                curIt = curIt.right;
                while (curIt != null && curIt.left != null) {
                    curIt = curIt.left;
                    curParent = curIt.parent;
                }
            }
        }

        public void iteratorDecrement() {
            if (curParent == null && curIt == null) {
                throw new InvalidParameterException("Decrement is not possible, iterator is nullptr");
            }
            if (curParent != null) {
                if (curIt == null) {
                    curIt = curParent;
                    curParent = curIt.parent;
                } else if (curIt.left != null) {
                    curIt = curIt.left;
                    while (curIt.right != null) {
                        curIt = curIt.right;
                    }
                    curParent = curIt.parent;
                } else if (curIt.parent.left == curIt) {
                    curIt = curIt.parent;
                    while (curIt.parent != null &&
                            curIt.parent.right != curIt) {
                        curIt = curIt.parent;
                    }
                    if (curIt.parent == null) {
                        curParent = curParent.left;
                        curIt = null;
                    } else {
                        curIt = curIt.parent;
                        curParent = curIt.parent;
                    }
                } else if (curIt.parent.right == curIt) {
                    curIt = curIt.parent;
                    curParent = curIt.parent;
                }
            } else {
                if (curIt.left == null) {
                    curParent = curIt;
                    curIt = null;
                } else {
                    curIt = curIt.left;
                    while (curIt.right != null) {
                        curIt = curIt.right;
                    }
                    curParent = curIt.parent;
                }
            }
        }
    } // iterator end

    private class Node<K extends Comparable<K>, V> implements Comparable<K>  {
        public K key;
        public V value;
        public Node<K, V> parent;
        public Node<K, V> right;
        public Node<K, V> left;
        int balance;
        int rightDepth;
        int leftDepth;

        public Node(K key, V value, Node<K, V> parent, Node<K, V> right,
                    Node<K, V> left) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.right = right;
            this.left = left;
            this.balance = 0;
            this.rightDepth = 0;
            this.leftDepth = 0;
        }

        @Override
        public int compareTo(@NotNull K otherKey) {
            return key.compareTo(otherKey);
        }
    }

    public boolean insert(K key, V value) {
        long bufSize = size;
        if (head == null) {
            head = new Node<K, V>(key, value, null, null, null);
            ++size;
        } else {
            addNode(key, value, head);
        }
        return size != bufSize;
    }

    private void addNode(K key, V value, @NotNull Node<K, V> curNode) {
        if (curNode.key.compareTo(key) > 0) {
            if (curNode.left == null) {
                curNode.left = new Node<K, V>(key, value, curNode, null, null);
                ++size;
                 balancing(curNode.left);
            } else {
                addNode(key, value, curNode.left);
            }
        } else if (curNode.key.compareTo(key) < 0) {
            if (curNode.right == null) {
                curNode.right = new Node<K, V>(key, value, curNode, null, null);
                ++size;
                 balancing(curNode.right);
            } else {
                addNode(key, value, curNode.right);
            }
        } else {
            curNode.value = value;
        }
    }

    public boolean contains(K compareKey) {

        Node<K, V> buf = head;
        while (buf != null) {
            if (buf.key.compareTo(compareKey) == 0) {
                return true;
            } else if (buf.key.compareTo(compareKey) > 0) {
                buf = buf.left;
            } else {
                buf = buf.right;
            }
        }
        return false;
    }

    private void smallLeftTurn(@NotNull Node<K, V> curNode) {
        if (curNode.parent != null) {
            if (curNode.parent.right == curNode) {
                curNode.parent.right = curNode.right;
            } else {
                curNode.parent.left = curNode.right;
            }
        }
        boolean flag = curNode.parent == null;
        Node<K, V> bufR = curNode.right;
        curNode.right.parent = curNode.parent;
        curNode.parent = curNode.right;
        Node<K, V> buffer = curNode.right.left;
        curNode.right.left = curNode;
        curNode.right = buffer;
        if (buffer != null) {
            buffer.parent = curNode;
            buffer = null;
        }
        if (flag) {
            head = bufR;
        }
        changeBalance(curNode);
        changeBalance(bufR);
    }

    void smallRightTurn(@NotNull Node<K, V> curNode) {

        if (curNode.parent != null) {
            if (curNode.parent.left == curNode) {
                curNode.parent.left = curNode.left;
            } else {
                curNode.parent.right = curNode.left;
            }
        }
        boolean flag = curNode.parent == null;
        Node<K, V> bufL = curNode.left;
        curNode.left.parent = curNode.parent;
        curNode.parent = curNode.left;
        Node<K, V> buffer = curNode.left.right;
        curNode.left.right = curNode;
        curNode.left = buffer;
        if (buffer != null) {
            buffer.parent = curNode;
            buffer = null;
        }
        if (flag) {
            head = bufL;
        }
        changeBalance(curNode);
        changeBalance(bufL);
    }

    private void bigRightTurn(@NotNull Node<K, V> curNode) {
        smallLeftTurn(curNode.left);
        smallRightTurn(curNode);
    }

    private void bigLeftTurn(@NotNull Node<K, V> curNode) {
        smallRightTurn(curNode.right);
        smallLeftTurn(curNode);
    }

    private void balancing(@NotNull Node<K, V> curNode) {
        while (curNode != null) {
            if (curNode.left != null) {
                curNode.leftDepth =
                        (curNode.left.leftDepth * -1) >= curNode.left.rightDepth
                                ? curNode.left.leftDepth - 1
                                : (-1 * curNode.left.rightDepth) - 1;
            } else {
                curNode.leftDepth = 0;
            }
            if (curNode.right != null) {
                curNode.rightDepth =
                        (curNode.right.leftDepth * -1) >= curNode.right.rightDepth
                                ? (curNode.right.leftDepth * -1) + 1
                                : curNode.right.rightDepth + 1;
            } else {
                curNode.rightDepth = 0;
            }
            curNode.balance = curNode.leftDepth + curNode.rightDepth;
            if (curNode.left != null && curNode.balance == -2 && curNode.left.balance <= 0) {
                smallRightTurn(curNode);
            } else if (curNode.balance == -2) {
                bigRightTurn(curNode);
            } else if (curNode.right != null && curNode.balance == 2 && curNode.right.balance >= 0) {
                smallLeftTurn(curNode);
            } else if (curNode.right != null && curNode.balance == 2) {
                bigLeftTurn(curNode);
            } else {
                curNode = curNode.parent;
            }
        }
    }

    private int depthMax(Node<K, V> curNode) {
        if (curNode == null) {
            return (-1);
        } else {
            int leftDepth = depthMax(curNode.left);
            int rightDepth = depthMax(curNode.right);
            if (leftDepth > rightDepth)
                return (leftDepth + 1);
            else
                return (rightDepth + 1);
        }
    }

    private void changeBalance(Node<K, V> curNode) {
        if (curNode != null) {
            if (curNode.left == null) {
                curNode.leftDepth = 0;
            } else {
                curNode.leftDepth = (-1 * depthMax(curNode.left)) - 1;
            }
            if (curNode.right == null) {
                curNode.rightDepth = 0;
            } else {
                curNode.rightDepth = depthMax(curNode.right) + 1;
            }
            curNode.balance = curNode.leftDepth + curNode.rightDepth;
        }
    }


    public void printTree(Node<K, V> curNode, String delim) {
        if (curNode == null) {
            curNode = head;
        }
        if (curNode != null) {
            System.out.println("Key: " +curNode.key + " Value: " + curNode.value + " Balance: " +
                    curNode.balance + " Left Depth: " + curNode.leftDepth + " Right Depth: " + curNode.rightDepth);
            delim += "    ";
            System.out.println(delim + "|");
            System.out.println(delim + "V");

            if (curNode.right != null) {
                printTree(curNode.right, delim);
            }
            if (curNode.left != null) {
                printTree(curNode.left, delim);
            }
        }
    }




}
