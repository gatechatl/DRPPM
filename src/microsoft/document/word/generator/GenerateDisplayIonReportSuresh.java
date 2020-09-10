package microsoft.document.word.generator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

public class GenerateDisplayIonReportSuresh {
	public static String parameter_info() {
		/*        String peptide_psm_list = args[0];
        String path_img = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\output_img_raw\\";
        String path_csv = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\output_csv_raw\\";
        String outputFile = args[3]; //docx*/
		return "[peptide_psm_list] [path_img] [pathToCSV] [outputFile]";
	}
    public static void execute(String[] args) throws Exception {
        XWPFDocument doc = new XWPFDocument();

        CTDocument1 document = doc.getDocument();
        CTBody body = document.getBody();
        
        if (!body.isSetSectPr()) {
             body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();

        if(!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        
        CTPageSz pageSize = section.getPgSz();

        pageSize.setOrient(STPageOrientation.LANDSCAPE);
        
        pageSize.setW(BigInteger.valueOf(15840));
        pageSize.setH(BigInteger.valueOf(12240));
        
        HashMap map = new HashMap();
        
        String peptide_psm_list = args[0];
        String path_img = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\output_img_raw\\";
        String path_csv = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\output_csv_raw\\";
        String outputFile = args[3]; //docx
        
        if (!outputFile.contains(".docx")) {
        	outputFile += ".docx";
        }
        boolean first = true;
        //String orderFileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\GenerateReport\\order2.txt";
        //String orderFileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\Input\\MertzBai_ReOrderedList.txt";
        //String orderFileName = "T:\\Computational Biology\\Timothy Shaw\\PROTEOMICS\\DisplayIonReport_20150823\\one_hit_wonder.txt";
		FileInputStream fstream2 = new FileInputStream(peptide_psm_list);
		DataInputStream din2 = new DataInputStream(fstream2);
		BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));		
		//in2.readLine();
		while (in2.ready()) {
			String str2 = in2.readLine();
			String[] split2 = str2.split("\t");
	        //String geneName = split2[0];
			
			String geneName = split2[0];
	        
	        //String peptideName = split2[1].replaceAll("\\*", "").split("\\.")[1];
			String peptideName = split2[1];
	        /*if (map.containsKey(geneName + peptideName)) {
	        	System.out.println("Non Uniq");
	        } else {
	        	map.put(geneName + peptideName, "");
	        }*/
	        /*String outFileName = split2[2].split("/")[split2[2].split("/").length - 1];
	        String scanNum = outFileName.split("\\.")[1];
	        String charge = outFileName.split("\\.")[3];*/
	        /*String scanNum = split2[10];
	        String xcorr = split2[14];
	        String dJn = split2[15];
	        String charge = split2[12];*/
	        
	        String dtaFile = split2[2];
	        String id = dtaFile.replaceAll(".dta", "");
	        String scanNum = dtaFile.split("_")[1];
	        String charge = dtaFile.split("_")[2];
	        String link = split2[4];
	        XWPFParagraph data = doc.createParagraph();
	        XWPFRun run_data = data.createRun();
	        
	        //String name = "sp_A0JPJ7_OLA1_RAT_IGIVGLPNVGK_tmt1.93127.1.2.out";
	        //String name = geneName.replaceAll("\\|", "_") + "_" + peptideName + "_" + outFileName;
	        String name = geneName.replaceAll("\\|", "_") + "_" + peptideName;
	        //System.out.println(name);
	        //String path = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\FinalOutput\\";	        
	
	        XWPFParagraph p1 = doc.createParagraph();
	        p1.setAlignment(ParagraphAlignment.CENTER);
	        p1.setBorderBottom(Borders.DOUBLE);
	        p1.setBorderTop(Borders.DOUBLE);
	
	        p1.setBorderRight(Borders.DOUBLE);
	        p1.setBorderLeft(Borders.DOUBLE);
	        p1.setBorderBetween(Borders.SINGLE);
	
	        p1.setVerticalAlignment(TextAlignment.TOP);
	
	        XWPFRun r1 = p1.createRun();
	        if (!first) {
	        	r1.addBreak(BreakType.PAGE);
	        }
	        r1.setBold(true);
	        r1.addBreak();		    
	        r1.setText("ID: " + geneName);
	        r1.addTab();
	        r1.setText("Peptide: " + peptideName);
	        r1.addBreak();
	        r1.setText("Scan Number: " + scanNum);
	        r1.addTab();
	        r1.setText("Charge: " + charge);
	        r1.addTab();
	        r1.addBreak();
	        r1.setText("Weblink: " + link);
	        r1.addTab();
	        //r1.setText("Jscore: " + xcorr);
	        //r1.addTab();
	        //r1.setText("dJn: " + dJn);	        
	        r1.addBreak();
		    
	        r1.setBold(true);
	        //r1.setFontFamily("Courier");
	        //r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
	        //r1.setTextPosition(100);
	
	        XWPFTable table = doc.createTable();
	        //table.setCellMargins(0, 0, 0, 0);
	        //create first row
	        //table.setColBandSize(0);
	        
	        //table.getCTTbl().getTblPr().unsetTblBorders();
	        
	        XWPFTableRow tableRowOne = table.getRow(0);
	        
		    tableRowOne.addNewTableCell().setText("");        
	        
	        XWPFParagraph text = table.getRow(0).getCell(0).getParagraphs().get(0);//doc.createParagraph();
	        XWPFRun text_run = text.createRun();
	        
		    //text_run.addBreak();
		    text_run.setFontSize(8);
		    if (new File(path_csv + "/" + id + ".csv").exists()) {
			FileInputStream fstream = new FileInputStream(path_csv + "/" + id + ".csv");
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				
				int i = 0;
				
				for (String s: split) {
					if (i == 0) {
						text_run.setText("  " + s);
						text_run.addTab();
					} else {
						text_run.setText(s);
						text_run.addTab();
					}
					/*if (i == 0) {
						text_run.setText("  " + s);
						text_run.addTab();
					} else if (i < split.length - 1) {
						text_run.setText(s);					
						text_run.addTab();
					} else {
						text_run.setText(s + "  ");
					}*/
					i++;			
				}
				text_run.addBreak();
			    
				//text_run.addCarriageReturn();
			}
			in.close();
			
			text_run.setText("* Ion Match, # Phosphorylated");
	        //text_run.addPicture(text_is, XWPFDocument.PICTURE_TYPE_GIF, text_imgFile, Units.toEMU(400), Units.toEMU(200)); // 200x200 pixels        
	        
	        //System.out.println(table.getRow(0).getCell(1).getParagraphs().size());
	        XWPFParagraph title = table.getRow(0).getCell(1).getParagraphs().get(0);//doc.createParagraph();            
	        XWPFRun run = title.createRun();
	        
	        //run.setText("Fig.1 A Natural Scene");
	        run.setBold(true);
	        title.setAlignment(ParagraphAlignment.CENTER);
	        String imgFile = path_img + "/" + id + ".gif";
	        System.out.println(imgFile);
		    FileInputStream is = new FileInputStream(imgFile);
		    run.addBreak();
		    run.addPicture(is, XWPFDocument.PICTURE_TYPE_GIF, imgFile, Units.toEMU(400), Units.toEMU(300)); // 200x200 pixels
		    run.setText("		                                                                                                       ");
		    run.addBreak();
		    is.close();
		    		    
		    } // file exists
		    else {
		    	//System.out.println("Missing: " + id);
		    }
		    first = false;
		}
		in2.close();

