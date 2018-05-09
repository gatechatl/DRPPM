package stjude.tools.rnaseq;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import misc.CommandLine;

/**
 * A temporary emergency mapping pipeline for RNAseq mapping
 * @author tshaw
 *
 */
public class RNASEQConfig2MappingScriptGenerator {

	public static String type() {
		return "STJUDERNASEQ";
	}
	public static String description() {
		return "A temporary emergency mapping pipeline for RNAseq mapping";
	}
	public static String parameter_info() {
		return "[inputFile] [organism (hg19/hg38/mm9/mm10/xenograft/dm3)]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String organism = args[1];
			//String step1_copy_file_script = copy_file_script(inputFile);
			String step1_softlink_script = softlink_script(inputFile);
			//CommandLine.writeFile("step1_copy_file_script.sh", step1_copy_file_script);
			CommandLine.writeFile("step1_softlink_script.sh", step1_softlink_script);
			//String step2_gunzipFiles = gunzipFiles(inputFile);
			//CommandLine.writeFile("step2_gunzipFiles.sh", step2_gunzipFiles);
			String step3_combineFiles = combineFiles();
			CommandLine.writeFile("step2_combineFiles.sh", step3_combineFiles);
			String step4_submitSTARMappingScript = submitSTARMappingScript(organism);
			CommandLine.writeFile("step3_submitSTARMappingScript.sh", step4_submitSTARMappingScript);
			String step5_mergeBamFileScript = mergeBamFileScript();
			CommandLine.writeFile("step4_mergeBamFileScript.sh", step5_mergeBamFileScript);
			String step6_cleanup = cleanUp();
			CommandLine.writeFile("step5_cleanup.sh", step6_cleanup);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String cleanUp() {
		String script = "#rm STAR*\n";
		script += "#rm sh*\n";
		script += "#rm *.out\n";
		script += "#rm *.fastq\n";
		script += "echo \"*** Note that the script is commented out to prevent files being accidentally deleted. Please run the clean up script manually (check before you delete)...\"\n";
		return script;
	}
	public static String mergeBamFileScript() {
		String currentPath = "";
		String script = "";
		try {
			
			currentPath = new java.io.File( "." ).getCanonicalPath();
			script += "/rgs01/resgen/dev/wc/tshaw/DRPPM/drppm -MergeBamFiles " + currentPath + "/ 1,2 > mergeBamFiles.sh\n";
			script += "/research/rgs01/staging/apps/internal/rnaseq/tshaw/RNASEQ_Tools/shellscripts/bsub_array_for_cmdfile.sh mergeBamFiles.sh -M 32000\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return script;
	}
	public static String submitSTARMappingScript(String organism) {
		String fastaPath = "";
		//String gtfFile = "";
		if (organism.equals("mm9")) {
			fastaPath = "/research/rgs01/staging/reference/Mus_musculus/mm9/STAR/";
			//gtfFile = "/research/rgs01/staging/reference/Mus_musculus/mm9/STAR/Mus_musculus.NCBIM37.67.gtf";
			
		} else if (organism.equals("mm10")) {
			fastaPath = "/research/rgs01/staging/reference/Mus_musculus/Mm10/STAR";
			//gtfFile = "/research/rgs01/staging/reference/Mus_musculus/Mm10/STAR/gencode.vM5.primary_assembly.annotation.gtf";
			
		} else if (organism.equals("hg19")) {
			fastaPath = "/research/rgs01/staging/reference/Homo_sapiens/GRCh37-lite/STAR/GRCh37p13_GenCode19/";
			//gtfFile = "/research/rgs01/staging/reference/Homo_sapiens/GRCh37-lite/STAR/GRCh37p13_GenCode19/gencode.v19.annotation_for_cufflink_ercc.gtf";
			
		} else if (organism.equals("hg38")) {
			fastaPath = "/research/rgs01/staging/reference/Homo_sapiens/GRCh38_no_alt/STAR/";
			//gtfFile = "/research/rgs01/staging/reference/Homo_sapiens/GRCh38_no_alt/ANNOTATIONS/gencode/gencode.v24.annotation.gtf";
		} else if (organism.equals("xenograft")) {
			//fastaPath = "/nfs_exports/apps/internal/rnaseq/tshaw/XenoGraphReference/STAR/star-genome";
			fastaPath = "/research/rgs01/staging/apps/internal/rnaseq/tshaw/XenoGraphReference/STARwithGTF/star-genome-withRef/";
			//gtfFile = "/research/rgs01/staging/apps/internal/rnaseq/tshaw/XenoGraphReference/STARwithGTF/gencode.v19.annotation_level1and2_withChrMT_withChr_Mus_musculus.NCBIM37.67-novague_withChr.gtf";
		} else if (organism.equals("dm3")) {
			fastaPath = "/research/rgs01/staging/reference/Drosophila_melanogaster/dm3/STAR/Genome_dm3/";
		} else if (organism.equals("BDGPr5")) {
			fastaPath = " /research/rgs01/resgen/prod/tartan/index/reference/Drosophila_melanogaster/BDGPr5/STAR/";
		}
		//String script = "drppm -STARMappingScriptGenerator input.txt /rgs01/resgen/dev/wc/tshaw/pipeline_examples/RNASEQ/Tools/bin/STAR " + fastaPath + " > mapping_script.sh\n";
		String script = "/rgs01/resgen/dev/wc/tshaw/DRPPM/drppm -STARMappingScriptGenerator input.txt STAR " + fastaPath + " > mapping_script.sh\n";
		script += "/rgs01/resgen/dev/wc/tshaw/Tools/bsub_array_for_cmdfile.sh mapping_script.sh -M 32000\n";
		return script;
	}
	public static String combineFiles() {
		//String script = "ls *.fastq > fastq.lst\n";
		String script = "ls *.gz > gz.lst\n";
		script += "/rgs01/resgen/dev/wc/tshaw/DRPPM/drppm -Fastq2FileList gz.lst input.txt\n";
		return script;
	}
	public static String gunzipFiles(String inputFile) {
		String script = "";
		try {
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("file")) {
					map.put(split[0], split[0]);
				}				
			}
			in.close();			
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String file = (String)itr.next();
				File f = new File(file);
				
				script += "gunzip " + f.getName() + " -c > " + f.getName().substring(0, f.getName().length() - 3) + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return script;
	}
		
	public static String copy_file_script(String inputFile) {
		String script = "";
		try {
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("file")) {
					map.put(split[0], split[0]);
				}
			}
			in.close();			
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String file = (String)itr.next();
				script += "cp " + file + " .\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return script;
	}
	
	public static String softlink_script(String inputFile) {
		String script = "";
		try {
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("file")) {
					map.put(split[0], split[8]);
				}
			}
			in.close();			
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String file = (String)itr.next();
				File f = new File(file);
				String sampleName = (String)map.get(file);
				script += "ln -s " + file + " " + sampleName + "_" + f.getName() + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return script;
	}
}
