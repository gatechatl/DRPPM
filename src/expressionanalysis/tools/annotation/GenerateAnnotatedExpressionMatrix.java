package expressionanalysis.tools.annotation;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GenerateAnnotatedExpressionMatrix {

	public static void main(String[] args) {
		
		try {
			
			String gtfFile = "/home/gatechatl/ExpressionAnalysisTools/PediatricCancer/CombinedPediatricGTExCases/gencode.v31.primary_assembly.annotation.gtf";
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			
			HashMap map = new HashMap();
			HashMap map_tissue = new HashMap();
			String inputFile = "/home/gatechatl/ExpressionAnalysisTools/PediatricCancer/CombinedPediatricGTExCases/sample_metainfo.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
				map_tissue.put(split[0], split[2]);
			}
			in.close();
			
			String gene_list = "/home/gatechatl/ExpressionAnalysisTools/PediatricCancer/CombinedPediatricGTExCases/PediatricCancer_brainxenograft_GTEx_geneList.txt";
			FileWriter fwriter2 = new FileWriter(gene_list);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			String expressionFile = "/home/gatechatl/ExpressionAnalysisTools/PediatricCancer/CombinedPediatricGTExCases/PediatricCancer_brainxenograft_GTEx.txt";
			fstream = new FileInputStream(expressionFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				
				String biotype = (String)gtf.geneName2biotype.get(split[0]);
				if (biotype.equals("protein_coding")) {
					out2.write(split[0] + "\n");
					String output_gene_matrix = "/home/gatechatl/ExpressionAnalysisTools/PediatricCancer/CombinedPediatricGTExCases/GeneMatrix/" + split[0] + ".txt";
					FileWriter fwriter = new FileWriter(output_gene_matrix);
					BufferedWriter out = new BufferedWriter(fwriter);
					out.write("SampleName\tTPM\tType\tTissueType\tSource\n");
					for (int i = 1; i < split.length; i++) {
						if (map.containsKey(header[i].replaceAll("_expected_count", ""))) {
							String type = (String)map.get(header[i]);
							String tissue_type = (String)map_tissue.get(header[i]);
							out.write(header[i] + "\t" + split[i] + "\t" + type + "\t" + tissue_type + "\t" + type.split("_")[0] + "\n");
							//if (type.substring(0, 2).equals("SJ")) {
							//	if (type.contains("_Diagnosis") || type.contains("_Xenograft")) {
							//		out.write(header[i] + "\t" + split[i] + "\t" + type + "\tPed" + "\n");
							//	}
							//} else {
							//	out.write(header[i] + "\t" + split[i] + "\t" + type + "\tGTEx" + "\n");
							//}
						}
					}
					out.close();
				}
			}
			in.close();			
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
