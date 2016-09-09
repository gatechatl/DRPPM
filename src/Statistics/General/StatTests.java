package Statistics.General;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * See HELP string or run with no arguments for usage.
 * <p>
 * The code used to calculate a Fisher p-value comes originally from a
 * <a href="http://infofarm.affrc.go.jp/~kadasowa/fishertest.htm">JavaScript program</a>
 * by T. Kadosawa (kadosawa@niaes.affrc.go.jp).
 *
 * @author David Hopwood
 * @date   2000/04/23
 */
public class StatTests {
    private static final String HELP =
        "Usage: java StatTests [-phi] [-fisher1] [-fisher2] filename [>outputfile]\n" +
        "\n" +
        "  -phi       Output a table of phi coefficients.\n" +
        "  -fisher1   Output a table of p-values for a 1-tailed\n" +
        "                 Fisher Exact Test.\n" +
        "  -fisher2   Output a table of p-values for a 2-tailed\n" +
        "                 Fisher Exact Test.\n" +
        "  filename   The name of the input file (\"-\" for standard input).\n" +
        "\n" +
        "At least one of -phi, -fisher1 and/or -fisher2 must be specified.\n" +
        "\n" +
        "Input file format:\n" +
        "\n" +
        "  Blank lines and lines starting with '#' are ignored.\n" +
        "  All other lines are in the form:\n" +
        "\n" +
        "    label ws ('0'|'1' [ws])*\n" +
        "\n" +
        "  where 'label' is a text label for the category, and 'ws' is a sequence\n" +
        "  of whitespace characters (spaces, tabs, or commas). The label may not\n" +
        "  contain whitespace.\n" +
        "\n" +
        "The code used to calculate a Fisher p-value comes originally from a\n" +
        "JavaScript program at http://infofarm.affrc.go.jp/~kadasowa/fishertest.htm,\n" +
        "by T. Kadosawa <kadosawa@niaes.affrc.go.jp>.\n";


    public static final int PHI_COEFFICIENT = 1;
    public static final int FISHER_1TAILED = 2;
    public static final int FISHER_2TAILED = 4;

    private static final int WIDTH = 7;
    private static final int DECIMALS = 3;

    public static void main(String[] args) throws Exception {
    	double[] pvals = {0.01, 0.02, 0.04, 0.01, 0.04};
    	double[] fdrs = BenjaminiHochberg(pvals);
    	for (int i = 0; i < pvals.length; i++) {
    		System.out.println(fdrs[i]);
    	}
    	System.out.println();
        /*int tests = 0;
        String filename = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-phi"))
                tests |= PHI_COEFFICIENT;
            else if (arg.equals("-fisher1") || arg.equals("-fisher"))
                tests |= FISHER_1TAILED;
            else if (arg.equals("-fisher2"))
                tests |= FISHER_2TAILED;
            else
                filename = arg;
        }

        if (filename == null || tests == 0) {
            System.err.println(HELP);
            System.exit(1);
        }

        StatTests obj = new StatTests();
        InputStream in = filename.equals("-") ? System.in : new FileInputStream(filename);
        obj.readData(in);
        in.close();
        obj.count();

        if ((tests & PHI_COEFFICIENT) != 0) {
            System.out.println("Phi coefficients:");
            System.out.println();
            obj.calculate(PHI_COEFFICIENT);
            obj.writeTable(System.out);
        }
        if ((tests & FISHER_1TAILED) != 0) {
            System.out.println("Significance using Fisher's Exact Test (1-tailed):");
            System.out.println();
            obj.calculate(FISHER_1TAILED);
            obj.writeTable(System.out);
        }
        if ((tests & FISHER_2TAILED) != 0) {
            System.out.println("Significance using Fisher's Exact Test (2-tailed):");
            System.out.println();
            obj.calculate(FISHER_2TAILED);
            obj.writeTable(System.out);
        }
        System.out.println("--");
        System.out.println("Calculated using StatTests.java,");
        System.out.println("(c) David Hopwood <hopwood@zetnet.co.uk>, 2000/04/23.");
        System.out.println("Based partly on code (c) T. Kadosawa <kadosawa@niaes.affrc.go.jp>.");
        System.out.println();
        */
    }

    /** Number of categories. */
    private int n;

    /** Number of records. */
    private int m;

    /** A label for each category. */
    private String[] label;

    /** The raw data (a boolean matrix of n rows by m columns). */
    private boolean[][] raw;

    /**
     * count1[i][j] is the number of records in category i but not in category j.
     */
    private int[][] count1;

    /**
     * count2[i][j] is the number of records in both category i and category j.
     * count2[i][i] is the sample size for category i.
     */
    private int[][] count2;

    /** The calculated results (a real lower triangular matrix of n rows x n columns). */
    private double[][] result;

    public StatTests() {}

