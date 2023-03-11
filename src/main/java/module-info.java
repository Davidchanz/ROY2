module com.example.roy2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires OpenGLChemiEngine;
    requires UnityMath;


    opens com.example.roy2 to javafx.fxml;
    exports com.example.roy2;
}