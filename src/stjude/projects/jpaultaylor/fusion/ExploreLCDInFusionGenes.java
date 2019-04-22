package stjude.projects.jpaultaylor.fusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ExploreLCDInFusionGenes {

	public static void main(String[] args) {
		
		try {
			String fusion_gene_file = "Z:\\ResearchHome\\ClusterHome\\tshaw\\PROTEOMICS\\PaulTaylorFusionProteins\\fusion_list.txt";
			
			String lcd_length_file = "Z:\\ResearchHome\\ClusterHome\\tshaw\\PROTEOMICS\\PaulTaylorFusionProteins\\GeneLCDLength.txt";
			
			String outputFile = "Z:\\ResearchHome\\ClusterHome\\tshaw\\PROTEOMICS\\PaulTaylorFusionProteins\\fusion_list_info.txt";			

			String outputFile2 = "Z:\\ResearchHome\\ClusterHome\\tshaw\\PROTEOMICS\\PaulTaylorFusionProteins\\GeneLCDLength_Fusion.txt";			

        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
        	FileWriter fwriter2 = new FileWriter(outputFile2);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            
            HashMap left_gene = new HashMap();
            HashMap right_gene = new HashMap();
			HashMap lcd_length_map = new HashMap();
			FileInputStream fstream = new FileInputStream(lcd_length_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				lcd_length_map.put(split[1], str);
			}
			in.close();			
			
			fstream = new FileInputStream(fusion_gene_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene1 = split[0].trim();
				String gene2 = split[1].trim();
				if (gene1.equals("MLL")) {
					gene1 = "KMT2A";
				}
				if (gene2.equals("MLL")) {
					gene2 = "KMT2A";
				}
				left_gene.put(gene1, gene1);
				right_gene.put(gene2, gene2);
				
				String gene1_result = "NA\tNA\tNA\tNA\tNA";
				String gene2_result = "NA\tNA\tNA\tNA\tNA";
				if (lcd_length_map.containsKey(gene1)) {
					String lcd_length_str = (String)lcd_length_map.get(gene1);
					gene1_result = lcd_length_str;
				}
				if (lcd_length_map.containsKey(gene2)) {
					String lcd_length_str = (String)lcd_length_map.get(gene2);
					gene2_result = lcd_length_str;
				}
				out.write(str + "\t" + gene1_result + "\t" + gene2_result + "\n");
			}
			in.close();			
			out.close();
			
			fstream = new FileInputStream(lcd_length_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String tag = split[4];
				if (left_gene.containsKey(split[1])) {
					tag = "UpstreamFusionGene";
				}
				if (right_gene.containsKey(split[1])) {
					tag = "DownstreamFusionGene";
				}
				if (left_gene.containsKey(split[1]) && right_gene.containsKey(split[1])) {
					tag = "BothUpDownFusionGene";
				}
				out2.write(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + tag + "\n");
			}
			in.close();			
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
