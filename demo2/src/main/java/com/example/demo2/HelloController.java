package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class HelloController {
    @FXML
    private Label CurrentlyPlaying;
    @FXML
    private ImageView playPause;
    @FXML
    private ListView<String> Library;
    @FXML
    private ListView<String> PlayList;
    @FXML
    protected void onHelloButtonClick() {
        CurrentlyPlaying.setText("Welcome to JavaFX Application!");
    }
}