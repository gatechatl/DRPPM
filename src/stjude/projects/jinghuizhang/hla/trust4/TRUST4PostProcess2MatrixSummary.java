package stjude.projects.jinghuizhang.hla.trust4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
<<<<<<< HEAD
import java.util.LinkedList;

import statistics.general.MathTools;
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712

/**
 * Read in the annotation and parse through the individual output summary folder
 * @author tshaw
 *
 */
public class TRUST4PostProcess2MatrixSummary {

	public static String description() {
		return "Summarize result from TRUST4 into a matrix per sampleName.";
	}
	public static String type() {
		return "HLA";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			
			String[] membership = {"IGHV6-1","IGHVII-1-1","IGHV1-2","IGHVIII-2-1","IGHV1-3","IGHV4-4","IGHV7-4-1","IGHV2-5","IGHVIII-5-1","IGHVIII-5-2","IGHV3-6","IGHV3-7","IGHV3-64D","IGHV5-10-1","IGHV3-11","IGHVIII-11-1","IGHV1-12","IGHV3-13","IGHVIII-13-1","IGHV1-14","IGHV3-15","IGHVII-15-1","IGHV3-16","IGHVIII-16-1","IGHV1-17","IGHV1-18","IGHV3-19","IGHV3-20","IGHV3-21","IGHV3-22","IGHVII-22-1","IGHVIII-22-2","IGHV3-23","IGHV1-24","IGHV3-25","IGHVIII-25-1","IGHV2-26","IGHVIII-26-1","IGHVII-26-2","IGHV7-27","IGHV4-28","IGHVII-28-1","IGHV3-32","IGHV3-30","IGHVII-30-1","IGHV3-30-2","IGHV4-31","IGHVII-30-21","IGHV3-29","IGHV3-33","IGHVII-33-1","IGHV3-33-2","IGHV4-34","IGHV7-34-1","IGHV3-35","IGHV3-36","IGHV3-37","IGHV3-38","IGHVIII-38-1","IGHV4-39","IGHV7-40","IGHVII-40-1","IGHV3-41","IGHV3-42","IGHV3-43","IGHVII-43-1","IGHVIII-44","IGHVIV-44-1","IGHVII-44-2","IGHV1-45","IGHV1-46","IGHVII-46-1","IGHV3-47","IGHVIII-47-1","IGHV3-48","IGHV3-49","IGHVII-49-1","IGHV3-50","IGHV5-51","IGHVIII-51-1","IGHVII-51-2","IGHV3-52","IGHV3-53","IGHVII-53-1","IGHV3-54","IGHV4-55","IGHV7-56","IGHV3-57","IGHV1-58","IGHV4-59","IGHV3-60","IGHVII-60-1","IGHV4-61","IGHV3-62","IGHVII-62-1","IGHV3-63","IGHV3-64","IGHV3-65","IGHVII-65-1","IGHV3-66","IGHV1-67","IGHVII-67-1","IGHVIII-67-2","IGHVIII-67-3","IGHVIII-67-4","IGHV1-68","IGHV1-69","IGHV2-70D","IGHV3-69-1","IGHV1-69-2","IGHV1-69D","IGHV2-70","IGHV3-71","IGHV3-72","IGHV3-73","IGHV3-74","IGHVII-74-1","IGHV3-75","IGHV3-76","IGHVIII-76-1","IGHV5-78","IGHVII-78-1","IGHV3-79","IGHV4-80","IGHV7-81","IGHVIII-82","IGHV1OR15-9","IGHV1OR15-2","IGHV3OR15-7","IGHV1OR15-6","IGHV1OR15-1","IGHV1OR15-3","IGHV4OR15-8","IGHV1OR15-4","IGHV1OR16-1","IGHV1OR16-3","IGHV3OR16-9","IGHV2OR16-5","IGHV3OR16-15","IGHV3OR16-6","IGHV1OR16-2","IGHV3OR16-10","IGHV1OR16-4","IGHV3OR16-8","IGHV3OR16-12","IGHV3OR16-13","IGHV3OR16-11","IGHV3OR16-7","IGHV3OR16-16","IGHV1OR21-1","IGHJ6","IGHJ3P","IGHJ5","IGHJ4","IGHJ3","IGHJ2P","IGHJ2","IGHJ1","IGHJ1P","IGHM","IGHG4","IGHG2","IGHGP","IGHG1","IGHG3","IGHA2","IGHA1","IGHE","IGHD","IGLV8OR8-1","IGLVI-70","IGLV4-69","IGLVI-68","IGLV10-54","IGLV10-67","IGLVIV-66-1","IGLVV-66","IGLVIV-65","IGLVIV-64","IGLVI-63","IGLV1-62","IGLV8-61","IGLV4-60","IGLVIV-59","IGLVV-58","IGLV6-57","IGLVI-56","IGLV11-55","IGLVIV-53","IGLV5-52","IGLV1-51","IGLV1-50","IGLV9-49","IGLV5-48","IGLV1-47","IGLV7-46","IGLV5-45","IGLV1-44","IGLV7-43","IGLVI-42","IGLVVII-41-1","IGLV1-41","IGLV1-40","IGLVI-38","IGLV5-37","IGLV1-36","IGLV7-35","IGLV2-34","IGLV2-33","IGLV3-32","IGLV3-31","IGLV3-30","IGLV3-29","IGLV2-28","IGLV3-27","IGLV3-26","IGLVVI-25-1","IGLV3-25","IGLV3-24","IGLV2-23","IGLVVI-22-1","IGLV3-22","IGLV3-21","IGLVI-20","IGLV3-19","IGLV2-18","IGLV3-17","IGLV3-16","IGLV3-15","IGLV2-14","IGLV3-13","IGLV3-12","IGLV2-11","IGLV3-10","IGLV3-9","IGLV2-8","IGLV3-7","IGLV3-6","IGLV2-5","IGLV3-4","IGLV4-3","IGLV3-2","IGLV3-1","IGLVIVOR22-1","IGLVIVOR22-2","IGLJCOR18","IGLJ1","IGLJ2","IGLJ3","IGLJ4","IGLJ5","IGLJ6","IGLJ7","IGLC1","IGLC2","IGLC3","IGLC4","IGLC5","IGLC6","IGLC7","IGLCOR22-1","IGLCOR22-2","IGKV1OR1-1","IGKV3OR2-268","IGKV4-1","IGKV5-2","IGKV7-3","IGKV2-4","IGKV1-5","IGKV1-6","IGKV3-7","IGKV1-8","IGKV1-9","IGKV2-10","IGKV3-11","IGKV1-12","IGKV1-13","IGKV2-14","IGKV3-15","IGKV1-16","IGKV1-17","IGKV2-18","IGKV2-19","IGKV3-20","IGKV6-21","IGKV1-22","IGKV2-23","IGKV2-24","IGKV3-25","IGKV2-26","IGKV1-27","IGKV2-28","IGKV2-29","IGKV2-30","IGKV3-31","IGKV1-32","IGKV1-33","IGKV3-34","IGKV1-35","IGKV2-36","IGKV1-37","IGKV2-38","IGKV1-39","IGKV2-40","IGKV2D-40","IGKV1D-39","IGKV2D-38","IGKV1D-37","IGKV2D-36","IGKV1D-35","IGKV3D-34","IGKV1D-33","IGKV1D-32","IGKV3D-31","IGKV2D-30","IGKV2D-29","IGKV2D-28","IGKV1D-27","IGKV2D-26","IGKV3D-25","IGKV2D-24","IGKV2D-23","IGKV1D-22","IGKV6D-21","IGKV3D-20","IGKV2D-19","IGKV2D-18","IGKV6D-41","IGKV1D-17","IGKV1D-16","IGKV3D-15","IGKV2D-14","IGKV1D-13","IGKV1D-12","IGKV3D-11","IGKV2D-10","IGKV1D-42","IGKV1D-43","IGKV1D-8","IGKV3D-7","IGKV1OR2-118","IGKV1OR2-1","IGKV1OR2-2","IGKV2OR2-1","IGKV2OR2-2","IGKV1OR2-3","IGKV1OR2-9","IGKV2OR2-10","IGKV2OR2-7D","IGKV3OR2-5","IGKV1OR2-6","IGKV2OR2-7","IGKV2OR2-8","IGKV1OR2-11","IGKV1OR2-108","IGKV1OR9-2","IGKV1OR-2","IGKV1OR9-1","IGKV1OR-3","IGKV1OR10-1","IGKV1OR22-5","IGKV2OR22-4","IGKV2OR22-3","IGKV3OR22-2","IGKV1OR22-1","IGKJ5","IGKJ4","IGKJ3","IGKJ2","IGKJ1","IGKC","TRAV1-1","TRAV1-2","TRAV2","TRAV3","TRAV4","TRAV5","TRAV6","TRAV7","TRAV8-1","TRAV9-1","TRAV10","TRAV11","TRAV12-1","TRAV8-2","TRAV8-3","TRAV13-1","TRAV12-2","TRAV8-4","TRAV8-5","TRAV13-2","TRAV14DV4","TRAV9-2","TRAV15","TRAV12-3","TRAV8-6","TRAV16","TRAV17","TRAV18","TRAV19","TRAV20","TRAV21","TRAV22","TRAV23DV6","TRAV24","TRAV25","TRAV26-1","TRAV8-7","TRAV27","TRAV28","TRAV29DV5","TRAV30","TRAV31","TRAV32","TRAV33","TRAV26-2","TRAV34","TRAV35","TRAV36DV7","TRAV37","TRAV38-1","TRAV38-2DV8","TRAV39","TRAV40","TRAV41","TRAJ61","TRAJ60","TRAJ59","TRAJ58","TRAJ57","TRAJ56","TRAJ55","TRAJ54","TRAJ53","TRAJ52","TRAJ51","TRAJ50","TRAJ49","TRAJ48","TRAJ47","TRAJ46","TRAJ45","TRAJ44","TRAJ43","TRAJ42","TRAJ41","TRAJ40","TRAJ39","TRAJ38","TRAJ37","TRAJ36","TRAJ35","TRAJ34","TRAJ33","TRAJ32","TRAJ31","TRAJ30","TRAJ29","TRAJ28","TRAJ27","TRAJ26","TRAJ25","TRAJ24","TRAJ23","TRAJ22","TRAJ21","TRAJ20","TRAJ19","TRAJ18","TRAJ17","TRAJ16","TRAJ14","TRAJ13","TRAJ12","TRAJ11","TRAJ10","TRAJ9","TRAJ8","TRAJ7","TRAJ6","TRAJ5","TRAJ4","TRAJ3","TRAJ2","TRAJ1","TRAC","TRBV1","TRBV2","TRBV3-1","TRBV4-1","TRBV5-1","TRBV6-1","TRBV7-1","TRBV4-2","TRBV6-2","TRBV7-2","TRBV8-1","TRBV5-2","TRBV6-4","TRBV7-3","TRBV8-2","TRBV5-3","TRBV9","TRBV10-1","TRBV11-1","TRBV12-1","TRBV10-2","TRBV11-2","TRBV12-2","TRBV6-5","TRBV7-4","TRBV5-4","TRBV6-6","TRBV7-5","TRBV5-5","TRBV6-7","TRBV7-6","TRBV5-6","TRBV6-8","TRBV7-7","TRBV5-7","TRBV7-9","TRBV13","TRBV10-3","TRBV11-3","TRBV12-3","TRBV12-4","TRBV12-5","TRBV14","TRBV15","TRBV16","TRBV17","TRBV18","TRBV19","TRBV20-1","TRBV21-1","TRBV22-1","TRBV23-1","TRBV24-1","TRBV25-1","TRBVA","TRBV26","TRBVB","TRBV27","TRBV28","TRBV29-1","TRBV30","TRBV20OR9-2","TRBV21OR9-2","TRBV22OR9-2","TRBV23OR9-2","TRBV24OR9-2","TRBV25OR9-2","TRBV26OR9-2","TRBV29OR9-2","TRBJ1-1","TRBJ1-2","TRBJ1-3","TRBJ1-4","TRBJ1-5","TRBJ1-6","TRBJ2-1","TRBJ2-2","TRBJ2-2P","TRBJ2-3","TRBJ2-4","TRBJ2-5","TRBJ2-6","TRBJ2-7","TRBC1","TRBC2","TRDV1","TRDV2","TRDV3","TRDJ1","TRDJ4","TRDJ2","TRDJ3","TRDC","TRGV11","TRGVB","TRGV10","TRGV9","TRGVA","TRGV8","TRGV7","TRGV6","TRGV5P","TRGV5","TRGV4","TRGV3","TRGV2","TRGV1","TRGJ2","TRGJP2","TRGJ1","TRGJP","TRGJP1","TRGC2","TRGC1"};
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
<<<<<<< HEAD
			out.write("SampleName\tTCR_CPK\tBCR_CPK\tTCR_to_BCR_ratio\tTotalTCR\tTotalBCR\tTCR_Membership\tBCR_Membership\tTCR_Entropy\tBCR_Entropy\tTotal_TRA\tTRA_Membership\tTRA_Entropy\tTRA_Frac\tTRA_CPK\tTotal_TRB\tTRB_Membership\tTRB_Entropy\tTRB_Frac\tTRB_CPK\tTotal_TRD\tTRD_Membership\tTRD_Entropy\tTRD_Frac\tTRD_CPK\tTotal_TRG\tTRG_Membership\tTRG_Entropy\tTRG_Frac\tTRG_CPK\tTRAB_Frac\tTRDG_Frac\tTotal_IGHG\tIGHG_Membership\tIGHG_Entropy\tIGHG_Frac\tIGHG_CPK\tTotal_IGHA\tIGHA_Membership\tIGHA_Entropy\tIGHA_Frac\tIGHA_CPK\tTotal_IGHE\tIGHE_Membership\tIGHE_Entropy\tIGHE_Frac\tIGHE_CPK\tTotal_IGHD\tIGHD_Membership\tIGHD_Entropy\tIGHD_Frac\tIGHD_CPK\tTotal_IGHM\tIGHM_Membership\tIGHM_Entropy\tIGHM_Frac\tIGHM_CPK\n");
=======
			out.write("SampleName\tTCR_CPK\tBCR_CPK\tTCR_to_BCR_ratio\tTotalTCR\tTotalBCR\tTCR_Membership\tBCR_Membership\tTRA_Frac\tTRA_CPK\tTRB_Frac\tTRB_CPK\tTRD_Frac\tTRD_CPK\tTRG_Frac\tTRG_CPK\tTRAB_Frac\tTRDG_Frac\tIGHG_Frac\tIGHG_CPK\tIGHA_Frac\tIGHA_CPK\tIGHE_Frac\tIGHE_CPK\n");
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String report_tsv = split[1];

				double total_count_of_tcr = 0;
				double total_count_of_bcr = 0;
				double TRA_membership = 0.0;
				double TRB_membership = 0.0;
				double TRD_membership = 0.0;
				double TRG_membership = 0.0;
				
				double total_TRA = 0.0;
				double total_TRB = 0.0;
				double total_TRD = 0.0;
				double total_TRG = 0.0;
				
				double IGHG_membership = 0.0;
				double IGHA_membership = 0.0;
				double IGHE_membership = 0.0;
<<<<<<< HEAD
				double IGHD_membership = 0.0;
				double IGHM_membership = 0.0;
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
				
				double total_IGHG = 0.0;
				double total_IGHA = 0.0;
				double total_IGHE = 0.0;
<<<<<<< HEAD
				double total_IGHD = 0.0;
				double total_IGHM = 0.0;
				
				double tcr_membership = 0;
				double bcr_membership = 0;
				
				LinkedList TCR_values = new LinkedList();
				LinkedList BCR_values = new LinkedList();
				LinkedList TRA_values = new LinkedList();
				LinkedList TRB_values = new LinkedList();
				LinkedList TRD_values = new LinkedList();
				LinkedList TRG_values = new LinkedList();
				LinkedList IGHG_values = new LinkedList();
				LinkedList IGHA_values = new LinkedList();
				LinkedList IGHE_values = new LinkedList();
				LinkedList IGHD_values = new LinkedList();
				LinkedList IGHM_values = new LinkedList();
				
=======
				
				double tcr_membership = 0;
				double bcr_membership = 0;
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
				FileInputStream fstream2 = new FileInputStream(report_tsv);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				in2.readLine(); // skip the header
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					int count = new Integer(split2[0]);
					String v_region = split2[4];
					String d_region = split2[5];
					String j_region = split2[6];
					String c_region = split2[7];
					boolean TCR = false;
					boolean BCR = false;
					boolean TRA = false;
					boolean TRB = false;
					boolean TRD = false;
					boolean TRG = false;
					boolean IGHG = false;
					boolean IGHE = false;
					boolean IGHA = false;
<<<<<<< HEAD
					boolean IGHD = false;
					boolean IGHM = false;
					
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					if (v_region.equals("*")) {
						v_region = "*****";
					}
					if (d_region.equals("*")) {
						d_region = "*****";
					}
					if (j_region.equals("*")) {
						j_region = "*****";
					}
					if (c_region.equals("*")) {
						c_region = "*****";
					}
					if (v_region.substring(0, 2).equals("TR") || d_region.substring(0, 2).equals("TR") || j_region.substring(0, 2).equals("TR") || c_region.substring(0, 2).equals("TR")) {
						TCR = true;
					}
					if (v_region.substring(0, 3).equals("TRA") || d_region.substring(0, 3).equals("TRA") || j_region.substring(0, 3).equals("TRA") || c_region.substring(0, 3).equals("TRA")) {
						TRA = true;
					}
					if (v_region.substring(0, 3).equals("TRB") || d_region.substring(0, 3).equals("TRB") || j_region.substring(0, 3).equals("TRB") || c_region.substring(0, 3).equals("TRB")) {
						TRB = true;
					}
					if (v_region.substring(0, 3).equals("TRD") || d_region.substring(0, 3).equals("TRD") || j_region.substring(0, 3).equals("TRD") || c_region.substring(0, 3).equals("TRD")) {
						TRD = true;
					}
					if (v_region.substring(0, 3).equals("TRG") || d_region.substring(0, 3).equals("TRG") || j_region.substring(0, 3).equals("TRG") || c_region.substring(0, 3).equals("TRG")) {
						TRG = true;
					}
					if (v_region.substring(0, 2).equals("IG") || d_region.substring(0, 2).equals("IG") || j_region.substring(0, 2).equals("IG") || c_region.substring(0, 2).equals("IG")) {
						BCR = true;
					} 
					if (v_region.substring(0, 4).equals("IGHG") || d_region.substring(0, 4).equals("IGHG") || j_region.substring(0, 4).equals("IGHG") || c_region.substring(0, 4).equals("IGHG")) {
						IGHG = true;
					}
					if (v_region.substring(0, 4).equals("IGHA") || d_region.substring(0, 4).equals("IGHA") || j_region.substring(0, 4).equals("IGHA") || c_region.substring(0, 4).equals("IGHA")) {
						IGHA = true;
					}
					if (v_region.substring(0, 4).equals("IGHE") || d_region.substring(0, 4).equals("IGHE") || j_region.substring(0, 4).equals("IGHE") || c_region.substring(0, 4).equals("IGHE")) {
						IGHE = true;
					}
<<<<<<< HEAD
					if (v_region.substring(0, 4).equals("IGHD") || d_region.substring(0, 4).equals("IGHD") || j_region.substring(0, 4).equals("IGHD") || c_region.substring(0, 4).equals("IGHD")) {
						IGHD = true;
					}
					if (v_region.substring(0, 4).equals("IGHM") || d_region.substring(0, 4).equals("IGHM") || j_region.substring(0, 4).equals("IGHM") || c_region.substring(0, 4).equals("IGHM")) {
						IGHM = true;
					}
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712

					
					if (TCR && BCR) {
						System.out.println("Doesn't make sense");						
					}
					if (TCR) {
						tcr_membership++;
						total_count_of_tcr += count;
<<<<<<< HEAD
						TCR_values.add(new Double(count));
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}
					if (BCR) {
						bcr_membership++;
						total_count_of_bcr += count;
<<<<<<< HEAD
						BCR_values.add(new Double(count));
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}
					if (TRA) {
						TRA_membership++;
						total_TRA += count;
<<<<<<< HEAD
						TRA_values.add(new Double(count));
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}
					if (TRB) {
						TRB_membership++;
						total_TRB += count;
<<<<<<< HEAD
						TRB_values.add(new Double(count));
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}
					if (TRG) {
						TRG_membership++;
						total_TRG += count;
<<<<<<< HEAD
						TRG_values.add(new Double(count));
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}
					if (TRD) {
						TRD_membership++;
						total_TRD += count;
<<<<<<< HEAD
						TRD_values.add(new Double(count));
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}
					if (IGHG) {
						IGHG_membership++;
						total_IGHG += count;
<<<<<<< HEAD
						IGHG_values.add(new Double(count));
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}
					if (IGHA) {
						IGHA_membership++;
						total_IGHA += count;
<<<<<<< HEAD
						IGHA_values.add(new Double(count));
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}
					if (IGHE) {
						IGHE_membership++;
						total_IGHE += count;
<<<<<<< HEAD
						IGHE_values.add(new Double(count));
					}
					if (IGHD) {
						IGHD_membership++;
						total_IGHD += count;
						IGHD_values.add(new Double(count));
					}
					if (IGHM) {
						IGHM_membership++;
						total_IGHM += count;
						IGHM_values.add(new Double(count));
					}
				}
				in2.close();
				
