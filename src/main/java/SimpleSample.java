/**
 * This is the main class that controls the running process.
 * At first, You have to create a user and password for yourself
 * It will create a new playlist for you.
 *
 */
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * class for main function
 */
public class SimpleSample {

    /**
     * default constructor
     */
    public SimpleSample() {
    }

    /**
     * Get the user's info. If it's a new user, create a new user info.
     * @return username, password, log in / sign up
     */
    public static ArrayList<String> GetUser() {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> ret = new ArrayList<>();
        String username = "";
        String password = "";
//        SymbolTable st = new SymbolTable();
        System.out.println("1. Log in / 2. Sign up ?");
        String loginSignup = sc.nextLine();
        if (loginSignup.equals("1")) {
            System.out.println("Username:");
            username = sc.nextLine();
            System.out.println("Password:");
            password = sc.nextLine();
        } else if (loginSignup.equals("2")) {
            boolean verified = false;
            while (!verified) {
                System.out.println("New username:");
                username = sc.nextLine();
                System.out.println("New password:");
                String nPassword = sc.nextLine();
                System.out.println("Verify password:");
                String vPassword = sc.nextLine();
                if (nPassword.equals(vPassword)) {
                    password = nPassword;
                    verified = true;
                } else {
                    System.out.println("Password should be the same. Please verify it again.");
                }
            }
        } else {
            System.out.println("Please enter \"1\" or \"2\".");
        }

        ret.add(username);
        ret.add(password);
        ret.add(loginSignup);

        return ret;
    }


    /**
     * Load data from user's database to library
     * @param statement connected to user's database
     * @return a library
     * @throws SQLException errors when connection goes wrong
     */
    public static Library DBtoLib(Statement statement) throws SQLException {
        Library lib = new Library();
        ResultSet rsArtists = statement.executeQuery("select * from artists;");
        while (rsArtists.next()) {
            String name = rsArtists.getString("name");
            String id = rsArtists.getString("id");
            lib.addArtist(new Artist(name, id));
        }

        ResultSet rsAlbums = statement.executeQuery("select * from albums;");
        while (rsAlbums.next()) {
            String name = rsAlbums.getString("name");
            String id = String.valueOf(rsAlbums.getString("id"));
            String artistID = rsAlbums.getString("artist");

            Album album = new Album(name, id);
            album.setArtist(lib.getArtist(artistID));
            lib.addAlbum(album);
        }

        ResultSet rsSongs = statement.executeQuery("select * from songs;");
        while (rsSongs.next()) {
            Artist artist = lib.getArtist(rsSongs.getString("artist"));
            Album album = lib.getAlbum(rsSongs.getString("album"));
            String name = rsSongs.getString("name");
            String id = rsSongs.getString("id");
            lib.addSong(new Song(name, id, artist, album));
        }

        return lib;
    }

    /**
     * Initiate a new Database when there is a new user
     * @param statement connected to user's database
     * @throws SQLException errors when connection goes wrong
     */
    public static void initiateDatabase(Statement statement) throws SQLException {
        statement.executeUpdate("CREATE TABLE artists (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL);\n" +
                "CREATE TABLE songs (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, artist TEXT, album TEXT, FOREIGN KEY (artist) REFERENCES artists (id), FOREIGN KEY (album) REFERENCES albums (id));\n" +
                "CREATE TABLE albums (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, artist TEXT, FOREIGN KEY(artist) REFERENCES artists(id));");
    }


    /**
     * Main program
     *
     * Introduction:
     * When you first run it, you have to create a new user, and search outside your playlist, because it's empty now.
     * After you add some songs into your playlist, you can leave it. Those songs in your playlist will be saved into database.
     * At the mean time, a xml file of user's playlist will be generated.
     * So do not search before it was saved, or you will get nothing.
     * @param args main function
     */
    public static void main(String[] args) {
        Connection connection = null;

        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String un = null;

            boolean logedIn = false;
            while (!logedIn) {
                logedIn = true;

                ArrayList<String> userPass = GetUser();
                un = userPass.get(0);
                String pw = userPass.get(1);
                String loginSignup = userPass.get(2);

                int id = 0;
                ResultSet countId = statement.executeQuery("select * from users;");
                while (countId.next()) {
                    id++;
                }

                if (loginSignup.equals("1")) { // log in
                    ResultSet rs = statement.executeQuery("select * from users where name == \"" + un + "\"");
                    if (!rs.next()) {
                        System.out.println("User can not be found.");
                        logedIn = false;
                    } else {
                        if(rs.getString("password").equals(pw)) {
                            System.out.println("Welcome back, " + un);
                        } else {
                            System.out.println("Wrong password.");
                            logedIn = false;
                        }
                    }
                }
                if (loginSignup.equals("2")) { //sign up
                    ResultSet rs = statement.executeQuery("select * from users where name == \"" + un + "\"");
                    if (!rs.next()) {
                        statement.executeUpdate("insert into users values ("+id+", +\""+un+"\", \""+pw+"\")");
                        System.out.println("New user created!");
                    } else {
                        System.out.println("This username has already existed, please change a name.");
                        logedIn = false;
                    }
                }
            }

            Connection connectionPL = DriverManager.getConnection("jdbc:sqlite:" + un + ".db");
            Statement statementPL = connectionPL.createStatement();
            statementPL.setQueryTimeout(30);  // set timeout to 30 sec.

            // initiate database
            try {
                initiateDatabase(statementPL);
                System.out.println("Your playlist has been created!");
            } catch (Exception e) {
                System.out.println("Your playlist >>> ");
            }
            // from user DB to library
            Library lib = DBtoLib(statementPL);
            lib.displaySongs(); // display playlist

            Scanner sc = new Scanner(System.in);
            boolean run = true;
            while (run) {
                System.out.println("Operation >>>\n 1. Check my playlist\n 2. search\n 3. search outside your playlist\n 4. leave my playlist");
                String op = sc.nextLine();
                switch (op) {
                    case "1":
                        lib.displaySongs();
                        break;
                    case "2":
                        System.out.print("Search >>> ");
                        String search = sc.nextLine();
                        try {
                            ResultSet rsSearch = statementPL.executeQuery("select * from songs where name like \"" + search + "\";");

                            String name = rsSearch.getString("name");
                            Artist artist = lib.getArtist(rsSearch.getString("artist"));
                            Album album = lib.getAlbum(rsSearch.getString("album"));

                            System.out.format("%20s | %20s | %s %n", name, artist.getName(), album.getName());
                        } catch (Exception e) {
                            System.out.println("*** No result. Try again or search outside the database. ***\n");
                        }
                        break;
                    case "3":
                        System.out.print("Search for songs not in the database >>> ");
                        String scSearch = sc.nextLine();
                        String url = "https://musicbrainz.org/ws/2/recording?query=%22" + scSearch.replace(" ", "%20") + "%22&fmt=json";
                        lib.readFromJSON(url);
                        break;
                    case "4":
                        lib.LibtoDB(statementPL);
                        lib.toXML(un + "Playlist.xml");
                        run = false;
                        System.out.println("Bye!");
                        break;
                    default:
                        System.out.println("Please input operations between 1 - 4.");
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}