package proteomics.comet.pepxmlparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Here's a parser for the COMET pepxml. Grab the top hit peptide of the scan.
<spectrum_query spectrum="NCI-11plex-15-F2-m17228.48163.48163.3" start_scan="48163" end_scan="48163" precursor_neutral_mass="1843.010885" assumed_charge="3" index="20971" retention_time_sec="8086.6">
  <search_result>
   <search_hit hit_rank="1" peptide="FHPDINVYIIEVR" peptide_prev_aa="K" peptide_next_aa="E" protein="tr|E9PF17|E9PF17_HUMAN" num_tot_proteins="4" num_matched_ions="30" tot_num_ions="48" calc_neutral_pep_mass="1843.019509" massdiff="-0.008623" num_tol_term="2" num_missed_cleavages="0" num_matched_peptides="9715">
    <alternative_protein protein="sp|P13611|CSPG2_HUMAN"/>
    <alternative_protein protein="sp|P13611-3|CSPG2_HUMAN"/>
    <alternative_protein protein="sp|P13611-5|CSPG2_HUMAN"/>
    <modification_info modified_peptide="FHPDINVYIIEVR" mod_nterm_mass="230.170756">
    </modification_info>
    <search_score name="xcorr" value="3.695"/>
    <search_score name="deltacn" value="0.674"/>
    <search_score name="deltacnstar" value="0.000"/>
    <search_score name="spscore" value="2296.3"/>
    <search_score name="sprank" value="1"/>
    <search_score name="expect" value="2.56E-06"/>
   </search_hit>
 * @author gatechatl
 *
 */
public class COMETPepXML2Table {

