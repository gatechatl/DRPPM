package expressionanalysis.tools.genename;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * 
 * @author tshaw
 *
 */
public class GeneSymbol2UCSCIDAppend {

	public static String type() {
		return "IDCONVERSION";
	}
	public static String parameter_info() {
		return "[inputFile] [id_conversion_table] [outputFile]";
	}
	public static String description() {
		return "Append UCSCID to the genesymbol based expression matrix";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap geneSymbol2UCSCID = new HashMap();
			String id_conversion_table = args[1];
			FileInputStream fstream = new FileInputStream(id_conversion_table);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				geneSymbol2UCSCID.put(split[4], split[9].split("\\.")[0]);
			}
			in.close();

			String inputFile = args[0];
			String outputFile = args[2];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0] + "\tUCSC_ID");
			for (int i = 1; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String ucsc_id = (String)geneSymbol2UCSCID.get(split[0]);
				out.write(split[0] + "\t" + ucsc_id);
				for (int i = 1; i < split.length; i++) {
					out.write("\t" + split[i]);
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
