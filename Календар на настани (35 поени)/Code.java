import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class WrongDateException extends RuntimeException {
    public WrongDateException(Date date) {
        super("Wrong date: "+date.toString().replaceAll("GMT", "UTC"));
    }
}

class Event
{
    private String name, location;
    private Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public int getMonth()
    {
        return date.getMonth();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        String[] dateText = date.toString().split("[:\\s+]");
        sb.append(dateText[2]);
        sb.append(" ");
        sb.append(dateText[1]);
        sb.append(", ");
        sb.append(dateText[7]);
        sb.append(" ");
        sb.append(dateText[3]);
        sb.append(":");
        sb.append(dateText[4]);
        sb.append(" at ");
        sb.append(location);
        sb.append(", ");
        sb.append(name);

        return sb.toString();
    }
}

class EventCalendar
{
    private int year;
    private HashMap<String, TreeSet<Event> > events;

    public EventCalendar(int year) {
        this.year = year;
        events = new HashMap<>();
    }

    public void addEvent(String name, String location, Date date)
    {
        if (date.getYear()+1900!=year) {

            //System.out.println(date.getYear());
            throw new WrongDateException(date);
        }

        String[] dateText = date.toString().split("\\s+");

        events.computeIfAbsent(dateText[1]+dateText[2]+dateText[5], k -> new TreeSet<>(Comparator.comparing(Event::getDate).thenComparing(Event::getName)));

        events.computeIfPresent(dateText[1]+dateText[2]+dateText[5], (k, v) -> {v.add(new Event(name, location, date)); return v;});
    }

    public void listEvents(Date date)
    {
        String[]dateInfo = date.toString().split("\\s+");
        //System.out.println(dateInfo.length);
        if(events.containsKey(dateInfo[1].concat(dateInfo[2]).concat(dateInfo[5])))
        {
            TreeSet<Event> temp = events.getOrDefault(dateInfo[1].concat(dateInfo[2]).concat(dateInfo[5]), null);
            if(temp!=null)
                temp.stream().forEach(System.out::println);
        }
        else
            System.out.println("No events on this day!");
    }

    public void listByMonth()
    {
        Map<Integer, Long> result = events.values().stream()
                                                   .flatMap(Collection::stream)
                                                   .collect(Collectors.groupingBy(Event::getMonth, Collectors.counting()));
        for(int i=1; i<=12; i++)
        {
            System.out.print(i+" : ");
            if(result.containsKey(i-1))
                System.out.println(result.get(i-1));
            else
                System.out.println(0);
        }
    }
}

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}
// vashiot kod ovde