				double TCR_CPK = tcr_membership * 1000 / total_count_of_tcr;								
=======
					}

				}
				in2.close();
				
				double TCR_CPK = tcr_membership * 1000 / total_count_of_tcr;
				
				
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
				double TRA_CPK = TRA_membership * 1000 / total_count_of_tcr;
				double TRB_CPK = TRB_membership * 1000 / total_count_of_tcr;
				double TRD_CPK = TRD_membership * 1000 / total_count_of_tcr;
				double TRG_CPK = TRG_membership * 1000 / total_count_of_tcr;
				
				double BCR_CPK = bcr_membership * 1000 / total_count_of_bcr;
				
				double TRA_Frac = TRA_membership / tcr_membership;
				
				double TRB_Frac = TRB_membership / tcr_membership;
				double TRD_Frac = TRD_membership / tcr_membership;
				double TRG_Frac = TRG_membership / tcr_membership;
				
				double IGHG_Frac = IGHG_membership / bcr_membership;
				double IGHA_Frac = IGHA_membership / bcr_membership;
				double IGHE_Frac = IGHE_membership / bcr_membership;
<<<<<<< HEAD
				double IGHD_Frac = IGHD_membership / bcr_membership;
				double IGHM_Frac = IGHM_membership / bcr_membership;
				
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
				
				double IGHG_CPK = IGHG_membership * 1000 / total_count_of_bcr;
				double IGHA_CPK = IGHA_membership * 1000 / total_count_of_bcr;
				double IGHE_CPK = IGHE_membership * 1000 / total_count_of_bcr;
