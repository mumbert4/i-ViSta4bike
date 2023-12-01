package sceneController;

import Functions.RemoveFolders;
import controllers.InfoController;
import controllers.MainController;
import controllers.ResultController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class SceneController {

    private static SceneController instance;
    public Scene main;
    public Stage mainStage;

    public Stage infoStage;
    public HashMap<String,Stage> stages;
    public HashMap<String, String> franjas;
    public static SceneController getInstance() throws IOException {
        if(instance==null) instance = new SceneController();
        return instance;
    }
    private SceneController() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/mainAux.fxml"));
        this.main = new Scene(fxmlLoader.load(), 320, 240);
        this.stages = new HashMap<>();
        franjas= new HashMap<>();
        franjas.put("Morning","01");
        franjas.put("Afternoon","02");
        franjas.put("Evening","03");
        franjas.put("LateEvening","04");
        franjas.put("Night","05");

        infoStage = new Stage();
    }


    public void close(){
        closeAll();
        //RemoveFolders.getInstance().removeFolders();
        this.mainStage.close();
    }

    public void closeAll(){
        for(Stage s: stages.values()) s.close();
    }
    public void createSearch(ArrayList<String> neighbours, ArrayList<String> hours) throws IOException {
        closeAll();
        FXMLLoader fxmlLoader;
        for(String n : neighbours){
            for(String h: hours){

                fxmlLoader = new FXMLLoader(ResultController.class.getResource("/views/resultAux.fxml"));
                Stage aux = new Stage();
                aux.setScene(new Scene(fxmlLoader.load(), 320, 240));
                aux.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-200);
                aux.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);
                aux.setTitle(n + " " + h);
                ResultController r = fxmlLoader.getController();
                r.setTitle(n,h);
                int index = n.indexOf('-');
                String nAux = n.substring(0,index);
                r.setId(nAux+franjas.get(h));
                aux.show();
                stages.put(n+"-"+h,aux);
            }
        }


    }

    public void showLoading() throws IOException {
        System.out.println("Enseñando loading");
        FXMLLoader fxmlLoader= new FXMLLoader(InfoController.class.getResource("/views/wait.fxml"));
        infoStage.setScene(new Scene(fxmlLoader.load(),400,50));
        infoStage.setResizable(false);
        infoStage.show();
        System.out.println("Loading enseñado");
    }

    public void closeLoading(){
        if(infoStage.isShowing())infoStage.close();
    }
    public void showInfo() throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader(InfoController.class.getResource("/views/info.fxml"));
        infoStage.setScene(new Scene(fxmlLoader.load(),500,50));
        infoStage.setResizable(false);
        infoStage.show();
    }

}