    /**
     * Read data from the given InputStream.
     * <p>
     * Blank lines and lines starting with '#' are ignored.
     * All other lines are in the form:
     * <pre>
     *     label ws ('0'|'1' [ws])*
     * </pre>
     * where 'label' is a text label for the category, and 'ws' is a sequence
     * of whitespace characters (spaces, tabs, or commas). The label may not
     * contain whitespace.
     */
    public void readData(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);

        int row = 0;
        raw = new boolean[10][];
        label = new String[10];
        String str;
        char ch;

        for (int line = 1; (str = dis.readLine()) != null; line++) {
            int len = str.length();
            if (len == 0 || str.charAt(0) == '#') continue;

            if (row >= label.length) label = expand(label);
            if (row >= raw.length) raw = expand(raw);
            raw[row] = new boolean[row == 0 ? 10 : m];

            int p;
            for (p = 0; p < len; p++) {
                ch = str.charAt(p);
                if (ch == ' ' || ch == '\t' || ch == ',') break;
            }
            label[row] = str.substring(0, p);

            int col = 0;
            for (; p < len; p++) {
                ch = str.charAt(p);
                switch (ch) {
                  case ' ': case '\t': case ',':
                    break;

                  case '0': case '1':
                    if (col >= raw[row].length) raw[row] = expand(raw[row]);
                    raw[row][col++] = (ch == '1');
                    break;

                  default:
                    throw new IOException("Invalid character '" + ch + "' in input line " +
                        line + " (not 0, 1, or separator)");
                }
            }
            if (row == 0) {
                m = col;
            } else if (col != m) {
                throw new IOException("Inconsistent number of records (" + col + ", expected " +
                    m + ") in line " + line);
            }
            row++;
        }
        if (row == 0) throw new IOException("No data rows found");
        n = row;
    }

    /** Fill in the count1 and count2 arrays. */
    public void count() {
        // allocate triangular matrices
        result = new double[n][];
        count1 = new int[n][];
        count2 = new int[n][];
        for (int i = 0; i < n; i++) {
            result[i] = new double[i];
            count1[i] = new int[i];   // initially zeroes
            count2[i] = new int[i+1]; // initially zeroes
        }

        for (int r = 0; r < m; r++) { // for each record r...
            for (int i = 0; i < n; i++) { // for each category i...
                if (raw[i][r]) {
                    for (int j = 0; j <= i; j++) { // for each category j...
                        (raw[j][r] ? count2 : count1)[i][j]++;
                    }
                }
            }
        }
    }

    
    /**
     * Generate BenjaminiHochberg method
     */
    public static double[] BenjaminiHochberg(double[] pvalues) {
    	String[] pvalue_str = new String[pvalues.length];
    	String[] terms_strs = new String[pvalues.length];
    	int count = 0;
    	LinkedList list = new LinkedList();
    	for (double pval: pvalues) {    		    		
    		pvalue_str[count] = new Double(pvalues[count]).toString(); 
    		terms_strs[count] = "FAKEGO_" + count;
    		list.add(terms_strs[count]);
    		count++;
    	}
    	BenjaminiHochbergFDR fdr = new BenjaminiHochbergFDR(pvalue_str, terms_strs, "0.05");
    	fdr.calculate();
    	String[] fdr_strs = fdr.getAdjustedPvalues();
    	String[] go_label = fdr.getOrdenedGOLabels();
    	double[] fdr_vals = new double[fdr_strs.length];
    	Iterator itr = list.iterator();    	
    	while (itr.hasNext()) {
    		String terms = (String)itr.next();
	    	for (int i = 0; i < fdr_strs.length; i++) {
	    		//fdr_vals[i] = new Double(fdr_strs[i]);
	    		if (terms.equals(go_label[i])) {
	    			int index = new Integer(go_label[i].replaceAll("FAKEGO_", ""));
	    			fdr_vals[index] = new Double(fdr_strs[i]);
	    		}
	    	}
    	}
    	return fdr_vals;
    }
    /** Write the output table to a PrintStream. */
    public void writeTable(PrintStream out) {
        out.print(nspaces(13));
        out.println(formatRight(label[0], WIDTH-1));

        for (int i = 0; i < n; i++) {
            out.print(formatLeft(label[i], 12));
            out.print(' ');

            for (int j = 0; j < i; j++) {
                out.print(format(result[i][j], WIDTH-DECIMALS-2, DECIMALS));
                out.print(' ');
            }
            if (i < n-1) out.print(formatRight("------ ", WIDTH));
            if (i < n-2) out.print(formatRight(label[i+1], WIDTH-1));
            out.println();
        }
        out.println();
        out.println();
    }

    /** Calculate the result table for the given type of test. */
    public void calculate(int test) {
        double[] logFactorial = null;

        if (test != PHI_COEFFICIENT) {
            logFactorial = new double[m+1];
            logFactorial[0] = 0.0;
            for (int i = 1; i <= m; i++) {
                logFactorial[i] = logFactorial[i-1] + Math.log(i);
            }
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int c = count1[i][j];
                int d = count2[i][j];
                int sample_j = count2[j][j];
                int a = m - sample_j - c;
                int b = sample_j - d;

                switch (test) {
                  case PHI_COEFFICIENT:
                    result[i][j] = phi(a, b, c, d);
                    break;

                  case FISHER_1TAILED:
                  case FISHER_2TAILED:
                    result[i][j] = fisher(a, b, c, d, test, logFactorial);
                }
            }
        }
    }

    /** Calculate a phi coefficient. */
    public static double phi(int a, int b, int c, int d) {
        return (a*d - b*c)/Math.sqrt((a+b)*(c+d)*(a+c)*(b+d));
    }

    /** Calculate a p-value for Fisher's Exact Test. */
    public static double fisher(int a, int b, int c, int d, int test, double[] logFactorial) {
        if (a * d > b * c) {
            a = a + b; b = a - b; a = a - b; 
            c = c + d; d = c - d; c = c - d;
        }
        if (a > d) { a = a + d; d = a - d; a = a - d; }
        if (b > c) { b = b + c; c = b - c; b = b - c; }

        int a_org = a;
        double p_sum = 0.0d;

        double p = fisherSub(a, b, c, d, logFactorial);
        double p_1 = p;

        while (a >= 0) {
            p_sum += p;
            if (a == 0) break;
            --a; ++b; ++c; --d;
            p = fisherSub(a, b, c, d, logFactorial);
        }
        if (test == FISHER_1TAILED) return p_sum;

        a = b; b = 0; c = c - a; d = d + a;
        p = fisherSub(a, b, c, d, logFactorial);

        while (p < p_1) {
            if (a == a_org) break;
            p_sum += p;
            --a; ++b; ++c; --d;
            p = fisherSub(a, b, c, d, logFactorial);
        }
        return p_sum;
    }

    private static double fisherSub(int a, int b, int c, int d, double[] logFactorial) {
        return Math.exp(logFactorial[a + b] +
                        logFactorial[c + d] +
                        logFactorial[a + c] +
                        logFactorial[b + d] -
                        logFactorial[a + b + c + d] -
                        logFactorial[a] -
                        logFactorial[b] -
                        logFactorial[c] -
                        logFactorial[d]);
    }


