package stjude.projects.jinghuizhang.usp7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangMergeDownSampledUSP7PeaksToOther6Peaks {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\PanTARGET\\zhanggrp\\ltian\\ForTim\\USP7_project\\beisipipeline\\downsamplingUSP7\\Bam\\MACS2\\USP7_sub.sorted_peaks.narrowPeak";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\Manuscript_Figure_Pipeline\\chip_seq_figure_5\\Updated_DownSampled_USP7_Annotated_20200331.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(str, str);
				
			}
			in.close();
			
			int count = 0;
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\Manuscript_Figure_Pipeline\\chip_seq_figure_5\\H3K27ac_TAL1_TCF3_TCF12_GATA3_Merged_Peaks_Coordinates.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\t" + "USP7_Downsample_Hit\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String merge6_chr = split[0];
				int merge6_start = new Integer(split[1]);
				int merge6_end = new Integer(split[2]);
				boolean hit = false;
				Iterator itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					String[] split_line = line.split("\t");
					int downsampling_start = new Integer(split_line[1]);
					int downsampling_end = new Integer(split_line[2]);
					if (split[0].equals(split_line[0])) {
						if (overlap(merge6_start, merge6_end, downsampling_start, downsampling_end)) {
							hit = true;
							break;
						}
					}					
				}
				out.write(str + "\t" + hit + "\n");
				
				count++;
				System.out.println(count);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean overlap(int a1, int a2, int b1, int b2) {
		if (a1 <= b1 && b1 <= a2) {
			return true;
		}
		if (a1 <= b2 && b2 <= a2) {
			return true;
		}
		if (b1 <= a1 && a1 <= b2) {
			return true;
		}
		if (b1 <= a2 && a2 <= b2) {
			return true;
		}
		return false;
	}
}
