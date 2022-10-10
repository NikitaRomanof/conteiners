public class Main {
    public static void main(String[] args) {
       Map<Integer, Integer> test2= new Map<>();
       test2.insert(5, 1);
       test2.insert(8, 2);
       test2.insert(9, 3);
       test2.insert(6, 4);
       test2.insert(3, 5);
       test2.insert(2, 6);
       Map<Integer, Integer>.IteratorMap first = test2.new IteratorMap();
       first = first.begin();

       first.iteratorDecrement();
        System.out.println(first.getKey() + " , " + first.getValue() );








    }

}