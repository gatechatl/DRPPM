package PhosphoTools.MotifTools.MotifX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Fasta2MotifXFriendly {

	public static void main(String[] args) {
		
		try {
			
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\HonboCHI\\MotifX\\16Hour\\jumpq_up_extend.txt";
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\HonboCHI\\MotifX\\2Hour\\jumpq_up_extend.txt";
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\HonboCHI\\MotifX\\16Hour\\jumpq_up.motifX.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\HonboCHI\\MotifX\\2Hour\\jumpq_up.motifX.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			int count = 1;
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				str = str.split("\t")[0];
				if (!str.contains(">")) {
					str = str.replaceAll("\\.", "");
					str = str.replaceAll("\\*", "#");
					str = str.replaceAll("-", "");
					int modloc = -1;
					for (int i = 0; i < str.length(); i++) {
						if (str.substring(i, i + 1).equals("#")) {
							modloc = i;
						}
					}
					if (modloc > 7 && str.length() - modloc > 7) {
						str = str.substring(modloc - 7, modloc + 7).replaceAll("#",  "");
						if (!map.containsKey(str)) {
							//out.write(">" + count + "\n");
							out.write(str + "\n");
							count++;
						}
						map.put(str, str);
					}
					
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
