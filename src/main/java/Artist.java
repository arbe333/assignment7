import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * class Artist
 */
public class Artist extends Entity {
    /**
     * contructor of artist
     * @param name name of this artist
     * @param entityID entityID of this artist
     */
    public Artist(String name, String entityID) {
        super(name, entityID);
    }
}
