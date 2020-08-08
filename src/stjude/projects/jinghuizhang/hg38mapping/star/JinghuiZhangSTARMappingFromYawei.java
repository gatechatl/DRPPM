package stjude.projects.jinghuizhang.hg38mapping.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import misc.CommandLine;


/**
 * The parameter was from yawei.
 * Example script: /scratch_space/tshaw/TARGET_STAR_Mapping/star_mapping
 * @author tshaw
 *
 */
public class JinghuiZhangSTARMappingFromYawei {

	public static String description() {
		return "Generate star mapping based on Yawei's script";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputLstFile] [star_index_dir] [num_thread] [outputFile] [gzip true false]";
	}
	public static void execute(String[] args) {
		try {
			String inputLstFile = args[0];
			String star_index_dir = args[1];
			String num_thread = args[2];
			String outputScript = args[3];
			boolean gzip_flag = false;
			if (args.length > 4) {
				gzip_flag = new Boolean(args[4]);
			} 
			FileWriter fwriter = new FileWriter(outputScript);
			BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(inputLstFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String script = generate_star_script(star_index_dir, split[0], split[1], split[2], num_thread, gzip_flag);
				CommandLine.writeFile(split[2] + ".sh", script);
				out.write("sh " + split[2] + ".sh\n");
			}
			in.close();
			out.close();
			//String header = in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generate_star_script(String genomeDir, String inputR1, String inputR2, String prefix, String thread, boolean gzip_flag) {
		String line = "";
		line += "mkdir " + prefix + " \n";
		line += "cd " + prefix + " \n";
		line += "\n";
		line += "STAR \\\n";
		line += "     --runThreadN " + thread + " \\\n";
		line += "     --genomeDir " + genomeDir + " \\\n";
		line += "     --readFilesIn " + inputR1 + " " + inputR2 + " \\\n";
		line += "     --outFilterType BySJout \\\n";
		line += "     --outFilterMultimapNmax 20 \\\n";
		line += "     --alignSJoverhangMin 8 \\\n";
		line += "     --alignSJstitchMismatchNmax 5 -1 5 5 \\\n";
		line += "     --alignSJDBoverhangMin 10 \\\n";
		line += "     --outFilterMismatchNmax 999 \\\n";
		line += "     --outFilterMismatchNoverReadLmax 0.04 \\\n";
		line += "     --alignIntronMin 20 \\\n";
		line += "     --alignIntronMax 100000 \\\n";
		line += "     --alignMatesGapMax 100000 \\\n";
		line += "     --genomeLoad NoSharedMemory \\\n";
		line += "     --outFileNamePrefix " + prefix + ".STAR. \\\n";
		line += "     --outSAMmapqUnique 60 \\\n";
		line += "     --outSAMmultNmax 1 \\\n";
		line += "     --outSAMstrandField intronMotif \\\n";
		line += "     --outSAMtype BAM SortedByCoordinate \\\n";
		line += "     --outReadsUnmapped None \\\n";
		line += "     --outSAMattrRGline ID:${RG_ID} LB:${RG_LB} PL:${RG_PL} SM:${RG_SM} PU:${RG_PU} \\\n";
		line += "     --outSAMattributes NH HI AS nM MD \\\n";
		line += "     --chimSegmentMin 12 \\\n";
		line += "     --chimJunctionOverhangMin 12 \\\n";
		line += "     --chimSegmentReadGapMax 3 \\\n";
		line += "     --chimMultimapNmax 10 \\\n";
		line += "     --chimMultimapScoreRange 10 \\\n";
		line += "     --chimNonchimScoreDropMin 10 \\\n";
		line += "     --chimOutJunctionFormat 1 \\\n";
		line += "     --quantMode TranscriptomeSAM GeneCounts \\\n";
		line += "     --twopassMode Basic \\\n";
		line += "     --peOverlapNbasesMin 12 \\\n";
		line += "     --peOverlapMMp 0.1 \\\n";
		line += "     --outWigType wiggle \\\n";
		line += "     --outWigStrand Stranded \\\n";
		line += "     --outWigNorm RPM \\\n";
		line += "     --limitBAMsortRAM 100672447591\\\n";
		if (gzip_flag) {
			line += "     --readFilesCommand zcat\\\n";
		}
		return line;
	}
}
