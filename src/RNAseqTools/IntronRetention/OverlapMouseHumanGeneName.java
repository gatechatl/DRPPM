package RNAseqTools.IntronRetention;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import MISC.HumanMouseGeneNameConversion;


public class OverlapMouseHumanGeneName {
	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Overlap the mouse and human gene list";
	}
	public static String parameter_info() {
		return "[overlapFile] [mouseFile] [hs2mmFile] [humanFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String mouseFile = args[1];
			String hs2mmFile = args[2];
			String humanFile = args[3];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);				
			}
			in.close();
						
			LinkedList list = new LinkedList();
			fstream = new FileInputStream(mouseFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine();
				
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					list.add(str);
				}				
			}
			in.close();
			
			
			HashMap human2mouse = HumanMouseGeneNameConversion.human2mouse(hs2mmFile);
			HashMap finalList = new HashMap();
			fstream = new FileInputStream(humanFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (human2mouse.containsKey(split[0])) {
					String mouseGene = (String)human2mouse.get(split[0]);
					if (map.containsKey(mouseGene)) {
						finalList.put(mouseGene, mouseGene);
					}
				}
			}
			in.close();
			
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String str  = (String)itr.next();
				String[] split = str.split("\t");
				if (finalList.containsKey(split[0])) {
					System.out.println(str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
