package sceneController;

import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        System.out.println("Iniciando proceso obtencion agenda");
        Process process = null;

        String pythonInterpreter = "python"; // O la ruta completa a python.exe si es necesario
        String pythonScript = "procesoAgenda.py"; // Reemplaza con la ruta a tu script Python

        String[] pythonArgs = new String[]{
                pythonInterpreter,
                pythonScript
        };
        ProcessBuilder processBuilder = new ProcessBuilder(pythonArgs);
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine())!=null){
            System.out.println(line);
        }

        int n= process.waitFor();
        System.out.println("Estado del proceso: " + n);
        System.out.println("Agenda obtenida");
    }
}