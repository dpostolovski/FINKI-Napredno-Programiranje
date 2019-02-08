import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Measurement
{
    private float temperature, wind, humidity, visibility;
    private Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public Measurement(Date date) {
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getWind() {
        return wind;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

class WeatherStation
{
    private int days;
    private TreeSet<Measurement> measurements;

    public WeatherStation(int days) {
        this.days = days;
        measurements = new TreeSet<>(Comparator.comparing(Measurement::getDate));
    }

    private boolean isValid(Measurement m)
    {
        if(measurements.isEmpty())
            return true;
        for(Measurement i : measurements)
        {
            if (Math.abs(m.getDate().getTime()-i.getDate().getTime())<150000)//2.5*60s*1000
            {
                return false;
            }
        }

        return true;
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date)
    {
        Measurement newMeasurment = new Measurement(temperature, wind, humidity, visibility, date);
        if(!isValid(newMeasurment))
            return;

        List<Measurement> temp = measurements.stream()
                                             .filter(e -> newMeasurment.getDate().getTime()-e.getDate().getTime()>days*24*60*60*1000)
                                             .collect(Collectors.toList());
        measurements.removeAll(temp);

        measurements.add(newMeasurment);
    }

    public int total()
    {
        return measurements.size();
    }

    public void status(Date from, Date to)
        throws RuntimeException
    {
        Measurement tempFrom = new Measurement(from), tempTo = new Measurement(to);

        Set<Measurement> temp = measurements.subSet(tempFrom, true, tempTo, true);

        if(temp.isEmpty())
            throw new RuntimeException();

        temp.stream()
             .forEach(e -> System.out.println(e.getTemperature()+" "+e.getWind()+" km/h "+e.getHumidity()+"% "+e.getVisibility()+" km "+e.getDate().toString()));

        System.out.printf("Average temperature: %.2f\n",temp.stream()
                                                               .mapToDouble(Measurement::getTemperature)
                                                               .average()
                                                               .getAsDouble());
    }
}

public class WeatherStationTest<K, V> {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde