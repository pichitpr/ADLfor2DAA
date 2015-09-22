package adl_2daa.tool;

import java.util.ArrayList;
import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.ast.ASTStatement;
import adl_2daa.ast.expression.ASTUnary;
import adl_2daa.ast.expression.And;
import adl_2daa.ast.expression.Arithmetic;
import adl_2daa.ast.expression.Bitwise;
import adl_2daa.ast.expression.BooleanConstant;
import adl_2daa.ast.expression.Comparison;
import adl_2daa.ast.expression.FloatConstant;
import adl_2daa.ast.expression.Function;
import adl_2daa.ast.expression.Identifier;
import adl_2daa.ast.expression.IntConstant;
import adl_2daa.ast.expression.Or;
import adl_2daa.ast.expression.StringConstant;
import adl_2daa.ast.statement.Action;
import adl_2daa.ast.statement.Condition;
import adl_2daa.ast.statement.Loop;
import adl_2daa.ast.structure.Agent;
import adl_2daa.ast.structure.Root;
import adl_2daa.ast.structure.Sequence;
import adl_2daa.ast.structure.State;
import adl_2daa.internal.Token;
import adl_2daa.internal.TokenType;


public class Parser {

	private Tokenizer tokenizer;
	
	public Root parse(String input) throws Exception{
		List<Agent> agents = new ArrayList<Agent>();
		tokenizer = new Tokenizer(input);
		while(tokenizer.hasNext()){
			agents.add(nextAgent());
		}
		return new Root(agents);
	}
	
	private Token matchAndNext(TokenType expected) throws Exception{
		if(tokenizer.getToken().type != expected){
			throw new Exception("Line "+tokenizer.getLineNumber()+
					" : Expecting "+expected.name()+
					", Found "+tokenizer.getToken());
		}
		return tokenizer.getAndNext();
	}
	
	private Agent nextAgent() throws Exception{
		String agentName = matchAndNext(TokenType.IDEN).value.substring(1);
		matchAndNext(TokenType.L_BRACE);
		Token tok = tokenizer.getToken();
		Sequence init = null, des = null;
		List<State> states = new ArrayList<State>();
		String identifier;
		while(tok.type == TokenType.IDEN){
			identifier = tok.value.substring(1);
			if(identifier.equals("init")){
				init = nextSequence();
			}else if(identifier.equals("des")){
				des = nextSequence();
			}else{
				states.add(nextState());
			}
			tok = tokenizer.getToken();
		}
		matchAndNext(TokenType.R_BRACE);
		return new Agent(agentName, init, des, states);
	}
	
	private State nextState() throws Exception{
		String stateName = matchAndNext(TokenType.IDEN).value.substring(1);
		matchAndNext(TokenType.L_BRACE);
		List<Sequence> sequences = new ArrayList<Sequence>();
		while(tokenizer.getToken().type == TokenType.IDEN){
			sequences.add(nextSequence());
		}
		matchAndNext(TokenType.R_BRACE);
		return new State(stateName, sequences);
	}
	
	private Sequence nextSequence() throws Exception{
		String sequenceName = matchAndNext(TokenType.IDEN).value.substring(1);
		matchAndNext(TokenType.L_BRACE);
		List<ASTStatement> sequenceBody = nextStatements();
		matchAndNext(TokenType.R_BRACE);
		return new Sequence(sequenceName, sequenceBody);
	}
	
	private List<ASTStatement> nextStatements() throws Exception{
		ASTStatement statement;
		List<ASTStatement> statements = new ArrayList<ASTStatement>();
		while((statement = nextStatement()) != null){
			statements.add(statement);
		}
		return statements;
	}
	
	private ASTStatement nextStatement() throws Exception{
		Token tok = tokenizer.getToken();
		if(tok.type == TokenType.RESERVED && tok.value.equalsIgnoreCase("if")){
			return nextCondition();
		}else if(tok.type == TokenType.RESERVED && tok.value.equalsIgnoreCase("loop")){
			return nextLoop();
		}else if(tok.type == TokenType.FUNC){
			return nextAction();
		}else if(tok.type == TokenType.R_BRACE){
			return null;
		}
		throw new Exception("Line "+tokenizer.getLineNumber()+
				" : Expecting statement, Found "+tok);
	}
	
	private Condition nextCondition() throws Exception{
		matchAndNext(TokenType.RESERVED);
		ASTExpression booleanExp = nextExpression();
		matchAndNext(TokenType.L_BRACE);
		List<ASTStatement> ifbody = nextStatements();
		matchAndNext(TokenType.R_BRACE);
		List<ASTStatement> elsebody = null;
		Token tok = tokenizer.getToken();
		if(tok.type == TokenType.RESERVED && tok.value.equalsIgnoreCase("else")){
			matchAndNext(TokenType.RESERVED);
			matchAndNext(TokenType.L_BRACE);
			elsebody = nextStatements();
			matchAndNext(TokenType.R_BRACE);
		}
		return new Condition(booleanExp, ifbody, elsebody);
	}
	
	private Loop nextLoop() throws Exception{
		matchAndNext(TokenType.RESERVED);
		matchAndNext(TokenType.L_PAREN);
		ASTExpression intExpression = nextExpression();
		matchAndNext(TokenType.R_PAREN);
		matchAndNext(TokenType.L_BRACE);
		List<ASTStatement> content = nextStatements();
		matchAndNext(TokenType.R_BRACE);
		return new Loop(intExpression, content);
	}
	
