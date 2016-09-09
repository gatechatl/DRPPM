package PhosphoTools.MISC.KunduLab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SpecializedConversionFile {

	public static void main(String[] args) {
		
		try {
			
			String geneName = "";
			String seq = "";
			HashMap map = new HashMap();
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\ULK1_CLASP1-2_Project\\Convert2fasta\\kundu_sent_thisfile.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine();
				if (str.length() > 1) {
					while (str.contains("  " )) {
						str = str.replaceAll("  ", " ");
					}
					
					if (!str.substring(0, 1).equals(" ")) {
						//System.out.println("'" + str.substring(0, 1) + "'");
						geneName = str.split(" ")[0].replaceAll("\t", "");
						seq = "";
						
						String[] split = str.split(" ");						
						for (int i = 2; i < split.length; i++) {
							seq += split[i];
						}
					} else {
						str = "B" + str;
						String[] split = str.split(" ");						
						for (int i = 2; i < split.length; i++) {
							seq += split[i];
						}
					}
					
					map.put(geneName, seq.toUpperCase());
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				geneName = (String)itr.next();
				String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\ULK1_CLASP1-2_Project\\Convert2fasta\\" + geneName + ".fasta";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);				
				seq = (String)map.get(geneName);
				out.write(">" + geneName + "\n" + seq + "\n");
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
