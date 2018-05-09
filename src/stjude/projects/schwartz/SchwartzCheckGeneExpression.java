package stjude.projects.schwartz;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class SchwartzCheckGeneExpression {


	public static String description() {
		return "Count gene check";
	}
	public static String type() {
		return "SCHWARTZ";
	}
	public static String parameter_info() {
		return "[10XgeneFile] [10xbarcode] [10xmatrix] [genequery: Dhb or Th]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String gene = args[0];
			String barcode = args[1];
			String matrix = args[2];
			String gene_query = args[3];
			int gene_index = -1;
			int i = 1;
			LinkedList list_gene = new LinkedList();
			FileInputStream fstream = new FileInputStream(gene);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));									
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].toUpperCase().equals(gene_query.toUpperCase())) {
					gene_index = i;
				}
				list_gene.add(str);
				i++;
			}
			in.close();
			
			LinkedList list_barcodes = new LinkedList();
			fstream = new FileInputStream(barcode);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));									
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				list_barcodes.add(str);
				
			}
			in.close();
			
			fstream = new FileInputStream(matrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				int geneIndex = new Integer(split[0]);
				int cellIndex = new Integer(split[1]);
				int count = new Integer(split[2]);
				if (geneIndex == gene_index) {
					if (count > 0) {
						String barcodeStr = (String)list_barcodes.get(cellIndex);
						System.out.println(barcodeStr + "\t" + count);
					}
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
