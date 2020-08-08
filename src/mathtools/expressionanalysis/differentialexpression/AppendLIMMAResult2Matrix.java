package mathtools.expressionanalysis.differentialexpression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendLIMMAResult2Matrix {

	public static String type() {
		return "DIFFERENTIALEXPRESSION";
	}
	public static String description() {
		return "Append LIMMA result to the matrix";
	}
	public static String parameter_info() {
		return "[matrixFile] [limmaFiles: file1,file2] [tags: tag1,tag2] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String matrixFile = args[0];
			String[] limmaFiles = args[1].split(",");
			String[] tags = args[2].split(",");
			String outputFile = args[3];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap[] limma = new HashMap[limmaFiles.length];
			int i = 0;
			for (String limmaFile: limmaFiles) {
				limma[i] = new HashMap();
				FileInputStream fstream = new FileInputStream(limmaFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String geneName = split[0].replaceAll("\"",  "");
					String logFC = split[1];
					String pvalue = split[4];
					String fdr = split[5];
					limma[i].put(geneName, logFC + "\t" + pvalue + "\t" + fdr);
				}
				in.close();
				i++;
			}
			HashMap map = new HashMap();
			String header = "";
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header);
			for (String tag: tags) {
				out.write("\t" + tag + "_logFC" + "\t" + tag + "_pval" + "\t" + tag + "_fdr");
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll("\"", "");
				//if (limma[0].containsKey(split[0])) {
					out.write(str);
					for (i = 0; i < limma.length; i++) {
						if (limma[i].containsKey(split[0])) {
							String line = (String)limma[i].get(split[0]);
							out.write("\t" + line);							
						} else {
							String line = "NA\tNA\tNA";
							out.write("\t" + line);
						}
					}
					out.write("\n");
					//map.put(split[0], str);
				//}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
