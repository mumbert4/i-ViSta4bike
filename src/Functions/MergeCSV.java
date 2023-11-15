package Functions;



import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeCSV {
    public HashMap<String, String> franjas;
    public static MergeCSV instance;

    public static MergeCSV getInstance(){
        if(instance==null) instance = new MergeCSV();
        return instance;
    }

    private MergeCSV(){
        franjas= new HashMap<>();
        franjas.put("Morning","01");
        franjas.put("Afternoon","02");
        franjas.put("Evening","03");
        franjas.put("LateEvening","04");
        franjas.put("Night","05");
    }

    public void mergeCSV(ArrayList<String> neighbours) throws IOException {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = currentDate.format(formatter);

        String path = System.getProperty("user.dir") + "/../Output/" + date;
        for(String n : neighbours){
            Boolean first = true;
            String nAux = n.substring(0,n.indexOf('-'));
            ArrayList<String> archivos = listarArchivos(path,nAux);
            String archivoSalida = path+"/resultCSV_"+nAux+".csv";
            if(archivos.size()>0){
                for(String archivo : archivos){
                    List<String> lineasCSV = leerCSV(path+'/'+archivo);
                    Files.deleteIfExists(Paths.get(path+'/'+archivo));
                    // Imprimir las líneas leídas del CSV
                    for (String linea : lineasCSV) {
                        System.out.println(linea);
                    }
                    escribirCSV(archivoSalida, lineasCSV,first);
                    first=false;
                }
            }


        }
    }


    public ArrayList<String> listarArchivos(String path, String n){
        ArrayList<String> archivos = new ArrayList<>();
        File dir = new File(path);

        if(dir.exists() && dir.isDirectory()){
            File[] aux = dir.listFiles(((dir1, name) ->
                    name.toLowerCase().startsWith("resultado_"+n) && name.toLowerCase().endsWith(".csv")));

            if (aux != null) {
                for (File archivo : aux) {
                    archivos.add(archivo.getName());
                }
                System.out.println("Archivos encontrados: " + archivos);
            }
        }
        else {
            System.err.println("El directorio no existe o no es válido.");
        }
        return archivos;
    }


    public static List<String> leerCSV(String archivoCSV) {
        List<String> lineas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineas;
    }
    public static void escribirCSV(String archivoCSV, List<String> lineas,Boolean first) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCSV,true))) {
            if(first){
                for (String linea : lineas) {
                    bw.write(linea);
                    bw.newLine(); // Añadir una nueva línea después de cada línea del CSV
                }
            }
            else {
                bw.write(lineas.get(1));
                bw.newLine(); // Añadir una nueva línea después de cada línea del CSV

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
