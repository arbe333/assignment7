import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Library will be created after the project is running.
 * it will be the register of data from database.
 */

public class Library {
    /**
     * songs of library
     */
    protected ArrayList<Song> songs;
    /**
     * artists of library
     */
    protected ArrayList<Artist> artists;
    /**
     * albums of library
     */
    protected ArrayList<Album> albums;

    /**
     * constructor of library
     */
    public Library() {
        songs = new ArrayList<>();
        artists = new ArrayList<>();
        albums = new ArrayList<>();
    }

    /**
     * find songs
     * @param s song to be found
     * @return true if songs contain this song.
     */
    public boolean findSong(Song s) {
        return songs.contains(s);
    }

    /**
     * getter of songs
     * @return songs
     */
    public ArrayList<Song> getSongs() {
        return songs;
    }

    /**
     * add song into library
     * @param s song to be added
     */
    public void addSong(Song s) {
        songs.add(s);
    }

    /**
     * add artist into library
     * @param artist artist to be added
     */
    public void addArtist(Artist artist) { artists.add(artist); }

    /**
     * add album into library
     * @param album album to be added
     */
    public void addAlbum(Album album) { albums.add(album); }

    /**
     * get artist from id
     * @param id id of artist
     * @return artist
     */
    public Artist getArtist(String id) {
        for (Artist art : artists) {
            if (id.equals(art.getEntityID())) {
                return art;
            }
        }
        return null;
    }
    /**
     * get album from id
     * @param id id of album
     * @return album
     */
    public Album getAlbum(String id) {
        for (Album alb : albums) {
            if (id.equals(alb.getEntityID())) {
                return alb;
            }
        }
        return null;
    }

