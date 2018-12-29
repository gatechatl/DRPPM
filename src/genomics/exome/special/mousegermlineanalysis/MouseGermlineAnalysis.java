package genomics.exome.special.mousegermlineanalysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * This is a specialized class for Peter McKinnon's mouse samples
 * @author tshaw
 *
 */
public class MouseGermlineAnalysis {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\GermlineAnalysis\\RemoveGermlineSNV\\GangSuspiciousRecurrent.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("_");
				if (split.length > 1) {
					map.put(split[1], str);
					//System.out.println(split[1]);
				}				
			}
			in.close();
			
			String germlineListBatch1 = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\GermlineAnalysis\\RemoveGermlineSNV\\new_matrix_combined_matrix_sample_x_cell_variant_count_first.tab";
			fstream = new FileInputStream(germlineListBatch1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));	
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String variantNuc = str.split("\t")[0];
				String[] split = variantNuc.split("\\.");
				String variant = split[0] + ":" + split[1];
				if (map.containsKey(variant)) {
					System.out.println(variant);
				}
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