	public static String parameter_info() {
		return "[pepXML] [outputTable]";
	}
	public static String type() {
		return "pepXML";
	}
	public static String description() {		
		return "Convert COMET pepxml to an output table.";
	}
	public static void execute(String[] args) {
		
		try {
			String pepXML = args[0]; //"/home/gatechatl/Projects/Proteomics/pepxml2table/NCI-11plex-1-F1-f10268.pep.xml"; //args[0];
			String outputTable = args[1]; //"/home/gatechatl/Projects/Proteomics/pepxml2table/NCI-11plex-1-F1-f10268.pep.output.table.txt"; //args[1];
						
			FileWriter fwriter = new FileWriter(outputTable);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("outfile\tcharge\ttryptic\tmiscleavage\tscore\tdeltaScore\tprecursor_neutral_mass\tcalc_neutral_pep_mass\tpeptide\tpepLength\tprotein\tTargetDecoyAnnot\tnum_protein\talt_protein\trt\tstart_scan\tend_scan\n");
			boolean first = true;
			QUERY_SPECTRUM current_spectrum = new QUERY_SPECTRUM();
			FileInputStream fstream = new FileInputStream(pepXML);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String peptide_psm_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				if (str.contains("<spectrum_query spectrum=")) {
					
					if (!first) {
						//outfile charge  tryptic miscleavage     score   deltaScore      precursor_neutral_mass  calc_neutral_pep_mass   peptide pepLength       protein TD
						//NCI-11Plex-10-F10-f10508.43035.43035.6  6       3       1       1.733   0.025   3753.774090     3751.755532     R.GCSSADSSRSSSASPLAACAGWAPSALCPTTLPSR.R 35      sp|Q96LV5|IN4L1_HUMAN   target
						String target_decoy = "target";
						if (current_spectrum.PROTEIN.contains("decoy") || current_spectrum.ALT_PROTEIN.contains("decoy")) {
							target_decoy = "decoy";
						}
						if (!current_spectrum.NUM_PROTEIN.equals("") || !current_spectrum.CALC_NEUTRAL_PEP_MASS.equals("") || !current_spectrum.NUM_MISSED_CLEAVAGE.equals("")) {
							/*System.out.println(current_spectrum.SPECTRUM + "\t"
									+ current_spectrum.CHARGE + "\t"
									+ current_spectrum.NUM_TOL_TERM + "\t"
									+ current_spectrum.NUM_MISSED_CLEAVAGE + "\t"
									+ current_spectrum.XCORR + "\t"
									+ current_spectrum.DELTACN + "\t"
									+ current_spectrum.PRECURSOR_NEUTRAL_MASS + "\t"
									+ current_spectrum.CALC_NEUTRAL_PEP_MASS + "\t"
									+ current_spectrum.PEPTIDE_PREV_AA + "." + current_spectrum.PEPTIDE + "." + current_spectrum.PEPTIDE_NEXT_AA + "\t"
									+ current_spectrum.PEPTIDE.length() + "\t"
									+ current_spectrum.PROTEIN + "\t"
									+ target_decoy + "\t"
									+ current_spectrum.NUM_PROTEIN + "\t"
									+ current_spectrum.ALT_PROTEIN + "\t"
									+ current_spectrum.RT + "\t"
									+ current_spectrum.START_SCAN + "\t"
									+ current_spectrum.END_SCAN + "\n");
							*/
							out.write(current_spectrum.SPECTRUM + "\t"
									+ current_spectrum.CHARGE + "\t"
									+ current_spectrum.NUM_TOL_TERM + "\t"
									+ current_spectrum.NUM_MISSED_CLEAVAGE + "\t"
									+ current_spectrum.XCORR + "\t"
									+ current_spectrum.DELTACN + "\t"
									+ current_spectrum.PRECURSOR_NEUTRAL_MASS + "\t"
									+ current_spectrum.CALC_NEUTRAL_PEP_MASS + "\t"
									+ current_spectrum.PEPTIDE_PREV_AA + "." + current_spectrum.PEPTIDE + "." + current_spectrum.PEPTIDE_NEXT_AA + "\t"
									+ current_spectrum.PEPTIDE.length() + "\t"
									+ current_spectrum.PROTEIN + "\t"
									+ target_decoy + "\t"
									+ current_spectrum.NUM_PROTEIN + "\t"
									+ current_spectrum.ALT_PROTEIN + "\t"
									+ current_spectrum.RT + "\t"
									+ current_spectrum.START_SCAN + "\t"
									+ current_spectrum.END_SCAN + "\n");
							
						}
						
					}
					first = false;
					current_spectrum = new QUERY_SPECTRUM();
					//System.out.println("spectrum_query" + str);
					for (int i = 0; i < split.length; i++) {
						if (split[i].contains("spectrum=")) {
							current_spectrum.SPECTRUM = split[i].split("spectrum=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");							
						}
						if (split[i].contains("start_scan=")) {
							current_spectrum.START_SCAN = split[i].split("start_scan=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
						}
						if (split[i].contains("end_scan=")) {
							current_spectrum.END_SCAN = split[i].split("end_scan=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
						}
						if (split[i].contains("precursor_neutral_mass=")) {
							current_spectrum.PRECURSOR_NEUTRAL_MASS = split[i].split("precursor_neutral_mass=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");			
						}
						if (split[i].contains("assumed_charge=")) {
							current_spectrum.CHARGE = split[i].split("assumed_charge=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
						}
						if (split[i].contains("retention_time_sec=")) {
							current_spectrum.RT = split[i].split("retention_time_sec=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
						}
					
					}
					in.readLine();
					str = in.readLine();
					if (str.contains("<search_hit hit_rank=\"1\"")) {
						//System.out.println("search_hit_rank" + str);
						split = str.split(" ");
						for (int i = 0; i < split.length; i++) {
							if (split[i].contains("peptide=")) {
								current_spectrum.PEPTIDE = split[i].split("peptide=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");					
							}
							if (split[i].contains("peptide_prev_aa=")) {
								current_spectrum.PEPTIDE_PREV_AA = split[i].split("peptide_prev_aa=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("peptide_next_aa=")) {
								current_spectrum.PEPTIDE_NEXT_AA = split[i].split("peptide_next_aa=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("peptide_prev_aa=")) {
								current_spectrum.PEPTIDE_PREV_AA = split[i].split("peptide_prev_aa=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("protein=")) {
								current_spectrum.PROTEIN = split[i].split("protein=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("num_tot_proteins=")) {
								current_spectrum.NUM_PROTEIN = split[i].split("num_tot_proteins=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("calc_neutral_pep_mass=")) {
								current_spectrum.CALC_NEUTRAL_PEP_MASS = split[i].split("calc_neutral_pep_mass=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("massdiff=")) {
								current_spectrum.MASS_DIFF = split[i].split("massdiff=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("num_tol_term=")) {
								current_spectrum.NUM_TOL_TERM = split[i].split("num_tol_term=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("num_missed_cleavages")) {
								current_spectrum.NUM_MISSED_CLEAVAGE = split[i].split("num_missed_cleavages=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							if (split[i].contains("massdiff=")) {
								current_spectrum.MASS_DIFF = split[i].split("massdiff=")[1].replaceAll("\"", "").replaceAll(">",  "").replaceAll("\\/", "");
							}
							//
							
						}	
						if (!current_spectrum.NUM_PROTEIN.equals("")) {
							for (int j = 1; j < new Integer(current_spectrum.NUM_PROTEIN); j++) {
								str = in.readLine();
								split = str.split(" ");
								current_spectrum.ALT_PROTEIN += str.replaceAll("\"", "").replaceAll("<", "").replaceAll(">", "").replaceAll("\\/", "").replaceAll("alternative_protein protein=", "").trim() + ",";
							}
						}
					} // if search hit
					
				} else if (str.contains("<search_score name=\"xcorr\"") && current_spectrum.XCORR.equals("")) {
					current_spectrum.XCORR = str.replaceAll("<",  "").replaceAll("\"", "").replaceAll(">", "").replaceAll("\\/", "").split(" value=")[1];
				} else if (str.contains("<search_score name=\"deltacn\"") && current_spectrum.DELTACN.equals("")) {
					current_spectrum.DELTACN = str.replaceAll("<",  "").replaceAll("\"", "").replaceAll(">", "").replaceAll("\\/", "").split(" value=")[1];
				}
			}
			in.close();
			
			String target_decoy = "target";
			if (current_spectrum.PROTEIN.contains("decoy") || current_spectrum.ALT_PROTEIN.contains("decoy")) {
				target_decoy = "decoy";
			}
			if (!current_spectrum.NUM_PROTEIN.equals("") || !current_spectrum.CALC_NEUTRAL_PEP_MASS.equals("") || !current_spectrum.NUM_MISSED_CLEAVAGE.equals("")) {
				/*System.out.println(current_spectrum.SPECTRUM + "\t"
						+ current_spectrum.CHARGE + "\t"
						+ current_spectrum.NUM_TOL_TERM + "\t"
						+ current_spectrum.NUM_MISSED_CLEAVAGE + "\t"
						+ current_spectrum.XCORR + "\t"
						+ current_spectrum.DELTACN + "\t"
						+ current_spectrum.PRECURSOR_NEUTRAL_MASS + "\t"
						+ current_spectrum.CALC_NEUTRAL_PEP_MASS + "\t"
						+ current_spectrum.PEPTIDE_PREV_AA + "." + current_spectrum.PEPTIDE + "." + current_spectrum.PEPTIDE_NEXT_AA + "\t"
						+ current_spectrum.PEPTIDE.length() + "\t"
						+ current_spectrum.PROTEIN + "\t"
						+ target_decoy + "\t"
						+ current_spectrum.NUM_PROTEIN + "\t"
						+ current_spectrum.ALT_PROTEIN + "\t"
						+ current_spectrum.RT + "\t"
						+ current_spectrum.START_SCAN + "\t"
						+ current_spectrum.END_SCAN + "\n");
				*/
				out.write(current_spectrum.SPECTRUM + "\t"
						+ current_spectrum.CHARGE + "\t"
						+ current_spectrum.NUM_TOL_TERM + "\t"
						+ current_spectrum.NUM_MISSED_CLEAVAGE + "\t"
						+ current_spectrum.XCORR + "\t"
						+ current_spectrum.DELTACN + "\t"
						+ current_spectrum.PRECURSOR_NEUTRAL_MASS + "\t"
						+ current_spectrum.CALC_NEUTRAL_PEP_MASS + "\t"
						+ current_spectrum.PEPTIDE_PREV_AA + "." + current_spectrum.PEPTIDE + "." + current_spectrum.PEPTIDE_NEXT_AA + "\t"
						+ current_spectrum.PEPTIDE.length() + "\t"
						+ current_spectrum.PROTEIN + "\t"
						+ target_decoy + "\t"
						+ current_spectrum.NUM_PROTEIN + "\t"
						+ current_spectrum.ALT_PROTEIN + "\t"
						+ current_spectrum.RT + "\t"
						+ current_spectrum.START_SCAN + "\t"
						+ current_spectrum.END_SCAN + "\n");
				
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
