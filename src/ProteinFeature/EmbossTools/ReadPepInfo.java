package ProteinFeature.EmbossTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class ReadPepInfo {

	public static String parameter_info() {
		return "[inputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String name = "";
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				str = str.replaceAll("\t", " ");
				while (str.contains("  ")) {
					str = str.replaceAll("\t", " ");
					str = str.replaceAll("\r", "");
					str = str.replaceAll("\n", "");
					str = str.replaceAll("  ", " ");					
				}
				
				String[] split = str.split(" ");
				if (str.contains("PEPSTATS")) {
					name = split[2];
					PEPSTATS pepstats = new PEPSTATS();
					pepstats.NAME = name;
					map.put(name, pepstats);
				}
				if (str.contains("Molecular weight =")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.MOL_WEIGHT = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Residues =")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.RESIDUES = new Double(split[6]);
					map.put(name, pepstats);
				}
				if (str.contains("Average Residue Weight =")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.AVG_MOL_WEIGHT = new Double(split[4]);
					map.put(name, pepstats);
				}
				if (str.contains("Charge ")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.CHARGE = new Double(split[7]);
					map.put(name, pepstats);
				}
				if (str.contains("Isoelectric Point =")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.ISOELECTRIC_POINT = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("A280 Extinction")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.EXTINCTION_COEF = new Double(split[5]);
					map.put(name, pepstats);
				}
				if (str.contains("Improbability of expression in inclusion bodies")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.PROB_EXPR_INCLUSION = (1 - new Double(split[7]));
					map.put(name, pepstats);
				}
				if (str.contains("Probability of expression in inclusion bodies")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.PROB_EXPR_INCLUSION = new Double(split[7]);
					map.put(name, pepstats);
				}
				if (str.contains("Tiny")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.TINY = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Small")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.SMALL = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Aliphatic")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.ALIPHATIC = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Aromatic")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.AROMATIC = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Non-polar")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.NONPOLAR = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Polar")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.POLAR = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Charged")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.CHARGED = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Basic")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.BASIC = new Double(split[3]);
					map.put(name, pepstats);
				}
				if (str.contains("Acidic")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.ACIDIC = new Double(split[3]);
					map.put(name, pepstats);
				}
			}
			in.close();
			/*		String NAME = "";
		double MOL_WEIGHT = Double.NaN;
		double RESIDUES = Double.NaN;
		double AVG_MOL_WEIGHT = Double.NaN;
		double CHARGE = Double.NaN;
		double ISOELECTRIC_POINT = Double.NaN;
		double EXTINCTION_COEF = Double.NaN;
		double PROB_EXPR_INCLUSION = Double.NaN;
		double TINY = Double.NaN;
		double SMALL = Double.NaN;
		double ALIPHATIC = Double.NaN;
		double AROMATIC = Double.NaN;
		double NONPOLAR = Double.NaN;
		double POLAR = Double.NaN;
		double CHARGED = Double.NaN;
		double BASIC = Double.NaN;
		double ACIDIC = Double.NaN;
		*/
			System.out.println("NAME\tMOL_WEIGHT\tRESIDUES\tAVG_MOL_WEIGHT\tCHARGE\tISOELECTRIC_POINT\tEXTINCTION_COEF\tPROB_EXPR_INCLUSION\tTINY\tSMALL\tALIPHATIC\tAROMATIC\tNONPOLAR\tPOLAR\tCHARGED\tBASIC\tACIDIC");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				PEPSTATS pepstats = (PEPSTATS)map.get(geneName);
				System.out.println(pepstats.NAME + "\t" + pepstats.MOL_WEIGHT + "\t" + pepstats.RESIDUES + "\t" + pepstats.AVG_MOL_WEIGHT +	"\t" + pepstats.CHARGE + "\t" + pepstats.ISOELECTRIC_POINT + "\t" + pepstats.EXTINCTION_COEF + "\t" + pepstats.PROB_EXPR_INCLUSION + "\t" + pepstats.TINY + "\t" + pepstats.SMALL + "\t" + pepstats.ALIPHATIC + "\t" + pepstats.AROMATIC + "\t" + pepstats.NONPOLAR + "\t" + pepstats.POLAR + "\t" + pepstats.CHARGED + "\t" + pepstats.ACIDIC + "\t" + pepstats.BASIC);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static class PEPSTATS {
		String NAME = "";
		double MOL_WEIGHT = Double.NaN;
		double RESIDUES = Double.NaN;
		double AVG_MOL_WEIGHT = Double.NaN;
		double CHARGE = Double.NaN;
		double ISOELECTRIC_POINT = Double.NaN;
		double EXTINCTION_COEF = Double.NaN;
		double PROB_EXPR_INCLUSION = Double.NaN;
		double TINY = Double.NaN;
		double SMALL = Double.NaN;
		double ALIPHATIC = Double.NaN;
		double AROMATIC = Double.NaN;
		double NONPOLAR = Double.NaN;
		double POLAR = Double.NaN;
		double CHARGED = Double.NaN;
		double BASIC = Double.NaN;
		double ACIDIC = Double.NaN;
		
	}
}

