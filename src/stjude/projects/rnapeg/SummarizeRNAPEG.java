package stjude.projects.rnapeg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class SummarizeRNAPEG {

	public static String description() {
		return "Summarize RNApeg";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[fileList: two column(sample, path to sample)] [minCount: 5] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			double min_count_cutoff = new Double(args[1]);
			String outputFile = args[2];
			HashMap value = new HashMap();
			HashMap junctions = new HashMap();
			HashMap geneName = new HashMap();
			HashMap transcripts = new HashMap();
			HashMap known_novel = new HashMap();
			HashMap sample_geneName = new HashMap();
			HashMap sample_total = new HashMap();
			LinkedList sampleName = new LinkedList(); 

			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sampleName.add(split[0]);
				HashMap calculateMedian = new HashMap();
				double total = 0;
				FileInputStream fstream2 = new FileInputStream(split[1]);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				String header = in2.readLine();
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					junctions.put(split2[0], split2[0]);
					known_novel.put(split2[0], split2[2]);
					geneName.put(split2[0], split2[3]);
					transcripts.put(split2[0], split2[4]);
					value.put(split2[0] + "\t" + split[0], new Double(split2[1]));
					total += new Integer(split2[1]);
					if (calculateMedian.containsKey(split2[3])) {
						LinkedList list = (LinkedList)calculateMedian.get(split2[3]);
						list.add(split2[1]);
						calculateMedian.put(split2[3], list);
					} else {
						LinkedList list = new LinkedList();
						list.add(split2[1]);
						calculateMedian.put(split2[3], list);
					}
					sample_total.put(split[0], total);
				}
				in2.close();
				
				Iterator itr = calculateMedian.keySet().iterator();
				while (itr.hasNext()) {
					String geneName_str = (String)itr.next();
					LinkedList list = (LinkedList)calculateMedian.get(geneName_str);
					double[] all = MathTools.convertListStr2Double(list);
					double median = MathTools.median(all);
					sample_geneName.put(geneName_str + "\t" + split[0], median);
				}
			}
			in.close();			
			out.write("Name\tType\tGenes\tTranscript");
			Iterator itr = sampleName.iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				out.write("\t" + sample + "_Median\t" + sample + "_Count");
			}
			out.write("\n");
			Iterator itr2 = junctions.keySet().iterator();
			while (itr2.hasNext()) {
				String junction = (String)itr2.next();
				String gene = (String)geneName.get(junction);
				String known = (String)known_novel.get(junction);
				String transcript = (String)transcripts.get(junction);
				
				
				boolean skip = false;
				double average_count = 0.0;
				itr = sampleName.iterator();
				while (itr.hasNext()) {
					String sample = (String)itr.next();
					double total = (Double)sample_total.get(sample);
					double median = Double.NaN;
					
					if (sample_geneName.containsKey(gene + "\t" + sample)) {
						median = (Double)sample_geneName.get(gene + "\t" + sample);
					} else {
						skip = true;
					}
					if (value.containsKey(junction + "\t" + sample)) {
						double val = (Double)value.get(junction + "\t" + sample);
						average_count += val / sample_total.size();
					} 
					
				}
				if (average_count < min_count_cutoff) {
					skip = true;
				}
				// check if all the values satisfy minimum cutoff
				if (!skip) {
					out.write(junction + "\t" + known + "\t" + gene + "\t" + transcript);
					
					itr = sampleName.iterator();
					while (itr.hasNext()) {
						String sample = (String)itr.next();
						double total = (Double)sample_total.get(sample);
						double median = Double.NaN;
						if (sample_geneName.containsKey(gene + "\t" + sample)) {
							median = (Double)sample_geneName.get(gene + "\t" + sample);
						}
						if (value.containsKey(junction + "\t" + sample)) {
							double val = (Double)value.get(junction + "\t" + sample);
							out.write("\t" + median * 100000 / total + "\t" + val * 100000 / total);
						} else {
							out.write("\t" + median * 100000 / total + "\t0.0");
						}
					}
					out.write("\n");
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
