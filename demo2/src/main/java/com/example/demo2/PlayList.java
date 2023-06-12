package com.example.demo2;

import java.util.ArrayList;

public class PlayList {
    private ArrayList<Song> Playlist = new ArrayList<>();
    public ArrayList<Song> getPlaylist() {
        return Playlist;
    }
    public void setPlaylist(ArrayList<Song> playlist) {
        Playlist = playlist;
    }
}