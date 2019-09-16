package proteomics.phospho.tools.peptide.coverage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * Based on the scan count provide an estimated quantification for each peptide
 * Label each peptide as shared or unique
 * @author tshaw
 *
 */
public class PeptideCategoriesSharedOrUniqIDmod {

	public static void execute(String[] args) {
		
		try {
			
			HashMap shared = new HashMap();
			HashMap mouse = new HashMap();
			HashMap human = new HashMap();
			
			HashMap peptide_mouse_count = new HashMap();
			HashMap peptide_human_count = new HashMap();
			HashMap peptide_shared = new HashMap();
			
			String outputFile_shared = args[2];
			FileWriter fwriter_shared = new FileWriter(outputFile_shared);
			BufferedWriter out_shared = new BufferedWriter(fwriter_shared);
			
			String outputFile_mouse = args[3];
			FileWriter fwriter_mouse = new FileWriter(outputFile_mouse);
			BufferedWriter out_mouse = new BufferedWriter(fwriter_mouse);
			
			String outputFile_human = args[4];
			FileWriter fwriter_human = new FileWriter(outputFile_human);
			BufferedWriter out_human = new BufferedWriter(fwriter_human);			
			
			
			String vennDiagramScript = args[5];
			String outputPNGFile = args[6];
			String[] groups = args[1].split(":");
			String [] group1 = groups[0].split(",");
			String [] group2 = groups[1].split(",");
			
			int[] group1_samples = new int[group1.length];
			int[] group2_samples = new int[group2.length];
			
			double[] group1_vals = new double[group1.length];
			double[] group2_vals = new double[group2.length];
			
			for (int i = 0; i < group1.length; i++) {
				group1_samples[i] = new Integer(group1[i]);
			}
			for (int i = 0; i < group2.length; i++) {
				group2_samples[i] = new Integer(group2[i]);
			}
			
			HashMap shared_peptides_prev = new HashMap();
			String sharedFile = args[7];
			FileInputStream fstream = new FileInputStream(sharedFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				shared_peptides_prev.put(split[0], split[0]);
			}
			in.close();
			
			String inputFile = args[0];
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = split[0];
				String pepName = split[2];
				if (pepName.contains("MOUSE")) {
					if (peptide_mouse_count.containsKey(peptide)) {
						int count = (Integer)peptide_mouse_count.get(peptide);
					} else {
						peptide_mouse_count.put(peptide, 1);
					}
				} else {
					if (peptide_human_count.containsKey(peptide)) {
						int count = (Integer)peptide_human_count.get(peptide);
					} else {
						peptide_human_count.put(peptide, 1);
					}
				}
			}
			in.close();
			
			Iterator itr = peptide_mouse_count.keySet().iterator();
			while (itr.hasNext()) {
				String peptide = (String)itr.next();
				if (peptide_human_count.containsKey(peptide)) {
					peptide_shared.put(peptide, 1);
				}
				if (shared_peptides_prev.containsKey(peptide)) {
					peptide_shared.put(peptide, 1);
				}
			}
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = split[0];
				String pepName = split[2];
				String num_scans = split[5];
				String xCorr = split[11];
				//int start = new Integer(split[13].replaceAll("AA", "").split("to")[0]);
				//int end = new Integer(split[13].replaceAll("AA", "").split("to")[1]);
				String sample_vals = "";
				int n = 0;
				for (int i: group1_samples) {
					group1_vals[n] = new Double(split[14 + i - 1]);
					sample_vals += "\t" + group1_vals[n];
					n++;
				}
				n = 0;
				for (int i: group2_samples) {
					group2_vals[n] = new Double(split[14 + i - 1]);
					sample_vals += "\t" + group2_vals[n];
					n++;
				}
				
				if (peptide_shared.containsKey(peptide)) {
					String type = "";
					if (pepName.contains("MOUSE")) {
						type = "MOUSE";
					} else {
						type = "HUMAN";
					}
					if (!shared.containsKey(peptide + "\t" + type)) {
						shared.put(peptide + "\t" + type, peptide + "\t" + type);
						out_shared.write(str + "\n");
						//out_shared.write(peptide + "\t" + pepName + "\t" + num_scans + "\t" + xCorr + sample_vals + "\n");
					}
					
				} else {
					if (pepName.contains("MOUSE")) {
						if (!mouse.containsKey(peptide)) {
							mouse.put(peptide, peptide);
							out_mouse.write(str + "\n");
							//out_mouse.write(peptide + "\t" + pepName + "\t" + num_scans + "\t" + xCorr + sample_vals + "\n");
							//out_mouse.write(peptide + "\t" + pepName + "\t" + num_scans + "\t" + xCorr + "\t" + start + "\t" + end + sample_vals + "\n");
						}
					} else {
						if (!human.containsKey(peptide)) {
							human.put(peptide, peptide);
							out_human.write(str + "\n");
							//out_human.write(peptide + "\t" + pepName + "\t" + num_scans + "\t" + xCorr + sample_vals + "\n");
							//out_human.write(peptide + "\t" + pepName + "\t" + num_scans + "\t" + xCorr + "\t" + start + "\t" + end + sample_vals + "\n");
						}
					}
				}
			}
			in.close();
			
			out_shared.close();
			out_mouse.close();
			out_human.close();
			
			
			FileWriter fwriter = new FileWriter(vennDiagramScript);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(generateVennDiagramScript(outputPNGFile, human, shared, mouse));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateRList(HashMap uniq, HashMap shared) {
		String stuff = "";
		boolean first = true;
		Iterator itr = uniq.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			if (first) {
				stuff += "'" + key + "'";
			} else {
				stuff += ",'" + key + "'";
			}
			first = false;
		}
		itr = shared.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			if (first) {
				stuff += "'" + key + "'";
			} else {
				stuff += ",'" + key + "'";
			}
			first = false;
		}
		return stuff;
	}
	public static String generateVennDiagramScript(String outputPNG, HashMap human, HashMap shared, HashMap mouse) {
		String script = "";
		script += "library(VennDiagram);\n";
		script += "venn.diagram(\n";
		script += "x = list(\n";
		script += "Human = c(" + generateRList(human, shared) + "),\n";
		script += "Mouse = c(" + generateRList(mouse, shared) + ")\n";
		script += "),\n";
		script += "filename = \"" + outputPNG + "\",\n";
		script += "imagetype = \"png\",\n";
		script += "lwd = 4,\n";
		script += "fill = c(\"red\", \"blue\"),\n";
		script += "alpha = 0.75,\n";
		script += "label.col = \"white\",\n";
		script += "cex = 4,\n";
		script += "fontfamily = \"serif\",\n";
		script += "fontface = \"bold\",\n";
		script += "cat.col = c(\"cornflowerblue\", \"darkorchid1\"),\n";
		script += "cat.cex = 3,\n";
		script += "cat.fontfamily = \"serif\",\n";
		script += "cat.fontface = \"bold\",\n";
		script += "cat.dist = c(0.03, 0.03),\n";
		script += "cat.pos = c(-20, 14)\n";
		script += ");\n";
		return script;
	}
}
