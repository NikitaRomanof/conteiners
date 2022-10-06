public class Main {
    public static void main(String[] args) {
        List<Integer> test1 = new List<>();
        test1.pushBack(1);
        test1.pushBack(2);
        test1.pushBack(3);
        test1.pushBack(4);
        int rez = test1.popBack();
        test1.printList();
        System.out.println(rez);
        test1.pushFront(0);
        test1.pushFront(-1);
        test1.printList();
        test1.popFront();
        test1.printList();
        test1.erase(3);
        test1.printList();


    }
}