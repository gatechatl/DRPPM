package functional.pathway.enrichment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import MISC.CommandLine;

/**
 * Calculate BH, bonferroni and FDR based on ORA pvalues
 * @author tshaw
 */
public class OverRepresentationAnalysisFDR {

	public static String dependencies() {
		return "Require R installation";
	}
	public static String type() {
		return "ENRICHMENT";
	}
	public static String description() {
		return "Calculate BH, bonferroni and FDR based on ORA pvalues";
	}
	public static String parameter_info() {
		return "[enrichmentInputFile] [geneListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String enrichmentInputFile = args[0];
			String geneListFile = args[1];
			String outputFile = args[2];
			
			
			int countGneList = countNumGeneList(geneListFile);
			
			
			
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(enrichmentInputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					LinkedList list = (LinkedList)map.get(split[0]);
					list.add(str);
					map.put(split[0], list);
				} else {
					double pvalue = new Double(split[2]);
					LinkedList list = new LinkedList();
					list.add(str);
					map.put(split[0], list);
					//out2.write(pvalue + "\n");
				}
				
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(header + "\t" + "BH" + "\t" + "Bonferroni" + "\t" + "Hochberg\n");
			
			Iterator itrMap = map.keySet().iterator();
			while (itrMap.hasNext()) {
				String mod = (String)itrMap.next();
				LinkedList list = (LinkedList)map.get(mod);
				
				String buffer = UUID.randomUUID().toString();
				String buffer_output = buffer + "_output";
				FileWriter fwriter2 = new FileWriter(buffer);
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				
				Iterator itr_list = list.iterator();
				while (itr_list.hasNext()) {
					String line = (String)itr_list.next();
					String[] split = line.split("\t");
					out2.write(split[2] + "\n");
				}
				for (int i = list.size(); i < countGneList; i++) {
					out2.write(1.0 + "\n");
				}
				out2.close();
				//System.out.println("Total GeneList: " + countGneList);
				//System.out.println("Module Size: " + list.size());
				String script = generateFDRScript(buffer, buffer_output);
				CommandLine.writeFile(buffer + "pvalue.r", script);
				CommandLine.executeCommand("R --vanilla < " + buffer + "pvalue.r");
				
				LinkedList pvals = new LinkedList();
				fstream = new FileInputStream(buffer_output);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					pvals.add(str);
				}
				in.close();
				
				Iterator itr = list.iterator();
				Iterator itr2 = pvals.iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					String pval = (String)itr2.next();
					String[] split = pval.split("\t");
					line += "\t" + split[1] + "\t" + split[2] + "\t" + split[3];
					out.write(line + "\n");
				}
				
				deleteFile(buffer);
				deleteFile(buffer_output);
				deleteFile(buffer + "pvalue.r");
				
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteFile(String inputFile) {
		File f = new File(inputFile);
		if (f.exists()) {
			f.delete();
		}
	}
	public static String generateFDRScript(String inputFile, String outputFile) {
		String script = "";
		script += "pval = read.table(\"" + inputFile + "\",colClasses='numeric');\n";
		script += "pval = as.array(pval[,1])\n";
		script += "BH = p.adjust(pval, method = \"BH\")\n";
		script += "bonferroni = p.adjust(pval, method = \"bonferroni\");\n";
		script += "hochberg = p.adjust(pval, method = \"hochberg\");\n";
		script += "data = cbind(BH, bonferroni, hochberg)\n";
		script += "write.table(data, file = \"" + outputFile + "\", sep = \"\t\");\n";

		return script;
	}
	public static int countNumGeneList(String inputFile) {
		int count = 0;
		
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				String cluster_name = split[0];
				String geneListStr = split[1];
				String[] geneListSplit = geneListStr.split(",");
				String buffer = UUID.randomUUID().toString();
				count++;
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