// Utility methods

    private String[] expand(String[] a) {
        int len = a.length;
        String[] b = new String[len < 5 ? 10 : (len * 2)];
        System.arraycopy(a, 0, b, 0, len);
        return b;
    }

    private boolean[] expand(boolean[] a) {
        int len = a.length;
        boolean[] b = new boolean[len < 5 ? 10 : (len * 2)];
        System.arraycopy(a, 0, b, 0, len);
        return b;
    }

    private boolean[][] expand(boolean[][] a) {
        int len = a.length;
        boolean[][] b = new boolean[len < 5 ? 10 : (len * 2)][];
        System.arraycopy(a, 0, b, 0, len);
        return b;
    }

    /**
     * Returns the string <i>s</i> left-justified to exactly <i>width</i> characters.
     */
    public static String formatLeft(String s, int width) {
        int slen = s.length();
        return (slen < width) ? (s + nspaces(width-slen)) : s.substring(0, width);
    }

    /**
     * Returns the string <i>s</i> right justified to <i>width</i> characters.
     */
    public static String formatRight(String s, int width) {
        int slen = s.length(); 
        return (slen < width) ? (nspaces(width-slen) + s) : s;
    }

    /**
     * Returns a decimal string representation of <i>l</i>, padded with
     * spaces to <i>width</i> characters.
     */
    public static String format(long l, int width) {
        String s = Long.toString(l);
        int slen = s.length();
        return (slen < width) ? (nspaces(width-slen) + s) : s;
    }

    /**
     * Returns a decimal string representation of <i>d</i>, padded with
     * spaces to <i>width</i> characters before the decimal point, and
     * <i>decimals</i> characters after the decimal point.
     */
    public static String format(double d, int width, int decimals) {
        return format(Double.toString(d), width, decimals);
    }

    private static String format(String s, int width, int decimals) {
        int slen = s.length();
        int p = s.indexOf("E");
        if (p != -1)
            return format(s.substring(0, p), width-slen+p, decimals) + s.substring(p);

        p = s.indexOf(".");
        if (p == -1) {
            if (s.equals("NaN")) return formatRight(s, width+1+decimals);
            return nspaces(width-slen) + s + nspaces(decimals+1);
        }

        if (p < width) {
            s = nspaces(width-p) + s;
            slen = s.length();
            p = width;
        }
        if (p > slen-decimals-1)
            return s + nspaces(p+decimals+1-slen);

        return s.substring(0, p+decimals+1);
    }

    /**
     * Returns a string consisting of <i>n</i> spaces (or "" if <i>n</i> < 0).
     */
    public static String nspaces(int n) {
        if (n < 0) return "";
        if (n <= 15) return "               ".substring(0, n);
        char[] c = new char[n];
        for (int i = 0; i < n; i++)
            c[i] = ' ';
        return new String(c);
    }
}