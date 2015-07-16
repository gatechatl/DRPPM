package PhosphoTools.MotifTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Look up fasta sequence and extend the fasta tag if unique
 * @author tshaw
 *
 */
public class PeptideExtension {

	public static void main(String[] args) {
		try {
			HashMap seq = new HashMap();
					
			String fastaFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE.fasta";
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String name = "";
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str.split("\\|")[1];
					//System.out.println(name);
					seq.put(name, "");
				} else {
					String orig = (String)seq.get(name);
					orig += str.trim();
					seq.put(name, orig);
				}
			}
			in.close();
			
			
			String outputFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\extended.fasta";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			int total = 0;
			String fileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\TTest.txt";
			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			in.readLine();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] geneNames = split[1].split(",");
				for (String geneName: geneNames) {				
					String orig = split[0];
					String newSeq = orig.replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\#", "").replaceAll("\\*", "").replaceAll("\\@", "");
					//System.out.println(orig + "\t" + newSeq);
					String geneSeq = (String)seq.get(geneName);
					
					String finalSeq = "";
					if (seq.containsKey(geneName)) {
						//System.out.println(geneName);
						int count = 0;
						int index = 0;
						for (int i = 0; i < geneSeq.length() - newSeq.length(); i++) {
							if (geneSeq.substring(i, i + newSeq.length()).equals(newSeq)) {
								count++;
								//System.out.println("found");
								index = i;
							}
						}
						if (count == 1) {
							//if (index >= 5 && index + newSeq.length() < geneSeq.length() - 5) {
							int buffer = 5;
							int end_buffer = 5;
							if (index < 5) {
								buffer = index;
							}
							if (geneSeq.length() - index - newSeq.length() < 5) {
								end_buffer = geneSeq.length() - index - newSeq.length(); 
							}
							HashMap coord_map = new HashMap();
							//System.out.println(buffer + "\t" + end_buffer);
							String temp = geneSeq.substring(index - buffer, index + newSeq.length() + end_buffer);
							finalSeq = temp.substring(0, buffer);
							int oi = 0;
							for (int j = buffer; j < temp.length() - end_buffer; j++) {
								String p = orig.substring(oi, oi + 1);
								
								if (p.equals(".") || p.equals("*")) {
									oi++;
								} else {								
									if (p.equals("#")) {
										finalSeq += "#";
										oi++;
										coord_map.put(index - buffer + j, "S#");
									}
									if (p.equals("^")) {
										finalSeq += "^";
										coord_map.put(index - buffer + j, "Y^");
										oi++;
									}
									if (p.equals("@")) {
										finalSeq += "@";
										coord_map.put(index - buffer + j, "T@");
										oi++;
									}			
								}
								finalSeq += temp.substring(j, j + 1);
								oi++;
							}
							total++;
							finalSeq += geneSeq.substring(index + newSeq.length(), index + newSeq.length() + end_buffer);
						
							String mod = "";
							Iterator itr = coord_map.keySet().iterator();
							while (itr.hasNext()) {
								int coord = (Integer)itr.next();
								String type = (String)coord_map.get(coord);
								mod += coord + type + ",";
							}
							out.write(">" + geneName + "\n" + finalSeq + "\n");
							System.out.println("found: " + geneName + "\t" + finalSeq + "\t" + orig + "\t" + geneSeq + "\t" + mod);
						}
					}
				}
			} 
			in.close();
			out.close();
			System.out.println(total);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

