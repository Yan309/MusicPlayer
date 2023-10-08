/*
Group Members:
    Name: Muhammad Muneeb Zaidi     (FA21-BCS-036)
    Name: M. Aliyan                 (FA21-BCS-105)
    Name: Ajlal Haider              (FA21-BCS-054)
*/
package com.example.MusicPlayer;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class MusicPlayer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        MusicPlayerController2 controller = new MusicPlayerController2();

        BorderPane root = new BorderPane();
        root.setPrefSize(766.0, 458.0);
        root.getStyleClass().add("background");
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Styling.css")).toExternalForm());
        root.setPadding(new Insets(10));

        VBox libraryPane = new VBox(10);
        libraryPane.setAlignment(Pos.CENTER);
        libraryPane.setPadding(new Insets(10));
        libraryPane.getChildren().addAll(controller.getLibraryLabel(), controller.getLibraryListView(), controller.getAddButton());

        VBox playlistPane = new VBox(10);
        HBox saves = new HBox(10);
        saves.setAlignment(Pos.CENTER);
        saves.getChildren().addAll(controller.getSavePlaylistButton(), controller.getLoadPlaylistButton());
        playlistPane.setAlignment(Pos.CENTER);
        playlistPane.setPadding(new Insets(10));
        playlistPane.getChildren().addAll(controller.getPlaylistLabel(), controller.getPlaylistListView(),saves);

        HBox controlsPane = new HBox(10);
        controlsPane.setPrefSize(600.0, 72.0);
        controlsPane.getStyleClass().add("bottom");
        controlsPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Styling.css")).toExternalForm());
        controlsPane.setAlignment(Pos.CENTER);
        controlsPane.getChildren().addAll(controller.getPlayPreviousButton(), controller.getPlayButton(), controller.getPlayNextButton(), controller.getSortButton(),
                controller.getReversesortbutton(),
                controller.getShuffleButton(), controller.Empty);

        VBox control = new VBox(10);
        AnchorPane wave = new AnchorPane();
        wave.getStyleClass().add("playgif");
        wave.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Styling.css")).toExternalForm());
        control.setAlignment(Pos.CENTER);
        control.setPadding(new Insets(15));
        control.getChildren().addAll(controller.Current,wave);

        root.setLeft(libraryPane);
        root.setCenter(control);
        root.setBottom(controlsPane);
        BorderPane.setAlignment(controlsPane, javafx.geometry.Pos.CENTER_LEFT);
        root.setRight(playlistPane);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Music Player");

        primaryStage.show();

        controller.selectMusicFolder();
    }
}

class MusicPlayerController2 {
    private final Queue<String> playlistQueue;
    private final List<String> musicLibrary;
    private List<String> currentPlaylist;
    private MediaPlayer mediaPlayer;
    private final ListView<String> libraryListView;
    private final ListView<String> playlistListView;
    private final JFXButton addButton;
    private final JFXButton playNextButton;
    private final JFXButton playPreviousButton;
    private final JFXButton sortButton;
    private final JFXButton shuffleButton;
    private final JFXButton playButton;
    private final JFXButton savePlaylistButton;
    private final JFXButton loadPlaylistButton;
    private final Label libraryLabel;

    private final Label playlistLabel;
    public Label Current;
    private final Stack<String> previousSongs;
    private final JFXButton reversesortbutton;
    public JFXButton Empty;

