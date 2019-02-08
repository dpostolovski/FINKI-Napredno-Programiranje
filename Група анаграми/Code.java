import java.io.InputStream;
import java.util.ArrayList;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde
        Scanner scanner = new Scanner(inputStream);

        Map<String, TreeSet<String>> anagrams = new LinkedHashMap<>();

        while(scanner.hasNextLine())
        {
            String value = scanner.nextLine();
            char[] temp = value.toCharArray();
            Arrays.sort(temp);
            StringBuilder sb=new StringBuilder();

            for(char c : temp)
                sb.append(c);

            String key = sb.toString();

            anagrams.computeIfAbsent(key, (k) -> new TreeSet<>());

            anagrams.computeIfPresent(key, (k, v) -> {v.add(value); return v;});
            
            /*TreeSet<String> temp1=anagrams.get(key);
            temp1.add(value);

            anagrams.put(key, temp1);*/

            //System.out.println(key+" "+value);

            /*if(anagrams.containsKey(key))
            {
                anagrams.get(key).add(value);
                anagrams.put(key, anagrams.get(key));
            }

            else
            {
                TreeSet<String> temp1 = new TreeSet<>();
                temp1.add(value);
                anagrams.put(key, temp1);
            }*/

        }

        anagrams.entrySet().stream()
                           .map(Map.Entry::getValue)
                           .filter(e -> e.size()>=5)
                           .forEach(e -> {e.forEach(e1 -> { System.out.print(e1); if(e.tailSet(e1).size()!=1){System.out.print(" ");}}); System.out.println();});

    }
}
