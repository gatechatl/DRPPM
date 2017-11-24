package proteomics.apms.saint;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GeneratePreyGeneLength {

	public static String description() {
		return "Generate the prey table for SAINT.";
	}
	public static String type() {
		return "SAINT";
	}
	public static String parameter_info() {
		return "[IDConversionTable] [interactiveFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String interactiveFile = args[1];
			
			HashMap uniq = new HashMap();
			FileInputStream fstream = new FileInputStream(interactiveFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2];
				uniq.put(gene, gene);
				
			}
			in.close();
			HashMap map = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[4], split[6]);
				/*if (uniq.containsKey(split[4])) {
					//if (map.containsKey(gene)) {
					//	String length = (String)map.get(gene);
					System.out.println(split[4] + "\t" + split[6] + "\t" + split[4]);
					
				} else {
					//System.out.println(split[4] + "\tNA\t" + split[4]);
				}*/
			}
			in.close();

			Iterator itr = uniq.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (map.containsKey(geneName)) {
					String length = (String)map.get(geneName);
					System.out.println(geneName + "\t" + length + "\t" + geneName);
				} else {
					System.out.println(geneName + "\tNA\t" + geneName);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
