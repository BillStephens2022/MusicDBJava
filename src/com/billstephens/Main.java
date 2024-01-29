package com.billstephens;

import com.billstephens.model.Album;
import com.billstephens.model.Artist;
import com.billstephens.model.Datasource;
import com.billstephens.model.SongArtist;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_ASC);
        if(artists == null) {
            System.out.println("No artists found!");
            return;
        }

        for(Artist artist : artists) {
            System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
        }

        List<String> albumsByArtist =
                datasource.queryAlbumsByArtist("Metallica", Datasource.ORDER_BY_ASC);

        for (String album : albumsByArtist) {
            System.out.println(album);
        }

        List<SongArtist> songArtists = datasource.queryArtistsBySong("Go Your Own Way", Datasource.ORDER_BY_ASC);
        if (songArtists == null) {
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for (SongArtist artist : songArtists) {
            System.out.println("Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track = " + artist.getTrack());
        }

        datasource.querySongsMetadata();

        datasource.close();
    }
}
