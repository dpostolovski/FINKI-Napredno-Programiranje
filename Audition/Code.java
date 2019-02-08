import java.util.*;

class Participant
{
    private String city, code, name;
    private int age;

    public Participant(String city, String name, String code, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

class Audition
{
    HashMap<String, HashSet<Participant> > participants;

    public Audition() {
        participants = new HashMap<>();
    }

    public void addParticpant(String city, String name, String code, int age)
    {
        participants.computeIfAbsent(city, k -> new HashSet<>());
        participants.computeIfPresent(city, (k, v) -> { if (!v.contains(new Participant(null, null, code, 0))){v.add(new Participant(city, code, name, age));} return v;});
    }

    public void listByCity(String city)
    {
        if(participants.containsKey(city))
            participants.get(city).stream()
                                  .sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge).thenComparing(Participant::getCode))
                                  .forEach(e -> {System.out.printf("%s %s %d\n", e.getCode(), e.getName(), e.getAge());});
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}