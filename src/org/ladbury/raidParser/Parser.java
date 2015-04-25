package org.ladbury.raidParser;

// A simple recursive parser for the following grammar
// grammar is case insensitive
// <raidArray> ::= <raidName>{<raidMembers>}
// <raidMembers>	::= <raidMember>,<raidMembers>|
//						<raidMember>
// <raidMember>     ::= <driveDefinition>|
//						<raidArray>
// <raidType>		::=	RAID0|RAID1|RAID5|RAID6
// <driveDefinition>::= [<number>,<number>,<number>]
// <number>			::= <nonZeroDigit>|
//						<nonZeroDigit><digit>|
//						<nonZeroDigit><number>
// nonZeroDigit		::= 1|2|3|4|5|6|7|8|9
// digit			::= 0|<nonZeroDigit>

import java.util.LinkedList;
import java.util.List;

import org.ladbury.raid.Drive;
import org.ladbury.raid.RaidArray;

public class Parser {

	LinkedList<Token> tokens;
	Token lookahead;
	RaidArray rootRaid;
	
	public Parser() {
		// all variables are initialised when parse is called
	}

	public RaidArray parse(String raidInput) throws ParserException
	  {
	    Tokenizer tokenizer = Tokenizer.getRaidTokenizer(); //create the tokenizer for raid definitions
	    tokenizer.tokenize(raidInput); //run the tokenizer on the input string
	    LinkedList<Token> tokens = tokenizer.getTokens(); 
	    //System.out.println(tokens.toString());  
	    return this.parse(tokens); // run the parser on the list of tokens
	  }
	
	public RaidArray parse(List<Token> tokens) throws ParserException
	  {
		RaidArray raid;
	    this.tokens = new LinkedList<Token>(tokens); //clone the list of tokens
	    lookahead = this.tokens.getFirst();
		if (lookahead.token == Token.RAID_TYPE){
			//System.out.println( "Raid type: "+lookahead.sequence);
		    raid = raidArray(rootRaid);
		}
		else
			throw new ParserException("Unexpected symbol %s found Raid Type expected", lookahead);
	    if (lookahead.token != Token.END_OF_INPUT)
	      throw new ParserException("Unexpected symbol %s found", lookahead);
	    return raid;
	  }
	
	private void nextToken()
	  {
	    tokens.pop();
	    // at the end of input we return an end of input token
	    if (tokens.isEmpty())
	      lookahead = new Token(Token.END_OF_INPUT, "",-1);
	    else
	      lookahead = tokens.getFirst();
	  }
	
	private RaidArray raidArray(RaidArray raid) throws ParserException
	  {
		RaidArray currentRaid;
		//System.out.println( "<raidArray>");
		currentRaid = new RaidArray(lookahead.sequence);
		if (raid == null)
			raid = currentRaid;
		else
			raid.addRaidMember(currentRaid);
		
	    raidType(currentRaid);
	    if (lookahead.token ==Token.OPEN_CURLY)
	    	nextToken();
	    else
	    	throw new ParserException("Unexpected symbol %s found { expected", lookahead);
	    raidMembers(currentRaid);
	    if (lookahead.token ==Token.CLOSE_CURLY)
	    	nextToken();
	    else
	    	throw new ParserException("Unexpected symbol %s found } expected", lookahead);
	    return raid;
	  }
	private void raidType(RaidArray raid) throws ParserException
	  {
		//System.out.println( "<raidType>");
		if (lookahead.token == Token.RAID_TYPE){
			//System.out.println( "Raid type: "+lookahead.sequence);
			raid.setType(lookahead.sequence);
			nextToken();
		}
		else
			throw new ParserException("Unexpected symbol %s found Raid Type expected", lookahead);
	  }

	private void raidMembers(RaidArray raid) throws ParserException
	  {
		//System.out.println( "<raidMembers>");
		raidMember(raid);
		switch(lookahead.token)
		{
		case Token.COMMA: 
			nextToken();
			raidMembers(raid);
			break;
		case Token.CLOSE_CURLY : 
			break;
		default: throw new ParserException("Unexpected symbol %s found \",\" or } expected", lookahead);
	    }
	  }
	
	private void raidMember(RaidArray raid) throws ParserException
	  {
		//System.out.println( "<raidMember>");
		switch(lookahead.token)
		{
		case Token.RAID_TYPE: raidArray(raid);
			break;
		case Token.OPEN_SQUARE : raid.addRaidMember(DriveDefinition());
			break;
		default: throw new ParserException("Unexpected symbol %s found Raid type or drive definition expected", lookahead);
	    }
   
	  }
	
	private Drive DriveDefinition() throws ParserException
	  { int capacity, readSpeed, writeSpeed;
		//System.out.println( "<DriveDefinition>");
	    nextToken(); //consume the "["
	    capacity = number();
	    if (lookahead.token == Token.COMMA)
	    	nextToken();//consume the ","
	    else
	    	throw new ParserException("Unexpected symbol %s found \",\" expected", lookahead);
	    readSpeed = number();
	    if (lookahead.token == Token.COMMA)
	    	nextToken();//consume the ","
	    else
	    	throw new ParserException("Unexpected symbol %s found \",\" expected", lookahead);

	    writeSpeed = number();
	    if (lookahead.token == Token.CLOSE_SQUARE)
	    	nextToken();//consume the ","
	    else
	    	throw new ParserException("Unexpected symbol %s found \",\" expected", lookahead);
	    //System.out.println( "Drive [Capacity:"+capacity+
	    //					" Read speed: "+readSpeed+
	    //					" Write speed: "+writeSpeed+"]");
	    return new Drive(capacity,readSpeed,writeSpeed);
	  }

	private int number() throws ParserException
	  {
		int i;
		//System.out.println( "<number>");
	    if (lookahead.token == Token.NUMBER){
	    	i= Integer.parseInt(lookahead.sequence);
	    	nextToken();//consume the number
	    	return i;
	    }
	    else
	    	throw new ParserException("Unexpected symbol %s found number expected", lookahead);
    
	  }
}
