package stjude.projects.jinghuizhang.chipseq;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * To normalize the USP7 chip-seq annotation, we needed to calculate the length for each region.
 * @author tshaw
 *
 */
public class JinghuiZhangCalculateChipseqReferenceLength {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\USP7\\common\\USP7_Chipseq\\Homer_Annotation_RegionLength\\hg19.full.annotation.copy.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String title = split[0].split(" ")[0];
				if (title.contains("Intergenic") || title.equals("non-coding") || title.equals("exon") || title.equals("intron") || title.equals("3'") || title.equals("5'") || title.equals("TTS") || title.equals("promoter-TSS")) {
					if (title.contains("Intergenic")) {
						title = title.split("--")[0];
					}
					if (split.length > 3) {
						int length = new Integer(split[3]) - new Integer(split[2]) + 1;
						if (map.containsKey(title)) {
							double total = (Double)map.get(title);
							total += length;
							map.put(title, total);
						} else {
							double total = length;										
							map.put(title, total);
						}
					}
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				System.out.println(name + "\t" + map.get(name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
