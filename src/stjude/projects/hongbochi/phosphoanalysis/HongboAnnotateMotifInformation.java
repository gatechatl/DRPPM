package stjude.projects.hongbochi.phosphoanalysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Using Yuxin's supplementary table, append AGC, MTORC1 Score and Rapamycin sensitive information
 * @author tshaw
 *
 */
public class HongboAnnotateMotifInformation {

	public static String description() {
		return "Using Yuxin's supplementary table, append AGC, MTORC1 Score and Rapamycin sensitive information";
	}
	public static String type() {
		return "HONGBO";
	}
	public static String parameter_info() {
		return "[supplementaryTableFile] [AGCMotifInformation] [mtorc1_st_file] [phosphositeRegulationFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String supplementaryTableFile = args[0];
			String AGCMotifInformation = args[1];
			String mtorc1_st_file = args[2];
			String phosphositeRegulationFile = args[3];
			
			HashMap agc_site = new HashMap();
			HashMap mtorc1_score = new HashMap();
			HashMap rapamycin_site = new HashMap();
			
			FileInputStream fstream = new FileInputStream(AGCMotifInformation);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				String accession = split[0].split(":")[0];
				String site = split[1];
				String agc = split[5];
				String[] pos = split[1].split(",");
				for (int i = 0; i < pos.length; i++) {
					String p = pos[i].replaceAll("\\*", "");
					if (p.contains("S")) {
						p = "S" + p.replaceAll("S", "");
					}
					if (p.contains("T")) {
						p = "T" + p.replaceAll("T", "");
					}
					if (p.contains("Y")) {
						p = "Y" + p.replaceAll("Y", "");
					}
					if (agc.contains("AGC")) {
						agc_site.put(accession + "\t" + p, accession + "\t" + p);
					}
				}				
			}
			in.close();
			
			
			fstream = new FileInputStream(mtorc1_st_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				mtorc1_score.put(split[0], split[1]);
			}
			in.close();
			
			fstream = new FileInputStream(phosphositeRegulationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String site = split[3];
				String meta = split[10];
				if (meta.contains("Rapamycin-sensitive")) {
					rapamycin_site.put(accession + "\t" + site, accession + "\t" + site);
				}
			}
			in.close();
			
			fstream = new FileInputStream(supplementaryTableFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println(header + "\tAGC_Motif\tmTORC1Score\tRapamycin_sensitive_site");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String full_accession = split[1];
				String accession = full_accession.split("\\|")[1];
				String sites = split[3];
				String[] pos = sites.split(",");
				boolean AGC = false;
				double mTORC1 = -1;
				boolean rapamycin = false;
				
				for (int i = 0; i < pos.length; i++) {					

					if (agc_site.containsKey(full_accession + "\t" + pos[i])) {
						AGC = true;
					}
					if (rapamycin_site.containsKey(accession + "\t" + pos[i])) {
						rapamycin = true;
					}
					if (mtorc1_score.containsKey(accession + "_" + pos[i].replaceAll("S", "").replaceAll("T", "").replaceAll("Y", ""))) {						
						double score = new Double((String)mtorc1_score.get(accession + "_" + pos[i].replaceAll("S", "").replaceAll("T", "").replaceAll("Y", "")));
						if (score > mTORC1) {
							mTORC1 = score;
						}
					}
				}				
				
				System.out.println(str + "\t" + AGC + "\t" + mTORC1 + "\t" + rapamycin);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
