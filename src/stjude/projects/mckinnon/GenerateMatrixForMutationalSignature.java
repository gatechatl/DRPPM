package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Generate the mutational signature input matrix from SNPDetect result
 * @author tshaw
 *
 */
public class GenerateMatrixForMutationalSignature {

	public static void main(String[] args) {
		
		String flank = "ACTTCCTTTAATAGCATCTC[G/A]TTTTCTGGTGGACCATCCCA";
		//String mid = test.split("\\[")[1].split("\\]")[0].replaceAll("\\/", ">");
		
		String mid = flank.split("\\[")[1].split("\\]")[0];		
		if (mid.length() == 3) {
			String swap1 = mid.substring(0, 1);
			String swap2 = mid.substring(2, 3);
			mid = swap1 + ">" + swap2;
			String firstpart = flank.split("\\[")[0];
			String secondpart = flank.split("\\[")[1].split("\\]")[1];
			String codon = firstpart.substring(firstpart.length() - 1, firstpart.length()) + swap1 + secondpart.substring(0, 1);
			
			
		System.out.println(mid);
		System.out.println(codon);
		}
	}
	
	
	public static String description() {
		return "Generate the mutational signature input matrix from SNPDetect result";
	}
	public static String type() {
		return "SNV";
	}
	public static String parameter_info() {
		return "[SnpDetectInput] [indexOfSampleName] [indexOfFlankingSeq]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String[] substitution = {"C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>A","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>G","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","C>T","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>A","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>C","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G","T>G"};
			String[] codons = {"ACA","ACC","ACG","ACT","CCA","CCC","CCG","CCT","GCA","GCC","GCG","GCT","TCA","TCC","TCG","TCT","ACA","ACC","ACG","ACT","CCA","CCC","CCG","CCT","GCA","GCC","GCG","GCT","TCA","TCC","TCG","TCT","ACA","ACC","ACG","ACT","CCA","CCC","CCG","CCT","GCA","GCC","GCG","GCT","TCA","TCC","TCG","TCT","ATA","ATC","ATG","ATT","CTA","CTC","CTG","CTT","GTA","GTC","GTG","GTT","TTA","TTC","TTG","TTT","ATA","ATC","ATG","ATT","CTA","CTC","CTG","CTT","GTA","GTC","GTG","GTT","TTA","TTC","TTG","TTT","ATA","ATC","ATG","ATT","CTA","CTC","CTG","CTT","GTA","GTC","GTG","GTT","TTA","TTC","TTG","TTT"};
			HashMap map = new HashMap();
			HashMap sampleNames = new HashMap();
			String inputFile = args[0];
			int sampleNameIndex = new Integer(args[1]);
			int flankingIndex = new Integer(args[2]);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[sampleNameIndex];
				sampleNames.put(sampleName, sampleName);
				String flank = split[flankingIndex];
				String mid = flank.split("\\[")[1].split("\\]")[0];		
				if (mid.length() == 3) {
					String swap1 = mid.substring(0, 1);
					String swap2 = mid.substring(2, 3);
					mid = swap1 + ">" + swap2;
					String firstpart = flank.split("\\[")[0];
					String secondpart = flank.split("\\[")[1].split("\\]")[1];
					String codon = firstpart.substring(firstpart.length() - 1, firstpart.length()) + swap1 + secondpart.substring(0, 1);
					
					String key = sampleName + "\t" + mid + "\t" + codon;
					if (map.containsKey(key)) {
						int count = (Integer)map.get(key);
						count++;
						map.put(key, count);
					} else {
						map.put(key, 1);
					}
					
					//if (sampleName.equals("SJHGG077_A") && mid.equals("C>A") && codon.equals("ACA")) {
					//	System.out.println(str);
					//}
				}
			}
			in.close();
			String sampleList = "";
			Iterator itr = sampleNames.keySet().iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				if (sampleList.equals("")) {
					sampleList += sample;
				} else {
					sampleList += "\t" + sample;
				}
			}
			System.out.println("PFEPD");
			System.out.println("C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>A	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>G	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	C>T	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>A	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>C	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G	T>G");
			System.out.println("ACA	ACC	ACG	ACT	CCA	CCC	CCG	CCT	GCA	GCC	GCG	GCT	TCA	TCC	TCG	TCT	ACA	ACC	ACG	ACT	CCA	CCC	CCG	CCT	GCA	GCC	GCG	GCT	TCA	TCC	TCG	TCT	ACA	ACC	ACG	ACT	CCA	CCC	CCG	CCT	GCA	GCC	GCG	GCT	TCA	TCC	TCG	TCT	ATA	ATC	ATG	ATT	CTA	CTC	CTG	CTT	GTA	GTC	GTG	GTT	TTA	TTC	TTG	TTT	ATA	ATC	ATG	ATT	CTA	CTC	CTG	CTT	GTA	GTC	GTG	GTT	TTA	TTC	TTG	TTT	ATA	ATC	ATG	ATT	CTA	CTC	CTG	CTT	GTA	GTC	GTG	GTT	TTA	TTC	TTG	TTT");
			System.out.println(sampleList);
			for (int i = 0; i < substitution.length; i++) {
				String mid = substitution[i];
				String codon = codons[i];
				String line = "";
				itr = sampleNames.keySet().iterator();
				while (itr.hasNext()) {
					String sample = (String)itr.next();
					String value = "0";
					if (map.containsKey(sample + "\t" + mid + "\t" + codon)) {
						value = (Integer)map.get(sample + "\t" + mid + "\t" + codon) + "";
					}
					if (line.equals("")) {
						line = value;
					} else {
						line += "\t" + value;
					}
				}
				System.out.println(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
