package Functions;



import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.awt.*;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CreatePDF {

    public HashMap<String, String> franjas;

    public static CreatePDF instance;


    public String fecha = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    char[] letrasDias = {' ', 'D', 'L', 'M', 'M', 'J', 'V', 'S'};
    String[] dias = { " ", "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    public String dia="";
    int diaSemana;
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
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);
        diaSemana= calendar.get(Calendar.DAY_OF_WEEK);

        this.dia= dias[diaSemana];
    }

    public void createPDF(ArrayList<String> neighbours, ArrayList<String> hours, Boolean onlyUnexpected) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = currentDate.format(formatter);

        String path = System.getProperty("user.dir") + "/../Output/" + date;
        System.out.println("Path pdf " + path);
        for (String n : neighbours) {
            PDDocument document = new PDDocument();
            String nAux = n.substring(0, n.indexOf('-'));
            try {
                for (String h : hours) {

                    String hAux = franjas.get(h);
                    String id = nAux + hAux;



                    PDImageXObject expected = PDImageXObject.createFromFile(path+"/Graficos_average_" + id+ ".png",document);
                    PDImageXObject observed = PDImageXObject.createFromFile(path+"/Graficos_day_" + id+ ".png",document);


                    FileReader reader = new FileReader(path + "/resultado_" + id + ".json");
                    System.out.println("Path:" + path + "/resultado_" + id + ".json");
                    JSONTokener tokener = new JSONTokener(reader);
                    System.out.println(tokener);
                    JSONArray jsonArray = new JSONArray(tokener);

                    String respuesta = jsonArray.getJSONObject(0).getString("respuesta");
                    String text = "Expected";
                    ArrayList<String> eventos = new ArrayList<>();
                    if(onlyUnexpected && respuesta.equals("Expected")){
                        continue;
                    }


                    System.out.println("Hemos continuado");

                    PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);



                    PDPage page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    PDPageContentStream content = new PDPageContentStream(document, page);
                    PDImageXObject logoIdeai = PDImageXObject.createFromFile(
                            System.getProperty("user.dir") + "/Images/Marca_UPC_IDEAI_BLAU.png", document);
                    System.out.println(page.getMediaBox().getHeight());
                    content.drawImage(logoIdeai, 25, 770, 300, 75);
                    content.drawImage(expected,0,445,300,300);
                    content.drawImage(observed,300,445,295,300);

                    content.beginText();
                    content.setFont(font, 22);
                    content.newLineAtOffset(400, 800);
                    content.setNonStrokingColor(new Color(22, 156, 205));
                    content.showText("i-ViSta4Bike");
                    content.endText();


                    content.beginText();
                    content.setFont(font, 10);
                    content.newLineAtOffset(80, 747);
                    content.setNonStrokingColor(new Color(22, 156, 205));
                    content.showText("G"+"("+n+"-"+ letrasDias[diaSemana]+"-"+h+")");
                    content.endText();

                    content.beginText();
                    content.setFont(font, 10);
                    content.newLineAtOffset(400, 747);
                    content.showText("G"+"("+n+"-"+new SimpleDateFormat("dd-MM-yyyy").format(new Date()) +")");
                    content.endText();




                    content.beginText();
                    content.setNonStrokingColor(Color.BLACK);
                    content.setFont(font, 14);
                    content.newLineAtOffset(60, 760);
                    content.showText("Expected behaviour");
                    content.endText();
                    content.beginText();
                    content.setFont(font, 14);
                    content.newLineAtOffset(360, 760);
                    content.showText("Observed Behaviour");
                    content.endText();

                    content.beginText();
                    content.setNonStrokingColor(Color.BLACK);
                    content.setFont(font, 14);
                    content.newLineAtOffset(40, 420);
                    content.showText("Prediction for tomorrow:");
                    content.endText();

                    float margin = 40;
                    float yStart = 380; // Initial position for the first page
                    float yPosition = yStart;
                    if (respuesta.equals("Unexpected")) {
                        yPosition= 340;
                        text = "Unexpected";
                        JSONArray eventosJSON = jsonArray.getJSONObject(0).getJSONArray("eventos");
                        for (int i = 0; i < 10; ++i) {
                            eventos.add(eventosJSON.getString(i));
                        }
                        content.beginText();
                        content.setNonStrokingColor(new Color(110,110,110));
                        content.setFont(font, 12);
                        content.newLineAtOffset(40, 400);
                        content.showText("The behaviour of the " + n.substring(n.indexOf('-')+1) + " on " +this.dia+ " " + this.fecha + " "+ h +" is expected to be signifficantly");
                        content.endText();

                        content.beginText();
                        content.setFont(font, 12);
                        content.newLineAtOffset(40, 385);
                        content.showText("different from usual. Check if a virtual station might be installed in advance");
                        content.endText();

                        content.beginText();
                        content.setFont(font, 12);
                        content.newLineAtOffset(40, 360);
                        content.showText("The agenda of Barcelona city reports the following events by tomorrow " + h + ":");
                        content.endText();

                    }
                    else{
                        content.beginText();
                        content.setNonStrokingColor(new Color(110,110,110));
                        content.setFont(font, 12);
                        content.newLineAtOffset(40, 400);
                        content.showText("The behaviour of the " + n.substring(n.indexOf('-')+1) + " on " +this.dia+ " " + this.fecha + " "+ h +" is expected to be normal.");
                        content.endText();
                    }


                    content.beginText();
                    content.setNonStrokingColor(new Color(22, 156, 205));
                    content.setFont(font, 14);
                    content.newLineAtOffset(190, 420);
                    content.showText(text);
                    content.endText();




                    content.setNonStrokingColor(Color.BLACK);
                    for (String s : eventos) {
                        if (page == null || content == null) {
                            page = new PDPage(PDRectangle.A4);
                            document.addPage(page);
                            content = new PDPageContentStream(document, page);
                        }


                        ArrayList<String> lines = splitString(s, 85);
                        for (String line : lines) {
                            if (yPosition < margin) {
                                content.close();
                                // Create a new page
                                page = new PDPage(PDRectangle.A4);
                                document.addPage(page);

                                // Create a new content stream for the new page
                                content = new PDPageContentStream(document, page);

                                // Reset the position for the new page
                                yPosition = page.getMediaBox().getHeight() - margin;
                            }

                            if (content == null) {
                                // Create the first page and content stream
                                page = new PDPage(PDRectangle.A4);
                                document.addPage(page);
                                content = new PDPageContentStream(document, page);
                            }

                            content.setFont(font, 12);
                            content.beginText();
                            content.newLineAtOffset(margin, yPosition);
                            content.showText(line);
                            content.endText();
                            yPosition -= 15; // Adjust this value based on your desired line spacing
                        }
                    }

                    if (content != null) {
                        content.close();
                    }



                    reader.close();
                }
                document.save(path + (onlyUnexpected? "/resultUnexpectedPDF_" : "/resultPDF_") + nAux +".pdf");
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> splitString(String s, int chunkSize) {
        if (s == null || s.isEmpty() || chunkSize <= 0) {
            // Manejar casos inválidos
            return new ArrayList<>();
        }
        s="·"+s;
        ArrayList<String> result = new ArrayList<>();
        int length = s.length();
        for (int i = 0; i < length; i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, length);
            result.add(s.substring(i, endIndex));
        }

        return result;
    }

}