<<<<<<< HEAD
				double IGHD_CPK = IGHD_membership * 1000 / total_count_of_bcr;
				double IGHM_CPK = IGHM_membership * 1000 / total_count_of_bcr;

				double TCR_entropy = MathTools.shannon_entropy(TCR_values);
				double BCR_entropy = MathTools.shannon_entropy(BCR_values);
				
				double TRA_entropy = MathTools.shannon_entropy(TRA_values);
				double TRB_entropy = MathTools.shannon_entropy(TRB_values);
				double TRD_entropy = MathTools.shannon_entropy(TRD_values);
				double TRG_entropy = MathTools.shannon_entropy(TRG_values);
				
				double IGHG_entropy = MathTools.shannon_entropy(IGHG_values);
				double IGHA_entropy = MathTools.shannon_entropy(IGHA_values);
				double IGHE_entropy = MathTools.shannon_entropy(IGHE_values);
				double IGHD_entropy = MathTools.shannon_entropy(IGHD_values);
				double IGHM_entropy = MathTools.shannon_entropy(IGHM_values);
				
=======

>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
				if (total_count_of_bcr == 0) {
					IGHG_CPK = 0.0;
					IGHA_CPK = 0.0;
					IGHE_CPK = 0.0;
<<<<<<< HEAD
					IGHM_CPK = 0.0;
					IGHD_CPK = 0.0;
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					BCR_CPK = 0.0;
				}
				if (total_count_of_tcr == 0) {
					TCR_CPK = 0.0;
					TRA_CPK = 0.0;
					TRB_CPK = 0.0;
					TRG_CPK = 0.0;
					TRD_CPK = 0.0;
				}
				if (bcr_membership == 0) {
					IGHG_Frac = 0.0;
					IGHA_Frac = 0.0;
					IGHE_Frac = 0.0;
<<<<<<< HEAD
					IGHM_Frac = 0.0;
					IGHD_Frac = 0.0;
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
				}
				if (tcr_membership == 0) {
					TRA_Frac = 0.0;
					TRB_Frac = 0.0;
					TRD_Frac = 0.0;
					TRG_Frac = 0.0;
				}

				double TRAB_Frac = TRA_Frac + TRB_Frac;
				double TRDG_Frac = TRD_Frac + TRG_Frac;
				
				if (BCR_CPK == 0) {
					BCR_CPK += 0.1;
					TCR_CPK += 0.1;
				}
				double T2B_ratio = (TCR_CPK) / (BCR_CPK);
				if (total_count_of_tcr >= 1 && total_count_of_bcr >= 1) {
				// out.write("SampleName\t     TCR_CPK\       BCR_CPK\t         TotalTCR\t                  TotalBCR\t                   TCR_Membership\t       BCR_Membership\t         TRA_Frac\tTRA_CPK\tTRB_Frac\tTRB_CPK\tTRD_Frac\tTRD_CPK\tTRG_Frac\tTRG_CPK\tTRAB_Frac\tTRGD_FRAC\n");
<<<<<<< HEAD
					// out.write("SampleName\tTCR_CPK\tBCR_CPK\tTCR_to_BCR_ratio\tTotalTCR\tTotalBCR\tTCR_Membership\tBCR_Membership\tTotal_TRA\tTRA_Frac\tTRA_CPK\tTotal_TRB\tTRB_Frac\tTRB_CPK\tTotal_TRD\tTRD_Frac\tTRD_CPK\tTotal_TRG\tTRG_Frac\tTRG_CPK\tTRAB_Frac\tTRDG_Frac\tTotal_IGHG\tIGHG_Frac\tIGHG_CPK\tTotal_IGHA\tIGHA_Frac\tIGHA_CPK\tTotal_IGHE\tIGHE_Frac\tIGHE_CPK\tTotal_IGHD\tIGHD_Frac\tIGHD_CPK\tTotal_IGHM\tIGHM_Frac\tIGHM_CPK\n");
					out.write(sampleName + "\t" + TCR_CPK + "\t" + BCR_CPK + "\t" + T2B_ratio + "\t" + total_count_of_tcr + "\t" + total_count_of_bcr + "\t" + tcr_membership + "\t" + bcr_membership + "\t" + TCR_entropy + "\t" + BCR_entropy + "\t" + total_TRA + "\t" + TRA_membership + "\t" + TRA_entropy + "\t" + TRA_Frac + "\t" + TRA_CPK + "\t" + total_TRB + "\t" + TRB_membership + "\t" + TRB_entropy + "\t" + TRB_Frac + "\t" + TRB_CPK + "\t" + total_TRD + "\t" + TRD_membership + "\t" + TRD_entropy + "\t" + TRD_Frac + "\t" + TRD_CPK + "\t" + total_TRG + "\t" + TRG_membership + "\t" + TRG_entropy + "\t" + TRG_Frac + "\t" + TRG_CPK + "\t" + TRAB_Frac + "\t" + TRDG_Frac + "\t" + total_IGHG + "\t" + IGHG_membership + "\t" + IGHG_entropy + "\t" + IGHG_Frac + "\t" + IGHG_CPK + "\t" + total_IGHA + "\t" + IGHA_membership + "\t" + IGHA_entropy + "\t" + IGHA_Frac + "\t" + IGHA_CPK + "\t" + total_IGHE + "\t" + IGHE_membership + "\t" + IGHE_entropy + "\t" + IGHE_Frac + "\t" + IGHE_CPK + "\t" + total_IGHD + "\t" + IGHD_membership + "\t" + IGHD_entropy + "\t" + IGHD_Frac + "\t" + IGHD_CPK + "\t" + total_IGHM + "\t" + IGHM_membership + "\t" + IGHM_entropy + "\t" + IGHM_Frac + "\t" + IGHM_CPK + "\n");
=======
					out.write(sampleName + "\t" + TCR_CPK + "\t" + BCR_CPK + "\t" + T2B_ratio + "\t" + total_count_of_tcr + "\t" + total_count_of_bcr + "\t" + tcr_membership + "\t" + bcr_membership + "\t" + TRA_Frac + "\t" + TRA_CPK + "\t" + TRB_Frac + "\t" + TRB_CPK + "\t" + TRD_Frac + "\t" + TRD_CPK + "\t" + TRG_Frac + "\t" + TRG_CPK + "\t" + TRAB_Frac + "\t" + TRDG_Frac + "\t" + IGHG_Frac + "\t" + IGHG_CPK + "\t" + IGHA_Frac + "\t" + IGHA_CPK + "\t" + IGHE_Frac + "\t" + IGHE_CPK + "\n");
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
<<<<<<< HEAD
	
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
}
