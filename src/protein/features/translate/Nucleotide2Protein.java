package protein.features.translate;

public class Nucleotide2Protein {

	public static void main(String[] args) {
		System.out.println(nucleotide2protein("UCAUU", -3, true));
	}
	/**
	 * Translate the nucleotide to protein
	 * @param nucleotide sequence
	 * @param frame -3, -2, -1, 1, 2, 3
	 * @param includeX a boolean true or false
	 * @return
	 */
	public static String nucleotide2protein(String nucleotide, int frame, boolean includeX) {
		if (frame > 3 || frame < -3 && frame != 0) {
        	System.out.println("Frame offset should be between -3 to 3");
            System.exit(-1);			
		}
		nucleotide = nucleotide.toUpperCase();
		int index = 0;
		String peptide = "";
		if (frame < 0) {
			frame = frame * -1;
			nucleotide = reverseComplement(nucleotide);
			//System.out.println(nucleotide);
		}
		int offset = Math.abs(frame) - 1;
		
		if (frame == 2) {
			if (includeX) {
				peptide += "X";
			}
			index = 1;
		}
		if (frame == 3) {
			if (includeX) {
				peptide += "X";
			}
			index = 2;
		}
		while (index <= nucleotide.length() - 3) {
			peptide += trinucleotide2aminoacid(nucleotide.substring(index, index + 3));
			index += 3;
		}
		if (includeX && (nucleotide.length() - offset) % 3 != 0) {
			peptide += "X";
		}
		return peptide;
	}
    public static String reverseComplement(String nucleotide) {
        String out = "";
        for(int i = nucleotide.length() - 1; i >= 0; i--) {
            char curr = nucleotide.charAt(i);
            if (curr == 'U') {
            	curr = 'T';
            }
            if (curr == 'A') {
                out += 'T';
            } else if (curr == 'T') {
                out += 'A';
            } else if (curr == 'C') {
                out += 'G';
            } else if (curr == 'G') {
                out += 'C';
            } else if (curr == 'N') {
                out += 'N';
            } else {
            	
            	System.out.println("ERROR: invalid nucleotides.");
            	System.out.println(nucleotide);
            	System.exit(-1);
            }
        }
        return out;
    }
	public static String trinucleotide2aminoacid(String trinucleotide) {
		String query = trinucleotide.toUpperCase();
		query = query.replaceAll("T", "U");
		if (query.equals("UCA")) {
			return "S";
		}
		if (query.equals("AUA")) {
			return "I";
		}
		if (query.equals("UCC")) {
			return "S";
		}
		if (query.equals("AUC")) {
			return "I";
		}
		if (query.equals("UCG")) {
			return "S";
		}
		if (query.equals("AUU")) {
			return "I";
		}
		if (query.equals("UCU")) {
			return "S";
		}
		if (query.equals("AUG")) {
			return "M";
		}
		if (query.equals("UUC")) {
			return "F";
		}
		if (query.equals("ACA")) {
			return "T";
		}
		if (query.equals("UUU")) {
			return "F";
		}
		if (query.equals("ACC")) {
			return "T";
		}
		if (query.equals("UUA")) {
			return "L";
		}
		if (query.equals("ACG")) {
			return "T";
		}
		if (query.equals("UUG")) {
			return "L";
		}
		if (query.equals("ACU")) {
			return "T";
		}
		if (query.equals("UAC")) {
			return "Y";
		}
		if (query.equals("AAC")) {
			return "N";
		}
		if (query.equals("UAU")) {
			return "Y";
		}
		if (query.equals("AAU")) {
			return "N";
		}
		if (query.equals("UAA")) {
			return "_";
		}
		if (query.equals("AAA")) {
			return "K";
		}
		if (query.equals("UAG")) {
			return "_";
		}
		if (query.equals("AAG")) {
			return "K";
		}
		if (query.equals("UGC")) {
			return "C";
		}
		if (query.equals("AGC")) {
			return "S";
		}
		if (query.equals("UGU")) {
			return "C";
		}
		if (query.equals("AGU")) {
			return "S";
		}
		if (query.equals("UGA")) {
			return "_";
		}
		if (query.equals("AGA")) {
			return "R";
		}
		if (query.equals("UGG")) {
			return "W";
		}
		if (query.equals("AGG")) {
			return "R";
		}
		if (query.equals("CUA")) {
			return "L";
		}
		if (query.equals("GUA")) {
			return "V";
		}
		if (query.equals("CUC")) {
			return "L";
		}
		if (query.equals("GUC")) {
			return "V";
		}
		if (query.equals("CUG")) {
			return "L";
		}
		if (query.equals("GUG")) {
			return "V";
		}
		if (query.equals("CUU")) {
			return "L";
		}
		if (query.equals("GUU")) {
			return "V";
		}
		if (query.equals("CCA")) {
			return "P";
		}
		if (query.equals("GCA")) {
			return "A";
		}
		if (query.equals("CCC")) {
			return "P";
		}
		if (query.equals("GCC")) {
			return "A";
		}
		if (query.equals("CCG")) {
			return "P";
		}
		if (query.equals("GCG")) {
			return "A";
		}
		if (query.equals("CCU")) {
			return "P";
		}
		if (query.equals("GCU")) {
			return "A";
		}
		if (query.equals("CAC")) {
			return "H";
		}
		if (query.equals("GAC")) {
			return "D";
		}
		if (query.equals("CAU")) {
			return "H";
		}
		if (query.equals("GAU")) {
			return "D";
		}
		if (query.equals("CAA")) {
			return "Q";
		}
		if (query.equals("GAA")) {
			return "E";
		}
		if (query.equals("CAG")) {
			return "Q";
		}
		if (query.equals("GAG")) {
			return "E";
		}
		if (query.equals("CGA")) {
			return "R";
		}
		if (query.equals("GGA")) {
			return "G";
		}
		if (query.equals("CGC")) {
			return "R";
		}
		if (query.equals("GGC")) {
			return "G";
		}
		if (query.equals("CGG")) {
			return "R";
		}
		if (query.equals("GGG")) {
			return "G";
		}
		if (query.equals("CGU")) {
			return "R";
		}
		if (query.equals("GGU")) {
			return "G";
		}
		return "X";
	}
}
