package com.redhat.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;

public class ParseError extends RecognitionError {
	
	private Parser parser;
	private int code;
	private int expecting;
	private Token prevToken; // last token before EOF on hidden channel, if error occurred on EOF
	
	public ParseError(Parser parser, RecognitionException re, int expecting, String[] tn) {
		this(parser, re, tn, -1);
		this.expecting = expecting;
	}
	
    public ParseError(Parser parser, RecognitionException re, String[] tn, int code) {
        super(re, tn);
        this.parser = parser;
        this.code = code;
        if (re.token.getType() == Token.EOF && re.index > 0) {
        	Token token = parser.getTokenStream().get(re.index-1);
        	if (token.getChannel() == Token.HIDDEN_CHANNEL) {
        		prevToken = token;
        	}
        }
    }
    
	@Override
	public int getLine() {
		return prevToken != null ? prevToken.getLine() : super.getLine();
	}
	
	@Override
	public int getCharacterInLine() {
		return prevToken != null ? prevToken.getCharPositionInLine() : super.getCharacterInLine();
	}

	public String getToken() {
		return recognitionException.token.getText();
	}
	
	public String getHeader() {
		return parser.getErrorHeader(recognitionException);
	}
	
    @Override
    public int getCode() {
        return code;
    }
    
	@Override 
	public String getMessage() {
	    String message = parser.getErrorMessage(recognitionException, tokenNames)
	            .replace("'<EOF>'", "end of file")
	            .replace("input", "token")
	            .replace("missing null", "error");
        String result = "incorrect syntax: " + message;
		if (expecting!=-1 && !result.contains("expecting")) {
			result += " expecting " + tokenNames[expecting];
		}
		return result;
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
}
