package PhosphoTools.MISC.KunduLab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Calculate percent conservation
 * @author tshaw
 *
 */
public class CalculatePercentConservation {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Calculate Percent Conservation";
	}
	public static String parameter_info() {
		return "[alignmentFile] [posFile] [path] [outputFileSummary]";
	}
	public static void execute(String[] args) {
		try {
			//String alignmentFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\ULK1_Sites\\SEC16A_ncbi_alignment_withInvertebrate_muscle.fasta";			
			//String alignmentFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\ULK1_Sites\\SEC16A_ncbi_alignment_withInvertebrate_muscle2.fasta";
			String alignmentFile = args[0];
			String posFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\Conservation\\SEC16a_candidate_list.txt";
			String path = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\Conservation\\SEC16A_Sites\\";
			String outputFileSummary = args[3];
			

			FileWriter fwriter2 = new FileWriter(outputFileSummary);
			BufferedWriter out2 = new BufferedWriter(fwriter2);

			File f = new File(path);
			f.mkdir();
			
			HashMap pos_map = grabPositionAlignment(alignmentFile);
			System.out.println("Running alignment: " + alignmentFile);
			String humanGeneName = "";
			FileInputStream fstream = new FileInputStream(alignmentFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String temp = str.replaceAll(">", "");
					if (temp.contains("Homo_sapiens")) {
						humanGeneName = temp.replaceAll(">", "");				
					}
				}
			}
			in.close();
				
			
			LinkedList line = grabLine(posFile);
			//Sec16A_Homo_sapiens
			Iterator itr = line.iterator();
			String header = (String)itr.next();
			//System.out.println(header + "\tabsolute_conservation_vertebrate\tphospho_conservation_vertebrate\tabsolute_conservation_invertebrate\tphospho_conservation_invertebrate");
			out2.write(header + "\tabsolute_conservation_vertebrate\tphospho_conservation_vertebrate\tabsolute_conservation_invertebrate\tphospho_conservation_invertebrate\n");
			while (itr.hasNext()) {
				String str = (String)itr.next();
				String[] split = str.split("\t");
				
				String nuc = split[1].substring(0, 1);
				int loc = new Integer(split[1].substring(1, split[1].length()));
				int i = getPosition(loc, pos_map, humanGeneName);
				String outputFile = path + "/" + split[1] + ".fasta";
				writeAlignment(pos_map, i, outputFile);
				String phospho_conservation_invertebrate = phospho_conservation_invertebrate(pos_map, i);
				double phospho_conservation_vertebrate = phospho_conservation_vertebrate(pos_map, i);
				String absolute_conservation_invertebrate = absolute_conservation_invertebrate(pos_map, i, nuc);
				double absolute_conservation_vertebrate = absolute_conservation_vertebrate(pos_map, i, nuc);
				
				out2.write(str + "\t" + absolute_conservation_vertebrate + "\t" + phospho_conservation_vertebrate + "\t" + absolute_conservation_invertebrate + "\t" + phospho_conservation_invertebrate + "\n");
				//System.out.println(str + "\t" + absolute_conservation_vertebrate + "\t" + phospho_conservation_vertebrate + "\t" + absolute_conservation_invertebrate + "\t" + phospho_conservation_invertebrate);
			}
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeAlignment(HashMap pos_map, int i, String outputFile) {
		int start = i - 5;
		int end = i + 5;
		if (start < 0) {
			start = 0;
		}
		if (end > pos_map.size()) {
			end = pos_map.size();
		}
		try {

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			LinkedList list = new LinkedList();
			HashMap tmp_pos = (HashMap)pos_map.get(0);
			Iterator itr = tmp_pos.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				list.add(name);
			}
			itr = list.iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				out.write(">" + name + "\n");
				for (int j = start; j < end; j++) {
					HashMap pos = (HashMap)pos_map.get(j);
					String nuc = (String)pos.get(name);
					out.write(nuc);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double protion_absolute_conservation(HashMap pos_map, int i, String nuc) {
		//for (int i = 0; i < pos_map.size(); i++) {
		double match = 0;
		HashMap pos = (HashMap)pos_map.get(i);
		Iterator itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			String loc_nuc = (String)pos.get(name);
			if (loc_nuc.equals(nuc)) {
				match++;
			}
		}
		return match / pos.size();
	}
	
	public static boolean isVertebrate(String name) {
		if (name.contains("Danio_rerio") || name.contains("Xenopus_tropicalis") || name.contains("Gallus_gallus") || name.contains("Bos_taurus") || name.contains("Mus_musculus") || name.contains("Rattus_norvegicus") || name.contains("Canis_lupus_familiaris") || name.contains("Macaca_mulatta") || name.contains("Homo_sapiens") || name.contains("Pan_troglodytes")) {
			return true;
		}
		return false;
	}
	
	
	
	public static String phospho_conservation_invertebrate(HashMap pos_map, int i) {
		//for (int i = 0; i < pos_map.size(); i++) {
		
		double match = 0;
		double total = 0;
		HashMap pos = (HashMap)pos_map.get(i);
		Iterator itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			if (isInvertebrate(name)) {
				total++;
			}
		}
		String result = "";
		pos = (HashMap)pos_map.get(i);
		itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			String loc_nuc = (String)pos.get(name);
			if ((loc_nuc.equals("S") || loc_nuc.equals("T")) && isInvertebrate(name)) {
				//match++;
				if (name.contains("Drosophila")) {
					if (!result.contains("FruitFly")) {
						result += "FruitFly,";
					}
				}
				if (name.contains("elegans")) {
					result += "C.elegans,";
				}
				if (name.contains("YEAST")) {
					result += "Yeast,";
				}
			}
		}
		return result;
	}
	public static double phospho_conservation_vertebrate(HashMap pos_map, int i) {
		//for (int i = 0; i < pos_map.size(); i++) {
		
		double match = 0;
		double total = 0;
		HashMap pos = (HashMap)pos_map.get(i);
		Iterator itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			//if (isVertebrate(name)) {
				total++;
			//}
		}
		pos = (HashMap)pos_map.get(i);
		itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			String loc_nuc = (String)pos.get(name);
			//if ((loc_nuc.equals("S") || loc_nuc.equals("T")) && isVertebrate(name)) {
			if ((loc_nuc.equals("S") || loc_nuc.equals("T"))) {
				match++;
			}
		}
		return match / total;
	}
	//////////////////////////////////////////////
	
	public static boolean isInvertebrate(String name) {
		if (!(name.contains("Danio_rerio") || name.contains("Xenopus_tropicalis") || name.contains("Gallus_gallus") || name.contains("Bos_taurus") || name.contains("Mus_musculus") || name.contains("Rattus_norvegicus") || name.contains("Canis_lupus_familiaris") || name.contains("Macaca_mulatta") || name.contains("Homo_sapiens") || name.contains("Pan_troglodytes") || name.contains("Monodelphis_domestica"))) {
			return true;
		}
		return false;
	}
	public static String absolute_conservation_invertebrate(HashMap pos_map, int i, String nuc) {
		//for (int i = 0; i < pos_map.size(); i++) {
		String result = "";
		String hit = "";
		double match = 0;
		double total = 0;
		HashMap pos = (HashMap)pos_map.get(i);
		Iterator itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			if (isInvertebrate(name)) {
				total++;
			}
		}
		pos = (HashMap)pos_map.get(i);
		itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			String loc_nuc = (String)pos.get(name);
			if (loc_nuc.equals(nuc) && isInvertebrate(name)) {
				if (name.contains("Drosophila")) {
					if (!result.contains("FruitFly")) {
						result += "FruitFly,";
					}
				}
				if (name.contains("elegans")) {
					result += "C.elegans,";
				}
				if (name.contains("YEAST")) {
					result += "Yeast,";
				}				
			}
		}
		return result;
	}
	public static double absolute_conservation_vertebrate(HashMap pos_map, int i, String nuc) {
		//for (int i = 0; i < pos_map.size(); i++) {
		
		double match = 0;
		double total = 0;
		HashMap pos = (HashMap)pos_map.get(i);
		Iterator itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			//if (isVertebrate(name)) {
				total++;
			//}
		}
		pos = (HashMap)pos_map.get(i);
		itr = pos.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String)itr.next();
			String loc_nuc = (String)pos.get(name);
			//if (loc_nuc.equals(nuc) && isVertebrate(name)) {
			if (loc_nuc.equals(nuc)) {
				match++;
			}
		}
		return match / total;
	}
	public static int getPosition(int loc, HashMap pos_map, String name) {
		int index = 0;
		for (int i = 0; i < pos_map.size(); i++) {
			HashMap pos = (HashMap)pos_map.get(i);
			if (pos.containsKey(name)) {
				String nuc = (String)pos.get(name);
				if (!nuc.equals("-")) {
					index++;
					if (index == loc) {
						return i;
					}
				}
			}
		}
		return -1;
	}
	public static LinkedList grabLine(String inputFile) {
		LinkedList list = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				list.add(str);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static HashMap grabPositionAlignment(String inputFile) {
		HashMap pos_map = new HashMap();
		try {
			String name = "";
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str.replaceAll(">", "");
				} else {
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						seq += str.trim();
						map.put(name, seq);
					} else {
						map.put(name, str.trim());
					}
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				name = (String)itr.next();
				String seq = (String)map.get(name);
				for (int i = 0; i < seq.length(); i++) {
					if (pos_map.containsKey(i)) {
						HashMap pos = (HashMap)pos_map.get(i);
						pos.put(name, seq.substring(i, i + 1));
						pos_map.put(i, pos);
					} else {
						HashMap pos = new HashMap();
						pos.put(name, seq.substring(i, i + 1));
						pos_map.put(i, pos);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pos_map;
	}
}
