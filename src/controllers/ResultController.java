package controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import org.json.JSONTokener;


public class ResultController implements Initializable {


    @FXML
    private Label behaviourInfo;
    @FXML
    private ImageView expectedGraph;

    @FXML
    private ImageView observedGraph;

    @FXML
    private Label eventType;
    @FXML
    private Label title;
    @FXML
    private Label title2;

    @FXML
    private ListView<String> infoText;
    public String id="";//barrio(03) y luego franja(03); ej:0303
    char[] letrasDias = {' ', 'D', 'L', 'M', 'M', 'J', 'V', 'S'};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        System.out.println("Iniciando nueva escena");


    }

    public void setId(String id){
        this.id = id;
        LocalDate currentDate = LocalDate.now();

        // Crea un formateador para convertir la fecha en una cadena en formato "yyyyMMdd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = currentDate.format(formatter);
        System.out.println("Date: " + date);


        String path =System.getProperty("user.dir")+"/../Output/"+ date;
        expectedGraph.setImage(new Image("file:" +path+"/Graficos_average_"+id+".png"));
        observedGraph.setImage(new Image("file:" +path+"/Graficos_day_"+id+".png"));

        try{
            FileReader reader = new FileReader(path + "/resultado_"+ id +".json");
            System.out.println("Path:" + path + "/resultado_"+ id +".json");
            JSONTokener tokener = new JSONTokener(reader);
            System.out.println(tokener);
            JSONArray jsonArray = new JSONArray(tokener);

            String respuesta = jsonArray.getJSONObject(0).getString("respuesta");
            System.out.println("Respuesta: " + respuesta);

            if(respuesta.equals("Unexpected")){

                JSONArray eventosJSON = jsonArray.getJSONObject(0).getJSONArray("eventos");
                ArrayList<String> eventos= new ArrayList<>();
                for(int i =0; i < eventosJSON.length(); ++i){
                    eventos.add(eventosJSON.getString(i));
                }

                behaviourInfo.setText("The behaviour is expected to be signifficantly different from usual. Check if a virtual station might be installed in advice.\nThe agenda of Barcelona city reports the following events by tomorrow:");
                eventType.setText("Unexpected event");
                int aux = Math.min(10,eventos.size());
                for(int i =0; i < aux; ++i)infoText.getItems().add(eventos.get(i));


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void setTitle(String n, String h){
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

        title.setText("G"+"("+n+"-"+ letrasDias[diaSemana]+"-"+h+")");
        title2.setText("G"+"("+n+"-"+new SimpleDateFormat("dd-MM-yyyy").format(new Date()) +")");
    }
}
