package adl_2daa.internal;

public enum Opcode{
	NOP, //No operation  ()
	PUSH, //Push data onto a stack  (StackDatatype type, Object data) -- (INT/FLOAT/STRING,int/float/string)  (VAR/string)
	POP, //Pop data from stack and throw it away  ()
	//ASSIGN, //Pop data from stack and store in variable  (String varname)
	U_OP, //Pop data from stack, do operation and push result onto a stack. (OpParam op)
	B_OP, //Pop 2 data from stack, do operation and push result onto a stack. first pop = left side  (OpParam op)
	COMPARE, //Pop 2 data from stack, do comparison and push result onto a stack. first pop = left side  (OpParam op)
	FUNC, //Call specified function, parameters are passed by stack, first pop = first parameter. Return value are pushed onto stack (String funcname,int paramsNum)
	ACTION, //Do the same thing as FUNC but with spanned action supported and no return
	JUMP, //Real IJUMP (int address)
	BNEQ, //Real IBNEQ (int address)
	BREAK, //Stop interpreter
	STARTACTION,
	ENDACTION,
	//======= Internal opcode =======
	IJUMP, //Jump to specified label (int labelname)
	IBNEQ, //Pop data from stack, if false -- jump to specified label (int labelname)
	ILABEL //Create a label (int labelname)
};