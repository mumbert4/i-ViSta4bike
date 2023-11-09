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

    public void createPDF(ArrayList<String> neighbours, ArrayList<String> hours) {
        for (String n : neighbours) {
            String nAux = n.substring(0, n.indexOf('-'));
            try {
                for (String h : hours) {
                    PDDocument document = new PDDocument();
                    boolean first = true;
                    String hAux = franjas.get(h);
                    String id = nAux + hAux;

                    PDPage page = null;
                    PDPageContentStream content = null;

                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String date = currentDate.format(formatter);

                    String path = System.getProperty("user.dir") + "/../Output/" + date;
                    System.out.println("Path pdf " + path);

                    PDImageXObject expected = PDImageXObject.createFromFile(path+"/Graficos_average_" + id+ ".png",document);
                    PDImageXObject observed = PDImageXObject.createFromFile(path+"/Graficos_day_" + id+ ".png",document);


                    FileReader reader = new FileReader(path + "/resultado_" + id + ".json");
                    System.out.println("Path:" + path + "/resultado_" + id + ".json");
                    JSONTokener tokener = new JSONTokener(reader);
                    System.out.println(tokener);
                    JSONArray jsonArray = new JSONArray(tokener);

                    String respuesta = jsonArray.getJSONObject(0).getString("respuesta");
                    System.out.println("Respuesta: " + respuesta);
                    String text = "Expected";
                    ArrayList<String> eventos = new ArrayList<>();
                    if (respuesta.equals("Unexpected")) {
                        text = "Unexpected";
                        JSONArray eventosJSON = jsonArray.getJSONObject(0).getJSONArray("eventos");
                        for (int i = 0; i < eventosJSON.length(); ++i) {
                            eventos.add(eventosJSON.getString(i));
                        }
                    }
                    PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                    float margin = 50;
                    float yStart = 400; // Initial position for the first page
                    float yPosition = yStart;
                    int linesPerPage = 20; // Number of lines per page
                    for (String s : eventos) {
                        if (page == null || content == null) {
                            page = new PDPage(PDRectangle.A4);
                            document.addPage(page);
                            content = new PDPageContentStream(document, page);


                            if (first) {
                                PDImageXObject logoIdeai = PDImageXObject.createFromFile(
                                        System.getProperty("user.dir") + "/Images/Marca_UPC_IDEAI_BLAU.png", document);
                                System.out.println(page.getMediaBox().getHeight());
                                content.drawImage(logoIdeai, 25, 770, 300, 75);
                                content.drawImage(expected,0,455,300,300);
                                content.drawImage(observed,300,455,295,300);
                                content.beginText();
                                content.setFont(font, 22);
                                content.newLineAtOffset(400, 800);
                                content.setNonStrokingColor(new Color(22, 156, 205));
                                content.showText("i-ViSta4Bike");
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
                                content.newLineAtOffset(60, 420);
                                content.showText("Prediction for tomorrow:");
                                content.endText();

                                content.beginText();
                                content.setNonStrokingColor(new Color(22, 156, 205));
                                content.setFont(font, 14);
                                content.newLineAtOffset(250, 420);
                                content.showText(text);
                                content.endText();
                                content.setNonStrokingColor(Color.BLACK);



                                first = false;
                            }


                        }


//                        content.setFont(font, 12);
//                        content.beginText();
//                        content.setNonStrokingColor(Color.BLACK);
//                        content.newLineAtOffset(margin, yPosition);
//                        content.showText(s);
//                        content.endText();
//                        yPosition -= 15; // Adjust this value based on your desired line spacing
//                        if (yPosition < 50) {
//                            content.close();
//                            page = null;
//                            content = null;
//                            yPosition = new PDPage().getMediaBox().getHeight()-margin;
//                        }

                        ArrayList<String> lines = splitString(s, 95);
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

                    document.save(path + "/resultPDF_" + nAux + hAux + ".pdf");
                    document.close();

                    reader.close();
                }
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

        ArrayList<String> result = new ArrayList<>();
        int length = s.length();
        for (int i = 0; i < length; i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, length);
            result.add(s.substring(i, endIndex));
        }

        return result;
    }

}