        /*XWPFParagraph p2 = doc.createParagraph();
        p2.setAlignment(ParagraphAlignment.RIGHT);

        //BORDERS
        p2.setBorderBottom(Borders.DOUBLE);
        p2.setBorderTop(Borders.DOUBLE);
        p2.setBorderRight(Borders.DOUBLE);
        p2.setBorderLeft(Borders.DOUBLE);
        p2.setBorderBetween(Borders.SINGLE);

        XWPFRun r2 = p2.createRun();
        r2.setText("jumped over the lazy dog");
        r2.setStrike(true);
        r2.setFontSize(20);

        XWPFRun r3 = p2.createRun();
        r3.setText("and went away");
        r3.setStrike(true);
        r3.setFontSize(20);
        r3.setSubscript(VerticalAlign.SUPERSCRIPT);


        XWPFParagraph p3 = doc.createParagraph();
        p3.setWordWrap(true);
        p3.setPageBreak(true);
                
        //p3.setAlignment(ParagraphAlignment.DISTRIBUTE);
        p3.setAlignment(ParagraphAlignment.BOTH);
        p3.setSpacingLineRule(LineSpacingRule.EXACT);

        p3.setIndentationFirstLine(600);
        

        
        XWPFRun r4 = p3.createRun();
        r4.setTextPosition(20);
        r4.setText("To be, or not to be: that is the question: "
                + "Whether 'tis nobler in the mind to suffer "
                + "The slings and arrows of outrageous fortune, "
                + "Or to take arms against a sea of troubles, "
                + "And by opposing end them? To die: to sleep; ");
        r4.addBreak(BreakType.PAGE);
        r4.setText("No more; and by a sleep to say we end "
                + "The heart-ache and the thousand natural shocks "
                + "That flesh is heir to, 'tis a consummation "
                + "Devoutly to be wish'd. To die, to sleep; "
                + "To sleep: perchance to dream: ay, there's the rub; "
                + ".......");
        r4.setItalic(true);
//This would imply that this break shall be treated as a simple line break, and break the line after that word:

        XWPFRun r5 = p3.createRun();
        r5.setTextPosition(-10);
        r5.setText("For in that sleep of death what dreams may come");
        r5.addCarriageReturn();
        r5.setText("When we have shuffled off this mortal coil,"
                + "Must give us pause: there's the respect"
                + "That makes calamity of so long life;");
        r5.addBreak();
        r5.setText("For who would bear the whips and scorns of time,"
                + "The oppressor's wrong, the proud man's contumely,");
        
        r5.addBreak(BreakClear.ALL);
        r5.setText("The pangs of despised love, the law's delay,"
                + "The insolence of office and the spurns" + ".......");

        */
        
        
        FileOutputStream out = new FileOutputStream(outputFile); //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\Report\\simple.docx");
        doc.write(out);
        out.close();

    }
}
