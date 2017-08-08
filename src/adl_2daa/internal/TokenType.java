package adl_2daa.internal;
import java.util.regex.Pattern;


public enum TokenType {
	FLOAT("^[0-9]+\\.[0-9]+(E[0-9]+)?",false),
	INT("^[0-9]+",false),
	STRING("^\"(?:[^\"\\\\]|\\\\.)*\"",false), //http://stackoverflow.com/questions/249791/regex-for-quoted-string-with-escaping-quotes
	RESERVED("^(true|false|loop|if|else(\\s*if)?)",true),
	FUNC("^[a-zA-Z_]\\w*",false),
	IDEN("^\\.[a-zA-Z_]\\w*",false),
	L_PAREN("^\\(",false),
	R_PAREN("^\\)",false),
	L_BRACE("^\\{",false),
	R_BRACE("^\\}",false),
	OPERATOR("^(\\&{1,2}+| and |\\|{1,2}+| or |>>|<<|\\^|\\!(?!=)|\\+|-|\\*|/|%)",true),
	COMPARATOR("^(==|!=|>=|<=|>|<)",true),
	COMMA("^,",false),
	SINGLEQUERY("^\\$",false),
	END("^;",false),
	LINEEND("^\\n",false),
	EMPTY("^[^\\S\\n]+",false);
	
	public final Pattern pattern;
	public final boolean hasSubType;
	
	private TokenType(String regex,boolean hasSubType){
		pattern = Pattern.compile(regex);
		this.hasSubType = hasSubType;
	}
}
