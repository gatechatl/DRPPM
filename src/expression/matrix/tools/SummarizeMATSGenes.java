package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import idconversion.tools.GTFFile;

public class SummarizeMATSGenes {

	public static String description() {
		return "Summarize MATS Genes";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [gtfFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String summaryFile = args[0];
			//String gtfFile = args[1];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			//GTFFile gtf = new GTFFile();
			//gtf.initialize(gtfFile);
			System.out.println("Read File");
			HashMap genes = new HashMap();
			FileInputStream fstream = new FileInputStream(summaryFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains("Total Genes:")) {
					break;
				}
				String[] split = str.split("\t");
				String[] vals = split[split.length - 1].replaceAll("\"",  "").split(",");
				for (String val: vals) {
					//if (gtf.geneid2geneName.containsKey(val)) {
						//String geneName = (String)gtf.geneid2geneName.get(val);
						genes.put(val, val);
					//}
				}
			}
			in.close();
			
			out.write("Sample");
			Iterator itr = genes.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out.write("\t" + gene);				
			}
			out.write("\n");
			fstream = new FileInputStream(summaryFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains("Total Genes:")) {
					break;
				}
				String[] split = str.split("\t");
				String[] vals = split[split.length - 1].replaceAll("\"",  "").split(",");
				out.write(split[0]);
				itr = genes.keySet().iterator();
				while (itr.hasNext()) {
					String gene = (String)itr.next();
					boolean flag = false;
					for (String val: vals) {
						//if (gtf.geneid2geneName.containsKey(val)) {
						//	String geneName = (String)gtf.geneid2geneName.get(val);
						if (gene.equals(val)) {
							flag = true;
						}
						
					}
					out.write("\t" + flag);
				}
				
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
}
