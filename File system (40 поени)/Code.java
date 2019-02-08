import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

class  File implements Comparable<File>
{
    private String name;
    private Integer size;
    private LocalDateTime createdAt;

    public File(String name, Integer size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getYear()
    {
        return createdAt.getYear();
    }

    public String getMonthAndYear()
    {
        return createdAt.getMonth().toString()+"-"+createdAt.getDayOfMonth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(name, file.name)&&Objects.equals(size, file.size)&&Objects.equals(createdAt, file.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, createdAt);
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       Formatter formatter = new Formatter(sb);
       formatter.format("%-10s %5dB %s", name, size, createdAt);

       return sb.toString();
    }

    @Override
    public int compareTo(File o) {
        return createdAt.compareTo(o.createdAt);
    }
}

class FileSystem
{
    Map<Character, TreeSet<File> > fileMap;

    public FileSystem() {
        fileMap = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt)
    {
        fileMap.computeIfAbsent(folder, k -> new TreeSet<>(Comparator.comparing(File::getCreatedAt)));
        fileMap.computeIfPresent(folder, (k, v) -> {v.add(new File(name, size, createdAt)); return v;});
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size)
    {
        return fileMap.values().stream()
                               .flatMap(Collection::stream)
                               .filter(e -> e.getName().charAt(0)=='.' && e.getSize()<size)
                               .collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders)
    {
        return fileMap.entrySet().stream()
                                 .filter(e -> folders.contains(e.getKey()))
                                 .map(Map.Entry::getValue)
                                 .flatMap(Collection::stream)
                                 .mapToInt(File::getSize)
                                 .sum();
    }

    public Map<Integer, Set<File>> byYear()
    {
        return fileMap.values().stream()
                               .flatMap(Collection::stream)
                               .collect(Collectors.groupingBy(File::getYear, toSet()));
    }

    public Map<String, Long> sizeByMonthAndDay()
    {
        return fileMap.values().stream()
                               .flatMap(Collection::stream)
                               .collect(Collectors.groupingBy(File::getMonthAndYear, Collectors.summingLong(File::getSize)));
    }



}

/**
 * Partial exam II 2016/2017
 */
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

