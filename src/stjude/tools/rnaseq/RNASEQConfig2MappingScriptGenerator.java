package stjude.tools.rnaseq;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import MISC.CommandLine;

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
		return "[inputFile] [organism (hg19/mm9/xenograph/dm3)]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String organism = args[1];
			String step1_copy_file_script = copy_file_script(inputFile);
			CommandLine.writeFile("step1_copy_file_script.sh", step1_copy_file_script);
			String step2_gunzipFiles = gunzipFiles(inputFile);
			CommandLine.writeFile("step2_gunzipFiles.sh", step2_gunzipFiles);
			String step3_combineFiles = combineFiles();
			CommandLine.writeFile("step3_combineFiles.sh", step3_combineFiles);
			String step4_submitSTARMappingScript = submitSTARMappingScript(organism);
			CommandLine.writeFile("step4_submitSTARMappingScript.sh", step4_submitSTARMappingScript);
			String step5_mergeBamFileScript = mergeBamFileScript();
			CommandLine.writeFile("step5_mergeBamFileScript.sh", step5_mergeBamFileScript);
			String step6_cleanup = cleanUp();
			CommandLine.writeFile("step6_cleanup.sh", step6_cleanup);
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
			script += "drppm -MergeBamFiles " + currentPath + "/ 1,2 > mergeBamFiles.sh\n";
			script += "/nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/shellscripts/bsub_array_for_cmdfile.sh mergeBamFiles.sh -M 32000\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return script;
	}
	public static String submitSTARMappingScript(String organism) {
		String fastaPath = "";
		if (organism.equals("mm9")) {
			fastaPath = "/nfs_exports/genomes/1/Mus_musculus/mm9/STAR/";
		} else if (organism.equals("hg19")) {
			fastaPath = "/nfs_exports/genomes/1/Homo_sapiens/GRCh37-lite/STAR/GRCh37p13_GenCode19/";
		} else if (organism.equals("xenograph")) {
			//fastaPath = "/nfs_exports/apps/internal/rnaseq/tshaw/XenoGraphReference/STAR/star-genome";
			fastaPath = "/nfs_exports/apps/internal/rnaseq/tshaw/XenoGraphReference/STARwithGTF/star-genome-withRef/";
		} else if (organism.equals("dm3")) {
			fastaPath = "/nfs_exports/genomes/1/Drosophila_melanogaster/dm3/STAR/Genome_dm3/";
		}
		String script = "drppm -STARMappingScriptGenerator input.txt /nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/bin/STAR " + fastaPath + " > mapping_script.sh\n";
		script += "/nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/shellscripts/bsub_array_for_cmdfile.sh mapping_script.sh -M 32000\n";
		return script;
	}
	public static String combineFiles() {
		String script = "ls *.fastq > fastq.lst\n";
		script += "drppm -Fastq2FileList fastq.lst input.txt\n";
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
				
				script += "gunzip " + f.getName() + "\n";
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
}
