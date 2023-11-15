package sceneController;

import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneController.getInstance().mainStage=stage;
        stage.setTitle("i-ViSta4Bike");
        stage.setScene(SceneController.getInstance().main);
        stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-200);
        stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);
        stage.show();
    }
}