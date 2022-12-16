import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {

//    Library lib = new Library();

//    @Test
//    void toSQL() {
//    }
//
//    @Test
//    void fromSQL() {
//        Connection connection = null;
//        try {
//            // create a database connection
//
//            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
//            Statement statement = connection.createStatement();
//            statement.setQueryTimeout(30);  // set timeout to 30 sec.
//
//            ResultSet rsArtist = statement.executeQuery("select * from artists");
//
//            while (rsArtist.next()) {
//                // read the result set
//                Artist artist = new Artist();
//                artist.fromSQL(rsArtist);
//                lib.addArtist(artist);
//            }
//
//            // albums
//
//            ResultSet rsAlbum = statement.executeQuery("select * from albums");
//            while (rsAlbum.next()) {
//                // read the result set
//                Album album = new Album();
//                album.fromSQL(rsAlbum);
//                String artistID = String.valueOf(rsAlbum.getInt("artist"));
//
//                for (Artist a : lib.artists) {
//                    if (a.getEntityID().equals(artistID)) {
//                        album.setArtist(a);
//                    }
//                }
//
//                lib.addAlbum(album);
//            }
//
//            // songs
//            ResultSet rsSong = statement.executeQuery("select * from songs");
//            while (rsSong.next()) {
//                // read the result set
//                Song song = new Song();
//                song.fromSQL(rsSong);
//
//                String artistID = String.valueOf(rsSong.getInt("artist"));
//                String albumID = String.valueOf(rsSong.getInt("album"));
//
//                for (Album alb : lib.albums) {
//                    if (alb.getEntityID().equals(albumID)) {
//                        song.setAlbum(alb);
//                    }
//                }
//
//                for (Artist art : lib.artists) {
//                    if (art.getEntityID().equals(artistID)) {
//                        song.setPerformer(art);
//                    }
//                }
//
//                lib.addSong(song);
//            }
//
//            System.out.println(lib);
//        } catch (SQLException e) {
//            // if the error message is "out of memory",
//            // it probably means no database file is found
//            System.err.println(e.getMessage());
//        } finally {
//            try {
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException e) {
//                // connection close failed.
//                System.err.println(e.getMessage());
//            }
//        }
//    }
}