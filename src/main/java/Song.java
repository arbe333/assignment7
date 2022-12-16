import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * class song
 */
public class Song extends Entity implements Comparable<Song>{
    /**
     * album of the song
     */
    protected Album album;
    /**
     * artist of the song
     */
    protected Artist performer;
    /**
     * liked status of the song
     */
    protected boolean liked;

    /**
     * check if the song is liked
     * @return true if is liked
     */
    public boolean isLiked() {
        return liked;
    }

    /**
     * constructor of song
     * @param name name of song
     * @param entityID entityID of song
     * @param artist artist of song
     * @param album_ album of song
     */
    public Song(String name, String entityID, Artist artist, Album album_) {
        super(name, entityID);
        performer = artist;
        album = album_;
        liked = false;
    }

    /**
     * getter of album
     * @return album
     */
    protected Album getAlbum() {
        return album;
    }

    /**
     * setter of album
     * @param album set this album as the song
     */
    protected void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * getter of performer
     * @return artist
     */
    public Artist getPerformer() {
        return performer;
    }

    /**
     * setter of performer
     * @param performer set this artist as the performer of the song
     */
    public void setPerformer(Artist performer) {
        this.performer = performer;
    }

    /**
     * toString of the song
     * @return string when printing song
     */
    public String toString() {
        return super.toString() + " " + this.performer + " " + this.album;
    }

    /**
     * compare songs
     * @param s the object to be compared.
     * @return 1 if bigger than the other song, 0 if equals to the other song, -1 is smaller than the other song
     */
    public int compareTo(Song s) {
        return this.getName().compareTo(s.getName());
    }
}