    public MusicPlayerController2() {
        playlistQueue = new LinkedList<>();
        Current = new Label("Select Song");
        Current.setLayoutX(25.0);
        Current.setLayoutY(15.0);
        Current.setPrefSize(149.0, 68.0);
        Current.getStyleClass().add("Text");
        Current.setAlignment(javafx.geometry.Pos.CENTER);
        Current.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        Current.setFont(new Font("Agency FB", 29.0));

        musicLibrary = new ArrayList<>();
        currentPlaylist = new ArrayList<>();
        previousSongs = new Stack<>();
        libraryListView = new ListView<>();
        libraryListView.getStyleClass().add("Style");
        libraryListView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Styling.css")).toExternalForm());
        playlistListView = new ListView<>();
        playlistListView.getStyleClass().add("Style");
        playlistListView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Styling.css")).toExternalForm());
        addButton = new JFXButton("Add to Playlist");
        addButton.setPrefSize(102.0, 49.0);
        addButton.getStyleClass().add("Text");

        playButton = new JFXButton();
        playButton.getStyleClass().add("Text");
        ImageView playButtonImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play-button.png"))));
        playButtonImage.setFitHeight(41.0);
        playButtonImage.setFitWidth(58.0);
        playButtonImage.setPreserveRatio(true);
        playButtonImage.setPickOnBounds(true);
        playButtonImage.setEffect(new Glow(0.52));
        playButtonImage.setCursor(Cursor.DEFAULT);
        playButton.setGraphic(playButtonImage);

        playNextButton = new JFXButton();
        playNextButton.getStyleClass().add("Text");
        ImageView nextButtonImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("rewind-button.png"))));
        nextButtonImage.setFitHeight(41.0);
        nextButtonImage.setFitWidth(58.0);
        nextButtonImage.setPreserveRatio(true);
        nextButtonImage.setPickOnBounds(true);
        nextButtonImage.setEffect(new Glow(0.52));
        nextButtonImage.setCursor(Cursor.DEFAULT);
        nextButtonImage.setStyle("-fx-rotate: 180;");
        playNextButton.setGraphic(nextButtonImage);

        playPreviousButton = new JFXButton();
        playPreviousButton.setPrefSize(44.0, 49.0);
        playPreviousButton.getStyleClass().add("Text");
        ImageView prevButtonImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("rewind-button.png"))));
        prevButtonImage.setFitHeight(41.0);
        prevButtonImage.setFitWidth(58.0);
        prevButtonImage.setPreserveRatio(true);
        prevButtonImage.setPickOnBounds(true);
        prevButtonImage.setEffect(new Glow(0.52));
        prevButtonImage.setCursor(Cursor.DEFAULT);
        playPreviousButton.setGraphic(prevButtonImage);

        shuffleButton = new JFXButton();
        shuffleButton.setPrefSize(63.0, 49.0);
        shuffleButton.getStyleClass().add("Text");
        ImageView shuffleButtonImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons8-shuffle-32.png"))));
        shuffleButtonImage.setFitHeight(29.0);
        shuffleButtonImage.setFitWidth(35.0);
        shuffleButtonImage.setPreserveRatio(true);
        shuffleButtonImage.setPickOnBounds(true);
        shuffleButtonImage.setEffect(new Glow(0.52));
        shuffleButtonImage.setCursor(Cursor.DEFAULT);
        shuffleButton.setGraphic(shuffleButtonImage);

        savePlaylistButton = new JFXButton("Save Playlist");
        savePlaylistButton.setPrefSize(102.0, 49.0);
        savePlaylistButton.getStyleClass().add("Text");

        loadPlaylistButton = new JFXButton("Load Playlist");
        loadPlaylistButton.setPrefSize(102.0, 49.0);
        loadPlaylistButton.getStyleClass().add("Text");

        sortButton = new JFXButton("Sort Playlist");
        sortButton.setPrefSize(89.0, 48.0);
        sortButton.getStyleClass().add("Text");

        reversesortbutton  = new JFXButton("Reverse Sort Playlist");
        reversesortbutton.setPrefSize(150.0, 49.0);
        reversesortbutton.getStyleClass().add("Text");

        libraryLabel = new Label("Library");
        libraryLabel.setLayoutX(14.0);
        libraryLabel.setLayoutY(11.0);
        libraryLabel.setPrefSize(149.0, 68.0);
        libraryLabel.getStyleClass().add("Text");
        libraryLabel.setAlignment(javafx.geometry.Pos.CENTER);
        libraryLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        libraryLabel.setFont(new Font("Agency FB", 29.0));

        playlistLabel = new Label("Playlist");
        playlistLabel.setLayoutX(14.0);
        playlistLabel.setLayoutY(11.0);
        playlistLabel.setPrefSize(149.0, 68.0);
        playlistLabel.getStyleClass().add("Text");
        playlistLabel.setAlignment(javafx.geometry.Pos.CENTER);
        playlistLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        playlistLabel.setFont(new Font("Agency FB", 29.0));

        Empty = new JFXButton("Empty PlayList");
        Empty.setPrefSize(102.0, 49.0);
        Empty.getStyleClass().add("Text");

        Empty.setOnMouseClicked(event->{
            currentPlaylist.clear();
            refreshPlaylist();
        });
        addButton.setOnAction(event -> {
            String selectedSong = libraryListView.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                currentPlaylist.add(selectedSong);
                refreshPlaylist();
            }
        });
        libraryListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String play = libraryListView.getSelectionModel().getSelectedItem();
                playSong(play);
                Current.setText(play);
            }
        });
        playlistListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String play = playlistListView.getSelectionModel().getSelectedItem();
                playSong(play);
                Current.setText(play);
            }
        });
        playNextButton.setOnAction(event -> {
            try {
                if (!currentPlaylist.isEmpty()) {
                    String prev = currentPlaylist.remove(0);
                    previousSongs.push(prev);
                    refreshPlaylist();
                    String currentSong = currentPlaylist.get(0);
                    playlistQueue.add(currentSong);
                    if (!playlistQueue.isEmpty()) {
                        String currentSong2 = playlistQueue.poll();
                        playSong(currentSong2);
                        Current.setText(currentSong2);
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No More Songs");
                    alert.setHeaderText(null);
                    alert.setContentText("No more songs in the playlist.");
                    alert.showAndWait();
                }
            } catch (IndexOutOfBoundsException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while playing the next song.");
                alert.showAndWait();
            }
        });
        playPreviousButton.setOnAction(event -> {
            if (!previousSongs.isEmpty()) {
                String previousSong = previousSongs.pop();
                Queue<String> temp = new LinkedList<>(currentPlaylist);
                currentPlaylist = new ArrayList<>();
                refreshPlaylist();
                currentPlaylist.add(previousSong);
                currentPlaylist.addAll(temp);
                refreshPlaylist();
                playSong(previousSong);
                Current.setText(previousSong);
            }
        });

        playButton.setOnAction(event -> {
            if (mediaPlayer != null && (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING )) {
                mediaPlayer.pause();
            }else {
                if (!currentPlaylist.isEmpty()) {
                    String currentSong = currentPlaylist.get(0);
                    previousSongs.push(currentSong);
                    playlistQueue.offer(currentSong);
                    if (!playlistQueue.isEmpty()) {
                        String currentSong2 = playlistQueue.poll();
                        playSong(currentSong2);
                        Current.setText(currentSong2);
                        refreshPlaylist();
                    }
                    refreshPlaylist();
                }
                if (mediaPlayer != null && (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED ||
                        mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED)) {
                    mediaPlayer.play();
                }
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

        savePlaylistButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Playlist");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                savePlaylistToFile(file.getAbsolutePath(), currentPlaylist);
            }
        });
        sortButton.setOnAction(event -> {
            PriorityQueue<String> minHeap = new PriorityQueue<>(currentPlaylist);
            currentPlaylist = new ArrayList<>();
            while (!minHeap.isEmpty()) {
                currentPlaylist.add(minHeap.poll());
            }
            refreshPlaylist();
        });

        reversesortbutton.setOnAction(event -> {
            PriorityQueue<String> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            maxHeap.addAll(currentPlaylist);
            currentPlaylist = new ArrayList<>();
            while (!maxHeap.isEmpty()) {
                currentPlaylist.add(maxHeap.poll());
            }
            refreshPlaylist();
        });

        loadPlaylistButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Playlist");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                loadPlaylistFromFile(file);
                refreshPlaylist();
            }
        });
    }

    public ListView<String> getLibraryListView() {
        return libraryListView;
    }

    public ListView<String> getPlaylistListView() {
        return playlistListView;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getPlayNextButton() {
        return playNextButton;
    }

    public Button getPlayPreviousButton() {
        return playPreviousButton;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getSortButton() {
        return sortButton;
    }
    public Button getShuffleButton() {
        return shuffleButton;
    }
    public Button getReversesortbutton(){
        return reversesortbutton;
    }

    public Button getSavePlaylistButton() {
        return savePlaylistButton;
    }

    public Button getLoadPlaylistButton() {
        return loadPlaylistButton;
    }

    public Label getLibraryLabel() {
        return libraryLabel;
    }

    public Label getPlaylistLabel() {
        return playlistLabel;
    }

    public void selectMusicFolder() {
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

    private void playSong(String songPath) {
        Media media;
        try {
            media = new Media(Objects.requireNonNull(getClass().getResource("/" + songPath)).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    private void refreshLibrary() {
        ObservableList<String> libraryItems = FXCollections.observableArrayList(musicLibrary);
        libraryListView.setItems(libraryItems);
    }

    private void refreshPlaylist() {
        ObservableList<String> playlistItems = FXCollections.observableArrayList(currentPlaylist);
        playlistListView.setItems(playlistItems);
    }

    private void savePlaylistToFile(String filePath, List<String> playlist) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (String song : playlist) {
                writer.println(song);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadPlaylistFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            currentPlaylist.clear();
            while (scanner.hasNextLine()) {
                String song = scanner.nextLine();
                currentPlaylist.add(song);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}