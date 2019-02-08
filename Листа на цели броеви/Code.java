import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class IntegerList
{
   private List<Integer> list;

   public IntegerList()
   {
        list=new LinkedList<>();
   }

   public IntegerList(Integer[] a)
   {
        this();

        for(int i: a)
            list.add(i);
   }

   public void add(int el, int idx)
   {

       /*if(list.size()==0)
       {*/
            if(idx<=list.size())
                list.add(idx, el);
            else
            {
                while(list.size()<idx)
                {
                    list.add(0);
                }

                list.add(el);

                int cnt=0;
                for (int i: list)
                {
                    if(cnt!=idx-1)
                        list.set(cnt, 0);
                }
            }
       /*}*/
       /*else
       {
       int cnt = 0;
       for (int i : list) {
           if (cnt == idx) {
               list.add(idx, el);
           } else {
               list.set(cnt, 0);
           }

           cnt++;
       }

       System.out.println("ADD DEBUG");
       }*/
   }

   public int remove(int idx)
   {
       return list.remove(idx);
   }

   public void set(int el, int idx)
   {
        list.set(el, idx);
   }

   public int get(int idx)
   {
        return list.get(idx);
   }

   public int size()
   {
       return list.size();
   }

   public int count(int el)
   {
        return Math.toIntExact(list.stream()
                .filter(e -> e==el)
                .count());
   }

   public void removeDuplicates()
   {
        List<Integer> duplicates =list.stream()
                    .filter(e ->list.indexOf(e)!=list.lastIndexOf(e))
                    .collect(Collectors.toList());
        for(int i :duplicates)
        {
            while(list.indexOf(i)!=list.lastIndexOf(i))
            {
                list.remove(list.indexOf(i));
            }
        }
   }

   public int sumFirst(int k)
   {
       return list.stream()
            .filter(e -> list.indexOf(e)<k)
            .reduce((sum, current)-> sum+=current)
            .orElse(0);
   }

   public int sumLast(int k)
   {
       return list.stream()
               .filter(e -> list.indexOf(e)>list.size()-k-1)
               .reduce((sum, current)-> sum+=current)
               .orElse(0);
   }

   public void shiftLeft(int idx, int k)
   {
        int orgIdx=list.size();
        int el=list.remove(idx);

        //System.out.println("SHIFTING "+el+" "+k+" POSTIONS LEFT");

        k%=orgIdx;
        idx=(idx-k)%orgIdx;

        if(idx<0)
        {
            idx=orgIdx+idx;
        }

        list.add(idx, el);
   }

    public void shiftRight(int idx, int k)
    {
        int orgIdx=list.size();
        int el=list.remove(idx);

        //System.out.println("SHIFTING "+el+" "+k+" POSTIONS RIGHT");

        k%=orgIdx;

        idx=(idx+k)%orgIdx;

        list.add(idx, el);
    }

    public IntegerList addValue(int value)
    {
        List<Integer>tmp = list.stream()
                               .map(e -> e+=value)
                               .collect(Collectors.toList());
        return new IntegerList(tmp.toArray(new Integer[0]));
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}