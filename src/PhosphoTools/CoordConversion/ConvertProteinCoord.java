package PhosphoTools.CoordConversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;





import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.formats.Pair;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import jaligner.util.Commons;
import jaligner.util.SequenceParser;

/**
 * Convert all position to specified organism
 * @author tshaw
 *
 */
public class ConvertProteinCoord {

	public static void main(String[] args) {
		String seq1 = "MGISTVILEMCLLWGQVLSTGGWIPRTTDYASLIPSEVPLDPTVAEGSPFPSESTLESTVAEGSPISLESTLESTVAEGSLIPSESTLESTVAEGSDSGLALRLVNGDGRCQGRVEILYRGSWGTVCDDSWDTNDANVVCRQLGCGWAMSAPGNAWFGQGSGPIALDDVRCSGHESYLWSCPHNGWLSHNCGHGEDAGVICSAAQPQSTLRPESWPVRISPPVPTEGSESSLALRLVNGGDRCRGRVEVLYRGSWGTVCDDYWDTNDANVVCRQLGCGWAMSAPGNAQFGQGSGPIVLDDVRCSGHESYLWSCPHNGWLTHNCGHSEDAGVICSAPQSRPTPSPDTWPTSHASTAGPESSLALRLVNGGDRCQGRVEVLYRGSWGTVCDDSWDTSDANVVCRQLGCGWATSAPGNARFGQGSGPIVLDDVRCSGYESYLWSCPHNGWLSHNCQHSEDAGVICSAAHSWSTPSPDTLPTITLPASTVGSESSLALRLVNGGDRCQGRVEVLYRGSWGTVCDDSWDTNDANVVCRQLGCGWAMLAPGNARFGQGSGPIVLDDVRCSGNESYLWSCPHNGWLSHNCGHSEDAGVICSGPESSLALRLVNGGDRCQGRVEVLYRGSWGTVCDDSWDTNDANVVCRQLGCGWATSAPGNARFGQGSGPIVLDDVRCSGHESYLWSCPNNGWLSHNCGHHEDAGVICSAAQSRSTPRPDTLSTITLPPSTVGSESSLTLRLVNGSDRCQGRVEVLYRGSWGTVCDDSWDTNDANVVCRQLGCGWATSAPGNARFGQGSGPIVLDDVRCSGHESYLWSCPHNGWLSHNCGHHEDAGVICSVSQSRPTPSPDTWPTSHASTAGPESSLALRLVNGGDRCQGRVEVLYRGSWGTVCDDSWDTSDANVVCRQLGCGWATSAPGNARFGQGSGPIVLDDVRCSGYESYLWSCPHNGWLSHNCQHSEDAGVICSAAHSWSTPSPDTLPTITLPASTVGSESSLALRLVNGGDRCQGRVEVLYQGSWGTVCDDSWDTNDANVVCRQLGCGWAMSAPGNARFGQGSGPIVLDDVRCSGHESYLWSCPHNGWLSHNCGHSEDAGVICSASQSRPTPSPDTWPTSHASTAGSESSLALRLVNGGDRCQGRVEVLYRGSWGTVCDDYWDTNDANVVCRQLGCGWAMSAPGNARFGQGSGPIVLDDVRCSGHESYLWSCPHNGWLSHNCGHHEDAGVICSASQSQPTPSPDTWPTSHASTAGSESSLALRLVNGGDRCQGRVEVLYRGSWGTVCDDYWDTNDANVVCRQLGCGWATSAPGNARFGQGSGPIVLDDVRCSGHESYLWSCPHNGWLSHNCGHHEDAGVICSASQSQPTPSPDTWPTSHASTAGSESSLALRLVNGGDRCQGRVEVLYRGSWGTVCDDYWDTNDANVVCRQLGCGWATSAPGNARFGQGSGPIVLDDVRCSGHESYLWSCPHNGWLSHNCGHHEDAGVICSASQSQPTPSPDTWPTSRASTAGSESTLALRLVNGGDRCRGRVEVLYQGSWGTVCDDYWDTNDANVVCRQLGCGWAMSAPGNAQFGQGSGPIVLDDVRCSGHESYLWSCPHNGWLSHNCGHHEDAGVICSAAQSQSTPRPDTWLTTNLPALTVGSESSLALRLVNGGDRCRGRVEVLYRGSWGTVCDDSWDTNDANVVCRQLGCGWAMSAPGNARFGQGSGPIVLDDVRCSGNESYLWSCPHKGWLTHNCGHHEDAGVICSATQINSTTTDWWHPTTTTTARPSSNCGGFLFYASGTFSSPSYPAYYPNNAKCVWEIEVNSGYRINLGFSNLKLEAHHNCSFDYVEIFDGSLNSSLLLGKICNDTRQIFTSSYNRMTIHFRSDISFQNTGFLAWYNSFPSDATLRLVNLNSSYGLCAGRVEIYHGGTWGTVCDDSWTIQEAEVVCRQLGCGRAVSALGNAYFGSGSGPITLDDVECSGTESTLWQCRNRGWFSHNCNHREDAGVICSGNHLSTPAPFLNITRPNTDYSCGGFLSQPSGDFSSPFYPGNYPNNAKCVWDIEVQNNYRVTVIFRDVQLEGGCNYDYIEVFDGPYRSSPLIARVCDGARGSFTSSSNFMSIRFISDHSITRRGFRAEYYSSPSNDSTNLLCLPNHMQASVSRSYLQSLGFSASDLVISTWNGYYECRPQITPNLVIFTIPYSGCGTFKQADNDTIDYSNFLTAAVSGGIIKRRTDLRIHVSCRMLQNTWVDTMYIANDTIHVANNTIQVEEVQYGNFDVNISFYTSSSFLYPVTSRPYYVDLNQDLYVQAEILHSDAVLTLFVDTCVASPYSNDFTSLTYDLIRSGCVRDDTYGPYSSPSLRIARFRFRAFHFLNRFPSVYLRCKMVVCRAYDPSSRCYRGCVLRSKRDVGSYQEKVDVVLGPIQLQTPPRREEEPR";
		String seq2 = "MGISTVIFEICLLWGQILSTASQTAVPTDGTDSGLAVRLVNGGDRCQGRVEILYQGSWGTVCDDSWDLNDANVVCRQLGCGLAVSAPGNARFGQGSGPIVMDDVACGGYEDYLWRCSHRGWLSHNCGHQEDAGVICSDSQTSSPTPGWWNPGGTNNDVFYPTEQTTAEQTTIPDYTPIGTDSGLAVRLVNGGDRCQGRVEILYQGSWGTVCDDSWDVSDANVVCRQLGCGWAVSAPGNAYFGQGQGPIVLDDVACGGYENYLWSCSHQGWLSHNCGHQEDAGVICSASQSSSPTPGWWNPGGTNNDVFYPTEQTTAGTDSGLAVRLVNGGDRCQGRVEILYQGSWGTVCDDSWDTNDANVVCRQLGCGWAVSAPGNAYFGPGSGSIVLDDVACTGHEDYLWRCSHRGWLSHNCGHHEDAGVICSASQSSSPTPDVFYPTDQTTAEQTTVPDYTPIGTDSGLAVRLVNGGDRCQGRVEILYQGSWGTVCDDSWDLNDANVVCRQLGCGLAVSAPGSARFGQGTGPIVMDDVACGGYEDYLWRCSHRGWLSHNCGHHEDAGVICSASQSSSPTPDVFYPTDQTTAEQTTVPDYTPIGTDSGLAVRLVNGGDRCQGRVEILYQGSWGTVCDDSWDLNDANVVCRQLGCGLAVSAPGSARFGQGTGPIVMDDVACGGYEDYLWRCSHRGWLSHNCGHHEDAGVICSASQSSSPTPDVFYPTDQTTAEQTTVPDYTTIGTENSLAVRLENGGDRCQGRVEILYQGSWGTVCDDSWDLNDANVVCRQLGCGLAVSAPGSARFGQGTGPIVMDDVACGGYEDYLWRCSHRGWLSHNCGHHEDAGVICSASQSSSPTPDVFYPTDQTTVEQTTVPDYTPIGTENSLAVRLENGGDRCQGRVEILYQGSWGTVCDDSWDTKDANVVCRQLGCGWAVSAPGNAYFGPGSGSIVLDDVACTGHEDYLWSCSHRGWLSHNCGHHEDAGVICSDAQIQSTTRPDLWPTTTTPETTTELLTTTPYFDWWTTTSDYSCGGLLTQPSGQFSSPYYPSNYPNNARCSWKIVLPNMNRVTVVFTDVQLEGGCNYDYILVYDGPEYNSSLIARVCDGSNGSFTSTGNFMSVVFITDGSVTRRGFQAHYYSTVSTNYSCGGLLTQPSGQFSSPYYPSNYPNNARCSWEILVPNMNRVTVVFTDVQLEGGCNYDYILVYDGPQYNSSLIARVCDGSNGSFTSTGNFMSVVFITDGSVTRRGFQAHYYSTVSTTPPVPIPTTDDYSCGGLLTLPSGQFSSPHYPSNYPNNARCSWEILVPNMNRVTVAFTDVQLEGGCNYDYILVYDGPEYNSSLIARVCDGSNGSFTSTGNFMSVVFITDGSVTRRGFQAHYYSTVSTNYSCGGLLTQPSGQFSSPHYPSNYPNNVRCSWEILVPSMNRVTVAFTDVQLEGGCSFDYILVYDGPEYNSSLIAPVCDGFNGSFTSTGNFMSVVFITDGSVTRRGFQAYYYSTVSTPPSFHPNITGNDSSLALRLVNGSNRCEGRVEILYRGSWGTVCDDSWGISDANVVCRQLGCGSALSAPGNAWFGQGSGLIVLDDVSCSGYESHLWNCHHPGWLVHNCRHSEDAGVICALPEVTSPSPGWWTTSPSYVNYTCGGFLTQPSGQFSSPFYPGNYPNNARCLWNIEVPNNYRVTVVFRDLQLERGCSYDYIEIFDGPHHSSPLIARVCDGSLGSFTSTSNFMSIRFITDHSITARGFQAHYYSDFDNNTTNLLCQSNHMQASVSRSYLQSMGYSARDLVIPGWNSSYHCQPQITQREVIFTIPYTGCGTIKQADNETINYSNFLRAVVSNGIIKRRKDLNIHVSCKMLQNTWVNTMYITNNTVEIQEVQYGNFDVNISFYTSSSFLFPVTSSPYYVDLDQNLYLQAEILHSDASLALFVDTCVASPHPNDFSSLTYDLIRSGCVRDDTYQSYSSPSPRVSRFKFSSFHFLNRFPSVYLQCKLVVCRAYDTSSRCYRGCVVRSKRDVGSYQEKVDVVLGPIQLQSPSKEKRSLDLAVEDVKKPASSQAVYPTAAIFGGVFLAMVLAVAAFTLGRRTHIDRGQPPSTKL";
		String search = "GWIPRTTDYAS";
		System.out.println(seq1.indexOf(search) + search.length());
		int loc = 2073; 
		//int loc = 2028;
		//System.out.println(convertCoordinate(seq1, seq2, loc));
		
	}
	public static void execute(String[] args) {		
		try {
			
			String alignment_file = args[0];
			String kinase_substrate = args[1];
			String organism = args[2].toUpperCase();
			String output_kinase_substrate = args[3];
			String missed_kinase_substrate = args[4];
			int organism_index = new Integer(args[5]);
			int accession_index = new Integer(args[6]);
			int location_index = new Integer(args[7]);
			FileWriter fwriter = new FileWriter(output_kinase_substrate);
			BufferedWriter out = new BufferedWriter(fwriter);
			//out.write("KinaseGeneName\tKinaseUniprot\tKinaseOrganism\tHomologUniprot\tHomologLocation\tOrigSubstrateUniprot\tOrigSubstrateGeneName\tOrigSubstrateOrganism\tOrigSubstrateLocation\tOrigSubstrateSeq\n");
			
			FileWriter fwriter_missed = new FileWriter(missed_kinase_substrate);
			BufferedWriter out_missed = new BufferedWriter(fwriter_missed);
			//out_missed.write("KinaseGeneName\tKinaseUniprot\tKinaseOrganism\tHomologUniprot\tHomologLocation\tOrigSubstrateUniprot\tOrigSubstrateGeneName\tOrigSubstrateOrganism\tOrigSubstrateLocation\tOrigSubstrateSeq\n");
			
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(alignment_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String fasta_tag = in.readLine();
				String key_uniprot = grabUniprot(fasta_tag.split("\t")[1]);
				String seqs = in.readLine();
				String alignment_seq1 = in.readLine();
				String alignment_line = in.readLine();
				String alignment_seq2 = in.readLine();
				//String alignment = alignment_seq1 + "\t" + alignment_line + "\t" + alignment_seq2;
				AlignmentData data = new AlignmentData();
				data.FASTATAG_SEQ1 = fasta_tag.split("\t")[0];
				data.FASTATAG_SEQ2 = fasta_tag.split("\t")[1];
				data.SEQ1 = seqs.split("\t")[0];
				data.SEQ2 = seqs.split("\t")[1];
				data.ALIGNMENT_SEQ1 = alignment_seq1;
				data.ALIGNMENT_LINE = alignment_line;
				data.ALIGNMENT_SEQ2 = alignment_seq2;
				map.put(key_uniprot, data);
			}
			in.close();
			
			fstream = new FileInputStream(kinase_substrate);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sub_org = split[organism_index];
				String sub_uniprot = split[accession_index];
				String sub_loc = split[location_index];
				if (sub_org.toUpperCase().equals(organism)) {
					out.write(str + "\n");
					//out.write(sub_uniprot + "\t" + sub_gene + "\t" + sub_org + "\t" + sub_loc + "\t" + sub_seq + "\t" + "\n");
				} else if (map.containsKey(sub_uniprot)) {
					AlignmentData data = (AlignmentData)map.get(sub_uniprot);
					String type = "";
					int pred_loc = -1;
					if (sub_loc.contains("S")) {
						type = "S";							
					} else if (sub_loc.contains("T")) {
						type = "T";
					} else if (sub_loc.contains("Y")) {
						type = "Y";
					} 
					if (!type.equals("")) {
						int loc = new Integer(sub_loc.replaceAll("S", "").replaceAll("T", "").replaceAll("Y", "").split("-")[0]);														
						System.out.println("Try this location: " + loc);
						int new_loc = convertCoordinate(data.SEQ1, data.SEQ2, loc);
						String new_uniprot = grabUniprot(data.FASTATAG_SEQ1);
						for (int i = 0; i < split.length; i++) {
							if (i == 0) {
								if (i == accession_index) {
									out.write(new_uniprot);
								} else if (i == location_index) {
									out.write(type + new_loc);
								} else if (i == organism_index) {
									out.write(organism);
								} else {
									out.write(split[i]);
								}
							} else {
								if (i == accession_index) {
									out.write("\t" + new_uniprot);
								} else if (i == location_index) {
									out.write("\t" + type + new_loc);
								} else if (i == organism_index) {
									out.write("\t" + organism);
								} else {
									out.write("\t" + split[i]);
								}
							}
							
						}
						out.write("\n");
						//out.write(kin_gene + "\t" + kin_uniprot + "\t" + kin_org + "\t" + new_uniprot + "\t" + type + new_loc + "\t" + sub_uniprot + "\t" + sub_gene + "\t" + sub_org + "\t" + sub_loc + "\t" + sub_seq + "\t" + "original" + "\n");
					} else {
						out_missed.write(str + "\n");
					}
				} else {
					out_missed.write(str + "\n");
				}							
			}
			in.close();
			out.close();
			out_missed.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String grabUniprot(String input) {
		return input.split(" ")[0].split("\\|")[1];
	}
	public static int convertCoordinate(String seq1, String seq2, int seq2Coord) {
		try {
			Sequence s1 = SequenceParser.parse(seq1);
			Sequence s2 = SequenceParser.parse(seq2);
			Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("BLOSUM62"), 20f, 0.5f);
			System.out.println ( alignment.getSummary() );

			String aln1 = new String(alignment.getSequence1());
			String line = new String(alignment.getMarkupLine());
			String aln2 = new String(alignment.getSequence2());
			System.out.println(aln1);
			System.out.println(line);
			System.out.println(aln2);
			System.out.println(aln1.replaceAll("-",  "").length());
			return alignment.getSequence1IndexFromSequence2Index(seq2Coord - 1) + 1;
		} catch (Exception e) {
			System.out.println("Outside the alignment, can't provide a good coordinate");
			//e.printStackTrace();
			
		}
		return -1;
	}
	public static class AlignmentData {
		public String FASTATAG_SEQ1 = "";
		public String FASTATAG_SEQ2 = "";
		public String SEQ1 = "";
		public String SEQ2 = "";
		public String ALIGNMENT_SEQ1 = "";
		public String ALIGNMENT_LINE = "";
		public String ALIGNMENT_SEQ2 = "";
	}
}

