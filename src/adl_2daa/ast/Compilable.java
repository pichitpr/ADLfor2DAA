package adl_2daa.ast;

import java.util.List;

import adl_2daa.internal.Instruction;
import adl_2daa.tool.ADLCompiler;

public interface Compilable {
	public void compile(List<Instruction> ins, ADLCompiler compiler);
}
