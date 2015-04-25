package org.ladbury.raidParser;

/*
 * This software and all files contained in it are distributed under the MIT license.
 * 
 * Copyright (c) 2013 Cogito Learning Ltd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for reading an input string and separating it into tokens that can be
 * fed into Parser.
 * 
 * The user can add regular expressions that will be matched against the front
 * of the string. Regular expressions should not contain beginning-of-string or
 * end-of-string anchors or any capturing groups as these will be added by the
 * tokenizer itself.
 */
public class Tokenizer
{
  /**
   * Internal class holding the information about a token type.
   */
  private class TokenInfo
  {
    /** the regular expression to match against */
    public final Pattern regex;
    /** the token id that the regular expression is linked to */
    public final int token;

    /**
     * Construct TokenInfo with its values
     */
    public TokenInfo(Pattern regex, int token)
    {
      super();
      this.regex = regex;
      this.token = token;
    }
  }

  /** 
   * a list of TokenInfo objects
   * 
   * Each token type corresponds to one entry in the list
   */
  private LinkedList<TokenInfo> tokenInfos;
  
  /** the list of tokens produced when tokenizing the input */
  private LinkedList<Token> tokens;

  /** a tokenizer that can handle mathematical expressions */
  private static Tokenizer raidTokenizer = null;

  /**
   * Default constructor
   */
  public Tokenizer()
  {
    super();
    tokenInfos = new LinkedList<TokenInfo>();
    tokens = new LinkedList<Token>();
  }

  /**
   * A static method that returns a tokenizer for raid definitions
   * @return a tokenizer that can handle raid definitions
   */
  public static Tokenizer getRaidTokenizer()
  {
    if (raidTokenizer == null)
      raidTokenizer = createRaidTokenizer();
    return raidTokenizer;
  }

  /**
   * A static method that actually creates a tokenizer for raid definitions
   * @return a tokenizer that can handle raid definitions
   */
  private static Tokenizer createRaidTokenizer()
  {
    Tokenizer tokenizer = new Tokenizer();

    tokenizer.add("\\{", Token.OPEN_CURLY);
    tokenizer.add("\\}", Token.CLOSE_CURLY);
    tokenizer.add("\\[", Token.OPEN_SQUARE);
    tokenizer.add("\\]", Token.CLOSE_SQUARE);
    tokenizer.add(",", Token.COMMA);
    tokenizer.add("Raid\\d",Token.RAID_TYPE);
//    tokenizer.add("(?:\\d+\\.?|\\.\\d)\\d*(?:[Ee][-+]?\\d+)?", Token.NUMBER);//with exponents etc
//    tokenizer.add("(?:\\d+\\.?|\\.\\d)", Token.NUMBER); //with decimal places
    tokenizer.add("(?:\\d+)", Token.NUMBER); // integer
 
    return tokenizer;
  }

  /**
   * Add a regular expression and a token id to the internal list of recognized tokens
   * @param regex the regular expression to match against 
   * @param token the token id that the regular expression is linked to
   */
  public void add(String regex, int token)
  {
    tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex+")",Pattern.CASE_INSENSITIVE), token));
  }

  /**
   * Tokenize an input string.
   * 
   * The reult of tokenizing can be accessed via getTokens
   * 
   * @param str the string to tokenize
 * @throws ParserException 
   */
  public void tokenize(String str) throws ParserException
  {
    String s = str.trim();
    int totalLength = s.length();
    tokens.clear();
    while (!s.equals(""))
    {
      int remaining = s.length();
      boolean match = false;
      for (TokenInfo info : tokenInfos)
      {
        Matcher m = info.regex.matcher(s);
        if (m.find())
        {
          match = true;
          String tok = m.group().trim();
          // System.out.println("Success matching " + s + " against " +
          // info.regex.pattern() + " : " + tok);
          s = m.replaceFirst("").trim();
          tokens.add(new Token(info.token, tok, totalLength - remaining));
          break;
        }
      }
      if (!match)
        throw new ParserException("Unexpected character in input: " + s);
    }
  }

  /**
   * Get the tokens generated in the last call to tokenize.
   * @return a list of tokens to be fed to Parser
   */
  public LinkedList<Token> getTokens()
  {
    return tokens;
  }
}
