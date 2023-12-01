package sceneController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }
    SceneController controller;
    @Override
    public void start(Stage stage) throws Exception {

        controller= SceneController.getInstance();
        controller.mainStage=stage;

        stage.setTitle("i-ViSta4Bike");
        stage.setScene(controller.main);
        stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-200);
        stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);
        stage.show();

//        Platform.runLater(()->{
//            System.out.println("Iniciando proceso obtencion agenda");
//            Process process = null;
//
//            String pythonInterpreter = "python"; // O la ruta completa a python.exe si es necesario
//            String pythonScript = "procesoAgenda.py"; // Reemplaza con la ruta a tu script Python
//
//            String[] pythonArgs = new String[]{
//                    pythonInterpreter,
//                    pythonScript
//            };
//            ProcessBuilder processBuilder = new ProcessBuilder(pythonArgs);
//            processBuilder.redirectErrorStream(true);
//            try {
//                process = processBuilder.start();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            InputStream inputStream = process.getInputStream();
//            BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while(true){
//                try {
//                    if (!((line = reader.readLine())!=null)) break;
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println(line);
//            }
//
//            int n= 0;
//            try {
//                n = process.waitFor();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println("Estado del proceso: " + n);
//            System.out.println("Agenda obtenida");
//            }
//        );





    }
}