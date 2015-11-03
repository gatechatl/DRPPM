package WordDocumentGenerator.KinaseSummary;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.HashMap;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Tr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

public class GenerateWordKinaseSummary {

	
	public static String parameter_info() {
		return "[kinase_summary_file] [heatmap_folder] [output docx file]";
	}
    public static void execute(String[] args) throws Exception {
    	
    	
    	String kinase_summary_file = args[0];
    	String heatmap_folder = args[1];
    	String ouputWordFile = args[2]; // docx
    	
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

        //pageSize.setOrient(STPageOrientation.LANDSCAPE);
        pageSize.setOrient(STPageOrientation.PORTRAIT);
        
        pageSize.setH(BigInteger.valueOf(15840));
        pageSize.setW(BigInteger.valueOf(12240));

        
        XWPFParagraph data2 = doc.createParagraph();
        XWPFRun run_data2 = data2.createRun();
        
        //String name = "sp_A0JPJ7_OLA1_RAT_IGIVGLPNVGK_tmt1.93127.1.2.out";
        //String name = geneName.replaceAll("\\|", "_") + "_" + peptideName + "_" + outFileName;
        //String path = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\FinalOutput\\";
        

        XWPFParagraph p2 = doc.createParagraph();
        p2.setAlignment(ParagraphAlignment.CENTER);
        /*p2.setBorderBottom(Borders.DOUBLE);
        p2.setBorderTop(Borders.DOUBLE);

        p2.setBorderRight(Borders.DOUBLE);
        p2.setBorderLeft(Borders.DOUBLE);
        p2.setBorderBetween(Borders.SINGLE);
		*/
        p2.setVerticalAlignment(TextAlignment.TOP);

        XWPFRun r2 = p2.createRun();
        
        r2.setFontSize(30);
        r2.addBreak();
        r2.addBreak();
        r2.addBreak();
        r2.addBreak();
        r2.setText("KINASE REPORT");
        r2.addBreak();
        r2.addBreak();
        r2.addBreak();
        r2.addBreak();
        HashMap map = new HashMap();
        
        //String path_img = "T:\\Computational Biology\\Timothy Shaw\\PROTEOMICS\\DisplayIonReport_20150823\\output_img\\";
        //String path_csv = "T:\\Computational Biology\\Timothy Shaw\\PROTEOMICS\\DisplayIonReport_20150823\\output_csv\\";
        //String orderFileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\GenerateReport\\order2.txt";
        //String orderFileName = "T:\\Computational Biology\\Timothy Shaw\\PROTEOMICS\\DisplayIonReport_20150823\\one_hit_wonder.txt";
        
		FileInputStream fstream2 = new FileInputStream(kinase_summary_file);
		DataInputStream din2 = new DataInputStream(fstream2);
		BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
		String header = in2.readLine();
		while (in2.ready()) {
			String str2 = in2.readLine();
			String[] split = str2.split("\t");
			String kinaseName = split[0];
			String phosphoEvidenceActivated = split[1];
			boolean write = false;
			if (phosphoEvidenceActivated.equals("Found_and_ActivityUpReg")) {
				phosphoEvidenceActivated = "Found phosphosite that ACTIVATES kinase; its supporting peptide is UP-REGULATED";
				write = true;
			} else if (phosphoEvidenceActivated.equals("Found_and_ActivityDnReg")) {
				phosphoEvidenceActivated = "Found phosphosite that ACTIVATES kinase; its supporting peptide is DOWN-REGULATED";
				write = true;
			} else if (phosphoEvidenceActivated.equals("Found_and_NoChangeInActivity")) {
				phosphoEvidenceActivated = "Found phosphosite that ACTIVATES kinase; its supporting peptide is not differentially expressed";
				write = true;
			}
			String PhosphoEvidenceInhibited = split[2];
			if (PhosphoEvidenceInhibited.equals("")) {
				PhosphoEvidenceInhibited = "Found phosphosite that INHIBITS kinase; its supporting peptide is UP-REGULATED";
				write = true;
			} else if (PhosphoEvidenceInhibited.equals("")) {
				PhosphoEvidenceInhibited = "Found phosphosite that INHIBITS kinase; its supporting peptide is DOWN-REGULATED";
				write = true;
			} else if (PhosphoEvidenceInhibited.equals("")) {
				PhosphoEvidenceInhibited = "Found phosphosite that INHIBITS kinase; its supporting peptide is not differentially expressed";
				write = true;
			}
			String SubstrateEvidenceActivated = split[3];
			if (SubstrateEvidenceActivated.equals("-1.0")) {
				SubstrateEvidenceActivated = "NA";
			} else {
				
				double pvalue = new Double(SubstrateEvidenceActivated);
				if (pvalue > 1) {
					pvalue = 1;
				}
				if (pvalue < 0.05) {
					write = true;
					SubstrateEvidenceActivated = "Substrate enrichment of ACTIVATED kinase corrected pval: " + pvalue;
				} else {
					SubstrateEvidenceActivated = "NA";
				}
				
			}
			String SubstrateEvidenceInhibited = split[4];
			if (SubstrateEvidenceInhibited.equals("-1.0")) {
				SubstrateEvidenceInhibited = "NA";
			} else {
				
				double pvalue = new Double(SubstrateEvidenceInhibited);
				if (pvalue > 1) {
					pvalue = 1;
				}
				if (pvalue < 0.05) {
					write = true;
					SubstrateEvidenceInhibited = "Substrate enrichment of INHIBITED kinase corrected pval: " + pvalue;
				} else {
					SubstrateEvidenceInhibited = "NA";
				}
				
			}
			
	        String imgFile = heatmap_folder + "/" + kinaseName + ".png";
	        File f = new File(imgFile);
	        if (f.exists()) {
	        	write = true;
	        }
	        if (write) {
		        XWPFParagraph data = doc.createParagraph();
		        XWPFRun run_data = data.createRun();
		        
		        //String name = "sp_A0JPJ7_OLA1_RAT_IGIVGLPNVGK_tmt1.93127.1.2.out";
		        //String name = geneName.replaceAll("\\|", "_") + "_" + peptideName + "_" + outFileName;
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
		        r1.setFontSize(20);
		        r1.addBreak(BreakType.PAGE);
		        r1.setBold(true);
		        		    
		        r1.setText("+++ " + "Kinase Name: " + kinaseName + " +++");
		        //r1.addBreak();		        		        
		        
		        XWPFParagraph p3 = doc.createParagraph();
		        p3.setAlignment(ParagraphAlignment.LEFT);
		        p3.setBorderBottom(Borders.SINGLE);
		        p3.setBorderTop(Borders.SINGLE);
		
		        p3.setBorderRight(Borders.SINGLE);
		        p3.setBorderLeft(Borders.SINGLE);
		        p3.setBorderBetween(Borders.SINGLE);
		
		        p3.setVerticalAlignment(TextAlignment.TOP);
		        XWPFRun r4 = p3.createRun();
		        
		        r4.setFontSize(14);
		        r4.setBold(true);
		        r4.setText("Kinase Activity Information:");
		        r4.addBreak();
		        XWPFRun r3 = p3.createRun();
		        r3.setFontSize(11);
		        if ((SubstrateEvidenceActivated.equals("NA") && SubstrateEvidenceInhibited.equals("NA") && phosphoEvidenceActivated.equals("NA") && PhosphoEvidenceInhibited.equals("NA"))) {
		        		r3.setText("No annotated motif available for this kinase");
		        		r3.addBreak();
			        } else {
			        if (!phosphoEvidenceActivated.equals("NA")) {
			        	r3.setText(phosphoEvidenceActivated);
			        	r3.addBreak();
			        }
			        if (!PhosphoEvidenceInhibited.equals("NA")) {
				        r3.setText(PhosphoEvidenceInhibited);
				        r3.addBreak();
			        }
			        if (!SubstrateEvidenceActivated.equals("NA")) {
			        	//
				        
				        r3.setText(SubstrateEvidenceActivated);
				        r3.addBreak();
			        }
			        if (!SubstrateEvidenceInhibited.equals("NA")) {
				        r3.setText(SubstrateEvidenceInhibited);
				        r3.addBreak();
			        }
			        
		        }
		        
		        //r1.setFontFamily("Courier");
		        //r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
		        //r1.setTextPosition(100);
		
		        
	
		        //create first row
		        //table.setColBandSize(0);
		        
		        //table.getCTTbl().getTblPr().unsetTblBorders();
		        /*
		        XWPFTableRow tableRowOne = table.getRow(0);
		        
			    tableRowOne.addNewTableCell().setText("");        
		        
		        XWPFParagraph text = table.getRow(0).getCell(0).getParagraphs().get(0);//doc.createParagraph();
		        XWPFRun text_run = text.createRun();
		        
			    //text_run.addBreak();
			    text_run.setFontSize(8);
			    if (new File(path_csv + "\\" + name + ".csv").exists()) {
				FileInputStream fstream = new FileInputStream(path_csv + "\\" + name + ".csv");
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
						
						i++;			
					}
					text_run.addBreak();
				    
					//text_run.addCarriageReturn();
				}
				in.close();
			
				text_run.setText("* Ion Match");
				*/
		        //text_run.addPicture(text_is, XWPFDocument.PICTURE_TYPE_GIF, text_imgFile, Units.toEMU(400), Units.toEMU(200)); // 200x200 pixels        
		        
		        //System.out.println(table.getRow(0).getCell(1).getParagraphs().size());
		        imgFile = heatmap_folder + "/" + kinaseName + ".png";
		        f = new File(imgFile);
		        if (f.exists()) {
	
		        	XWPFParagraph p5 = doc.createParagraph();
			        p5.setAlignment(ParagraphAlignment.LEFT);
			
			        p5.setVerticalAlignment(TextAlignment.TOP);
			        XWPFRun r5 = p5.createRun();
			        r5.setFontSize(14);
			        r5.addBreak();
			        r5.setText("HEATMAP of known substrate (phosphosite) of the kinase:");			        
			        XWPFTable table = doc.createTable();
			        table.setCellMargins(0, 0, 0, 0);
			        XWPFParagraph title = table.getRow(0).getCell(0).getParagraphs().get(0);//doc.createParagraph();            
			        XWPFRun run = title.createRun();
			        
			        //run.setText("Fig.1 A Natural Scene");
			        run.setBold(true);
			        title.setAlignment(ParagraphAlignment.CENTER);
			        
		        
		        
				    FileInputStream is = new FileInputStream(imgFile);
				    run.addBreak();
				    run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(400), Units.toEMU(350)); // 200x200 pixels
				    run.setText("		                                                                                                       ");
				    run.addBreak();
				    is.close();
		        }	    
			    /*} // file exists
			    else {
			    	System.out.println("Missing: " + name);
			    }*/
	        }
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
        
        //ouputWordFile
        //FileOutputStream out = new FileOutputStream("T:\\Computational Biology\\Timothy Shaw\\PROTEOMICS\\DisplayIonReport_20150823\\simple.docx");
		FileOutputStream out = new FileOutputStream(ouputWordFile);
        doc.write(out);
        out.close();

    }
}

