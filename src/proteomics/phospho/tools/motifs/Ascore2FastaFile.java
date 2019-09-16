package proteomics.phospho.tools.motifs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Ascore2FastaFile {

	public static void main(String[] args) {
		String test = "MIANSLNHDS#PPY*HTPT%RR";
		LinkedList list = MotifTools.convert(MotifTools.replaceTag(test.trim()));
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String seq = (String)itr.next();
			System.out.println(seq);
		}
	}
	public static void execute(String[] args) {
		
		try {
			
			String fastaFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE.fasta";
			int name_index = new Integer(args[1]);
			int peptide_index = new Integer(args[2]);
			int pvalue_index = new Integer(args[3]);
			double cutoff = new Double(args[4]);
			int logChange_index = new Integer(args[5]);
			double logChangeCutOff = new Double(args[6]);
			String outputFileUP = args[7];
			String outputFileDN = args[8];
			String outputFileAll = args[9];
			FileWriter fwriterUP = new FileWriter(outputFileUP);
			BufferedWriter outUP = new BufferedWriter(fwriterUP);
			
			FileWriter fwriterDN = new FileWriter(outputFileDN);
			BufferedWriter outDN = new BufferedWriter(fwriterDN);
			
			FileWriter fwriterAll = new FileWriter(outputFileAll);
			BufferedWriter outAll = new BufferedWriter(fwriterAll);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine(); // skip header
			int count = 1;
			String name = "";
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double pvalue = new Double(split[pvalue_index]);
				
				if (pvalue <= cutoff) {
					LinkedList list = MotifTools.convert(MotifTools.replaceTag(split[peptide_index].trim()));
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String seq = (String)itr.next();
						
						double logFC = new Double(split[logChange_index]);
						if (logFC >= logChangeCutOff) {
							outUP.write(">" + split[name_index] + "\n" + seq + "\n");
						} else if (logFC <= -logChangeCutOff) {
							outDN.write(">" + split[name_index] + "\n" + seq + "\n");
						}
						outAll.write(">" + split[name_index] + "\n" + seq + "\n");
						count++;
					}
				} else {
					LinkedList list = MotifTools.convert(MotifTools.replaceTag(split[peptide_index].trim()));
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String seq = (String)itr.next();						
						double logFC = new Double(split[logChange_index]);
						outAll.write(">" + split[name_index] + "\n" + seq + "\n");						
						count++;
					}
				}
			}
			in.close();
			outUP.close();
			outDN.close();
			outAll.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
