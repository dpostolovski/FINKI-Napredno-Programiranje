import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

class Movie
{
    private String title;
    private List<Integer> ratings;
    private double ratingCoef;

    public Movie(String title, Integer[] ratings) {
        this.title = title;
        this.ratings = new ArrayList<>(Arrays.asList(ratings));
    }

    public String getTitle() {
        return title;
    }

    public void setRatingCoef(int maxRatings)
    {
        ratingCoef =  (averageRating()*numRatings())/(double)maxRatings;
    }

    public double getRatingCoef() {

        return ratingCoef;
    }

    public float averageRating()
    {
        int sum = ratings.stream().mapToInt(e->e).sum();

        return (float)sum/(float) numRatings();

    }

    public int numRatings()
    {
        return ratings.size();
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        DecimalFormat formatter = new DecimalFormat("#.00");

        formatter.setRoundingMode(RoundingMode.HALF_UP);

        sb.append(title);
        sb.append(" (");
        sb.append(formatter.format(averageRating()));
        sb.append(") of ");
        sb.append(numRatings());
        sb.append(" ratings");
        /*sb.append("DEBUG RATING COEFFICIENT: ");
        sb.append(ratingCoef);*/

        return sb.toString();
    }

}

class MoviesList
{
    private List<Movie> movies;

    public MoviesList() {
        movies = new ArrayList<>();
    }

    public void addMovie(String title, Integer[] ratings)
    {
        movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating()
    {
        return movies.stream()
                     .sorted(Comparator.comparing(Movie::averageRating).reversed().thenComparing(Movie::getTitle))
                     .limit(10)
                     .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef()
    {
        int maxRating = movies.stream()
                              .mapToInt(Movie::numRatings)
                              .max()
                              .orElse(0);
        return movies.stream()
                     .peek(e->e.setRatingCoef(maxRating))
                     .sorted(Comparator.comparing(Movie::getRatingCoef).reversed().thenComparing(Movie::getTitle))
                     .limit(10)
                     .collect(Collectors.toList());
    }


}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            Integer[] ratings = new Integer[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie.toString());
        }
    }
}

// vashiot kod ovde