package stjude.projects.jinghuizhang.dexseq.exon.annotation.pcgptarget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Program used for Figure 2B
 * @author gatechatl
 *
 */
public class JinghuiZhangCheckTissueTypeCategory {

	public static void main(String[] args) {
		
		try {
			
			String outputFile = "/home/gatechatl/CSI-Miner/NewCandidateScore/Output_20210909.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			String inputFile = "/home/gatechatl/CSI-Miner/NewCandidateScore/Exon_TissueType_20210909.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tCategory\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String category = "NA";
				
				if (split.length >= 2) {
					System.out.println(str);
					String tissues = split[1];
					String[] split_tissues = tissues.split(",");
					boolean brain = false;
					boolean solid = false;
					//EWS,ACT,MB,EPD,ST,DSRCT,BT,OS,MEL,HGG,NBL,WLM,RHB,LGG,
					for (String tissue: split_tissues) {
						if (tissue.equals("ST") || tissue.equals("RB") || tissue.equals("EWS") || tissue.equals("ACT") || tissue.equals("DSRCT") || tissue.equals("OS") || tissue.equals("NBL") || tissue.equals("RHB") || tissue.equals("WLM") || tissue.equals("MB")) {
							solid = true;
						}
						if (tissue.equals("HGG") || tissue.equals("EPD") || tissue.equals("MB") || tissue.equals("CPC") || tissue.equals("LGG") || tissue.equals("BT")) {
							brain = true;
						}
					}
					
					if (solid && brain) {
						category = "BOTH";
					} else if (solid) {
						category = "SOLID";
					} else if (brain) {
						category = "BRAIN";
					}
				}
				out.write(str + "\t" + category + "\n");
				
			}
			
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
