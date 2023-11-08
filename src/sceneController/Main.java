package sceneController;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneController.getInstance().mainStage=stage;
        stage.setTitle("i-ViSta4Bike");
        stage.setScene(SceneController.getInstance().main);
        stage.setWidth(1375);
        stage.setHeight(970);
        stage.show();
    }
}