package com.billstephens.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "music.db";  // name of database (.db file)
    public static final String CONNECTION_STRING = // location of .db file
            "jdbc:sqlite:C:\\Users\\two4o\\Desktop\\JavaDevelopment\\MusicDBJava\\" + DB_NAME;

    // set variable names for all tables and columns in the DB.
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    private Connection conn;

    // Opens connection to database
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    // closes connection to database
    public void close() {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public List<Artist> queryArtists(int sortOrder) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                // note that ASC ends up being the default value (i.e. if
                // nothing specified or if typo when method is called).
                sb.append("ASC");
            }
        }
        // try with resources statement will close statement and result set automatically.
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString())) {
            List<Artist> artists = new ArrayList<>();
            while(results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }

            return artists;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<String> queryAlbumsByArtist(String artistName, int sortOrder) {
        // Sample SQL query
        // SELECT albums.name FROM albums INNER JOIN artists ON albums.artist = artists._id WHERE artists.name = "Pink Floyd" ORDER BY albums.name COLLATE NOCASE ASC
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(TABLE_ALBUMS);
        sb.append(".");
        sb.append(COLUMN_ALBUM_NAME);
        sb.append(" FROM ");
        sb.append(TABLE_ALBUMS);
        sb.append(" INNER JOIN ");
        sb.append(TABLE_ARTISTS);
        sb.append(" ON ");
        sb.append(TABLE_ALBUMS);
        sb.append(".");
        sb.append(COLUMN_ALBUM_ARTIST);
        sb.append(" = ");
        sb.append(TABLE_ARTISTS);
        sb.append(".");
        sb.append(COLUMN_ARTIST_ID);
        sb.append(" WHERE ");
        sb.append(TABLE_ARTISTS);
        sb.append(".");
        sb.append(COLUMN_ARTIST_NAME);
        sb.append(" = \"");
        sb.append(artistName);
        sb.append("\"");

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(TABLE_ALBUMS);
            sb.append(".");
            sb.append(COLUMN_ALBUM_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL statement = " + sb.toString());

        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString())) {
          List<String> albums = new ArrayList<>();
          while(results.next()) {
              albums.add(results.getString(1));
          }

          return albums;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
}
