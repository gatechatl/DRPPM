package RNAseqTools.IntronRetention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import MISC.HumanMouseGeneNameConversion;

public class OverlapAllMouseHuman {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "overlap the two dataset, one mouse one human";
	}
	public static String parameter_info() {
		return "[hs2mmFile] [mouseFile] [humanFile] [outputMouse] [outputHuman]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String hs2mmFile = args[0];
			String mouseFile = args[1];			
			String humanFile = args[2];
			String outputMouseFile = args[3];
			String outputHumanFile = args[4];
			HashMap map = new HashMap();			
			HashMap map_human = new HashMap();
			HashMap human2mouse = HumanMouseGeneNameConversion.human2mouse(hs2mmFile);
			HashMap mouse2human = HumanMouseGeneNameConversion.mouse2human(hs2mmFile);
			FileInputStream fstream = new FileInputStream(humanFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (human2mouse.containsKey(split[0])) {
					String mouseGene = (String)human2mouse.get(split[0]);
					map.put(mouseGene, mouseGene);	
					
				}
				
			}
			in.close();
			
        	FileWriter fwriter = new FileWriter(outputMouseFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
            
			fstream = new FileInputStream(mouseFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (mouse2human.containsKey(split[0])) {
					String humanGene = (String)mouse2human.get(split[0]);
					map_human.put(humanGene, humanGene);
				}
				if (map.containsKey(split[0])) {
					out.write(str + "\n");
					
					//System.out.println(str);
				}								
			}
			in.close();
			out.close();
			

        	FileWriter fwriter2 = new FileWriter(outputHumanFile);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            
			fstream = new FileInputStream(humanFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map_human.containsKey(split[0])) {
					out2.write(str + "\n");
					
					//System.out.println(str);
				}			
			}
			in.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
