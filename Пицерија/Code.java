import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.Iterator;

class InvalidPizzaTypeException extends Exception
{
    public InvalidPizzaTypeException()
    {
        super("Invalid pizza type");
    }
}

class InvalidExtraTypeException extends Exception
{
    public InvalidExtraTypeException()
    {
        super("Invalid extra item type");
    }
}

class ItemOutOfStockException extends Exception
{
    private ItemOutOfStockException cause;

    public ItemOutOfStockException()
    {
        super("Item out of stock");
    }

    /*public ItemOutOfStockException(Item item);
    {
        super("Item out of stock");
    }*/
}

class OrderLockedException extends Exception
{
    public OrderLockedException()
    {
        super("Order is locked");
    }

}

class EmptyOrder extends Exception
{
    public EmptyOrder()
    {
        super("Order is empty");
    }
}

interface Item
{
    public int getPrice();

    public String getType();
}

class PizzaItem implements Item
{
    private String type;
    private static String[] validTypes={"Standard", "Pepperoni", "Vegetarian"};
    private static int[] priceList={10, 12, 8};

    public PizzaItem()
    {

    }

    public PizzaItem(String type)
            throws InvalidPizzaTypeException
    {
        this();

        boolean isValid=false;

        for(String validType: this.validTypes)
        {
            if(type.equals(validType))
            {
                isValid=true;
                break;
            }
        }

        if(!isValid)
        {
            throw new InvalidPizzaTypeException();
        }

        this.type=type;

    }

    @Override
    public String getType()
    {
        return this.type;
    }

    @Override
    public int getPrice()
    {
        for(int i=0; i<validTypes.length; i++)
        {
            if(this.type.equals(validTypes[i]))
            {
                return priceList[i];
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj.getClass()==this.getClass())
        {
            PizzaItem rhs=(PizzaItem)obj;
            return this.type.equals(rhs.type);
        }

        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return this.type.hashCode();
    }
}

class ExtraItem implements Item
{
    private String type;
    private static String[] validTypes={"Ketchup", "Coke"};
    private static int[] priceList={3, 5};

    public ExtraItem()
    {

    }

    public ExtraItem(String type)
        throws InvalidExtraTypeException
    {
        this();

        boolean isValid=false;

        for(String validType: validTypes)
        {
            if(type.equals(validType))
            {
                isValid=true;
                break;
            }
        }

        if(!isValid)
        {
            throw new InvalidExtraTypeException();
        }

        this.type=type;
    }

    @Override
    public String getType()
    {
        return this.type;
    }

    @Override
    public int getPrice()
    {
        for (int i=0; i<validTypes.length; i++)
        {
            if(this.type.equals(validTypes[i]))
            {
                return priceList[i];
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj.getClass()==this.getClass())
        {
            ExtraItem rhs=(ExtraItem)obj;
            return this.type.equals(rhs.type);
        }

        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return this.type.hashCode();
    }
}

class Order
{
    private LinkedHashMap<Item, Integer> orders;
    private boolean locked;

    public Order()
    {
        this.orders=new LinkedHashMap<Item, Integer>();
        this.locked=false;
    }

    public void addItem(Item item, int count)
            throws ItemOutOfStockException, OrderLockedException
    {
        if(this.locked)
        {
            throw new OrderLockedException();
        }
        if(count>10)
        {
            throw new ItemOutOfStockException();
        }
        this.orders.put(item, count);
    }

    public void removeItem(int index)
            throws IndexOutOfBoundsException, OrderLockedException
    {
        if(this.locked)
        {
            throw new OrderLockedException();
        }
        if(this.orders.size()<index)
        {
            throw new IndexOutOfBoundsException();
        }
        int i=0;
        for(Map.Entry<Item, Integer> entry : this.orders.entrySet())
        {
            if (i==index)
            {
                this.orders.remove(entry.getKey());
                return;
            }

            i++;
        }
    }

    public int getPrice()
    {
        int total=0;

        for(Map.Entry<Item, Integer>entry:this.orders.entrySet())
        {
            total+=entry.getValue()*entry.getKey().getPrice();
        }

        return total;
    }

    public void lock()
            throws EmptyOrder
    {
        if(this.orders.size()<1)
        {
            throw new EmptyOrder();
        }
        this.locked=true;
    }

    public void displayOrder()
    {
        int i=1;
        for(Map.Entry<Item, Integer>entry:this.orders.entrySet())
        {
            System.out.printf("%3d.", i);
            System.out.printf("%-15s", entry.getKey().getType());
            System.out.printf("x%2d", entry.getValue());
            System.out.printf("%5d$", entry.getValue()*entry.getKey().getPrice());
            System.out.print("\n");
            i++;
        }

        System.out.printf("%-22s","Total:");
        System.out.printf("%5d$", this.getPrice());
        System.out.print("\n");
    }

}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}