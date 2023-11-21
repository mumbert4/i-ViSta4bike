package Functions;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RemoveFolders {
    public static RemoveFolders instance;

    public static RemoveFolders getInstance(){
        if(instance==null) instance = new RemoveFolders();
        return instance;
    }

    private RemoveFolders(){
    }

    public void removeFolders(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = currentDate.format(formatter);

        String path = System.getProperty("user.dir") + "/../Output/" + date;
        eliminarCarpeta(new File(path));
    }
    private void eliminarCarpeta(File carpeta){
        if (carpeta.isDirectory()) {
            // Lista los archivos y carpetas dentro de la carpeta
            File[] archivos = carpeta.listFiles();

            // Elimina recursivamente cada archivo y carpeta dentro de la carpeta
            if (archivos != null) {
                for (File archivo : archivos) {
                    eliminarCarpeta(archivo);
                }
            }
        }

        // Elimina la carpeta actual
        carpeta.delete();
    }

}
