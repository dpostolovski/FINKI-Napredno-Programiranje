import java.util.*;
import java.util.stream.Collectors;

class InvalidPositionException extends RuntimeException {
    public InvalidPositionException(String s) {
        super(s);
    }
}

class Component
{
    private String colour;
    private int weight, level;

    private TreeSet<Component> internal;

    public Component(String colour, int weight) {
        this.colour = colour;
        this.weight = weight;

        level = 0;

        internal = new TreeSet<>(Comparator.comparing(Component::getWeight).thenComparing(Component::getColour));
    }

    public void addComponent(Component component)
    {
        internal.add(component);
        component.setLevel(level+1);

    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void changeColor(int weight, String color)
    {
        String prevColour = colour;
        if(this.weight<weight) {
            this.colour = color;
        }
        List<Component> temp = new ArrayList<>(internal);

        //System.out.println("DEBUG: CURRENT WEIGHT: "+this.weight+" REFERENT WEIGHT: "+weight+" PREVIOUS COLOUR: "+prevColour+" CURRENT COLOUR: "+this.colour+" SIZE: "+temp.size());

        for(Component c : temp)
        {
            c.changeColor(weight, color);
        }
    }


    public int getWeight() {
        return weight;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(weight);
        sb.append(":");
        sb.append(colour);
        sb.append("\n");


        internal.forEach(e -> {for(int i=0; i<=level; i++){sb.append("---");} sb.append(e);});
        //System.out.println("DEBUG: "+internal.size());
        return sb.toString();
    }
}

class Window
{
    private String name;

    private TreeMap<Integer, Component> components;

    public Window(String name) {
        this.name = name;

        components = new TreeMap<>();
    }

    public void addComponent(int position, Component component)
            throws InvalidPositionException
    {
        if(components.containsKey(position))
            throw new InvalidPositionException("Invalid position "+position+", alredy taken!");
        components.put(position, component);
    }

    public void changeColor(int weight, String color)
    {
        components.entrySet().stream()
                             .map(Map.Entry::getValue)
                             .forEach(e -> e.changeColor(weight, color));
    }


    public void swichComponents(int pos1, int pos2)
    {
        Component temp = components.get(pos1);

        components.put(pos1, components.get(pos2));

        components.put(pos2, temp);
    }

    public String toString()
    {
        StringBuilder sb= new StringBuilder();
        sb.append("WINDOW ");
        sb.append(name);
        sb.append("\n");

        components.entrySet().forEach(e -> {sb.append(e.getKey()); sb.append(":"); sb.append(e.getValue().toString());});

        return sb.toString();

    }

}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде