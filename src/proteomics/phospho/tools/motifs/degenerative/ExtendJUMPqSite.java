package proteomics.phospho.tools.motifs.degenerative;

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

public class ExtendJUMPqSite {
	
	public static String description() {
		return "Look up fasta sequence and extend the fasta tag if unique";
	}
	public static String type() {
		return "JUMPQ";
	}
	public static String parameter_info() {
		return "[fastaFileName] [inputfileName] [outputFastaFile] [orig_buffer] [outputFastaFile_missing]";
	}
	public static void execute(String[] args) {
		try {
			
			
			HashMap seq = new HashMap();
			
			String fastaFileName = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE.fasta";
			String inputfileName = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\TTest.txt";
			String outputFastaFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\extended.fasta";			
			int orig_buffer = new Integer(args[3]);
			String outputFastaFile_missing = args[4];
			
			FileInputStream fstream = new FileInputStream(fastaFileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String name = "";
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					//name = str.split("\\|")[1];
					name = str.split(" ")[0].replaceAll(">", "");
					//System.out.println(name);
					seq.put(name, "");
				} else {
					String orig = (String)seq.get(name);
					orig += str.trim();
					seq.put(name, orig);
				}
			}
			in.close();
			
			FileWriter fwriter_missing = new FileWriter(outputFastaFile_missing);
			BufferedWriter out_missing = new BufferedWriter(fwriter_missing);
			
			FileWriter fwriter = new FileWriter(outputFastaFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			int total = 0;
			
			fstream = new FileInputStream(inputfileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			int num = 0;
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				num++;
				//String[] geneNames = split[0].split(",");
				//for (String geneName: geneNames) {
				
				if (split[0].split(":").length >= 1) {
					String geneName = split[0].split(":")[0].replaceAll(">", "");;
					String pos = "";
					if (split[0].split(":").length > 1) {
						pos = split[0].split(":")[1];
					}
					
					if (str.contains(">")) {
						//geneName = str.split(" ")[0].split(":")[0].replaceAll(">", "");
					
						//String orig = split[0];
						//
						String orig = in.readLine();
						//String newSeq = split[3];
						String newSeq = orig.replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\#", "").replaceAll("\\*", "").replaceAll("\\@", "").replaceAll("\\%", ""); //
						//System.out.println(orig + "\t" + newSeq);
						
						
						String finalSeq = "";
						boolean found = false;
						System.out.println(geneName);
						if (seq.containsKey(geneName)) {
							String geneSeq = (String)seq.get(geneName);
	
							int count = 0;
							int index = 0;
							
							for (int i = 0; i < geneSeq.length() - newSeq.length() + 1; i++) {
								
								if (geneSeq.substring(i, i + newSeq.length()).equals(newSeq)) {
									count++;
									//System.out.println("found");
									index = i;
							
									if (count >= 1) {
										//if (index >= 5 && index + newSeq.length() < geneSeq.length() - 5) {
										//int buffer = 5;
										int buffer = orig_buffer;
										int end_buffer = buffer;
										if (index < buffer) {
											buffer = index;
										}
										
										/*System.out.println(newSeq);
										System.out.println(geneSeq);
										System.out.println(index);
										System.out.println(buffer);
										System.exit(0);*/
										
										if (geneSeq.length() - index - newSeq.length() < buffer) {
											end_buffer = geneSeq.length() - index - newSeq.length(); 
										}
										HashMap coord_map = new HashMap();
										//System.out.println(buffer + "\t" + end_buffer);
										
										if (geneSeq.length() < index + newSeq.length() + end_buffer) {
											end_buffer = end_buffer + (geneSeq.length() - (index + newSeq.length() + end_buffer));
										}
										if (end_buffer < 0) {
											end_buffer = 0;
										}
										String temp = geneSeq.substring(index - buffer, index + newSeq.length() + end_buffer);
										//System.out.println("Temp:" + temp);
										finalSeq = temp.substring(0, buffer);
										int oi = 0;
										for (int j = buffer; j < temp.length() - end_buffer; j++) {
											String p = orig.substring(oi, oi + 1);
											
											if (p.equals(".")) {
												oi++;
											} else {
												if (p.equals("*")) {
													String prev = orig.substring(oi - 1, oi);;
													finalSeq += "*";
													oi++;
													coord_map.put(index - buffer + j, prev + "*");	
												}
												if (p.equals("#")) {
													String prev = orig.substring(oi - 1, oi);;
													finalSeq += "#";
													oi++;
													coord_map.put(index - buffer + j, prev + "#");
												}
												if (p.equals("^")) {
													finalSeq += "^";
													String prev = orig.substring(oi - 1, oi);;
													coord_map.put(index - buffer + j, prev+ "^");
													oi++;
												}
												if (p.equals("%")) {
													finalSeq += "%";
													String prev = orig.substring(oi - 1, oi);;
													coord_map.put(index - buffer + j, prev + "%");
													oi++;
												}
												if (p.equals("@")) {
													finalSeq += "@";
													String prev = orig.substring(oi - 1, oi);
													coord_map.put(index - buffer + j, prev + "@");
													oi++;
												}
											}
											finalSeq += temp.substring(j, j + 1);
											oi++;
										}
										
										// this will not iterate through the last part of the peptide if it is a modified peptide
										// need to add the coord_map for the peptide if it is modified.
										int j = temp.length() - end_buffer;
										String p = orig.substring(orig.length() - 1, orig.length());
										if (p.equals("*")) {
											String prev = orig.substring(oi - 1, oi);;
											finalSeq += "*";
											oi++;
											coord_map.put(index - buffer + j, prev + "*");	
										}
										if (p.equals("#")) {
											String prev = orig.substring(oi - 1, oi);;
											finalSeq += "#";
											oi++;
											coord_map.put(index - buffer + j, prev + "#");
										}
										if (p.equals("^")) {
											finalSeq += "^";
											String prev = orig.substring(oi - 1, oi);;
											coord_map.put(index - buffer + j, prev+ "^");
											oi++;
										}
										if (p.equals("%")) {
											finalSeq += "%";
											String prev = orig.substring(oi - 1, oi);;
											coord_map.put(index - buffer + j, prev + "%");
											oi++;
										}
										if (p.equals("@")) {
											finalSeq += "@";
											String prev = orig.substring(oi - 1, oi);
											coord_map.put(index - buffer + j, prev + "@");
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
										found = true;
										out.write(">" + geneName + ":" + pos + "\t" + mod + "\n" + finalSeq + "\t" + orig + "\n");
										//System.out.println(num + "\tfound: " + geneName + "\t" + finalSeq + "\t" + orig + "\t" + geneSeq + "\t" + mod);
									}	
								}
							}
						
						}
						if (!found) {
							out_missing.write(">" + geneName + ":" + pos + "\t" + "NA" + "\n" + finalSeq + "\t" + orig + "\n");
							//out.write(">" + geneName + "\t-1\n" + seq + "\t" + seq + "\n");
						}
					} 
				} // if contains >
			} 
			in.close();
			out.close();
			out_missing.close();
			//System.out.println(total);
			//System.out.println("Buffer: " + orig_buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