    /**
     *  get data from musicBrainz
     * @param urlStr the api url
     * @return return response
     */
    public static String musicBrainz(String urlStr) {
        StringBuilder response = new StringBuilder();
        URL url;
        try {
            url = new URL(urlStr);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int code = httpConnection.getResponseCode();

            String message = httpConnection.getResponseMessage();
//            System.out.println(code + " " + message);
            if (code != HttpURLConnection.HTTP_OK) {
                return "NA";
            }
            InputStream inStream = connection.getInputStream();
            Scanner in = new Scanner(inStream);
            while (in.hasNextLine()) {
                response.append(in.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error reading response");
            return "NA";
        }
        return response.toString();
    }

    /**
     * save the library to database
     * @param statement statement of database connection
     * @throws SQLException errors when connection goes wrong
     */
    public void LibtoDB(Statement statement) throws SQLException {
        for (Artist artist: artists) {
            statement.executeUpdate("insert or ignore into artists values (\""+artist.getEntityID()+"\", +\""+artist.getName()+"\")");
        }

        for (Album album: albums) {
            statement.executeUpdate("insert or ignore into albums values (\""+album.getEntityID()+"\", +\""+album.getName()+"\", +\""+album.getArtist().getEntityID()+"\")");
        }

        for (Song song: songs) {
            statement.executeUpdate("insert or ignore into songs values (\""+song.getEntityID()+"\", +\""+song.getName()+"\", \""+song.getPerformer().getEntityID()+"\", \""+song.getAlbum().getEntityID()+"\")");
        }
    }

    /**
     * get json by API display in text UI
     * @param url the url
     * @throws ParseException errors when json parsing process goes wrong
     */
    public void readFromJSON(String url) throws ParseException {
        JSONParser parser = new JSONParser();
        String responseStr = musicBrainz(url);
        Object obj = parser.parse(responseStr);

        JSONObject jsonObject = (JSONObject) obj;
        JSONArray recordings = (JSONArray) jsonObject.get("recordings");
        System.out.println("Size of result = " + recordings.size());
        System.out.format("     %30s   %30s   %30s %n", "== NAME ==", "== ARTIST ==", "== ALBUM ==");
        for (int i = 0; i < recordings.size(); i++) {
            try {
                JSONObject recording = (JSONObject) recordings.get(i);
                String title = (String) recording.get("title");
                JSONArray artist = (JSONArray) recording.get("artist-credit");
                JSONObject artist0 = (JSONObject) artist.get(0);
                String artistName = (String) artist0.get("name");
                JSONArray releases = (JSONArray) recording.get("releases");
                JSONObject release = (JSONObject) releases.get(0);
                JSONObject jalbum = (JSONObject) release.get("release-group");
                String album = (String) jalbum.get("title");
                System.out.format("%2s | %30s | %30s | %30s %n", i + 1, title, artistName, album);
            } catch (Exception e) {
                System.out.format("%2s | %30s | %30s | %30s %n", i + 1, "", "", "");
            }
        }

        System.out.println("Which one do you want to add in your playlist? (Please type the number, type 0 to go back.)");
        Scanner sc = new Scanner(System.in);
        int idx = Integer.parseInt(sc.nextLine());
        idx--;
        if (idx != -1) {
            try {
                JSONObject recording = (JSONObject) recordings.get(idx);
                String title = (String) recording.get("title");
                String id = (String) recording.get("id");
                JSONArray artists = (JSONArray) recording.get("artist-credit");

                JSONObject artist0 = (JSONObject) artists.get(0);
                JSONObject artist_ = (JSONObject) artist0.get("artist");
                String artistName = (String) artist_.get("name");
                String artistID = (String) artist_.get("id");
                Artist artist = new Artist(artistName, artistID);
                addArtist(artist);

                JSONArray releases = (JSONArray) recording.get("releases");
                JSONObject release = (JSONObject) releases.get(0);
                JSONObject album_ = (JSONObject) release.get("release-group");
                String albumID = (String) album_.get("id");
                String albumName = (String) album_.get("title");
                Album album = new Album(albumName, albumID);
                album.setArtist(artist);
                addAlbum(album);

                addSong(new Song(title, id, artist, album));

                System.out.format("%2s | %30s | %30s | %30s %nhas been added to your playlist.%n", idx + 1, title, artistName, albumName);
            } catch (Exception e) {
                System.out.println("Invalid index");
            }
        }
    }

    /**
     * display songs in library
     */
    public void displaySongs() {
        System.out.println("=============================================================");
        for(Song song : songs) {
            System.out.format("%20s | %5s | %20s | %s %n", song.entityID, song.getName(), song.getPerformer(), song.getAlbum(), song.isLiked());
        }
        System.out.println("=============================================================\n");
    }

    /**
     * make library printable
     * @return string when printing library
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Song song : songs) {
            sb.append("Name:   ").append(song.getName())
                    .append(" (id: " + song.entityID+ ")")
                    .append("\tArtist: ").append(song.getPerformer())
                    .append("\tAlbum:  ").append(song.getAlbum())
                    .append("\tLiked:  ").append(song.isLiked())
                    .append("\n\n");
        }
        return sb.toString();
    }

    /**
     * write library to xml
     * @param filename xml filename
     */
    public void toXML(String filename) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"  ?>");
        sb.append("<library>");

        sb.append("<artists>");
        for (Artist artist: artists) {
            sb.append("<artist>");
            sb.append("<id>").append(artist.getEntityID()).append("</id>");
            sb.append("<name>").append(artist.getName()).append("</name>");
            sb.append("</artist>");
        }
        sb.append("<artists>");

        sb.append("<albums>");
        for (Album album: albums) {
            sb.append("<album>");
            sb.append("<id>").append(album.getEntityID()).append("</id>");
            sb.append("<name>").append(album.getName()).append("</name>");
            sb.append("</album>");
        }
        sb.append("<albums>");

        sb.append("<songs>");
        for (Song song: songs) {
            sb.append("<song>");
            sb.append("<id>").append(song.getEntityID()).append("</id>");
            sb.append("<name>").append(song.getName()).append("</name>");
            sb.append("<artist>").append(song.getPerformer().getName()).append("</artist>");
            sb.append("<album>").append(song.getAlbum().getName()).append("</album>");
            sb.append("</song>");
        }
        sb.append("</songs>");

        sb.append("</library>");

        try {
            FileWriter file = new FileWriter(filename);
            file.write(sb.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}