module com.example.roy {
    requires javafx.controls;
    requires javafx.fxml;
    requires OpenGLChemiEngine;
    requires UnityMath;
    requires java.desktop;


    opens com.roy to javafx.fxml;
    exports com.roy;
    exports com.roy.buildings;
    opens com.roy.buildings to javafx.fxml;
}