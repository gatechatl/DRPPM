package general.sequence.analysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GCScanner {
	
	public static String description() {
		return "A scanner to go through a sequence and calculate: GC content, CpG density, and GC skew";
	}
	public static String type() {
		return "SCANNER";
	}
	public static String parameter_info() {
		return "[inputFastaFile] [windowSize] [startPosition]";
	}
	public static void execute(String[] args) {
		
		try {
			StringBuffer buffer = new StringBuffer();
			String inputFile = args[0];
			int window_size = new Integer(args[1]);
			int startPosition = new Integer(args[2]);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					
				} else {
					buffer.append(str);
				}
			}
			in.close();
			
			System.out.println("Position\tGC%\tGC_Skew\tCpG(o/e)");
			String sequence = buffer.toString();
			int pos = startPosition;
			for (int i = 0; i < sequence.length() - window_size; i++) {
				String seq = sequence.substring(i , i + window_size);
				double cpgDensity = calculate_CpGDensity(seq);
				double gcSkew = calculate_GCSkew(seq);
				double GC = calculate_GC(seq);
				System.out.println((i + pos) + "\t" + GC + "\t" + gcSkew + "\t" + cpgDensity);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*public static void main(String[] args) {
		
		String sequence = "TCCATTCGACTCATCACGCTCCCCCCCCCCCCCCCCCCCCTTATCCGTTCCGTTCGACGTATTTCGTTGTCTAATTTCTGACGTAACTTGTTCCCTGTTAAGTACCGTTTATGGCCTATACTCCGGTATTTAAAACGACGACGATTCCACCGTAAAGCCGTCAACCAGATGAACGACCTCGCTCGTTATATTTTTCCGGC";
		System.out.println(calculate_CpGDensity(sequence));
	}*/
	/**
	 * This is another word for observed-to-expected CpG ratio
	 * Formula based on Gardiner-Garden and Frommer J Mol Biol 1987
	 * @param sequence
	 * @return
	 */
	public static double calculate_CpGDensity(String sequence) {
		String seq = sequence.toUpperCase();
		double GC = 0;
		double CG = 0;
		double G = 0;
		double C = 0;
		for (int i = 0; i < seq.length() - 1; i++) {
			if (seq.substring(i, i + 2).equals("GC")) {
				GC++;
			}
			if (seq.substring(i, i + 2).equals("CG")) {
				CG++;
			}
			
		}
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("G")) {
				G++;
			}
			if (seq.substring(i, i + 1).equals("C")) {
				C++;
			}
		}
		double CpGobserved = CG;
		//double GCobserved = G + C;
		double expected = (C * G);
		if (expected == 0) {
			return 0.0;
		}
		return CpGobserved / expected * sequence.length();
	}
	public static double calculate_GCSkew(String sequence) {
		String seq = sequence;
		double G = 0;
		double C = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("G")) {
				G++;
			}
			if (seq.substring(i, i + 1).equals("C")) {
				C++;
			}
		}
		double GCSkew = (G - C) / (G + C);
		if ((G + C) == 0) {
			GCSkew = 0;
		}
		return GCSkew;
	}
	public static double calculate_GC(String sequence) {
		String seq = sequence;
		double G = 0;
		double C = 0;
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("G")) {
				G++;
			}
			if (seq.substring(i, i + 1).equals("C")) {
				C++;
			}
		}
		double GC = (G + C) / sequence.length();
		return GC;
	}
}
