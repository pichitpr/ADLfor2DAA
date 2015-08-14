package adl_2daa.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import adl_2daa.tool.Parser;

public class Test {

	public static void main(String[] args) throws Exception{
		File f = new File("G:\\libgdx\\Script\\Megaman 6\\TomahawkMan.txt");
		BufferedReader buf = new BufferedReader(new FileReader(f));
		String script = "";
		String line;
		while((line=buf.readLine()) != null){
			script += line+"\n";
		}
		buf.close();
		script = script.trim();
		Parser p = new Parser();
		p.parse(script);
	}
}
