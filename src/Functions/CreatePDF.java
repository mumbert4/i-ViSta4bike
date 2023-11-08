package Functions;



import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.awt.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class CreatePDF {

    public HashMap<String, String> franjas;

    public static CreatePDF instance;

    public static CreatePDF getInstance(){
        if(instance==null) instance = new CreatePDF();
        return instance;
    }

    private CreatePDF(){
        franjas= new HashMap<>();
        franjas.put("Morning","01");
        franjas.put("Afternoon","02");
        franjas.put("Evening","03");
        franjas.put("LateEvening","04");
        franjas.put("Night","05");
    }

    public void createPDF(ArrayList<String> neighbours, ArrayList<String> hours){
        for(String n: neighbours){
            String nAux = n.substring(0,n.indexOf('-'));
            try{
                PDDocument document = new PDDocument();
                Boolean first = true;
                for(String h:hours){
                    String hAux= franjas.get(h);
                    PDPage page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    PDPageContentStream content = new PDPageContentStream(document,page);
                    if(first){
                        PDImageXObject logoIdeai = PDImageXObject.createFromFile(System.getProperty("user.dir")+"/Images/Marca_UPC_IDEAI_BLAU.png",document);
                        System.out.println(page.getMediaBox().getHeight());
                        content.drawImage(logoIdeai,25,770,300,75);
                        content.beginText();
                        content.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),22);
                        content.newLineAtOffset(400,800);
                        content.setNonStrokingColor(new Color(22,156,205));
                        content.showText("i-ViSta4Bike");
                        content.endText();
                        first = false;
                    }
                    content.beginText();
                    content.setNonStrokingColor(Color.BLACK);
                    content.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),14);
                    content.newLineAtOffset(60,760);
                    content.showText("Expected behaviour");
                    content.endText();
                    content.beginText();
                    content.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),14);
                    content.newLineAtOffset(360,760);
                    content.showText("Observed Behaviour");
                    content.endText();

                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String date = currentDate.format(formatter);

                    String path =System.getProperty("user.dir")+"/../Output/"+ date;
                    System.out.println("Path pdf " + path);
                    PDImageXObject expected = PDImageXObject.createFromFile(path+"/Graficos_average_" + nAux+hAux+ ".png",document);
                    PDImageXObject observed = PDImageXObject.createFromFile(path+"/Graficos_day_" + nAux+hAux+ ".png",document);
                    content.drawImage(expected,0,455,300,300);
                    content.drawImage(observed,300,455,295,300);


                    content.close();
                    document.save(path+"/resultPDF_." + nAux+hAux + ".pdf");


                }
            }
            catch (Exception e){
                System.out.printf(e.getMessage());
            }

        }
    }

}
