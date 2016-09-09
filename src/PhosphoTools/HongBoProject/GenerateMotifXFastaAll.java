package PhosphoTools.HongBoProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import PhosphoTools.MotifTools.MotifTools;

public class GenerateMotifXFastaAll {

	public static String parameter_info() {
		return "[inputFile] [peptide_index] [accession_index] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap mapS = new HashMap();
			HashMap mapT = new HashMap();
			HashMap mapY = new HashMap();
			LinkedList clusters = new LinkedList();
			String inputFile = args[0];
			int pep_index = new Integer(args[1]);
			int accession_index = new Integer(args[2]);
			String outputFolder = args[3];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = "Background";
				String accession = split[accession_index];
				if (!clusters.contains(type)) {
					clusters.add(type);
				}
				String seq = split[pep_index];
				seq = seq.replaceAll("\\.", "");
				if (seq.contains("S@")) {
					if (mapS.containsKey(type)) {		
						LinkedList list = (LinkedList)mapS.get(type);
						HashMap fasta = new HashMap();
						fasta.put(seq, accession); 
						if (!list.contains(fasta)) {
							list.add(fasta);
						}
						mapS.put(type, list);
					} else {
						LinkedList list = new LinkedList();
						HashMap fasta = new HashMap();
						fasta.put(seq, accession); 
						if (!list.contains(fasta)) {
							list.add(fasta);
						}
						list.add(fasta);
						mapS.put(type,  list);
					}
				} else if (seq.contains("T%")) {
					if (mapT.containsKey(type)) {				
						LinkedList list = (LinkedList)mapT.get(type);
						HashMap fasta = new HashMap();
						fasta.put(seq, accession); 
						if (!list.contains(fasta)) {
							list.add(fasta);
						}
						mapT.put(type,  list);						
					} else {
						LinkedList list = new LinkedList();
						HashMap fasta = new HashMap();
						fasta.put(seq, accession); 
						if (!list.contains(fasta)) {
							list.add(fasta);
						}
						
						mapT.put(type,  list);
					}
				} else if (seq.contains("Y*")) {
					if (mapY.containsKey(type)) {				
						LinkedList list = (LinkedList)mapY.get(type);
						HashMap fasta = new HashMap();
						fasta.put(seq, accession); 
						if (!list.contains(fasta)) {
							list.add(fasta);
						}
						mapY.put(type,  list);						
					} else {
						LinkedList list = new LinkedList();
						HashMap fasta = new HashMap();
						fasta.put(seq, accession); 
						if (!list.contains(fasta)) {
							list.add(fasta);
						}
						
						mapY.put(type,  list);
					}
				}
				
			}
			in.close();
			Iterator itr = clusters.iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				
				
				FileWriter fwriterS = new FileWriter(outputFolder + "/S_" + type + ".fasta");
				BufferedWriter outS = new BufferedWriter(fwriterS);		
				
				if (mapS.containsKey(type)) {
					int index = 0;
					LinkedList list = (LinkedList)mapS.get(type);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						HashMap map = (HashMap)itr2.next();
						Iterator itr3 = map.keySet().iterator();
						while (itr3.hasNext()) {
							String seq = (String)itr3.next();
							String accession = (String)map.get(seq);
							LinkedList list2 = MotifTools.convert(MotifTools.replaceTag(seq));
							Iterator itr4 = list2.iterator();
							while (itr4.hasNext()) {
								String seq2 = (String)itr4.next();
								outS.write(">" + accession + "\n");
								outS.write(seq2 + "\n");
							}
							//index++;
						}
					}
				}
				outS.close();
				
				FileWriter fwriterT = new FileWriter(outputFolder + "/T_" + type + ".fasta");
				BufferedWriter outT = new BufferedWriter(fwriterT);		
				
				if (mapT.containsKey(type)) {
					int index = 0;
					LinkedList list = (LinkedList)mapT.get(type);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						HashMap map = (HashMap)itr2.next();
						Iterator itr3 = map.keySet().iterator();
						while (itr3.hasNext()) {
							String seq = (String)itr3.next();
							String accession = (String)map.get(seq);
							LinkedList list2 = MotifTools.convert(MotifTools.replaceTag(seq));
							Iterator itr4 = list2.iterator();
							while (itr4.hasNext()) {
								String seq2 = (String)itr4.next();
								outT.write(">" + accession + "\n");
								outT.write(seq2 + "\n");
							}
							//index++;
						}
					}
				}
				outT.close();
				
				FileWriter fwriterY = new FileWriter(outputFolder + "/Y_" + type + ".fasta");
				BufferedWriter outY = new BufferedWriter(fwriterY);		
				
				if (mapY.containsKey(type)) {
					int index = 0;
					LinkedList list = (LinkedList)mapY.get(type);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						HashMap map = (HashMap)itr2.next();
						Iterator itr3 = map.keySet().iterator();
						while (itr3.hasNext()) {
							String seq = (String)itr3.next();
							String accession = (String)map.get(seq);
							LinkedList list2 = MotifTools.convert(MotifTools.replaceTag(seq));
							Iterator itr4 = list2.iterator();
							while (itr4.hasNext()) {
								String seq2 = (String)itr4.next();
								outY.write(">" + accession + "\n");
								outY.write(seq2 + "\n");
							}
							//index++;
						}
					}
				}
				outY.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
