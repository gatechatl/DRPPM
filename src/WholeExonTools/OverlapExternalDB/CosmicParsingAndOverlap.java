package WholeExonTools.OverlapExternalDB;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Customized mapping of cosmic SNV information 
 * @author tshaw
 *
 */
public class CosmicParsingAndOverlap {

	public static String parameter_info() {
		return "[inputFile] [mm92hg19] [cosmicDB] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String cosmicFile = args[1];
			String mm92hg19File = args[2];
			HashMap cosmicDB = new HashMap();
			
			HashMap countDisease = new HashMap();
			HashMap mm92hg19 = new HashMap();
			FileInputStream fstream = new FileInputStream(mm92hg19File);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				mm92hg19.put(split[0], split[1]);
			}
			
			fstream = new FileInputStream(cosmicFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				HashMap map = new HashMap();
				//System.out.println("Original:" + split[3] + "_" + split[4]);
				
				if (countDisease.containsKey(split[9])) {
					int num = (Integer)countDisease.get(split[9]);
					num++;
					countDisease.put(split[9], num);
				} else {
					countDisease.put(split[9], 1);
				}
				if (cosmicDB.containsKey(split[3] + ":" + split[4])) {
					map = (HashMap)cosmicDB.get(split[3] + ":" + split[4]);
					if (map.containsKey(split[9])) {
						int num = (Integer)map.get(split[9]);
						num++;
						map.put(split[9], num);
					} else {
						map.put(split[9], 1);
					}
					cosmicDB.put(split[3] + ":" + split[4], map);
				} else {
					if (map.containsKey(split[9])) {
						int num = (Integer)map.get(split[9]);
						num++;
						map.put(split[9], num);
					} else {
						map.put(split[9], 1);
					}
					cosmicDB.put(split[3] + ":" + split[4], map);
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[6].split("_").length > 1) {
					//System.out.println("Tag:" + split[6].split("_")[1]);
					String coordinate = "chr" + split[3] + ":" + split[4] + "-" + (new Integer(split[4]) + 1);
					//System.out.println(coordinate);
					if (mm92hg19.containsKey(coordinate)) {
						//System.out.println("Convert");
						String new_coordinate = (String)mm92hg19.get(coordinate);  
						new_coordinate = new_coordinate.split(":")[0] + ":" + (new Integer(new_coordinate.split(":")[1].split("-")[0]) - 1);
						//System.out.println(new_coordinate);
						if (cosmicDB.containsKey(new_coordinate.split("-")[0])) {
							String meta = "";
							int totalCount = 0;
							HashMap map = (HashMap)cosmicDB.get(new_coordinate.split("-")[0]);
							Iterator itr = map.keySet().iterator();
							while (itr.hasNext()) {
								String diseaseName = (String)itr.next();
								int count = (Integer)map.get(diseaseName);
								if (meta.equals("")) {
									meta = diseaseName + ":" + count;
								} else {
									meta += "," + diseaseName + ":" + count;
								}
								totalCount += count;
							}
							System.out.println(str + "\t" + new_coordinate + "\t" + meta + "\t" + totalCount);
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
