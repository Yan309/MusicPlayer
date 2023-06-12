package com.example.demo2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

public class MusicPlayer extends Application {

    private Queue<String> playlistQueue;
    private List<String> musicLibrary;
    private List<String> currentPlaylist;
    private MediaPlayer mediaPlayer;
    private ListView<String> libraryListView;
    private ListView<String> playlistListView;
    private Button addButton;
    private Button playButton;
    private Button sortButton;
    private Button shuffleButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        playlistQueue = new LinkedList<>();
        musicLibrary = new ArrayList<>();
        currentPlaylist = new ArrayList<>();
        libraryListView = new ListView<>();
        playlistListView = new ListView<>();
        addButton = new Button("Add to Playlist");
        playButton = new Button("Play Next");
        sortButton = new Button("Sort Playlist");
        shuffleButton = new Button("Shuffle Playlist");
        addButton.setOnAction(event -> {
            String selectedSong = libraryListView.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                currentPlaylist.add(selectedSong);
                refreshPlaylist();
            }
        });
        playButton.setOnAction(event -> {
            if (!currentPlaylist.isEmpty()) {
                String currentSong = currentPlaylist.remove(0);
                playlistQueue.add(currentSong);
                if (!playlistQueue.isEmpty()) {
                    String currentSong2 = playlistQueue.poll();
                    playSong(currentSong2);
                    refreshPlaylist();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Now Playing");
                alert.setHeaderText(null);
                alert.setContentText("Playing: " + currentSong);
                alert.showAndWait();
                refreshPlaylist();
            }
        });
        sortButton.setOnAction(event -> {
            Collections.sort(currentPlaylist);
            refreshPlaylist();
        });
        shuffleButton.setOnAction(event -> {
            Collections.shuffle(currentPlaylist);
            refreshPlaylist();
        });
        VBox root = new VBox(10, libraryListView, playlistListView, addButton, playButton, sortButton, shuffleButton);
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.setTitle("Music Player");
        primaryStage.show();
        selectMusicFolder();
    }
    private void playSong(String songPath) {
        Media media = null;
        try {
            media = new Media(getClass().getResource("/"+songPath).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
    private void selectMusicFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Music Folder");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            loadMusicLibrary(selectedDirectory);
            refreshLibrary();
        }
    }
    private void loadMusicLibrary(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    musicLibrary.add(file.getName());
                }
            }
        }
    }
    private void refreshLibrary() {
        ObservableList<String> libraryItems = FXCollections.observableArrayList(musicLibrary);
        libraryListView.setItems(libraryItems);
    }
    private void refreshPlaylist() {
        ObservableList<String> playlistItems = FXCollections.observableArrayList(currentPlaylist);
        playlistListView.setItems(playlistItems);
    }
}