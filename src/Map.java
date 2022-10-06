
import org.jetbrains.annotations.NotNull;
import java.security.InvalidParameterException;

public class Map<K extends Comparable<K>, V > {
    private Node<K, V> head;
    private long size;

    public Map() {
        this.head = null;
        this.size = 0;
    }

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

    private class IteratorMap {
        public Node<K, V> curParent;
        public Node<K, V> curIt;

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

}
