package stjude.projects.potter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * For Gang's SF3B1 dataset for Potts group
 * @author tshaw
 *
 */
public class PotterSummarizeCufflinksResultStringent {

	public static void execute(String[] args) {
		
		try {
			
			HashMap geneName = new HashMap();
			HashMap skipped = new HashMap();
			HashMap events = new HashMap();
			String inputFile = args[0];
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_Rh18\\junction\\SUD_vs_DMSO_diff_novel_junctions_upclassified.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));									
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String skipped_chr = split[0].split(":")[0];				
				geneName.put(split[7], split[7]);
				events.put(split[0],  split[7]);
				if (skipped.containsKey(skipped_chr)) {
					HashMap skipped_junction = (HashMap)skipped.get(skipped_chr);
					skipped_junction.put(str, str);
					skipped.put(skipped_chr, skipped_junction);					
				} else {
					HashMap skipped_junction = new HashMap();
					skipped_junction.put(str, str);
					skipped.put(skipped_chr, skipped_junction);
				}
			}
			in.close();						
	
			
			in.close();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_Rh18\\Cufflinks2\\summary_output_result.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Event\tGene");
			String[] files = {"SJRHB010470_C6", "SJRHB010470_C7", "SJRHB010470_C8", "SJRHB010470_C9", "SJRHB010470_C10", "SJRHB010470_C11"};

			LinkedList[] fileList = new LinkedList[files.length];
			
			int i = 0;
			for (String file: files) {
				out.write("\t" + file);
				fileList[i] = new LinkedList();
				inputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_Rh18\\Cufflinks2\\" + file + "_output_result.txt";
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));									
				while (in.ready()) {
					String str = in.readLine();
					fileList[i].add(str);
				}
				in.close();
				i++;
			}
			out.write("\n");
			
			Iterator itr = events.keySet().iterator();
			while (itr.hasNext()) {
				String event = (String)itr.next();	
				String gene = (String)events.get(event);
				out.write(event + "\t" + gene);
				i = 0;
				for (String file: files) {
					HashMap exons = new HashMap();
					String found = "NA";
					//String current_gene = "NA";
					String current_event = "NA";
					//inputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_Rh18\\Cufflinks2\\" + file + "\\transcripts.gtf";
					//inputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_Rh18\\Cufflinks2\\" + file + "_output_result.txt";
					//fstream = new FileInputStream(inputFile);
					//din = new DataInputStream(fstream);
					//in = new BufferedReader(new InputStreamReader(din));									
					//while (in.ready()) {
						//String str = in.readLine();
					Iterator itr2 = fileList[i].iterator();
					while (itr2.hasNext()) {
						String str = (String)itr2.next();
						if (!str.equals("")) {
							String[] split_str = str.split("\t");
							//System.out.println("Line: " + str);
							if (str.contains(">")) {							
								//if (gene.equals(split_str[7])) {
								//if (event.equals(split_str[0].replaceAll(">", ""))) {
									current_event = split_str[0].replaceAll(">", "");									
								//}
							} else {
								if (current_event.equals(event) && split_str[0].equals("TotalLength")) { 
									if (split_str[3].equals("true")) {
										System.out.println(file + "\t" + event + "\t" + split_str[0] + "\t" + split_str[3]);
										found = "true";
									} else {
										System.out.println(file + "\t" + current_event + "\t" + split_str[0] + "\t" + split_str[3]);
										if (found.equals("NA")) {
											found = "false";
										}
									}
								}
							}
							//if (current_event.equals(event) && split_str[0].equals("TotalLength") && split_str[3].equals("false")) {
							//	found = "false";
							//}
						} 
					}
					in.close();
					out.write("\t" + found);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
