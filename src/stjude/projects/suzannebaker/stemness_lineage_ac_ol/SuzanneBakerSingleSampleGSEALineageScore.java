package stjude.projects.suzannebaker.stemness_lineage_ac_ol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import statistics.general.MathTools;

/**
 * Modified lineage and stemness score based on Tirosh et al. Nature. 2016 
 * @author tshaw
 *
 */
public class SuzanneBakerSingleSampleGSEALineageScore {

	public static String description() {
		return "Calculate Lineage Score";
	}
	public static String type() {
		return "SuzanneBaker";
	}
	public static String parameter_info() {
		return "[ssGSEA_matrix_file] [stemness_index] [oligo_index] [astro_index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			Random rand = new Random();
			String ssGSEA_matrix_file = args[0];
			int stemness_index = new Integer(args[1]);
			int oligo_index = new Integer(args[2]);
			int astro_index = new Integer(args[3]);
			String outputFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tStemness_Score\tLineage_Score\n");
			HashMap control_map = new HashMap();
			HashMap gfap_map = new HashMap();
			HashMap nes_map = new HashMap();
			HashMap cluster_map = new HashMap();
			
			LinkedList name = new LinkedList();

			LinkedList stemness = new LinkedList();
			LinkedList astro = new LinkedList();
			LinkedList oligo = new LinkedList();
			
			FileInputStream fstream = new FileInputStream(ssGSEA_matrix_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split = header.split("\t");
			
			//System.out.println("stemness header: " + header_split[stemness_index]);
			//System.out.println("oligo header: " + header_split[oligo_index]);
			//System.out.println("astro header: " + header_split[astro_index]);			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double stemness_score = new Double(split[stemness_index]);
				double oligo_score = new Double(split[oligo_index]);
				double astro_score = new Double(split[astro_index]);
				//double downsample = Math.random();
				//if (downsample < 0.2) {
					
						
				name.add(split[0]);
				stemness.add(stemness_score);
				oligo.add(oligo_score);
				astro.add(astro_score);
			
					//}
				
			}
			in.close();			
			double[] stemness_array = MathTools.convertListDouble2Double(stemness);
			double[] oligo_array = MathTools.convertListDouble2Double(oligo);
			double[] astro_array = MathTools.convertListDouble2Double(astro);
			
			double stemness_avg = MathTools.mean(stemness_array);
			double stemness_stdev = MathTools.standardDeviation(stemness_array);
			
			double oligo_avg = MathTools.mean(oligo_array);
			double oligo_stdev = MathTools.standardDeviation(oligo_array);
			
			double astro_avg = MathTools.mean(astro_array);
			double astro_stdev = MathTools.standardDeviation(astro_array);
			LinkedList stemness_score = new LinkedList();						
			LinkedList lineage_score = new LinkedList();
			
			double buffer = 20;
			for (int i = 0; i < stemness_array.length; i++) {
				double z_score_stemness = (stemness_array[i] - stemness_avg) / stemness_stdev + buffer;
				double z_score_astro = (astro_array[i] - astro_avg) / astro_stdev + buffer;
				double z_score_oligo = (oligo_array[i] - oligo_avg) / oligo_stdev + buffer;
				
				double z_max_lineage = -999;
				if (z_max_lineage < z_score_astro) {
					z_max_lineage = z_score_astro;
				}
				if (z_max_lineage < z_score_oligo) {
					z_max_lineage = z_score_oligo;
				}
				
				z_score_stemness = z_score_stemness - z_max_lineage;
				if (z_score_astro > z_score_oligo) {
					z_max_lineage = (z_max_lineage - buffer) * -1 + buffer;
				}
				z_max_lineage = z_max_lineage - buffer;
				String name_str = (String)name.get(i);				
				
				if (z_score_stemness > 0) {
					double sign = (z_max_lineage / Math.abs(z_max_lineage));					
					z_max_lineage = 1.0 * rand.nextGaussian() * sign / 2.5;
				}
				out.write(name_str + "\t" + new Double(Math.round(z_score_stemness * 100)) / 100 + "\t" + new Double(Math.round(z_max_lineage * 100)) / 100 +  "\n");
				
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
