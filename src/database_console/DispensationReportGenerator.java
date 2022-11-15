package database_console;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.spire.doc.*;
import com.spire.doc.documents.*;
import com.spire.doc.fields.TextRange;
import com.spire.doc.formatting.CharacterFormat;

/**

 @author R-Mule
 */
public class DispensationReportGenerator {

    protected ArrayList<DispensationItem> items = new ArrayList<>();
    protected ArrayList<String> rxReportData;

    public DispensationReportGenerator(ArrayList<String> rxReportData) {
        this.rxReportData = rxReportData;
    }

    //Takes the path of the Qs1 report and loads dispensation items.
    public void loadAndSaveQs1ReportData(String qs1FilePath, String savePath, String startDate, String endDate) {
        //load the Qs1 Information
        BufferedReader in;
        try
        {
            in = new BufferedReader(new FileReader(qs1FilePath));

            String line;
            in.readLine();//remove the first line, it is just junk headers.

            while ((line = in.readLine()) != null)
            {
                String[] tokens = line.split("\",\"", 9);   //limits the split to 2 array elements ie only the first occurance so it will keep any colons in the value portion

                if (tokens.length < 9) //not all data is there just move along
                {
                    continue;
                }

                //System.out.println(tokens[0].substring(1) + " " + tokens[1] + " " + tokens[2] + " " + tokens[3] + " " + tokens[4] + " " + tokens[5] + " " + tokens[6] + " " + tokens[7] + " " + tokens[8].substring(0, tokens[8].length() - 1));
                int month = Month.valueOf(tokens[0].substring(1, tokens[0].indexOf(' ')).toUpperCase()).getValue();
                int day = Integer.parseInt(tokens[0].substring(tokens[0].indexOf(' ') + 1, tokens[0].indexOf(' ') + 3));
                int year = Integer.parseInt(tokens[0].substring(tokens[0].lastIndexOf(' ') + 1));
                LocalDate fillDate = LocalDate.of(year, month, day);
                //System.out.println(fillDate);
                int rxNumber = Integer.parseInt(tokens[2]);
                String drugName = tokens[3];
                double qtyDispensed = Double.parseDouble(tokens[4].replace(",", ""));
                String patientName = tokens[1];

                DispensationItem item = new DispensationItem();
                item.rxNumber = rxNumber;
                item.drugName = drugName;
                item.fillDate = fillDate;
                item.patientName = patientName;
                item.qtyDispensed = qtyDispensed;

                items.add(item);
            }

            //Merge with existing information
            for (String rxData : rxReportData)
            {
                int month = Integer.parseInt(rxData.substring(6, 14).trim().substring(0, 2));
                int day = Integer.parseInt(rxData.substring(6, 14).trim().substring(3, 5));
                int year = Integer.parseInt("20" + rxData.substring(6, 14).trim().substring(6, 8));

                LocalDate pickupDate = LocalDate.of(year, month, day);
                int rxNumber = Integer.parseInt(rxData.substring(26).trim());

                for (DispensationItem item : items)
                {
                    if (rxNumber == item.rxNumber && pickupDate.isAfter(item.fillDate))
                    {
                        item.pickupDate = pickupDate;
                        item.wasPickedUp = true;
                    }
                }
            }

        }
        catch (Exception ex)
        {
            Logger.getLogger(DispensationReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Sort
        Object[] a = items.toArray();
        Arrays.sort(a);
        Document doc = new Document();
        CharacterFormat format = new CharacterFormat(doc);
        //Set font
        format.setFontName("Calibri");
        format.setFontSize(8);
        CharacterFormat tableHeaderFormat = new CharacterFormat(doc);
        //Set font
        tableHeaderFormat.setFontName("Calibri");
        tableHeaderFormat.setFontSize(8);
        tableHeaderFormat.setBold(true);
        Section section = doc.addSection();
        Paragraph para = section.addParagraph();
        para.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        Table table = section.addTable(true);
        table.resetCells(a.length + 1, 6);
        table.autoFit(AutoFitBehaviorType.Auto_Fit_To_Contents);

        for (int i = -1; i < a.length; i++)
        {
            Paragraph para1;
            Paragraph para2;
            Paragraph para3;
            Paragraph para4;
            Paragraph para5;
            Paragraph para6;

            if (i == -1) //Headers
            {
                para1 = table.getRows().get(0).getCells().get(0).addParagraph();
                para2 = table.getRows().get(0).getCells().get(1).addParagraph();
                para3 = table.getRows().get(0).getCells().get(2).addParagraph();
                para4 = table.getRows().get(0).getCells().get(3).addParagraph();
                para5 = table.getRows().get(0).getCells().get(4).addParagraph();
                para6 = table.getRows().get(0).getCells().get(5).addParagraph();
                para1.appendText("Date Filled");
                para2.appendText("Pickup Date");
                para3.appendText("Patient Name");
                para4.appendText("Rx Number");
                para5.appendText("Drug Name");
                para6.appendText("Qty Dispensed");
                applyParagraphStyle(para1, tableHeaderFormat);
                applyParagraphStyle(para2, tableHeaderFormat);
                applyParagraphStyle(para3, tableHeaderFormat);
                applyParagraphStyle(para4, tableHeaderFormat);
                applyParagraphStyle(para5, tableHeaderFormat);
                applyParagraphStyle(para6, tableHeaderFormat);
            }
            else
            {
                para1 = table.getRows().get(i + 1).getCells().get(0).addParagraph();
                para2 = table.getRows().get(i + 1).getCells().get(1).addParagraph();
                para3 = table.getRows().get(i + 1).getCells().get(2).addParagraph();
                para4 = table.getRows().get(i + 1).getCells().get(3).addParagraph();
                para5 = table.getRows().get(i + 1).getCells().get(4).addParagraph();
                para6 = table.getRows().get(i + 1).getCells().get(5).addParagraph();
                DispensationItem item = (DispensationItem) a[i];
                para1.appendText(item.fillDate.format(formatters));
                para2.appendText(item.wasPickedUp ? item.pickupDate.format(formatters) : "No Pickup");
                para3.appendText(item.patientName);
                para4.appendText(Integer.toString(item.rxNumber));
                para5.appendText(item.drugName);
                String qtyDispensed = "";
                if (item.qtyDispensed % 1 != 0)
                {
                    qtyDispensed = Double.toString(item.qtyDispensed);
                }
                else
                {
                    qtyDispensed = Double.toString(item.qtyDispensed).substring(0, Double.toString(item.qtyDispensed).indexOf('.'));
                }

                para6.appendText(qtyDispensed);
                para6.getFormat().setHorizontalAlignment(HorizontalAlignment.Right);
                applyParagraphStyle(para1, format);
                applyParagraphStyle(para2, format);
                applyParagraphStyle(para3, format);
                applyParagraphStyle(para4, format);
                applyParagraphStyle(para5, format);
                applyParagraphStyle(para6, format);
            }

        }
        table.getFirstRow().isHeader(true);
        HeaderFooter header = section.getHeadersFooters().getHeader();
        Paragraph headerParagraph = header.addParagraph();
        headerParagraph.appendText(startDate + " - " + endDate + " - Dispensation - 24 D/S and Up - ");
        headerParagraph.appendField("Page ", FieldType.Field_Page);
        headerParagraph.appendText(" of ");
        headerParagraph.appendField("", FieldType.Field_Num_Pages);

        CharacterFormat headerFormat = new CharacterFormat(doc);
        //Set font
        headerFormat.setFontName("Calibri");
        headerFormat.setFontSize(11);
        headerFormat.setBold(true);
        headerParagraph.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
        applyParagraphStyle(headerParagraph, headerFormat);
        doc.saveToFile(savePath, FileFormat.Docx_2013);
        doc.close();

    }

    private void applyParagraphStyle(Paragraph para, CharacterFormat format) {
        //Loop through the childObjects of paragraph
        for (int j = 0; j < para.getChildObjects().getCount(); j++)
        {
            if (para.getChildObjects().get(j) instanceof TextRange)
            {
                TextRange tr = (TextRange) para.getChildObjects().get(j);
                //Apply character format
                tr.applyCharacterFormat(format);
            }
        }
    }
}