	private Action nextAction() throws Exception{
		String actionName = matchAndNext(TokenType.FUNC).value;
		List<ASTExpression> paramList = nextParameterList();
		matchAndNext(TokenType.END);
		return new Action(actionName, paramList);
	}
	
	private List<ASTExpression> nextParameterList() throws Exception{
		matchAndNext(TokenType.L_PAREN);
		List<ASTExpression> paramList = new ArrayList<ASTExpression>();
		if(tokenizer.getToken().type != TokenType.R_PAREN){
			paramList.add(nextExpression());
			while(tokenizer.getToken().type == TokenType.COMMA){
				matchAndNext(TokenType.COMMA);
				paramList.add(nextExpression());
			}
		}
		matchAndNext(TokenType.R_PAREN);
		return paramList;
	}
	
	private ASTExpression nextExpression() throws Exception{
		return nextAnd();
	}
	
	// Predecence (Highest > Lowest)
	// () , unary - ! , | & ^  , * / , + - , compare , || , &&
	
	private ASTExpression nextAnd() throws Exception{
		ASTExpression and = nextOr();
		Token tok = tokenizer.getToken();
		while(tok.type == TokenType.OPERATOR && tok.value.equals("&&")){
			matchAndNext(TokenType.OPERATOR);
			and = new And(and, nextOr());
			tok = tokenizer.getToken();
		}
		return and;
	}
	
	private ASTExpression nextOr() throws Exception{
		ASTExpression or = nextComparison();
		Token tok = tokenizer.getToken();
		while(tok.type == TokenType.OPERATOR && tok.value.equals("||")){
			matchAndNext(TokenType.OPERATOR);
			or = new Or(or, nextComparison());
			tok = tokenizer.getToken();
		}
		return or;
	}
	
	private ASTExpression nextComparison() throws Exception{
		ASTExpression node = nextAddSub();
		Token tok = tokenizer.getToken();
		while(tok.type == TokenType.COMPARATOR){
			node = new Comparison(node, matchAndNext(TokenType.COMPARATOR).value,
					nextAddSub());
			tok = tokenizer.getToken();
		}
		return node;
	}
	
	private ASTExpression nextAddSub() throws Exception{
		ASTExpression node = nextMulDiv();
		Token tok = tokenizer.getToken();
		while(tok.type == TokenType.OPERATOR && 
				(tok.value.equals("+") || tok.value.equals("-"))){
			node = new Arithmetic(node, matchAndNext(TokenType.OPERATOR).value,
					nextMulDiv());
			tok = tokenizer.getToken();
		}
		return node;
	}
	
	private ASTExpression nextMulDiv() throws Exception{
		ASTExpression node = nextBitwise();
		Token tok = tokenizer.getToken();
		while(tok.type == TokenType.OPERATOR && 
				(tok.value.equals("*") || tok.value.equals("/") 
						|| tok.value.equals("%"))){
			node = new Arithmetic(node, matchAndNext(TokenType.OPERATOR).value,
					nextBitwise());
			tok = tokenizer.getToken();
		}
		return node;
	}
	
	private ASTExpression nextBitwise() throws Exception{
		ASTExpression node = nextUnary();
		Token tok = tokenizer.getToken();
		while(tok.type == TokenType.OPERATOR && 
				(tok.value.equals("&") || tok.value.equals("|") || tok.value.equals("^")
						|| tok.value.equals(">>") || tok.value.equals("<<") )){
			node = new Bitwise(node, matchAndNext(TokenType.OPERATOR).value,
					nextUnary());
			tok = tokenizer.getToken();
		}
		return node;
	}
	
	private ASTExpression nextUnary() throws Exception{
		Token tok = tokenizer.getToken();
		if(tok.type == TokenType.OPERATOR
				&& (tok.value.equals("-") || tok.value.equals("!")) ){
			return new ASTUnary((matchAndNext(TokenType.OPERATOR)).value, nextBaseExpression());
		}
		return nextBaseExpression();
	}
	
	private ASTExpression nextBaseExpression() throws Exception{
		Token tok = tokenizer.getToken();
		if(tok.type == TokenType.FUNC){
			return nextFunction();
		}else if(tok.type == TokenType.INT){
			return new IntConstant((matchAndNext(TokenType.INT).value));
		}else if(tok.type == TokenType.FLOAT){
			return new FloatConstant((matchAndNext(TokenType.FLOAT).value));
		}else if(tok.type == TokenType.RESERVED &&
				(tok.value.equalsIgnoreCase("true") || tok.value.equalsIgnoreCase("false"))){
			return new BooleanConstant(matchAndNext(TokenType.RESERVED).value);
		}else if(tok.type == TokenType.IDEN){
			return new Identifier(matchAndNext(TokenType.IDEN).value);
		}else if(tok.type == TokenType.STRING){
			return new StringConstant(matchAndNext(TokenType.STRING).value);
		}else if(tok.type == TokenType.L_PAREN){
			matchAndNext(TokenType.L_PAREN);
			ASTExpression group = nextExpression();
			matchAndNext(TokenType.R_PAREN);
			return group;
		}
		throw new Exception("Line "+tokenizer.getLineNumber()+
				" : Expected base expression, got "+tok);
	}
	
	private Function nextFunction() throws Exception{
		String actionName = matchAndNext(TokenType.FUNC).value;
		List<ASTExpression> paramList = nextParameterList();
		boolean singleQuery = false;
		if(tokenizer.getToken().type == TokenType.SINGLEQUERY){
			matchAndNext(TokenType.SINGLEQUERY);
			singleQuery = true;
		}
		return new Function(actionName, paramList, singleQuery);
	}
}
