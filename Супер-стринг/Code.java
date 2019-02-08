import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.List;

class SuperString
{
    private LinkedList<String>superString;
    private List<Integer>lastInserted;
    private int timesFlipped;

    public SuperString()
    {
        superString=new LinkedList<>();
        lastInserted=new ArrayList<>();
    }

    public void append(String s)
    {
        superString.addLast(s);
        lastInserted.add(1);
    }

    public void insert(String s)
    {
        superString.addFirst(s);
        lastInserted.add(-1);
    }

    public String toString()
    {
        StringBuilder sb=new StringBuilder();

        for(String s: superString)
        {
            sb.append(s);
        }

        return sb.toString();
    }

    public boolean contains(String s)
    {
        return toString().contains(s);
    }

    public void reverse()
    {
        LinkedList<String> newSuperString=new LinkedList<>();

        for(String s: superString)
        {
            StringBuilder sb=new StringBuilder();
            newSuperString.addFirst(sb.append(s).reverse().toString());
        }

        superString=newSuperString;

        lastInserted=lastInserted.stream()
                                 .map(e -> -e)
                                 .collect(Collectors.toList());
    }

    public void removeLast(int k)
    {
        int i=0;
        while(i<k)
        {
            if(superString.size()==0)
            {
                return;
            }
            if(lastInserted.get(superString.size()-1)==1)
            {
                    superString.removeLast();
            }
            else
            {
                    superString.removeFirst();
            }

            lastInserted.remove(superString.size());
            i++;
        }
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
