package jump.pipeline.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

public class ExtractUniqPeptides {
	public static String description() {
		return "Extract uniq peptide for a particular proteinName";
	}
	public static String type() {
		return "JUMP";
	}
	public static String parameter_info() {
		return "[inputFile] [proteinName]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String proteinName = args[1];
			HashMap peptide = new HashMap();
			//LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");				
				if (split[1].equals(proteinName)) {
					peptide.put(split[0], split[0]);
					
					//System.out.println(str.replaceAll(";","\t"));
				}
			}
			in.close();
			
			HashMap bad_peptide = new HashMap();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");				
				if (peptide.containsKey(split[0])) {
					if (!split[1].equals(proteinName)) {
						bad_peptide.put(peptide, peptide);
					}
					//System.out.println(str.replaceAll(";","\t"));
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");				
				if (split[1].equals(proteinName) && !bad_peptide.containsKey(split[0])) {
					
					System.out.println(str.replaceAll(";","\t"));
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
