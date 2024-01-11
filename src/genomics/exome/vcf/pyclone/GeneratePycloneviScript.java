package genomics.exome.vcf.pyclone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class GeneratePycloneviScript {

	/*
	 *   1 pyclone-vi fit -i ../filtered_output_pyclone_input/TA33015_st_t_NA33016_st_gAF04_F1R21_F2R11_5reads.vcf.pyclone.tsv -o TA33015.h5 -c 40 -d beta-    binomial -r 10
  		 2 pyclone-vi write-results-file -i TA33015.h5 -o TA33015.tsv  
	 */
	
	public static void main(String[] args) {
		
		
		try {
			
			String outputFile = "/Users/4472414/Projects/ORIEN_AVATAR_MutationBurden/pyclonevi_run/complete_pyclone_script.sh";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			String path = "/Users/4472414/Projects/ORIEN_AVATAR_MutationBurden/filtered_output_pyclone_input";
			
			String output_path = "/Users/4472414/Projects/ORIEN_AVATAR_MutationBurden/pyclonevi_run";
			File folder = new File(path);
			for (File f: folder.listFiles()) {
				if (f.getPath().contains("pyclone.tsv")) {
					
					String fileName = f.getPath();
					String sampleName = f.getName().split("_st")[0];
					if (sampleName.substring(0, 1).equals("T")) {
						sampleName = sampleName.substring(1, sampleName.length());
					}
					out.write("pyclone-vi fit -i " + fileName + " -o " + output_path + "/" + sampleName + ".h5 -c 40 -d beta-binomial -r 10\n");
					out.write("pyclone-vi write-results-file -i " + output_path + "/" + sampleName + ".h5 -o " + output_path + "/" + sampleName + ".tsv \n");
					out.write("\n");
				}
			}
			out.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
