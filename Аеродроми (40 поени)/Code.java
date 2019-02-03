import java.time.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Airport implements Comparable<Airport>
{
    private final String name;
    private final String country;
    private final String code;
    private int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " (" + code + ")");
        sb.append(System.lineSeparator());
        sb.append(country);
        sb.append(System.lineSeparator());
        sb.append(passengers);
        return sb.toString();
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    @Override
    public int compareTo(Airport o) {
        return code.compareTo(o.code);
    }
}

class Flight
{
    private final Airport from;
    private final Airport to;

    private LocalTime departure;
    private Duration duration;

    public Flight(Airport from, Airport to, int dep, int duration) {
        this.from = from;
        this.to = to;

        if(dep/60 == 24)
            dep -= 24*60;

        this.departure = LocalTime.of(dep/60,dep%60 );
        this.duration = Duration.ofMinutes(duration);
    }

    @Override
    public String toString() {
        LocalTime arrival = this.departure.plus(this.duration);
        StringBuilder formatedDuration = new StringBuilder();
        if(arrival.isBefore(this.departure))
            formatedDuration.append("+1d ");
        formatedDuration.append(this.duration.toHours());
        formatedDuration.append('h');
        formatedDuration.append(String.format("%02d", this.duration.toMinutes()%60));
        formatedDuration.append('m');

        return String.format("%s-%s %s-%s %s",from.getCode(),to.getCode(),departure.toString(),arrival.toString(), formatedDuration.toString());
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}

class Airports
{
    private Map<String,Airport> airports = new TreeMap<String, Airport>();
    private Set<Flight> flights = new TreeSet<Flight>(Comparator.comparing(Flight::getTo).thenComparing(Flight::getDeparture).thenComparing(Flight::getFrom));


    public void addAirport(String name, String country, String code, int passengers)
    {
        Airport airport = new Airport(name, country, code, passengers);
        airports.put(airport.getCode(),airport);
    }

    public void addFlights(String from, String to, int time, int duration)
    {
        Airport fromAirport = airports.get(from);
        Airport toAirport = airports.get(to);
        flights.add(new Flight(fromAirport,toAirport, time,duration));
    }

    private void print(Stream<Flight> flightStream)
    {
        flightStream.forEach(flight -> flight.toString());
    }

    public void showFlightsFromAirport(String from) {
        int[] counter = new int[1];
        counter[0] =1;
        Airport departureAirport = airports.get(from);
        System.out.println(departureAirport);

        flights.stream().filter(flight -> flight.getFrom().getCode().equals(from)).forEach(flight -> System.out.println(counter[0]++ + ". " + flight));
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Stream<Flight> flightStream = flights.stream().filter(flight -> flight.getFrom().getCode().equals(from)&&flight.getTo().getCode().equals(to));

        System.out.println(flightStream.map(Flight::toString).
                reduce(String::concat).orElse("No flights from " + from + " to " + to));
    }

    public void showDirectFlightsTo(String to) {
        flights.stream().filter(flight -> flight.getTo().getCode().equals(to)).forEach(System.out::println);
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

