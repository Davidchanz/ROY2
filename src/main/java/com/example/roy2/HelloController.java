package com.example.roy2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.engine.Scene;
import org.engine.utils.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    public Label TRACKFOOD;
    @FXML
    public Label PopulationRED;
    @FXML
    public Label PopulationBLUE;
    @FXML
    public Label TracksHOME;
    public TextField bugsCount;
    public TextField bugsSpeed;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    private Stage stage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void startOnAction(ActionEvent event) {
        //this.stage.close();
        new Roy(this, Integer.parseInt(this.bugsCount.getText()), Integer.parseInt(this.bugsSpeed.getText()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}