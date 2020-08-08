package stjude.projects.jinghuizhang.hla;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangMISCTotalLength {

	
	public static void main(String[] args) {
		
		try {
			double total = 0.0;
			boolean swap = false;
			HashMap coord_exon= new HashMap();
			
			String prev_geneName = "";
			String inputFile = "\\\\gsc.stjude.org\\resgen\\dev\\wc\\tshaw\\REFERENCE\\GTF\\human\\hg19\\gencode.v11.annotation.gtf";
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String chr = split[0];
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					String meta = split[8];
					//System.out.println(meta);
					String biotype = GTFFile.grabMeta(meta, "gene_type");
					String geneName = GTFFile.grabMeta(meta, "gene_name");
					
					if (split[2].equals("exon") && biotype.equals("protein_coding")) {
						if (!coord_exon.containsKey(chr + "\t" + start) && !coord_exon.containsKey(chr + "\t" + end)) {
							total += (end - start);
						}
						//coord_exon.put(chr + "\t" + start, "");
						//coord_exon.put(chr + "\t" + end, "");
					}					
				}
			}
			in.close();
			System.out.println(total);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
