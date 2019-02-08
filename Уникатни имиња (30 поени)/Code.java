import java.util.*;
import java.util.stream.Collectors;

class Names
{
    private TreeMap<String, Integer> names;

    public Names()
    {
        names = new TreeMap<>();
    }

    public void addName(String name)
    {
        names.computeIfAbsent(name, k -> 0);
        names.computeIfPresent(name, (k, v) -> ++v);
    }

    public void printN(int n)
    {
        names.entrySet().stream()
                        .filter(e -> e.getValue()>=n)
                        .forEach(e -> { System.out.print(e.getKey()+" ("+e.getValue()+") "); ArrayList<Character> temp = new ArrayList(); char[] temp1 = e.getKey().toLowerCase().toCharArray(); for (char c : temp1){temp.add(c);}System.out.println(temp.stream()
                                                                                                                                                                                                                                                          .distinct()
                                                                                                                                                                                                                                                          .count());});
    }

    public String findName(int len, int x)
    {
        List<String> temp = names.keySet().stream()
                                            .filter(e -> e.length()<len)
                                            .collect(Collectors.toList());
        x%=temp.size();

        return temp.get(x);
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde