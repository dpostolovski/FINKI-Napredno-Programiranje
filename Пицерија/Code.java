import java.util.*;

public class PizzaOrderTest {

    public abstract static class Item
    {
        private final String name;

        public Item(String name) {
            this.name = name;
        }


        public abstract int getPrice();

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    public final static class InvalidPizzaTypeException extends Exception
    {
    }

    public final static class InvalidExtraTypeException extends Exception
    {
    }

    public final static class OrderLockedException extends Exception
    {
    }

    public final static class EmptyOrder extends Exception
    {
    }

    public final static class ItemOutOfStockException extends Exception
    {
        public ItemOutOfStockException(Item item) {
        }
    }



    public static final class PizzaItem extends Item
    {
        private static Map<String, Integer> prices = createMenu();

        private static Map<String,Integer> createMenu()
        {
            Map<String,Integer> menu = new HashMap<String,Integer>();
            menu.put("Standard",10);
            menu.put("Pepperoni",12);
            menu.put("Vegetarian",8);
            return menu;
        }

        public PizzaItem(String name) throws Exception {
            super(name);
            if (!prices.containsKey(name))
                throw new InvalidPizzaTypeException();
        }

        @Override
        public int getPrice() {
            return prices.get(super.name);
        }

        @Override
        public int hashCode() {
            return super.name.hashCode();
        }


        public boolean equals(Object i) {
            return super.name.equals( ((Item) i).name);
        }
    }

    public static final class ExtraItem extends Item
    {
        private static Map<String, Integer> prices = createMenu();

        private static Map<String,Integer> createMenu()
        {
            Map<String,Integer> menu = new HashMap<String,Integer>();
            menu.put("Ketchup",3);
            menu.put("Coke",5);
            return menu;
        }

        public ExtraItem(String name) throws Exception {
            super(name);
            if (!prices.containsKey(name))
                throw new InvalidExtraTypeException();
        }

        @Override
        public int getPrice() {
            return prices.get(super.name);
        }

        @Override
        public int hashCode() {
            return super.name.hashCode();
        }

        public boolean equals(Object i) {
            return super.name.equals( ((Item) i).name);
        }
    }

    public static class Order
    {
        private Map<Item, Integer> bucket = new LinkedHashMap<>();
        private boolean locked = false;


        public Order() {

        }

        public void lock() throws Exception
        {
            if(bucket.isEmpty())
                throw new EmptyOrder();

            locked = true;
        }


        public void addItem(Item item, int count) throws Exception
        {
            if(locked)
                throw new OrderLockedException();
            if(count > 10)
                throw new ItemOutOfStockException(item);

            bucket.put(item,count);
        }

        public void displayOrder()
        {
            final int i[] ={1};
            final int total[] = {0};

            bucket.entrySet().forEach( entry -> {
                System.out.printf("%3d.%-15sx%2d%5d$\n",i[0], entry.getKey().name,entry.getValue(),entry.getKey().getPrice()*entry.getValue());
                total[0]+=entry.getKey().getPrice() * entry.getValue();
                i[0]++;
            } );
            System.out.printf("%-22s%5d$\n", "Total:", total[0]);
        }

        public int getPrice()
        {
            final int total[] ={0};
            bucket.forEach((item,count) -> total[0]+= item.getPrice() * count);
            return total[0];
        }

        public void removeItem(int idx) throws Exception
        {
            if(locked)
                throw new OrderLockedException();
            int i=0;
            if(i > bucket.size())
                throw new ArrayIndexOutOfBoundsException(idx);
            for(Map.Entry<Item,Integer> entry : bucket.entrySet())
            {
                if(i == idx)
                {
                    Item itemToRemove = entry.getKey();
                    bucket.remove(itemToRemove);
                    return;
                }
                i++;
            }
        }
    }

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