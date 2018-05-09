package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Read 
 * @author tshaw
 *
 */
public class McKinnonSummarizeGCScanning {

	public static String description() {
		return "Summarize GC content scanner";
	}
	public static String type() {
		return "MCKINNON";
	}
	public static String parameter_info() {
		return "[inputBEDFile] [exprMatrixFile] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBEDFile = args[0];
			String sampleExprFile = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			HashMap names = new HashMap();
			HashMap sample_expr = new HashMap();
			FileInputStream fstream = new FileInputStream(inputBEDFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[4];
				String chr = split[0];
				String start = split[1];
				String end = split[2];
				String dir = split[5];
				String tag = chr + ":" + start + "-" + end + "(" + dir + ")";
				names.put(geneName, geneName);				
			}
			in.close();			
			
			
			HashMap gene_gc_content = new HashMap();
			Iterator itr = names.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				double max_gc_percent = -1;
				double max_gc_skew = 0;
				double max_add_both = -1;
				File f = new File(geneName + "_1000.fasta.ScannerResult.txt");
				if (f.exists()) {
					fstream = new FileInputStream(geneName + "_1000.fasta.ScannerResult.txt");
					din = new DataInputStream(fstream);
					in = new BufferedReader(new InputStreamReader(din));
					in.readLine();
					//while (in.ready()) {
					for (int i = 0; i < 1000; i++) {
						String str = in.readLine();
						String[] split = str.split("\t");
						double gc_percent = new Double(split[1]);
						double gc_skew = new Double(split[2]);
						double gc_both = gc_percent + (Math.abs(gc_skew) + 1.0) / 2;
						if (max_gc_percent < gc_percent) {
							max_gc_percent = gc_percent;						
						}
						if (Math.abs(max_gc_skew) < Math.abs(gc_skew)) {
							max_gc_skew = gc_skew;
						}
						if (gc_percent > 0.7) {
							
							if (max_add_both < gc_both) {
								max_add_both = gc_both;
							}
						}
					}
						
					//}
					if (gene_gc_content.containsKey(geneName)) {
						String line = (String)gene_gc_content.get(geneName);
						String[] split_line = line.split("\t");
						double prev_gc_percent = new Double(split_line[0]);
						double prev_gc_skew = new Double(split_line[1]);
						double prev_gc_both = new Double(split_line[2]);
						if (prev_gc_both < max_add_both) {
							gene_gc_content.put(geneName, max_gc_percent + "\t" + max_gc_skew + "\t" + max_add_both);
						}
					} else {
						gene_gc_content.put(geneName, max_gc_percent + "\t" + max_gc_skew + "\t" + max_add_both);
					}
					
					in.close();
				}
			}
			
			fstream = new FileInputStream(sampleExprFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tGC_Percentage\tGC_Skew\tBothScoreAvg\n");
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (gene_gc_content.containsKey(split[0])) {
					String line = (String)gene_gc_content.get(split[0]);
					out.write(str + "\t" + line + "\n");
				} else {
					out.write(str + "\tNA\tNA\tNA\n");
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
