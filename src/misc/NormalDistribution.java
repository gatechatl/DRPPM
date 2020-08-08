package misc;

public class NormalDistribution {

	
	/** * @(#)qnorm.js * * Copyright (c) 2000 by Sundar Dorai-Raj
	  * * @author Sundar Dorai-Raj
	  * * Email: sdoraira@vt.edu
	  * * This program is free software; you can redistribute it and/or
	  * * modify it under the terms of the GNU General Public License 
	  * * as published by the Free Software Foundation; either version 2 
	  * * of the License, or (at your option) any later version, 
	  * * provided that any use properly credits the author. 
	  * * This program is distributed in the hope that it will be useful,
	  * * but WITHOUT ANY WARRANTY; without even the implied warranty of
	  * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	  * * GNU General Public License for more details at http://www.gnu.org * * */

	public static double qnorm(double p) {
		
		// ALGORITHM AS 111, APPL.STATIST., VOL.26, 118-121, 1977.
		    // Computes z=invNorm(p)
		   double r = Double.NaN;
		   double ppnd = Double.NaN;
		   double split=0.42;
		   double  a0=  2.50662823884;
		   double  a1=-18.61500062529;
		   double  a2= 41.39119773534;
		   double  a3=-25.44106049637;
		   double  b1= -8.47351093090;
		   double  b2= 23.08336743743;
		   double  b3=-21.06224101826;
		   double  b4=  3.13082909833;
		   double  c0= -2.78718931138;
		   double  c1= -2.29796479134;
		   double  c2=  4.85014127135;
		   double  c3=  2.32121276858;
		   double  d1=  3.54388924762;
		   double  d2=  1.63706781897;
		   double  q=p-0.5;
		   if (Math.abs(q)<=split) {
		      r=q*q;
		      ppnd=q*(((a3*r+a2)*r+a1)*r+a0)/((((b4*r+b3)*r+b2)*r+b1)*r+1);
		   } else {
		      r=p;
		      if(q>0) r=1-p;
		      if(r>0) {
		        r=Math.sqrt(-Math.log(r));
		        ppnd=(((c3*r+c2)*r+c1)*r+c0)/((d2*r+d1)*r+1);
		        if(q<0) ppnd=-ppnd;
		      }
		      else {
		        ppnd=0;
		      }
		    }
		    return(ppnd);
	}	
}
