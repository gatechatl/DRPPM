package microarray.tools.methylation.EPIC850K;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class EPIC850KBedGraph2BW {

	public static String description() {
		return "Convert bedGraph to BW file";
	}
	public static String type() {
		return "METHYLATION";
	}
	public static String parameter_info() {
		return "[sampleNameFile] [chromSizeFile] [outputAllShellFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String sampleNameFile = args[0];
			String chromSizeFile = args[1]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\MethylationEPIC_v-1-0_B4.csv";
			String outputAllShellFile = args[2];
			
			FileWriter fwriter_all = new FileWriter(outputAllShellFile);
			BufferedWriter out_all = new BufferedWriter(fwriter_all);
			
			LinkedList sampleName = new LinkedList();
			FileInputStream fstream = new FileInputStream(sampleNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				sampleName.add(str);
			}
			in.close();
			
			Iterator itr = sampleName.iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				FileWriter fwriter_header = new FileWriter(sample + ".header");
				BufferedWriter out_header = new BufferedWriter(fwriter_header);
				
				FileWriter fwriter = new FileWriter(sample + ".bed");
				BufferedWriter out = new BufferedWriter(fwriter);
				
				FileInputStream fstream2 = new FileInputStream(sample + ".bedgraph");
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				String header = in2.readLine();				
				while (in2.ready()) {
					String str = in2.readLine();
					out.write(str + "\n");
				}
				in2.close();
				out.close();
				out_header.write(header + "\n");
				out_header.close();				
				
				fwriter = new FileWriter(sample + ".sh");
				out = new BufferedWriter(fwriter);
				out.write("bedSort " + sample + ".bed " + sample + ".sort.bed\n");
				out.write("cat " + sample + ".header" + " " + sample + ".sort.bed > " + sample + ".sort.bedgraph\n");
				out.write("bedGraphToBigWig " + sample + ".sort.bedgraph " + chromSizeFile + " " + sample + ".bw\n");
				out.write("rm " + sample + ".bed " + sample + ".sort.bed " + sample + ".header " + sample + ".sort.bedgraph" + "\n");
				out.close();
				
				out_all.write("sh " + sample + ".sh\n");
			}
			
			out_all.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
