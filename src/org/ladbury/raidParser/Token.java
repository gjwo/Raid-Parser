package org.ladbury.raidParser;

/**
 * A token that is produced by Tokenizer and fed into Parser.parse
 * 
 * A token consists of a token identifier, a string that the token was
 * created from and the position in the input string that the token was found.
 * 
 * The token id must be one of a number of pre-defined values
 */

public class Token implements Cloneable{
	
	public static final int END_OF_INPUT	= 0;
	public static final int OPEN_CURLY 		= 1;
	public static final int CLOSE_CURLY 	= 2;
	public static final int OPEN_SQUARE 	= 3;
	public static final int CLOSE_SQUARE 	= 4;
	public static final int COMMA 			= 5;
	public static final int NUMBER 			= 6;
	public static final int RAID_TYPE		= 7;

	 /** the token identifier */
	  public final int token;
	  /** the string that the token was created from */
	  public final String sequence;
	  /** the position of the token in the input string */
	  public final int pos;

	  /**
	   * Construct the token with its values
	   * @param token the token identifier
	   * @param sequence the string that the token was created from
	   * @param pos the position of the token in the input string
	   */
	  public Token(int token, String sequence, int pos)
	  {
	    super();
	    this.token = token;
	    this.sequence = sequence;
	    this.pos = pos;
	  }
}
