package microsoft.document.word.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

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

public class GenerateWordFile {

	/*public static void main(String[] args) {
		
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			wordMLPackage.getMainDocumentPart().addParagraphOfText("Hello Word!");
			wordMLPackage.save(new java.io.File("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\GenerateReport\\test.docx"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
    private static WordprocessingMLPackage  wordMLPackage;
    private static ObjectFactory factory;
 
    /**
     *  First we create the package and the object factory, so we can use them
     *  everywhere in the class. Then we create a table and add borders to it.
     *  Next we create a table row and add a first field with some text.
     *  For the second field, we use the same image file as before and create a
     *  paragraph with an image, that we add to it. Finally we add the row to the
     *  table, and the table to the package, and save the package.
     */
    public static void main (String[] args) throws Exception {
    	
    	WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		wordMLPackage.getMainDocumentPart().addParagraphOfText("Hello Word!");
		wordMLPackage.save(new java.io.File("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\GenerateReport\\test2.docx"));
		
        wordMLPackage = WordprocessingMLPackage.createPackage();
        factory = Context.getWmlObjectFactory();
 
        Tbl table = factory.createTbl();
        addBorders(table);
 
        Tr tr = factory.createTr();
 
        wordMLPackage.getMainDocumentPart().addParagraphOfText("Hello Word!");
        
        P paragraphOfText = wordMLPackage.getMainDocumentPart()
                        .createParagraphOfText("Field 1");
        addTableCell(tr, paragraphOfText);
 
        //File file = new File("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\FinalOutput\\Images\\sp_A0JPJ7_OLA1_RAT_IGIVGLPNVGK_tmt1.93127.1.2.out.gif");
        File file = new File("C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_05272014\\heatmap_top1000_SJMMATTEL_Cortex.png");
        P paragraphWithImage = addInlineImageToParagraph(createInlineImage(file));
        addTableCell(tr, paragraphWithImage);
        
        table.getContent().add(tr);
 
        wordMLPackage.getMainDocumentPart().addObject(table);
        wordMLPackage.save(new java.io.File("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\GenerateReport\\HelloWord8.docx"));
    }
    /**
     *  In this method we create a cell and add the given content to it.
     *  If the given width is greater than 0, we set the width on the cell.
     *  Finally, we add the cell to the row.
     */
    private static void addTableCellWithWidth(Tr row, String content, int width){
        Tc tableCell = factory.createTc();
        tableCell.getContent().add(
            wordMLPackage.getMainDocumentPart().createParagraphOfText(
                content));
 
        if (width > 0) {
            setCellWidth(tableCell, width);
        }
        row.getContent().add(tableCell);
    }
 
    /**
     *  In this method we create a table cell properties object and a table width
     *  object. We set the given width on the width object and then add it to
     *  the properties object. Finally we set the properties on the table cell.
     */
    private static void setCellWidth(Tc tableCell, int width) {
        TcPr tableCellProperties = new TcPr();
        TblWidth tableWidth = new TblWidth();
        tableWidth.setW(BigInteger.valueOf(width));
        tableCellProperties.setTcW(tableWidth);
        tableCell.setTcPr(tableCellProperties);
    }
 
   
    /**
     * Adds a table cell to the given row with the given paragraph as content.
     *
     * @param tr
     * @param paragraph
     */
    private static void addTableCell(Tr tr, P paragraph) {
        Tc tc1 = factory.createTc();
        tc1.getContent().add(paragraph);
        tr.getContent().add(tc1);
    }
 
    /**
     *  Adds the in-line image to a new paragraph and then returns the paragraph.
     *  Thism method has not changed from the previous example.
     *
     * @param inline
     * @return
     */
    private static P addInlineImageToParagraph(Inline inline) {
        // Now add the in-line image to a paragraph
        ObjectFactory factory = new ObjectFactory();
        P paragraph = factory.createP();
        R run = factory.createR();
        paragraph.getContent().add(run);
        Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return paragraph;
    }
 
    /**
     * Creates an in-line image of the given file.
     * As in the previous example, we convert the file to a byte array, and then
     * create an inline image object of it.
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static Inline createInlineImage(File file) throws Exception {
        byte[] bytes = convertImageToByteArray(file);
 
        BinaryPartAbstractImage imagePart =
            BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
 
        int docPrId = 1;
        int cNvPrId = 2;
 
        return imagePart.createImageInline("Filename hint",
                "Alternative text", docPrId, cNvPrId, false);
    }
 
    /**
     * Convert the image from the file into an array of bytes.
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static byte[] convertImageToByteArray(File file)
            throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file );
        long length = file.length();
        // You cannot create an array using a long, it needs to be an int.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "+file.getName());
        }
        is.close();
        return bytes;
    }
 
    /**
     * Adds simple black borders to the table
     *
     * @param table
     */
    private static void addBorders(Tbl table) {
        table.setTblPr(new TblPr());
        CTBorder border = new CTBorder();
        border.setColor("auto");
        border.setSz(new BigInteger("4"));
        border.setSpace(new BigInteger("0"));
        border.setVal(STBorder.SINGLE);
 
        TblBorders borders = new TblBorders();
        borders.setBottom(border);
        borders.setLeft(border);
        borders.setRight(border);
        borders.setTop(border);
        borders.setInsideH(border);
        borders.setInsideV(border);
        table.getTblPr().setTblBorders(borders);
    }
    
}
