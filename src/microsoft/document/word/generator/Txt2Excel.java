package microsoft.document.word.generator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.LogManager;
import org.biojava3.genome.App;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.wml.CTRecipientData.Column;
import org.docx4j.wml.Text;
import org.slf4j.LoggerFactory;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.SheetData;
import org.xlsx4j.sml.Worksheet;
import org.xlsx4j.sml.Cols;
import org.xlsx4j.sml.Col;
import org.xlsx4j.sml.STCellType;

public class Txt2Excel {
	
	public static String parameter_info() {
		return "[input.txt] [excel_output.xlsx]";
	}
	public static void main(String[] args) throws Exception {

		/*System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "OFF");
        final org.slf4j.Logger log = LoggerFactory.getLogger(App.class);
        */
		
		String inputFile = "C:/Users/tshaw/Desktop/PROTEOMICS/WordExample/test.txt";
		String outputfilepath = "C:/Users/tshaw/Desktop/PROTEOMICS/WordExample/example.xlsx";
		//String inputFile = args[0]; //"C:/Users/tshaw/Desktop/PROTEOMICS/WordExample/test.txt";
		//String outputfilepath = args[1]; //"C:/Users/tshaw/Desktop/PROTEOMICS/WordExample/example.xlsx";		
		try {
		SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();
		/*
		WorksheetPart sheet = pkg.createWorksheetPart(new PartName("/xl/worksheets/sheet1.xml"), "Sheet1", 1);
		
		//Set the columns widths
	    List<Cols> lstCols = sheet.getJaxbElement().getCols();
	    //System.out.println("lstCols.size() is " + lstCols.size());
	    
		addContent(inputFile, sheet);
		File f = new File(outputfilepath);
		pkg.save(f);
		//SaveToZipFile saver = new SaveToZipFile(pkg);
		//saver.save(outputfilepath);
				
		System.out.println("\n\n done .. " + outputfilepath);
		*/
		} catch (java.security.AccessControlException e) {
			
		}
	}
	public static void execute(String[] args) throws Exception {

		/*System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

        final org.slf4j.Logger log = LoggerFactory.getLogger(App.class);
        */
		//String inputFile = "C:/Users/tshaw/Desktop/PROTEOMICS/WordExample/test.txt";
		//String outputfilepath = "C:/Users/tshaw/Desktop/PROTEOMICS/WordExample/example.xlsx";
		String inputFile = args[0]; //"C:/Users/tshaw/Desktop/PROTEOMICS/WordExample/test.txt";
		String outputfilepath = args[1]; //"C:/Users/tshaw/Desktop/PROTEOMICS/WordExample/example.xlsx";		
		SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();
		
		WorksheetPart sheet = pkg.createWorksheetPart(new PartName("/xl/worksheets/sheet1.xml"), "Sheet1", 1);
		
		//Set the columns widths
	    List<Cols> lstCols = sheet.getJaxbElement().getCols();
	    //System.out.println("lstCols.size() is " + lstCols.size());
	    
		addContent(inputFile, sheet);
		File f = new File(outputfilepath);
		pkg.save(f);
		//SaveToZipFile saver = new SaveToZipFile(pkg);
		//saver.save(outputfilepath);
				
		System.out.println("\n\n done .. " + outputfilepath);	
	}
	
	private static void addContent(String inputFile, WorksheetPart sheet) {
		try {
			// Minimal content already present
			SheetData sheetData = sheet.getJaxbElement().getSheetData();
					
			// Now add
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				Row row = Context.getsmlObjectFactory().createRow();
				for (int i = 0; i < split.length; i++) {
					
					Cell cell = Context.getsmlObjectFactory().createCell();
					cell.setT(STCellType.STR);
		            cell.setV(split[i]);
		            
					//System.out.println(split[i]);
					// Note: if you are trying to add characters, not a number,
					// the easiest approach is to use inline strings (as opposed to the shared string table).
					// See http://openxmldeveloper.org/blog/b/openxmldeveloper/archive/2011/11/22/screen-cast-write-simpler-spreadsheetml-when-generating-spreadsheets.aspx
					// and http://www.docx4java.org/forums/xlsx-java-f15/cells-with-character-values-t874.html					
					row.getC().add(cell);
					
				}
				sheetData.getRow().add(row);
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}