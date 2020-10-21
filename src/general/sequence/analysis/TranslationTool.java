package general.sequence.analysis;

public class TranslationTool {

	public static void main(String[] args) {
		String seq = "CTTCTCCCACCCTTCTCCCCTGAAGCAGGCCATAAAAGAATCCTCTGACGTTTCTATAAAGTAGGTCATAAGAACCTTCATTCCAGAAGTACCCTCAAAGACAGAGACACCAAGAAGAATCGGAACATACAGGCTTTGATATCAAAGGTTTATAAAGCCAATATCTGGGAAAGAGAAAACCGTGAGACTTCCAGATCTTCTCTGGTGAAGTGTGTTTCCTGCAACGATCACGAACATGAACATCAAAGGATCGCCATGGAAAGCAGGGTCCCTCCTGCTGCTGCTGGTGTCAAACCTGCTCCTGTGCCAGAGCGTGGCCCCCTTGCCCATCTGTCCCGGCGGGGCTGCCCGATGCCAGGTGACCCTTCGAGACCTGTTTGACCGCGCCGTCGTCCTGTCCCACTACATCCATAACCTCTCCTCAGAAATGTTCAGCGAATTCGATAAACGGTATACCCATGGCCGGGGGTTCATTACCAAGGCCATCAACAGCTGCCACACTTCTTCCCTTGCCACCCCCGAAGACAAGGAGCAAGCCCAACAGATGAATCAAAAAGACTTTCTGAGCCTGATAGTCAGCATATTGCGATCCTGGAATGAGCCTCTGTATCATCTGGTCACGGAAGTACGTGGTATGCAAGAAGCCCCGGAGGCTATCCTATCCAAAGCTGTAGAGATTGAGGAGCAAACCAAACGGCTTCTAGAGGGCATGGAGCTGATAGTCAGCCAGGTTCATCCTGAAACCAAAGAAAATGAGATCTACCCTGTCTGGTCGGGACTTCCATCCCTGCAGATGGCTGATGAAGAGTCTCGCCTTTCTGCTTATTATAACCTGCTCCACTGCCTACGCAGGGATTCACATAAAATCGACAATTATCTCAAGCTCCTGAAGTGCCGAATCATCCACAACAACAACTGCTAAGCCCACATCCATTTCATCTATTTCTGAGAAGGTCCTTAATGATCCGTTCCATTGCAAGCTTCTTTTAGTTGTATCTCTTTTGAATCCATGCTTGGGTGTAACAGGTCTCCTCTTAAAAAATAAAAACTGACTCCTTAGAGACATCAAAATCTAAAA";
		System.out.println(translateDNA(seq, 1));
	}
	
	/**
	 * 
	 * @param nuc
	 * @param frame
	 * @return
	 */
	public static String translateDNA(String nuc, int frame) {
		if (frame < 0 || frame > 2) {
			return "Frame parameter should be between 0-2";
		}
		String protein = "";
		for (int i = frame; i < nuc.length() - 2; i = i + 3) {
			String codon = nuc.substring(i, i + 3);
			String aa = translateCodon(codon);
			protein += aa;
		}
		return protein;
	}
	public static String translateCodon(String codon) {
		codon = codon.replaceAll("U", "T").toUpperCase();
		if (codon.equals("TTT") || codon.equals("TTC")) {
			return "F";
		}
		if (codon.equals("TTA") || codon.equals("TTG") || codon.equals("CTT") || codon.equals("CTC") || codon.equals("CTA") || codon.equals("CTG")) {
			return "L";
		}
		if (codon.equals("ATT") || codon.equals("ATC") || codon.equals("ATA")) {
			return "I";
		}
		if (codon.equals("ATG")) {
			return "M";
		}
		if (codon.equals("GTT") || codon.equals("GTC") || codon.equals("GTA") || codon.equals("GTG")) {
			return "V";
		}
		if (codon.equals("TCT") || codon.equals("TCC") || codon.equals("TCA") || codon.equals("TCG")) {
			return "S";
		}
		if (codon.equals("CCT") || codon.equals("CCC") || codon.equals("CCA") || codon.equals("CCG")) {
			return "P";
		}
		if (codon.equals("ACT") || codon.equals("ACC") || codon.equals("ACA") || codon.equals("ACG")) {
			return "T";
		}
		if (codon.equals("GCT") || codon.equals("GCC") || codon.equals("GCA") || codon.equals("GCG")) {
			return "A";
		}
		if (codon.equals("TAT") || codon.equals("TAC")) {
			return "Y";
		}
		if (codon.equals("TAA") || codon.equals("TAG") || codon.equals("TGA")) {
			return "*";
		}
		if (codon.equals("TGG")) {
			return "W";
		}
		if (codon.equals("CAT") || codon.equals("CAC")) {
			return "H";
		}
		if (codon.equals("CAA") || codon.equals("CAG")) {
			return "Q";
		}
		if (codon.equals("AAT") || codon.equals("AAC")) {
			return "N";
		}
		if (codon.equals("AAA") || codon.equals("AAG")) {
			return "K";
		}
		if (codon.equals("GAT") || codon.equals("GAC")) {
			return "D";
		}
		if (codon.equals("GAA") || codon.equals("GAG")) {
			return "E";
		}
		if (codon.equals("TGT") || codon.equals("TGC")) {
			return "C";
		}
		if (codon.equals("CGT") || codon.equals("CGC") || codon.equals("CGA") || codon.equals("CGG")) {
			return "R";
		}
		if (codon.equals("AGT") || codon.equals("AGC")) {
			return "S";
		}
		if (codon.equals("GTT") || codon.equals("GGC") || codon.equals("GGA") || codon.equals("GGG")) {
			return "G";
		}
		return "X";
	}
    /**
     * From http://bioinformaticsalgorithms.blogspot.com/2015/03/reverse-complement-problem.html
     * Find the reverse complement of a given DNA string pattern.
     * @param pattern a dna string pattern.
     * @return reverse complement of pattern.
     */
    public static String reverse_complement(String pattern) {
        StringBuilder stringBuilder = new StringBuilder(pattern.length());
        char[] chars = pattern.toCharArray();
        int length = chars.length - 1;
        for (int i = length; i >= 0; i--) {
            switch (chars[i]) {
                case 'A':
                    stringBuilder.append('T');
                    break;
                case 'T':
                    stringBuilder.append('A');
                    break;
                case 'G':
                    stringBuilder.append('C');
                    break;
                case 'C':
                    stringBuilder.append('G');
                    break;
                default:
                	stringBuilder.append(chars[i]);
                    break;
            }
        }
        return stringBuilder.toString();
    }
}
