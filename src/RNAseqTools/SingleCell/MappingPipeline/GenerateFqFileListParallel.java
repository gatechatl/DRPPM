package RNAseqTools.SingleCell.MappingPipeline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A specialized class for generating a three column file R1fastq,R2fastq, SampleName
 * @author tshaw
 *
 */
public class GenerateFqFileListParallel {

	public static String type() {
		return "MAPPING";
	}
	public static String description() {
		return "A specialized class for generating a three column file for PolyA Extraction";
	}
	public static String parameter_info() {
		return "[filePath]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String filePath = args[0];
			File file = new File(filePath);
			HashMap map = new HashMap();
			HashMap all_map = new HashMap();
			
			for (File f: file.listFiles()) {
				String fileName = f.getName();
				if (fileName.contains("fastq")) {
					String[] split = fileName.split("_");
					String trim_fileName = "";
					for (int i = 0; i < split.length - 2; i++) {
						if (trim_fileName.equals("")) {
							trim_fileName = split[i];
						} else {
							trim_fileName += "_" + split[i];
						}
					}
					all_map.put(fileName, fileName);
					map.put(trim_fileName, split[0]);
				}
			}
						
			Iterator itr = map.keySet().iterator();			
			while (itr.hasNext()) {
				String trim_fileName = (String)itr.next();
				String name = (String)map.get(trim_fileName);
				String r1 = "";
				String r2 = "";
				Iterator itr2 = all_map.keySet().iterator();				
				while (itr2.hasNext()) {
					String file2 = (String)itr2.next();
					if (file2.contains(trim_fileName) && file2.contains("R1")) {
						r1 = file2;
					}
					if (file2.contains(trim_fileName) && file2.contains("R2")) {
						r2 = file2;
					}
				}
				String lstOutputFile = name + ".lst";
				FileWriter fwriter = new FileWriter(lstOutputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write(filePath + "/" + r1 + "\t" + filePath + "/" + r2 + "\t" + name + "\n");
				out.close();
				System.out.println("drppm -ExtractPolyAReadsUsePolyALibrarySingleCell " + lstOutputFile);
				
				//System.out.println(filePath + "/" + r1 + "\t" + filePath + "/" + r2 + "\t" + name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
