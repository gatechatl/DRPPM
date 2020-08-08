package protein.structure.domain.disorder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CalculateAminoAcidContentSummary {

	public static String parameter_info() {
		return "[fastaFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
		
			String fastaFile = args[0];
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("Name\tR\tH\tK\tD\tE\tS\tT\tN\tQ\tC\tU\tG\tP\tA\tV\tI\tL\tM\tF\tY\tW\n");
			HashMap map = readFastaFile(fastaFile);
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String seq = (String)map.get(name);
				out.write(name + "\t" + propR(seq) + "\t" + propH(seq) + "\t" + propK(seq) + "\t" + propD(seq)
						+ "\t" + propE(seq) + "\t" + propS(seq) + "\t" + propT(seq) + "\t" + propN(seq)
						+ "\t" + propQ(seq) + "\t" + propC(seq) + "\t" + propU(seq) + "\t" + propG(seq)
						+ "\t" + propP(seq) + "\t" + propA(seq) + "\t" + propV(seq) + "\t" + propI(seq)
						+ "\t" + propL(seq) + "\t" + propM(seq) + "\t" + propF(seq) + "\t" + propY(seq) + "\t" + propW(seq) + "\n");
				
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	public static double propR(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("R")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propH(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("H")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propK(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("K")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propD(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("D")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propE(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("E")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propS(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("S")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propT(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("T")) {
				count++;
			}
		}
		return count / seq.length();
	}
	
	public static double propN(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("N")) {
				count++;
			}
		}
		return count / seq.length();
	}
	
	public static double propQ(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("Q")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propC(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("C")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propU(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("U")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propG(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("G")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propP(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("P")) {
				count++;
			}
		}
		return count / seq.length();
	}
	
	public static double propA(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("A")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propV(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("V")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propI(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("I")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propL(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("L")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propM(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("M")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propF(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("F")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propY(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("Y")) {
				count++;
			}
		}
		return count / seq.length();
	}
	public static double propW(String seq) {
		if (seq.length() == 0) {
			return 0.0;
		}
		double count = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("W")) {
				count++;
			}
		}
		return count / seq.length();
	}
	
	public static HashMap readFastaFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String uniprotName = "";
			String combined_seq = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					//String[] split = str.split("\\|");
					//uniprotName = str.replaceAll(">", "");					
					
				} else {
					combined_seq += str.trim();
					/*if (map.containsKey(uniprotName)) {
						String seq = (String)map.get(uniprotName);
						seq += str.trim();
						map.put(uniprotName, seq);
					} else {
						map.put(uniprotName, str.trim());
					}*/
				}				
			}
			in.close();			
			map.put("Input", combined_seq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
