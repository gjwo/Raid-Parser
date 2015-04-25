/**
 * 
 */
package org.ladbury.raidParser;

import org.ladbury.raid.RaidArray;

/**
 * @author GJWood
 *
 */
public class Test {

	  /**
	   * The main method to test the functionality of the parser
	   */
	  public static void main(String[] args)
	  {
	    
	    String raidstr = "raid5{[1,1,1],raid1{[4,4,4],raid1{[4,4,4],[5,5,5]}},[2,2,2],[3,3,3]}";
	    RaidArray raid;
	    if (args.length>0) raidstr = args[0];
	    
	    Parser parser = new Parser();
	    
	    try
	    {
	      raid = parser.parse(raidstr);
	      System.out.println(raid.toString());
	      System.out.println(raid.getType()+
	    		  "{"+raid.getCapacity() +"; "+raid.getReadSpeed()+"; "+raid.getWriteSpeed()+"}");      
	    }
	    catch (ParserException e)
	    {
	      System.out.println(e.getMessage());
	    }
	  }

}
