package controllers;

import Functions.CreatePDF;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.stage.FileChooser;
import org.controlsfx.control.CheckComboBox;
import sceneController.SceneController;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML
    private CheckComboBox<String> barrioChoice;

    @FXML
    private CheckComboBox <String> horarioChoice;

    @FXML
    private Label fileInfo;

    @FXML
    private Label file;

    @FXML
    private RadioButton unexpectedEvents;

    @FXML
    private RadioButton generateCSV;

    @FXML
    private RadioButton generatePDF;
    @FXML
    private RadioButton displayPlots;

    public String pathToFile;
    public HashMap<String,String> franjas;
    private ArrayList<String> selectedNeighbours;
    private ArrayList<String> selectedHours;
    @FXML
    void importCSV(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Elige CSV");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV","*.csv")
        );
        File f= fileChooser.showOpenDialog(null);


        if(f==null){
            fileInfo.setText("No has elegido ningun fichero");
            file.setText("");
        }
        else{
            System.out.println("Fitxer CSV seleccionat: " + f.getAbsolutePath());
            fileInfo.setText("Fichero elegido:");
            file.setText(f.getName());
            pathToFile = f.getAbsolutePath();
            System.out.println(pathToFile);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        barrioChoice.getItems().add("01-el Raval");
        barrioChoice.getItems().add("02-el Barri Gòtic");
        barrioChoice.getItems().add("03-la Barceloneta");
        barrioChoice.getItems().add("04-Sant Pere, Santa Caterina i la Ribera");
        barrioChoice.getItems().add("05-el Fort Pienc");
        barrioChoice.getItems().add("06-la Sagrada Família");
        barrioChoice.getItems().add("07-la Dreta de l'Eixample");
        barrioChoice.getItems().add("08-l'Antiga Esquerra de l'Eixample");
        barrioChoice.getItems().add("09-la Nova Esquerra de l'Eixample");
        barrioChoice.getItems().add("10-Sant Antoni");
        barrioChoice.getItems().add("11-el Poble-sec");
        barrioChoice.getItems().add("12-la Marina del Prat Vermell");
        barrioChoice.getItems().add("13-la Marina de Port");
        barrioChoice.getItems().add("15-Hostafrancs");
        barrioChoice.getItems().add("16-la Bordeta");
        barrioChoice.getItems().add("17-Sants - Badal");
        barrioChoice.getItems().add("18-Sants");
        barrioChoice.getItems().add("19-les Corts");
        barrioChoice.getItems().add("20-la Maternitat i Sant Ramon");
        barrioChoice.getItems().add("21-Pedralbes");
        barrioChoice.getItems().add("23-Sarrià");
        barrioChoice.getItems().add("24-les Tres Torres");
        barrioChoice.getItems().add("25-Sant Gervasi - la Bonanova");
        barrioChoice.getItems().add("26-Sant Gervasi - Galvany");
        barrioChoice.getItems().add("27-el Putxet i el Farró");
        barrioChoice.getItems().add("28-Vallcarca i els Penitents");
        barrioChoice.getItems().add("30-la Salut");
        barrioChoice.getItems().add("31-la Vila de Gràcia");
        barrioChoice.getItems().add("32-el Camp d'en Grassot i Gràcia Nova");
        barrioChoice.getItems().add("33-el Baix Guinardó");
        barrioChoice.getItems().add("35-el Guinardó");
        barrioChoice.getItems().add("36-la Font d'en Fargues");
        barrioChoice.getItems().add("37-el Carmel");
        barrioChoice.getItems().add("41-la Vall d'Hebron");
        barrioChoice.getItems().add("42-la Clota");
        barrioChoice.getItems().add("43-Horta");
        barrioChoice.getItems().add("44-Vilapicina i la Torre Llobeta");
        barrioChoice.getItems().add("45-Porta'");
        barrioChoice.getItems().add("46-el Turó de la Peira");
        barrioChoice.getItems().add("48-la Guineueta");
        barrioChoice.getItems().add("49-Canyelles");
        barrioChoice.getItems().add("50-les Roquetes");
        barrioChoice.getItems().add("51-Verdun");
        barrioChoice.getItems().add("52-la Prosperitat");
        barrioChoice.getItems().add("53-la Trinitat Nova");
        barrioChoice.getItems().add("55-Ciutat Meridiana");
        barrioChoice.getItems().add("57-la Trinitat Vella");
        barrioChoice.getItems().add("58-Baró de Viver");
        barrioChoice.getItems().add("59-el Bon Pastor");
        barrioChoice.getItems().add("60-Sant Andreu");
        barrioChoice.getItems().add("61-la Sagrera");
        barrioChoice.getItems().add("62-el Congrés i els Indians");
        barrioChoice.getItems().add("63-Navas");
        barrioChoice.getItems().add("64-el Camp de l'Arpa del Clot");
        barrioChoice.getItems().add("65-el Clot");
        barrioChoice.getItems().add("66-el Parc i la Llacuna del Poblenou");
        barrioChoice.getItems().add("67-la Vila Olímpica del Poblenou");
        barrioChoice.getItems().add("68-el Poblenou");
        barrioChoice.getItems().add("69-Diagonal Mar i el Front Marítim del Poblenou");
        barrioChoice.getItems().add("70-el Besòs i el Maresme");
        barrioChoice.getItems().add("71-Provençals del Poblenou");
        barrioChoice.getItems().add("72-Sant Martí de Provençals");
        barrioChoice.getItems().add("73-la Verneda i la Pau");


        horarioChoice.getItems().add("Morning: from 6:00 h to 12:00 h");
        horarioChoice.getItems().add("Afternoon: from 12:00 h to 18:00 h");
        horarioChoice.getItems().add("Evening: from 18:00 h to 21:00 h");
        horarioChoice.getItems().add("LateEvening: from 21:00h to 24:00h");
        horarioChoice.getItems().add("Night: from 0:00 am to 6:00 am");

        System.out.println(System.getProperty("user.dir"));
    }



    void runPython() throws InterruptedException, MalformedURLException {
        Process process = null;
        String wd = System.getProperty("user.dir");
        //String aux = wd+"/../Solucion/scripts/";
        //String aux = "/home/miquel/Documentos/Solucion/scripts/";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        try{

            for(String n: selectedNeighbours){
                for(String h: selectedHours){
//                    System.out.println("Ejecutando script para: " + n + " " + h);
//                    String script= "procesoPython.py --neighborhood \"" + n + "\" --timeSlot \"" + h + "\" --path \"" + pathToFile.replace("\\","\\\\")+"\"";
//                    System.out.println(script);
//                    //String command = "python3 " + aux + script ;
//                    String command = "python3 " + script;
//                    ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);

                    String pythonInterpreter = "python"; // O la ruta completa a python.exe si es necesario
                    String pythonScript = "procesoPython.py"; // Reemplaza con la ruta a tu script Python

                    // Argumentos para el script Python
                    String[] pythonArgs = new String[] {
                            pythonInterpreter,
                            pythonScript,
                            "--neighborhood",
                            n,
                            "--timeSlot",
                            h,
                            "--path",
                            pathToFile.replace("\\","\\\\")
                    };

                    System.out.println(pythonArgs);

                    // Crear el proceso
                    ProcessBuilder processBuilder = new ProcessBuilder(pythonArgs);
                    processBuilder.redirectErrorStream(true);

                    process = processBuilder.start();

                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line = reader.readLine())!=null){
                        System.out.println(line);
                    }
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        int n= process.waitFor();
        System.out.println("Estado del proceso: " + n);

    }


    @FXML
    void okAction(ActionEvent event) throws IOException, InterruptedException {

        selectedNeighbours = new ArrayList<>(barrioChoice.getCheckModel().getCheckedItems());
        selectedHours = new ArrayList<String>();
        for(String s : horarioChoice.getCheckModel().getCheckedItems()){
            int index = s.indexOf(':');
            selectedHours.add(s.substring(0,index));
        }
        System.out.println(selectedNeighbours);
        System.out.println(selectedHours);
        //CreatePDF.getInstance().createPDF();
        if(selectedHours.size()== 0 || selectedNeighbours.size()==0 || pathToFile==null){
            System.out.println("Faltan datos para realizar el script");
        }
        else{
            runPython();
            if(displayPlots.isSelected()){
                SceneController.getInstance().createSearch(selectedNeighbours, selectedHours);
            }
            if(generatePDF.isSelected()){
                CreatePDF.getInstance().createPDF(selectedNeighbours,selectedHours);
            }
            if(unexpectedEvents.isSelected()){

            }

        }
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneController.getInstance().close();
    }
}

