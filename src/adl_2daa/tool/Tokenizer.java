package adl_2daa.tool;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import adl_2daa.internal.Token;
import adl_2daa.internal.TokenType;

public class Tokenizer {
	
	private List<Token> tokenList = new ArrayList<Token>();
	private List<Integer> lineLastToken = new ArrayList<Integer>();
	private int pointer = 0;
	
	/**
	 * Tokenize input code into a buffer
	 */
	public Tokenizer(String input) throws Exception{
		//Check for UTF8 header
		if(input.charAt(0) == 65279){
			//65279(0xFEFF)
			input = input.substring(1);
		}else if(input.charAt(0) == 3663){
			//3663(0xE4F) , 3611(0xE1B) , 3615(0xE1F) -- java -jar cmd recognize these char instead of above (recognized by eclipse)
			input = input.substring(3);
		}
		pointer = 0;
		Matcher m;
		boolean matched = false;
		while(input.length() > 0){
			for(TokenType tt : TokenType.values()){
				matched = false;
				m = tt.pattern.matcher(input);
				if(m.find()){
					matched = true;
					
					if(tt != TokenType.EMPTY){
						if(tt == TokenType.LINEEND){
							lineLastToken.add(tokenList.size());
						}else if(tt == TokenType.STRING){
							String str = m.group().trim();
							String newStr = "";
							for(int i=0; i<str.length()-1; i++){
								if(str.charAt(i) == '\\'){
									i++;
									switch(str.charAt(i)){
									case 'n': newStr += "\n"; break;
									case 't': newStr += "\t"; break;
									case '\"': newStr += "\""; break;
									case '\\': newStr += "\\"; break;
									default:
										newStr += str.charAt(i);
									}
								}else{
									newStr += str.charAt(i);
									if(i == str.length()-2)
										newStr += str.charAt(i+1);
								}
							}
							newStr = newStr.substring(1, newStr.length()-1);
							tokenList.add(new Token(tt, newStr));
						}else{
							tokenList.add(new Token(tt, m.group().trim()));
						}
					}
					
					//System.out.println(tt+": "+m.group().trim());
					input = m.replaceFirst("");
					break;
				}
			}
			if(!matched)
				throw new Exception("Tokenizing error at : "+input.substring(0,Math.min(20, input.length())));
		}
	}
	
	/**
	 * Return true if next token is available
	 */
	public boolean hasNext(){
		return pointer < tokenList.size();
	}
	
	/**
	 * Return current token
	 */
	public Token getToken(){
		if(pointer < 0 || pointer >= tokenList.size()) return null;
		return tokenList.get(pointer);
	}
	
	/**
	 * Return current token and advance pointer
	 */
	public Token getAndNext(){
		return tokenList.get(pointer++);
	}
	
	/**
	 * Return pointing index
	 */
	public int getPointer(){
		return pointer;
	}
	
	/**
	 * Get previous token (related to getNext()) by decrease pointer and get data at pointer-1 
	 */
	public Token previousToken(){
		pointer--;
		return tokenList.get(pointer-1);
	}
	
	/**
	 * Place pointer at the specified location, return the token before the pointer location
	 */
	public Token seek(int pointer){
		this.pointer = pointer;
		if(pointer < 1 || pointer > tokenList.size()) return null;
		return tokenList.get(pointer-1);
	}
	
	/**
	 * Push token at the pointer location
	 */
	public void pushToken(Token tok){
		if(pointer < 0 || pointer >= tokenList.size()) return;
		tokenList.add(pointer, tok);
	}
	
	/**
	 * Return code line number of current token
	 */
	public int getLineNumber(){
		int line = 0;
		while(pointer >= lineLastToken.get(line)){
			line++;
		}
		return line+1;
	}
	
	public String dumpTokenizedString(){
		StringBuilder strb = new StringBuilder();
		int counter = 0;
		int lineend = 0;
		for(Token tok : tokenList){
			strb.append(tok.toString()).append(" ");
			counter++;
			if(lineend < lineLastToken.size() && counter >= lineLastToken.get(lineend)){
				strb.append("\r\n");
				lineend++;
			}
		}
		return strb.toString();
	}
}
