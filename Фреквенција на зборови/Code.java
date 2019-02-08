import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
class TermFrequency
{
    private HashMap<String, Integer> frequency;

    public TermFrequency(InputStream in, String[] ignoreWords)
    {
        Scanner scanner = new Scanner(in);
        frequency = new HashMap<>();

        while (scanner.hasNext())
        {
            String word = scanner.next().toLowerCase().replace(",", "").replace(".", "");

            if(Arrays.stream(ignoreWords).noneMatch(word::equalsIgnoreCase)&&!word.equals("\n") && !word.equals("\r") && !word.equals(""))
            {
                frequency.computeIfAbsent(word, k -> 0);


                frequency.computeIfPresent(word, (k, v) -> ++v);
            }
        }
    }

    public int countTotal()
    {
        return frequency.entrySet().stream()
                                    .mapToInt(Map.Entry::getValue)
                                    .sum();
    }

    public int countDistinct()
    {
        return frequency.size();
    }

    public List<String> mostOften(int k)
    {
       return frequency.entrySet().stream()
                            .sorted(new Comparator<Entry<String, Integer>>() {
                                @Override
                                public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                                    if(o1.getValue().compareTo(o2.getValue())<0)
                                        return 1;
                                    else if (o1.getValue().compareTo(o2.getValue())>0)
                                        return -1;
                                    else
                                        return o1.getKey().compareTo(o2.getKey());
                                }
                            })
                            .limit(k)
                            .map(Entry::getKey)
                            .collect(Collectors.toList());
    }
}