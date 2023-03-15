package com.roy;

import com.roy.utils.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    @FXML
    public TextField bugsCount;
    @FXML
    public TextField bugsSpeed;
    @FXML
    public TextField height;
    @FXML
    public TextField width;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bugsSpeed.setText("5");
        this.bugsCount.setText("100");
        this.width.setText("800");
        this.height.setText("800");
    }

    public void startOnAction(ActionEvent event) {
        Platform.exit();
        Constants.ini(Integer.parseInt(this.width.getText()),
                Integer.parseInt(this.height.getText()),
        100, Integer.parseInt(this.bugsSpeed.getText()), Integer.parseInt(this.bugsCount.getText()));
    }
}