package stjude.projects.suzannebaker.stemness_lineage_ac_ol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Modified lineage and stemness score based on Tirosh et al. Nature. 2016 
 * @author tshaw
 *
 */
public class SuzanneBakerConvertSingleSampleGSEA2LineageScore {

	public static String description() {
		return "Calculate Lineage Score";
	}
	public static String type() {
		return "SuzanneBaker";
	}
	public static String parameter_info() {
		return "[ssGSEA_matrix_file] [control_samples_file] [gfap_samples_file] [nes_samples_file] [cluster_file] [stemness_index] [oligo_index] [astro_index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String ssGSEA_matrix_file = args[0];
			String control_samples_file = args[1];
			String gfap_samples_file = args[2];
			String nes_samples_file = args[3];
			String cluster_file = args[4];
			int stemness_index = new Integer(args[5]);
			int oligo_index = new Integer(args[6]);
			int astro_index = new Integer(args[7]);
			String outputFile = args[8];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tCluster\tStemness_Score\tLineage_Score\tSampleType\n");
			HashMap control_map = new HashMap();
			HashMap gfap_map = new HashMap();
			HashMap nes_map = new HashMap();
			HashMap cluster_map = new HashMap();
			
			LinkedList control_name = new LinkedList();
			LinkedList controL_cluster = new LinkedList();
			LinkedList control_stemness = new LinkedList();
			LinkedList control_astro = new LinkedList();
			LinkedList control_oligo = new LinkedList();

			LinkedList gfap_name = new LinkedList();
			LinkedList gfap_cluster = new LinkedList();
			LinkedList gfap_stemness = new LinkedList();
			LinkedList gfap_astro = new LinkedList();
			LinkedList gfap_oligo = new LinkedList();

			LinkedList nes_name = new LinkedList();
			LinkedList nes_cluster = new LinkedList();
			LinkedList nes_stemness = new LinkedList();
			LinkedList nes_astro = new LinkedList();
			LinkedList nes_oligo = new LinkedList();
			
			FileInputStream fstream = new FileInputStream(control_samples_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				control_map.put(split[0], split[0]);
			}
			in.close();

			fstream = new FileInputStream(gfap_samples_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				gfap_map.put(split[0], split[0]);
			}
			in.close();
			
			fstream = new FileInputStream(nes_samples_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				nes_map.put(split[0], split[0]);
			}
			in.close();
			
			fstream = new FileInputStream(cluster_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				cluster_map.put(split[0], split[1]);
			}
			in.close();
			fstream = new FileInputStream(ssGSEA_matrix_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split = header.split("\t");
			System.out.println("stemness header: " + header_split[stemness_index]);
			System.out.println("oligo header: " + header_split[oligo_index]);
			System.out.println("astro header: " + header_split[astro_index]);			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double stemness_score = new Double(split[stemness_index]);
				double oligo_score = new Double(split[oligo_index]);
				double astro_score = new Double(split[astro_index]);
				double downsample = Math.random();
				if (downsample < 0.2) {
					if (!((control_map.containsKey(split[0]) && gfap_map.containsKey(split[0])) || (control_map.containsKey(split[0]) && nes_map.containsKey(split[0])) || (gfap_map.containsKey(split[0]) && nes_map.containsKey(split[0])))) {
						if (control_map.containsKey(split[0])) {
							control_name.add(split[0]);
							control_stemness.add(stemness_score);
							control_oligo.add(oligo_score);
							control_astro.add(astro_score);
						}
						if (gfap_map.containsKey(split[0])) {
							gfap_name.add(split[0]);				
							gfap_stemness.add(stemness_score);
							gfap_oligo.add(oligo_score);
							gfap_astro.add(astro_score);					
						}
						if (nes_map.containsKey(split[0])) {
							nes_name.add(split[0]);					
							nes_stemness.add(stemness_score);
							nes_oligo.add(oligo_score);
							nes_astro.add(astro_score);		
						}
					}
				}
			}
			in.close();			
			double[] control_stemness_array = MathTools.convertListDouble2Double(control_stemness);
			double[] control_oligo_array = MathTools.convertListDouble2Double(control_oligo);
			double[] control_astro_array = MathTools.convertListDouble2Double(control_astro);
			
			double control_stemness_avg = MathTools.mean(control_stemness_array);
			double control_oligo_avg = MathTools.mean(control_oligo_array);
			double control_astro_avg = MathTools.mean(control_astro_array);

			double control_stemness_stdev = MathTools.standardDeviation(control_stemness_array);
			double control_oligo_stdev = MathTools.standardDeviation(control_oligo_array);
			double control_astro_stdev = MathTools.standardDeviation(control_astro_array);
			
			double[] gfap_stemness_array = MathTools.convertListDouble2Double(gfap_stemness);
			double[] gfap_oligo_array = MathTools.convertListDouble2Double(gfap_oligo);
			double[] gfap_astro_array = MathTools.convertListDouble2Double(gfap_astro);

			double[] nes_stemness_array = MathTools.convertListDouble2Double(nes_stemness);
			double[] nes_oligo_array = MathTools.convertListDouble2Double(nes_oligo);
			double[] nes_astro_array = MathTools.convertListDouble2Double(nes_astro);
						
			LinkedList gfap_stemness_score = new LinkedList();
			LinkedList nes_stemness_score = new LinkedList();
			
			LinkedList gfap_lineage_score = new LinkedList();
			LinkedList nes_lineage_score = new LinkedList();
			double buffer = 20;
			for (int i = 0; i < gfap_stemness_array.length; i++) {
				double z_score_stemness = (gfap_stemness_array[i] - control_stemness_avg) / control_stemness_stdev + buffer;
				double z_score_astro = (gfap_astro_array[i] - control_astro_avg) / control_astro_stdev + buffer;
				double z_score_oligo = (gfap_oligo_array[i] - control_oligo_avg) / control_oligo_stdev + buffer;
				
				double z_max_lineage = -999;
				if (z_max_lineage < z_score_astro) {
					z_max_lineage = z_score_astro;
				}
				if (z_max_lineage < z_score_oligo) {
					z_max_lineage = z_score_oligo;
				}
				
				// if stemness is larger than lineage
				/*if (z_score_stemness > z_max_lineage) {
					z_max_lineage = 0.1 * Math.random() + buffer;
				} else {
					if (z_max_lineage < 0) {
						z_max_lineage = 0.1 * Math.random() + buffer;
					}
				}*/
				
				z_score_stemness = z_score_stemness - z_max_lineage;
				if (z_score_astro > z_score_oligo) {
					z_max_lineage = (z_max_lineage - buffer) * -1 + buffer;
				}
				z_max_lineage = z_max_lineage - buffer;
				String name = (String)gfap_name.get(i);
				String cluster = (String)cluster_map.get(name);
				if (!(cluster.equals("5") || cluster.equals("12") || cluster.equals("7"))) {
					out.write(name + "\t" + cluster + "\t" + new Double(Math.round(z_score_stemness * 100)) / 100 + "\t" + new Double(Math.round(z_max_lineage * 100)) / 100 + "\tGfap" + "\n");
				}
			}
			
			for (int i = 0; i < nes_stemness_array.length; i++) {
				double z_score_stemness = (nes_stemness_array[i] - control_stemness_avg) / control_stemness_stdev + buffer;
				double z_score_astro = (nes_astro_array[i] - control_astro_avg) / control_astro_stdev + buffer;
				double z_score_oligo = (nes_oligo_array[i] - control_oligo_avg) / control_oligo_stdev + buffer;
				
				double z_max_lineage = -999;
				if (z_max_lineage < z_score_astro) {
					z_max_lineage = z_score_astro;
				}
				if (z_max_lineage < z_score_oligo) {
					z_max_lineage = z_score_oligo;
				}
				
				// if stemness is larger than lineage								
				/*if (z_score_stemness > z_max_lineage) {
					z_max_lineage = 0.1 + buffer;
				} else {
					if (z_max_lineage < 0) {
						z_max_lineage = 0.1 * Math.random() + buffer;
					}
				}*/
				z_score_stemness = z_score_stemness - z_max_lineage;
				if (z_score_astro > z_score_oligo) {
					z_max_lineage = (z_max_lineage - buffer) * -1 + buffer;
				}
				z_max_lineage = z_max_lineage - buffer;
				String name = (String)nes_name.get(i);
				String cluster = (String)cluster_map.get(name);
				if (!(cluster.equals("5") || cluster.equals("12") || cluster.equals("7"))) {
					out.write(name + "\t" + cluster + "\t" + new Double(Math.round(z_score_stemness * 100)) / 100 + "\t" + new Double(Math.round(z_max_lineage * 100)) / 100 + "\tNes" + "\n");
				}
			}
			
			for (int i = 0; i < control_stemness_array.length; i++) {
				double z_score_stemness = (control_stemness_array[i] - control_stemness_avg) / control_stemness_stdev + buffer;
				double z_score_astro = (control_astro_array[i] - control_astro_avg) / control_astro_stdev + buffer;
				double z_score_oligo = (control_oligo_array[i] - control_oligo_avg) / control_oligo_stdev + buffer;
				
				double z_max_lineage = -999;
				if (z_max_lineage < z_score_astro) {
					z_max_lineage = z_score_astro;
				}
				if (z_max_lineage < z_score_oligo) {
					z_max_lineage = z_score_oligo;
				}
				
				// if stemness is larger than lineage								
				/*if (z_score_stemness > z_max_lineage) {
					z_max_lineage = 0.1 + buffer;
				} else {
					if (z_max_lineage < 0) {
						z_max_lineage = 0.1 * Math.random() + buffer;
					}
				}*/
				z_score_stemness = z_score_stemness - z_max_lineage;
				if (z_score_astro > z_score_oligo) {
					z_max_lineage = (z_max_lineage - buffer) * -1 + buffer;
				}
				z_max_lineage = z_max_lineage - buffer;
				String name = (String)control_name.get(i);
				String cluster = (String)cluster_map.get(name);
				if (!(cluster.equals("5") || cluster.equals("12") || cluster.equals("7"))) {
					out.write(name + "\t" + cluster + "\t" + new Double(Math.round(z_score_stemness * 100)) / 100 + "\t" + new Double(Math.round(z_max_lineage * 100)) / 100 + "\t" + "control" + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
