import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */

class Team
{
    private String name;
    private int wins, losses, draws, played, points, goalDifference;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.played = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public int getPlayed() {
        return played;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public void addPoints(int points)
    {
        this.points+=points;

        this.played++;

        if(points==3)
            this.wins++;
        else if(points==1)
            this.draws++;
        else
            this.losses++;
    }

    public void updateDifference(int goals)
    {
        this.goalDifference+=goals;
    }

}

class FootballTable {
    private Map<String, Team> table;

    public FootballTable() {
        this.table = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        int homePoints = 0;
        int awayPoints = 0;

        if (homeGoals > awayGoals) {
            homePoints = 3;
        } else if (homeGoals < awayGoals) {
            awayPoints = 3;
        } else {
            homePoints = 1;
            awayPoints = 1;
        }

        int finalHomePoints = homePoints; //Ovie dve morash vaka da gi napravish zasto promenliva vo lambda ne smee da ima promeni na vrednost vo kodot
        int finalAwayPoints = awayPoints;

        table.computeIfAbsent(homeTeam, k -> new Team(homeTeam)); //Funkcija koja dodava element so kluch ednakov na prviot parametar i vrednost ednakva na taa shto kje ja vrati lambdata, lambdata e sekogash so eden parametar(obichno go pishuvam kako k) koj ti dava pristap do vrednosta na kluchot
        table.computeIfPresent(homeTeam, (k, v) -> {  //Funkcija koja dokolku vekje postoi element so kluch ednakov na prviot parametar ja prebrishuva negovata vrednost so taa koja kje ja vrati lambdata, lambdata sekogash ima dva parametri (obichno k i v) od koi k ti dava pristap do kluchot a v do momentalnata vrednost za toj kluch
            v.addPoints(finalHomePoints);
            v.updateDifference(homeGoals - awayGoals);
            return v;
        });

        //Vaka kako shto e iskucano ako go nema timot prvo kje dodade prazen tim pa posle pak kje proveri dali go ima kje go najde prazniot tim i kje mu dodade poeni golovi i sl.

        table.computeIfAbsent(awayTeam, k -> new Team(awayTeam));
        table.computeIfPresent(awayTeam, (k, v) -> {
            v.addPoints(finalAwayPoints);
            v.updateDifference(awayGoals - homeGoals);
            return v;
        });
    }

    public void printTable() {
        List<Team> teams = table.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(Team::getPoints).thenComparing(Team::getGoalDifference).reversed().thenComparing(Team::getName))//Po nekoja logika i ova bi trebalo reversed am on vaka gi ostavil
                .collect(Collectors.toList());

        for (Team t : teams) {
            System.out.printf("%2d. %-15s%5d%5d%5d%5d%5d\n", teams.indexOf(t)+1, t.getName(), t.getPlayed(), t.getWins(), t.getDraws(), t.getLosses(), t.getPoints());
        }

    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

