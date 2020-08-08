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
public class SuzanneBakerSingleSampleGSEAWishboneLineageScore {

	public static String description() {
		return "Calculate Lineage Score";
	}
	public static String type() {
		return "SuzanneBaker";
	}
	public static String parameter_info() {
		return "[ssGSEA_matrix_file] [stemness_index] [opc_index] [oligo_index] [early_astro_index] [late_astro_index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			Random rand = new Random();
			String ssGSEA_matrix_file = args[0];
			int cycling_index = new Integer(args[1]);
			int esc_index = new Integer(args[2]);
			int opc_index = new Integer(args[3]);
			int oligo_index = new Integer(args[4]);			
			int early_astro_index = new Integer(args[5]);
			int late_astro_index = new Integer(args[6]);
			String outputFile = args[7];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tStemness_Score\tLineage_Score\n");
			
			LinkedList name = new LinkedList();

			LinkedList cycling = new LinkedList();
			LinkedList esc = new LinkedList();
			LinkedList late_astro = new LinkedList();
			LinkedList early_astro = new LinkedList();
			LinkedList opc = new LinkedList();
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
				double cycling_score = new Double(split[cycling_index]);
				double esc_score = new Double(split[esc_index]);
				double oligo_score = new Double(split[oligo_index]);
				double opc_score = new Double(split[opc_index]);
				double early_astro_score = new Double(split[early_astro_index]);
				double late_astro_score = new Double(split[late_astro_index]);
				//double downsample = Math.random();
				//if (downsample < 0.2) {
					
						
				name.add(split[0]);
				cycling.add(cycling_score);
				esc.add(esc_score);
				oligo.add(oligo_score);
				opc.add(opc_score);
				early_astro.add(early_astro_score);
				late_astro.add(late_astro_score);
			
					//}
				
			}
			in.close();			
			double[] esc_array = MathTools.convertListDouble2Double(esc);
			double[] cycling_array = MathTools.convertListDouble2Double(cycling);
			double[] oligo_array = MathTools.convertListDouble2Double(oligo);
			double[] opc_array = MathTools.convertListDouble2Double(opc);
			double[] early_astro_array = MathTools.convertListDouble2Double(early_astro);
			double[] late_astro_array = MathTools.convertListDouble2Double(late_astro);
			
			double esc_avg = MathTools.mean(esc_array);
			double esc_stdev = MathTools.standardDeviation(esc_array);			

			double cycling_avg = MathTools.mean(esc_array);
			double cycling_stdev = MathTools.standardDeviation(cycling_array);
			
			double oligo_avg = MathTools.mean(oligo_array);
			double oligo_stdev = MathTools.standardDeviation(oligo_array);
			
			double opc_avg = MathTools.mean(opc_array);
			double opc_stdev = MathTools.standardDeviation(opc_array);

			double early_astro_avg = MathTools.mean(early_astro_array);
			double early_astro_stdev = MathTools.standardDeviation(early_astro_array);
			
			double late_astro_avg = MathTools.mean(late_astro_array);
			double late_astro_stdev = MathTools.standardDeviation(late_astro_array);
			LinkedList stemness_score = new LinkedList();						
			LinkedList lineage_score = new LinkedList();
			
			double buffer = 20;
			for (int i = 0; i < cycling_array.length; i++) {
				double z_score_cycling = ((cycling_array[i] - cycling_avg) / cycling_stdev) + buffer;
				double z_score_esc = ((esc_array[i] - esc_avg) / esc_stdev) + buffer;
				
				double z_score_late_astro = (late_astro_array[i] - late_astro_avg) / late_astro_stdev + buffer;
				double z_score_early_astro = (early_astro_array[i] - early_astro_avg) / early_astro_stdev + buffer;
				double z_score_oligo = (oligo_array[i] - oligo_avg) / oligo_stdev + buffer;
				double z_score_opc = (opc_array[i] - opc_avg) / opc_stdev + buffer;
				
				double z_max_lineage = -999;
				double average_oligo_opc = (z_score_oligo + z_score_opc) / 2;
				double average_late_early_astro = (z_score_early_astro + z_score_late_astro) / 2;
				
				/*
				if (z_max_lineage < z_score_late_astro) {
					z_max_lineage = z_score_late_astro;
				}				
				if (z_max_lineage < z_score_oligo) {
					z_max_lineage = z_score_oligo;
				}*/
				
				double z_score_stemness = (z_score_cycling + z_score_esc) / 2;
				
				double z_score_oligo_opc = -999;
				double z_score_early_late_astro = -999;
				if (z_score_oligo > z_score_opc) {
					z_score_oligo_opc = ((z_score_oligo - buffer) * 1.5 + (z_score_opc - buffer) * 0.5) + buffer;
					//z_score_oligo_opc = (z_score_oligo - buffer) + z_score_opc;
				} else {
					z_score_oligo_opc = z_score_opc;
				}
				if (z_score_late_astro > z_score_early_astro) {
					z_score_early_late_astro = ((z_score_late_astro - buffer) * 1.5 + (z_score_early_astro - buffer) * 0.5) + buffer;
					//z_score_early_late_astro = (z_score_late_astro - buffer) + z_score_early_astro;
				} else {
					z_score_early_late_astro = z_score_early_astro;
				}
				
				if (z_max_lineage < z_score_oligo_opc) {
					z_max_lineage = z_score_oligo_opc;
				}
				if (z_max_lineage < z_score_early_late_astro) {
					z_max_lineage = z_score_early_late_astro;
				}
				
				z_score_stemness = z_score_stemness - z_max_lineage;
				if (average_late_early_astro > average_oligo_opc) {
					z_max_lineage = (z_max_lineage - buffer) * -1 + buffer;
				}
				z_max_lineage = z_max_lineage - buffer;
				String name_str = (String)name.get(i);				
				
				
				if (z_score_stemness > 1) {
					double sign = (z_max_lineage / Math.abs(z_max_lineage));					
					z_max_lineage = 1.0 * rand.nextGaussian() * sign / 3;
				}
				out.write(name_str + "\t" + new Double(Math.round(z_score_stemness * 100)) / 100 + "\t" + new Double(Math.round(z_max_lineage * 100)) / 100 +  "\n");
				
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
