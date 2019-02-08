import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

class NoSuchRoomException extends RuntimeException
{
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends RuntimeException
{
    public NoSuchUserException(String message) {
        super(message);
    }
}

class ChatRoom
{
    private String name;
    private TreeSet<String > userList;

    public ChatRoom()
    {
        userList = new TreeSet<>();
    }

    public ChatRoom(String name)
    {
        this();

        this.name = name;
    }

    public void addUser(String user)
    {
        userList.add(user);
    }

    public void removeUser(String user)
    {
        if(hasUser(user))
        {
            userList.remove(user);
        }
    }

    public int numUsers()
    {
        return userList.size();
    }

    public boolean hasUser(String user)
    {
        return userList.contains(user);
    }

    @Override

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        sb.append('\n');

        if(numUsers()==0)
        {
            sb.append("EMPTY");
            sb.append('\n');

            return sb.toString();
        }

        userList.stream().forEach(e -> { sb.append(e); sb.append('\n'); });

        return sb.toString();
    }

    public String getName() {
        return name;
    }
}

class ChatSystem
{
    TreeMap<String, ChatRoom> roomList;
    TreeMap<String, LinkedHashSet<ChatRoom> > userList;

    public ChatSystem()
    {
        roomList = new TreeMap<>();
        userList = new TreeMap<>();
    }

    public void addRoom(String roomName)
    {
        roomList.putIfAbsent(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName)
    {
        if(roomList.containsKey(roomName))
        {
            ChatRoom tmp = roomList.remove(roomName);
            userList.entrySet().stream()
                               .map(Map.Entry::getValue)
                               .filter(e -> e.contains(tmp))
                               .forEach(e -> e.remove(tmp));
        }
    }

    public ChatRoom getRoom(String roomName)
            throws NoSuchRoomException
    {
        ChatRoom tmp = roomList.entrySet().stream()
                                  .map(Map.Entry::getValue)
                                  .filter(e -> e.getName().equals(roomName))
                                  .findFirst()
                                  .get();

        if(tmp==null)
            throw new NoSuchRoomException(roomName);

        return tmp;
    }

    public void register(String userName)
    {
        ChatRoom tmp = roomList.values().stream()
                                        .min((Comparator.comparing(ChatRoom::numUsers).thenComparing(ChatRoom::getName)))
                                        .orElse(null);

    if(tmp!=null)
        tmp.addUser(userName);

        userList.computeIfAbsent(userName, k -> new LinkedHashSet<>());
    if(tmp!=null)
        userList.computeIfPresent(userName, (k, v) -> { v.add(tmp); return v;});


    }

    public void registerAndJoin(String userName, String roomName)
    {
       ChatRoom tmp = roomList.entrySet().stream()
                           .filter(e -> e.getKey().equals(roomName))
                           .map(Map.Entry::getValue)
                           .findFirst()
                           .orElse(null);

       if(tmp!=null)
       {
           tmp.addUser(userName);

           userList.computeIfAbsent(userName, k -> new LinkedHashSet<>());

           userList.computeIfPresent(userName, (k, v) -> { v.add(tmp); return v;});
       }

       /*System.out.println("DEBUG: "+userName);

       userList.entrySet().stream()
                          .filter(e -> e.getKey().equals(userName))
                          .map(Map.Entry::getValue)
                          .forEach(System.out::println);*/
    }

    public void joinRoom(String userName, String roomName)
            throws NoSuchRoomException, NoSuchUserException
    {
        ChatRoom tmp = roomList.entrySet().stream()
                .filter(e -> e.getKey().equals(roomName))
                .map(e -> e.getValue())
                .findFirst()
                .get();

        if(tmp==null)
            throw  new NoSuchRoomException(roomName);

        tmp.addUser(userName);

        userList.computeIfAbsent(userName, k -> new LinkedHashSet<>());

        userList.computeIfPresent(userName, (k, v) -> { v.add(tmp); return v; });
    }

    public void leaveRoom(String userName, String roomName)
    {
        ChatRoom tmp = roomList.entrySet().stream()
                .filter(e -> e.getKey().equals(roomName))
                .map(Map.Entry::getValue)
                .findFirst()
                .get();

        if(tmp==null)
            throw  new NoSuchRoomException(roomName);


        /*if(!tmp.hasUser(userName))
            throw new NoSuchUserException(userName);*/

        tmp.removeUser(userName);

        userList.entrySet().stream()
                           .filter(e -> e.getKey().equals(userName))
                           .forEach(e -> e.getValue().remove(tmp));

    }

    public void followFriend(String userName, String friendUserName)
            throws NoSuchUserException
    {
       /* if (!userList.containsKey(friendUserName))
            throw new NoSuchUserException(friendUserName);*/
       
        List<ChatRoom> friendRooms = userList.entrySet().stream()
                                                        .filter(e -> e.getKey().equals(friendUserName))
                                                        .map(Map.Entry::getValue)
                                                        .flatMap(Collection::stream)
                                                        .collect(Collectors.toList());

        userList.computeIfAbsent(userName, k -> new LinkedHashSet<>());
        
        friendRooms.stream()
                    .forEach(e -> { userList.computeIfPresent(userName, (k, v) -> { v.add(e); return v; }); e.addUser(userName);});
        


    }



}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];


                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,(Object[]) params);
                    }
                }
            }
        }
    }

}
