package RNAseqTools.SingleCell.CellOfOrigin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Calculate the mutant allele counts per million
 * @author tshaw
 *
 */
public class CalculateReferenceAlleleExpressionMatrix {
	public static String type() {
		return "SNV";
	}
	public static String description() {
		return "Take variant matrix and calculate the mutant allele counts per million";
	}
	public static String parameter_info() {
		return "[snvTableMatrix] [sampleStatistics: second column total read count]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String maf_matrix = args[0];
			String sampleStats = args[1];
			
			HashMap total_reads = new HashMap();
			FileInputStream fstream = new FileInputStream(sampleStats);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				total_reads.put(split[0], new Double(split[1]));				
			}
			in.close();
			
			fstream = new FileInputStream(maf_matrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();

			header = header.replaceAll("Aligned.sortedByCoord.out.bam", "").replaceAll("_L001", "").replaceAll("_L002", "");
			String[] names = header.split("\t");
			HashMap names_map = new HashMap(); 
			for (int i = 1; i < names.length; i++) {
				names_map.put(names[i], names[i]);
			}
			System.out.print("Variant");
			Iterator itr = names_map.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				System.out.print("\t" + sampleName);
			}
			System.out.println();
			//System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				System.out.print(name);
				HashMap map_ref = new HashMap();
				HashMap map_mut = new HashMap();
				for (int i = 1; i < split.length; i++) {
					double ref = new Double(split[i].split("\\|")[1]);
					double mutant = new Double(split[i].split("\\|")[0]);
					//double total = ref + mutant;
					if (map_ref.containsKey(names[i])) {
						double old_ref = (Double)map_ref.get(names[i]);
						ref = ref + old_ref;
						map_ref.put(names[i], ref);
					} else {
						map_ref.put(names[i], ref);
					}
					if (map_mut.containsKey(names[i])) {
						double old_mut = (Double)map_mut.get(names[i]);
						mutant = mutant + old_mut;
						map_mut.put(names[i], mutant);
					} else {
						map_mut.put(names[i], mutant);
					}
					//System.out.print("\t" + (mutant / total));
				}
				//System.out.println();
				
				itr = names_map.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					double mutant = (Double)map_mut.get(sampleName);
					double ref = (Double)map_ref.get(sampleName);
					double total = (Double)total_reads.get(sampleName);
					double cpm = ref * 1000000 / total;
					System.out.print("\t" + cpm);
				}
				System.out.println();
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
