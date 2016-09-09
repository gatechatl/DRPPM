package PhosphoTools.Heatmap;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
/** 
 * Generate the complete matrix
 * @author tshaw
 *
 */
public class JUMPqDataMatrixGenerationAll {

	public static int[] header_expr_info(String header, String[] tags) {
		int[] index = new int[tags.length];
		String[] split = header.split("\t");
		for (int i = 0; i < split.length; i++) {
			for (int j = 0; j < tags.length; j++) {
				//System.out.println(split[i].split(" ")[0] + "\t" + tags[j] + "\t" + header);
				/*if (split[i].contains(tags[j])) {
					
				}*/
				if (split[i].split(" ")[0].equals(tags[j])) {
					
					index[j] = i;
				}
			}
		}
		return index;
	}
	public static String parameter_info() {
		return "[inputFile] [accession_index] [index_str] [alias_str] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();			
			String inputFile = args[0];
			int accession_index = new Integer(args[1]);
			String index_str = args[2];
			String alias_str = args[3];
			String outputFile = args[4];
			
			
			
			String gene = "";
			HashMap expression = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			if (header.split("\t").length < 4) {
				header = in.readLine();
			}
			String[] split_header = header.split("\t");
			String header_final = "";;
			String[] sample_strs = index_str.split(",");
			int[] samples = header_expr_info(header, sample_strs);
			for (int i: samples) {
				if (header_final.equals("")) {
					header_final = split_header[i];
				} else {
					header_final += "\t" + split_header[i];
				}
			}			
			if (alias_str.split(",").length == samples.length) {
				header_final = alias_str.replaceAll(",", "\t");
			}
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName_Accession_MODSITE\t" + header_final + "\n");
			
			HashMap writeOnce = new HashMap();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gene = split[accession_index].split("\\|")[1] + "\t" + split[accession_index].split(":")[1];				
				String result = "";;
				String geneName = split[4];
				
				/*for (int i = numeric_start; i <= numeric_end; i++) {
					if (i == numeric_start) {
						result = split[i];
					} else {
						result += "\t" + split[i];
					}
				}*/
				for (int i: samples) {
					if (result.equals("")) {
						result = split[i];
					} else {
						result += "\t" + split[i];
					}
				}
				
				String name = geneName + "_" + gene.replaceAll("\t", "_"); 
				if (!writeOnce.containsKey(name)) {
					out.write(name + "\t" + result + "\n");
				}
				writeOnce.put(name, name);
				expression.put(gene, result);
			}
			in.close();			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


