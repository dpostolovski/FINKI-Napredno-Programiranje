import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


class ChatRoom
{
    private String name;
    private Set<String> users = new TreeSet<String>();

    public ChatRoom(String name) {
        this.name = name;
    }

    public void addUser(String username)
    {
        users.add(username);
    }
    public void removeUser(String username) throws NoSuchUserException
    {
        users.remove(username);
    }
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name + "\n");
        stringBuilder.append( users.stream().reduce( (s, s2) -> s = s+ "\n" + s2 ).orElse("EMPTY") );
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public boolean hasUser(String username)
    {
        return users.contains(username);
    }

    public int numUsers()
    {
        return users.size();
    }

    public String getName() {
        return name;
    }
}

class ChatSystem implements UserRegistry, RoomRegistry
{
    private Set<String> users = new TreeSet<>();
    private Map<String, ChatRoom> chatRooms = new TreeMap<>();

    public ChatSystem() {
    }

    @Override
    public void addRoom(String roomName) {
        ChatRoom room = new ChatRoom(roomName);
        chatRooms.put(room.getName(), room);
    }

    private void checkRoomExists(String roomName) throws NoSuchRoomException
    {
        if(!chatRooms.containsKey(roomName)) throw new NoSuchRoomException();
    }

    @Override
    public void removeRoom(String roomName) throws NoSuchRoomException {
        checkRoomExists(roomName);
        chatRooms.remove(roomName);
    }

    @Override
    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        checkRoomExists(roomName);
        return chatRooms.get(roomName);
    }

    @Override
    public void register(String username) {
        registerOnly(username);

        Comparator<ChatRoom> chatRoomComparator = Comparator.comparing(ChatRoom::numUsers).thenComparing(ChatRoom::getName);
        Comparator<Map.Entry<String, ChatRoom>> comparator = Map.Entry.comparingByValue(chatRoomComparator);


        chatRooms.entrySet().stream().min(comparator).ifPresent(s->s.getValue().addUser(username) );
    }

    private void registerOnly(String username)
    {
        users.add(username);
    }

    @Override
    public boolean userExists(String username) {
        return users.contains(username);
    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        registerOnly(userName);
        joinRoom(userName, roomName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        checkUserExists(userName);
        getRoom(roomName).addUser(userName);
    }

    private void checkUserExists(String userName) throws NoSuchUserException
    {
        if(!userExists(userName)) throw new NoSuchUserException();
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        checkUserExists(username);
        getRoom(roomName).removeUser(username);
    }

    public void followFriend(String username, String friend_username) throws NoSuchElementException, NoSuchUserException {
        checkUserExists(username);
        checkUserExists(friend_username);

        chatRooms.entrySet()
                .stream()
                .filter( stringChatRoomEntry -> stringChatRoomEntry.getValue().hasUser(friend_username))
                .forEach( stringChatRoomEntry -> stringChatRoomEntry.getValue().addUser(username) );
    }

}


interface RoomRegistry
{
    public void addRoom(String roomName);

    public void removeRoom(String roomName) throws NoSuchRoomException;

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException;
}

interface UserRegistry
{
    public void register(String username);
    public boolean userExists(String username);
}


class NoSuchRoomException extends Exception
{
}

class NoSuchUserException extends Exception
{
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException, NoSuchUserException {
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
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }
}
