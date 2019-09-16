package rnaseq.splicing.rnapeg;

import java.util.HashMap;

public class EXON {
	
	public String transcript_id = "";
	public String exon_chr = "NA";
	public int exon_start = -1;
	public int exon_end = -1;
	public String exon_direction = "+";
	public String exon_type = "NA";
	public HashMap exon_junction = new HashMap();
	public HashMap novel_exon_junction = new HashMap();
	//public HashMap skip_junction = new HashMap();
	public String next_serial_exon_chr = "NA";
	public int next_serial_exon_start = -1;
	public int next_serial_exon_end = -1;
	public String next_serial_exon_position = "+";
	public String next_serial_exon_type = "NA";
	public String get_next_serial_line() {
		return next_serial_exon_chr + "\t" + next_serial_exon_start + "\t" + next_serial_exon_end + "\t" + exon_direction + "\t" + transcript_id;
	}
	public String get_line() {
		return exon_chr + "\t" + exon_start + "\t" + exon_end + "\t" + exon_direction + "\t" + transcript_id;
	}
}