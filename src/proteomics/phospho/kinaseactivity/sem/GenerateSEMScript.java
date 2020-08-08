package proteomics.phospho.kinaseactivity.sem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate the SEM model script and data file. Requires lavaan library.
 * Script is based on http://lavaan.ugent.be/tutorial/sem.html
 * @author tshaw
 */
public class GenerateSEMScript {

	public static String description() {
		return "Generate the SEM model script and data file. Requires lavaan library.\nScript is based on http://lavaan.ugent.be/tutorial/sem.html";
	}
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String parameter_info() {
		return "[phosphosite_matrix] [query_kinase] [repeat data n times: set as 1] [outputRscriptFile] [outputDataFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String phosphosite_matrix = args[0];
			String query_kinase = args[1];
			int repeat_data_n = new Integer(args[2]);
			String outputRscriptFile = args[3];
			String outputDataFile = args[4];
			
			FileWriter fwriter_script = new FileWriter(outputRscriptFile);
			BufferedWriter out_script = new BufferedWriter(fwriter_script);			

			FileWriter fwriter_data = new FileWriter(outputDataFile);
			BufferedWriter out_data = new BufferedWriter(fwriter_data);
			int index = 1;
			HashMap substrate2xval = new HashMap();
			HashMap kinase2substrate = new HashMap();
			HashMap substrate_measures = new HashMap();
			FileInputStream fstream = new FileInputStream(phosphosite_matrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinases = split[1];
				
				String data = split[2];
				for (int i = 3; i < split.length; i++) {
					data += "\t" + split[i];
				}
				substrate_measures.put(split[0], data);
				substrate2xval.put(split[0], "x" + index);
				index++;
				for (String kinase: kinases.split(",")) {
					if (kinase2substrate.containsKey(kinase)) {
						LinkedList list = (LinkedList)kinase2substrate.get(kinase);			
						list.add(split[0]);
						kinase2substrate.put(kinase, list);
					} else {
						LinkedList list = new LinkedList();						
						list.add(split[0]);
						kinase2substrate.put(kinase, list);
					}
				}
			}
			in.close();
			//out_data.write("Substrate");
			
			// write data file
			String line = "";
			Iterator itr = substrate_measures.keySet().iterator();
			while (itr.hasNext()) {
				String substrate = (String)itr.next();
				if (line.equals("")) {
					line = (String)substrate2xval.get(substrate);
				} else {
					line += "\t" + (String)substrate2xval.get(substrate);
				}
				
			}
			out_data.write(line + "\n");
			for (int j = 1; j <= repeat_data_n; j++) { // repeat 100 times
				for (int i = 2; i < split_header.length; i++) {
					itr = substrate_measures.keySet().iterator();
					line = "";
					while (itr.hasNext()) {
						String substrate = (String)itr.next();
						String values = (String)substrate_measures.get(substrate);
						String[] split_values = values.split("\t");
						if (line.equals("")) {
							line = split_values[i - 2];
						} else {
							line += "\t" + split_values[i - 2];
						}					
					}
					out_data.write(line + "\n");
				}
			} // repeat 100 times
			out_data.close();
			
			out_script.write("library(lavaan);\n");
			out_script.write("model <- '\n");
			out_script.write("  # measurement model\n");
			itr = kinase2substrate.keySet().iterator();
			while (itr.hasNext()) {
				String kinase = (String)itr.next();
				if (kinase.equals(query_kinase) || query_kinase.toUpperCase().equals("ALL")) {
					String relationship = "    " + kinase + " =~ ";
					String substrate_str = "";
					LinkedList substrate_list = (LinkedList)kinase2substrate.get(kinase);
					Iterator itr2 = substrate_list.iterator();
					while (itr2.hasNext()) {
						String substrate = (String)itr2.next();
						if (substrate_str.equals("")) {
							substrate_str = (String)substrate2xval.get(substrate);
						} else {
							substrate_str += " + " + (String)substrate2xval.get(substrate);
						}
					}
					out_script.write(relationship + substrate_str + "\n");
				}
			}
			out_script.write("'\n");
			out_script.write("kinase_data = read.table(\"" + outputDataFile + "\", sep=\"\\t\", header=T)\n");
			out_script.write("fit <- sem(model, data=kinase_data)\n");
			out_script.write("write(summary(fit), file=\"summary_fit.txt\", sep=\"\\t\")\n");
			out_script.write("write(lavPredict(fit), file=\"kinase_activity.txt\", sep=\"\\t\")\n");			
			out_script.close();
			
			//fit <- sem(model, data=PoliticalDemocracy)
			
			//lavPredict
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
