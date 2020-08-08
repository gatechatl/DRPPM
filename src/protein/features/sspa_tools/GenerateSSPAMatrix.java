package protein.features.sspa_tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateSSPAMatrix {

	public static String parameter_info() {
		return "[fileList]";
	}
	public static void execute(String[] args) {
		
		try {
			
			System.out.println("GeneName\tPositive_charge_segment\tNegative_charge_segment\tMixed_charge_segment\tUncharge_segment\thydrophobic_segment\ttransmember_segment\trepetitive_structure");
			String fileList = args[0];
			FileInputStream fstream = new FileInputStream(fileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
			
				String geneName = "";
				boolean pos_charge = true;
				boolean neg_charge = true;
				boolean mix_charge = true;
				boolean uncharge = true;
				boolean hydrophobic = true;
				boolean transmembrane = true;
				String repetitive_structure = "";
				
				boolean start_repeat = false;
				
				File f = new File(str);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(str);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine().trim();
						
						if (str2.length() > 3) {
							if (str2.substring(0, 3).equals("ID ")) {
								String str3 = str2;
								str3 = str3.replaceAll("\t", " ");
								while (str3.contains("  ")) {
									str3 = str3.replaceAll("\t", " ");
									str3 = str3.replaceAll("\r", "");
									str3 = str3.replaceAll("\n", "");
									str3 = str3.replaceAll("  ", " ");					
								}
								String[] split3 = str3.split(" ");
								geneName = split3[1];
							}
						}
						if (str2.contains("There are no high scoring positive charge segments")) {
							pos_charge = false;
						}
						if (str2.contains("There are no high scoring negative charge segments")) {
							neg_charge = false;
						}
						if (str2.contains("There are no high scoring mixed charge segments")) {
							mix_charge = false;
						}
						if (str2.contains("There are no high scoring uncharged segments")) {
							uncharge = false;
						}
						if (str2.contains("There are no high scoring hydrophobic segments")) {
							uncharge = false;
						}
						if (str2.contains("There are no high scoring transmembrane segments")) {
							uncharge = false;
						}
						if (str2.contains("REPETITIVE STRUCTURES")) {
							start_repeat = true;
						}
						if (str2.contains("---")) {
							start_repeat = false;
						}
						if (start_repeat) {
							if (str2.contains("]")) {
								String str3 = str2;
								str3 = str3.replaceAll("\t", " ");
								while (str3.contains("  ")) {
									str3 = str3.replaceAll("\t", " ");
									str3 = str3.replaceAll("\r", "");
									str3 = str3.replaceAll("\n", "");
									str3 = str3.replaceAll("  ", " ");					
								}
								String[] split3 = str3.split("] ");
								//System.out.println(str3);
								if (split3.length > 1) {
									String repeat = split3[1];
									
									if (!repeat.contains("+") && !repeat.contains("-") && !repeat.contains("ii")) {
										repetitive_structure += repeat + ",";
									}
								}
							}
						}
						
					}				
					in2.close();
					
					String[] split4 = repetitive_structure.split(",");
					HashMap map = new HashMap();
					for (String rep: split4) {
						map.put(rep, rep);
					}
					repetitive_structure = "";
					Iterator itr = map.keySet().iterator();
					while (itr.hasNext()) {
						String key = (String)itr.next();
						if (repetitive_structure.equals("")) {
							repetitive_structure = key;
						} else {
							repetitive_structure += "," + key;
						}
					}
					if (repetitive_structure.equals("")) {
						repetitive_structure = "NA";
					}
					System.out.println(geneName + "\t" + pos_charge + "\t" + neg_charge + "\t" + mix_charge + "\t" + uncharge + "\t" + hydrophobic + "\t" + transmembrane + "\t" + repetitive_structure);
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
