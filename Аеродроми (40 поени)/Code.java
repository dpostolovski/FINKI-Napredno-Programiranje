import java.util.*;
import java.util.stream.Collectors;

class Flight
{
    private String from;
    private String to;
    private int time, duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(from);
        sb.append("-");
        sb.append(to);
        sb.append(" ");

        int hours = time/60;
        int minutes = time%60;

        if(hours<10)
            sb.append("0");
        sb.append(hours);
        sb.append(":");
        if(minutes<10)
            sb.append("0");
        sb.append(minutes);

        sb.append("-");

        int newHours = (time+duration)/60;
        newHours%=24;
        int newMinutes = (time+duration)%60;

        if(newHours<10)
            sb.append("0");
        sb.append(newHours);
        sb.append(":");
        if(newMinutes<10)
            sb.append("0");
        sb.append(newMinutes);
        sb.append(" ");

        int dHours = duration/60;
        int dMinutes = duration%60;

        if (newHours<hours)
            sb.append("+1d ");

        sb.append(dHours);
        sb.append("h");
        if(dMinutes<10)
            sb.append("0");
        sb.append(dMinutes);
        sb.append("m\n");

        return sb.toString();

    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, time, duration);
    }
}

class Airport
{
    private TreeSet<Flight> flightsFrom, flightsTo;
    private String name, country, code;
    private int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;

        flightsFrom = new TreeSet<>(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime));
        flightsTo = new TreeSet<>(Comparator.comparing(Flight::getTime).thenComparing(Flight::getDuration));

    }

    public Airport(String code) {
        this.code = code;
    }

    public void addFlightFrom(String from, String to, int time, int duration)
    {
        flightsFrom.add(new Flight(from, to, time, duration));
    }

    public void addFlightTo(String from, String to, int time, int duration)
    {
        flightsTo.add(new Flight(from, to, time, duration));
    }

    public TreeSet<Flight> getFlightsFrom() {
        return flightsFrom;
    }

    public void setFlightsFrom(TreeSet<Flight> flightsFrom) {
        this.flightsFrom = flightsFrom;
    }

    public TreeSet<Flight> getFlightsTo() {
        return flightsTo;
    }

    public void setFlightsTo(TreeSet<Flight> flightsTo) {
        this.flightsTo = flightsTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(code, airport.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        sb.append(" (");
        sb.append(code);
        sb.append(")\n");
        sb.append(country);
        sb.append("\n");
        sb.append(passengers);
        sb.append("\n");

        List<Flight> flights = new ArrayList<>(flightsFrom);

        for (int i =0; i< flights.size(); i++)
        {
            sb.append(i+1);
            sb.append(". ");
            sb.append(flights.get(i));
        }
        sb.deleteCharAt(sb.lastIndexOf("\n"));

        return sb.toString();
    }
}

class Airports
{
    Map<String, Airport> airports;


    public Airports() {
        airports = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers)
    {
        airports.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration)
    {
        //to = to.replaceAll(",|;|\\.|\\r|\\n|\\s+", "");

        airports.get(from).addFlightFrom(from, to, time, duration);
        airports.get(to).addFlightTo(from, to, time, duration);

    }

    public void showFlightsFromAirport(String code)
    {
        System.out.println(airports.get(code));
    }

    public void showDirectFlightsFromTo(String from, String to)
    {
       List<Flight> temp =  airports.get(from).getFlightsFrom().stream()
                                                               .filter(e -> e.getTo().equals(to))
                                                               .collect(Collectors.toList());
       if(temp.size()==0)
           System.out.println("No flights from "+from+" to "+to);
       else
       {
           temp.forEach(System.out::print);
       }
    }

    public void showDirectFlightsTo(String to)
    {
        airports.get(to).getFlightsTo().forEach(System.out::print);
    }

}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }

       //airports.airports.get(airports.airports.indexOf(new Airport("AMS"))).addFlightTo("HKG", "AMS", 900, 265);
       // airports.airports.get(airports.airports.indexOf(new Airport("AMS"))).addFlightTo("SFO", "AMS", 900, 286);


        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

