import java.text.DecimalFormat;
import java.util.*;

class SeatNotAllowedException extends RuntimeException
{
    public SeatNotAllowedException() {
    }
}

class SeatTakenException extends RuntimeException
{
    public SeatTakenException() {
    }
}

class Sector
{
    private String code;
    private int seats, seatType;
    private List<Boolean> isTaken;

    public Sector(String code, int seats) {
        this.code = code;
        this.seats = seats;

        seatType=0;

        isTaken = new ArrayList<>(seats);
        for (int i=0; i<seats; i++)
            isTaken.add(false);
    }

    public int getSeatType() {
        return seatType;
    }

    public void setSeatType(int seatType) {
        this.seatType = seatType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSeats() {
        return seats;
    }

    public int getEmptySeats()
    {
        int taken = Math.toIntExact(isTaken.stream()
                           .filter(e->e)
                           .count());

        return seats-taken;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setSeat(int seat, int seatType)
    {

        if(isTaken.get(seat))
            throw new SeatTakenException();

        if(seatType!=0)
        {
            if(this.seatType!=0)
            {
                if(this.seatType!=seatType)
                    throw new SeatNotAllowedException();
            }
        }


        isTaken.set(seat, true);

        if(seatType!=0)
            this.seatType=seatType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sector sector = (Sector) o;
        return Objects.equals(code, sector.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString()
    {
        DecimalFormat formatter = new DecimalFormat("#0.0");
        StringBuilder sb = new StringBuilder();

        sb.append(code);
        sb.append("\t");
        sb.append(getEmptySeats());
        sb.append("/");
        sb.append(seats);
        sb.append("\t");
        sb.append(formatter.format(100.0F*(1.0F-((float)getEmptySeats()/(float)seats))));
        sb.append("%\n");

        //System.out.println("DEBUG: "+ (seats-getEmptySeats())+" "+seats);

        return sb.toString();
    }
}

class Stadium
{
    private String name;
    private List<Sector> sectors;

    public Stadium(String name) {
        this.name = name;
    }

    void createSectors(String[] sectorNames, int[] sizes)
    {
        sectors = new ArrayList<>(sizes.length);

        for (int i=0; i<sizes.length; i++)
        {
            sectors.add(new Sector(sectorNames[i], sizes[i]));
        }
    }

    public void buyTicket(String sectorName, int seat, int seatType)
    {
        sectors.get(sectors.indexOf(new Sector(sectorName, 0))).setSeat(seat-1, seatType);
    }

    public void showSectors()
    {
        sectors.stream()
               .sorted(Comparator.comparing(Sector::getEmptySeats).reversed().thenComparing(Sector::getCode))
               .forEach(System.out::print);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
