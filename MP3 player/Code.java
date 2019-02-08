import java.util.ArrayList;
import java.util.List;

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde

interface Pausable
{
    public boolean isPaused();

    public void pause();

    public void play();

}

class Song implements Pausable
{
    private String title, artist;
    private boolean isPaused;

    public Song() {
    }

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
        isPaused=true;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void play() {
        isPaused = false;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist+
                '}';
    }
}

class MP3Player implements Pausable
{
    int currentSong;
    boolean paused;

    List<Song> songList;

    public MP3Player(List<Song> list) {
        this.songList = list;
        paused=false;
        currentSong=0;
    }

    public void pressPlay()
    {
        if(songList.get(currentSong).isPaused())
        {
            songList.get(currentSong).play();

            System.out.println("Song "+currentSong+" is playing");
        }
        else
        {
            System.out.println("Song is already playing");
        }
    }

    public void pressStop()
    {
        if(!songList.get(currentSong).isPaused())
        {
            songList.get(currentSong).pause();

            System.out.println("Song "+currentSong+" is paused");
        }

        else
        {
            currentSong=0;

            if (!isPaused())
            {
                pause();

                System.out.println("Songs are stopped");
            }

            else
            {
                System.out.println("Songs are already stopped");
            }
        }
    }

    public void pressFWD()
    {
        System.out.println("Forward...");

        songList.get(currentSong).pause();
        
        play();

        currentSong++;

        if(currentSong== songList.size())
            currentSong = 0;
    }

    public void pressREW()
    {
        System.out.println("Reward...");

        songList.get(currentSong).pause();
        
        play();

        currentSong--;

        if(currentSong==-1)
            currentSong= songList.size()-1;
    }

    public void printCurrentSong()
    {
        System.out.println(songList.get(currentSong));
    }

    @Override
    public boolean isPaused()
    {
        return paused;
    }

    @Override
    public void pause()
    {
        paused = true;

    }

    @Override
    public void play()
    {
        paused = false;
    }


    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + currentSong +
                ", songList = " + songList +
                '}';
    }
}
