package adl_2daa.internal;

public class Token {

	public TokenType type;
	public String value;
	
	public Token(TokenType type,String value){
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String toString(){
		String str = type.toString()+": ";
		if(value != null) str += value;
		return str;
	}
}
