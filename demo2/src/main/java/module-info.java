module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.jfoenix;

    opens com.example.MusicPlayer to javafx.fxml;
    exports com.example.MusicPlayer;
}