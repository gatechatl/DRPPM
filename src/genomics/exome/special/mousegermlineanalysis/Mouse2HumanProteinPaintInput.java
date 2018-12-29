package genomics.exome.special.mousegermlineanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Convert mouse to human
 * Generate protein paint input file
 * 
 * @author tshaw
 *
 */
public class Mouse2HumanProteinPaintInput {

	public static String parameter_info() {
		return "[mouse2humanFile] [snv_final] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			
			HashMap mouse2human_refseq = new HashMap();
			HashMap mouse2human_geneName = new HashMap();
			String mouse2humanFile = args[0];
			String snv_final = args[1];
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			
			FileInputStream fstream = new FileInputStream(mouse2humanFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String human_geneName = split[0];
				String human_refseq = split[3].split("\\.")[0];
				String mouse_geneName = split[5];
				String mouse_refseq = split[8].split("\\.")[0];				
				mouse2human_refseq.put(mouse_refseq, human_refseq);
				mouse2human_geneName.put(mouse_geneName, human_geneName);
			}
			in.close();
			
			System.out.println("gene\trefseq\tchromosome\tstart\taachange\tclass\tsample");
			out.write("gene\trefseq\tchromosome\tstart\taachange\tclass\tsample\n");
			fstream = new FileInputStream(snv_final);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].equals("SJHQ")) {
					String geneName = split[0];
					String sampleName = split[2];
					String human_geneName = (String)mouse2human_geneName.get(geneName);
					String refseq = split[8];
					String human_refseq = (String)mouse2human_refseq.get(refseq);
					String mutation = split[6];
					String muttype = split[5];
					
					String human_mutation = mutation.split("\\.")[1].split("_")[0].split(",")[0];
					//String chr = mutation.split("\\.")[1].split("_")[1].split(":")[0];
					//String location = "NA";
					
					// new modification //
					String chr = "chr" + split[3];
					String location = split[4];
					/////////////////////
					/*if (mutation.split("\\.")[1].contains(":")) {
						location = mutation.split("\\.")[1].split(":")[1];
					}*/
					if (human_geneName != null && !location.equals("NA") && !human_mutation.equals("NA")) {
						System.out.println(human_geneName + "\t" + human_refseq + "\t" + chr + "\t" + location + "\t" + human_mutation + "\t" + muttype + "\t" + sampleName);
						out.write(human_geneName + "\t" + human_refseq + "\t" + chr + "\t" + location + "\t" + human_mutation + "\t" + muttype + "\t" + sampleName + "\n");
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
