import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * class Album
 */

public class Album extends Entity {
    /**
     * songs of albums
     */
    protected ArrayList<Song> songs;
    /**
     * artist of albums
     */
    protected Artist artist;

    /**
     * constructor of an album with name and id
     * @param name the name of this album
     * @param entityID the entityID of this album
     */
    public Album(String name, String entityID) {
        super(name, entityID);
    }

    /**
     * check if two albums are equal
     * @param otherAlbum the other album
     * @return true if equal
     */
    public boolean equals(Album otherAlbum) {
        if ((this.entityID.equals(otherAlbum.entityID)) &&
                (this.name.equals(otherAlbum.getName()))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get the artist of this album
     * @return class artist
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * set this album with param artist
     * @param artist artist of this album
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

}