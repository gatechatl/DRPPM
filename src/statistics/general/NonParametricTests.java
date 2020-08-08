package statistics.general;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.data.structures.sequence.StringSequence;
import javanpst.tests.equality.extendedMedianTest.ExtendedMedianTest;
import javanpst.tests.equality.kruskalWallisTest.KruskalWallisTest;
import javanpst.tests.randomness.numberRunsTest.NumberRunsTest;


/**
 * Calls the nonparametric tests
 * @author tshaw
 *
 */
public class NonParametricTests {

	
	public static void TestStringSequence(){
		
		/**
		  *  Sequence to test
		  */
		String sample [] = {"A","A","B","A","B","B","A","A","A","B"};
		
		//Create data structure
		StringSequence data = new StringSequence(sample);

		//Create test.
		NumberRunsTest test = new NumberRunsTest(data);
		
		//Run test
		test.doTest();
		
		//Print results 
		System.out.println("\nResults of Number of Runs test:\n"+test.printReport());

				
	}//end-method
	public static void main(String args []){
		
		/**
		 * K independant samples:   
		 */
		double samples [][] = {{19,14,3,38},
								{22,15,1,39},
	/*							{25,2,5,40},
								{24,6,8,30},
								{29,10,4,31},
								{26,16,13,32},
								{37,17,9,33},
								{23,11,15,36},
								{27,18,3,34},
								{28,7,20,3}*/
								};
	
		//Data is formatted
		DataTable data = new DataTable(samples);
	
		//Create tests
		ExtendedMedianTest median = new ExtendedMedianTest(data);
	
		KruskalWallisTest kw = new KruskalWallisTest(data);
	
		//Run Extended Median test
		median.doTest();
		
		//Print results of Extended Median test
		System.out.println("Results of Extended Median test:\n"+median.printReport());
		
		System.out.println();
		System.out.println("*******************");
		System.out.println("*******************");
		System.out.println("*******************");
		System.out.println();
		
		//Run Kruskal-Wallis test
		kw.doTest();

		//Print results of Kruskal-Wallis test
		System.out.println("Results of Kruskal-Wallis test:\n"+kw.printReport());
		
		//print results of the multiple comparisons procedure
		System.out.println("Results of the multiple comparisons procedure:\n"+kw.printMultipleComparisonsProcedureReport());
		
	}//end-method
}
