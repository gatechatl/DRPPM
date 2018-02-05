package bedtools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Code19 {
    public static void main(String[] args) {
          String [] names = new String [10];
          double [] numbers = new double [10];
          int n = 0;
          try {
                Scanner inFile = new Scanner(new File("C:\\Users\\tshaw\\misc\\test.txt"));              
                while (inFile.hasNext()) {                	
                    names[n] = inFile.next();
                    numbers[n] = inFile.nextDouble();
                    n++;
                }
                inFile.close();
          } catch (FileNotFoundException e) {
                System.out.println("No file");
                System.exit(1);
          }
    }
}
