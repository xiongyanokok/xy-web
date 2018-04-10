package com.xy.web;

import io.shardingjdbc.core.constant.DatabaseType;
import io.shardingjdbc.core.parsing.SQLParsingEngine;
import io.shardingjdbc.core.parsing.lexer.LexerEngine;
import io.shardingjdbc.core.parsing.lexer.LexerEngineFactory;
import io.shardingjdbc.core.parsing.lexer.token.Token;
import io.shardingjdbc.core.parsing.parser.sql.SQLStatement;

public class SqlParser {

	public static void main(String[] args) {
		String sql = "select * from cdsq_article where is_delete = 0 order by create_time desc limit 1, 10";
		LexerEngine le = LexerEngineFactory.newInstance(DatabaseType.MySQL, sql);
		for (int i=0; i<sql.length(); i++) {
			le.nextToken();
			Token token = le.getCurrentToken();
			System.out.println(token.getLiterals());
			i = token.getEndPosition();
		}
		
		
		SQLParsingEngine spe = new SQLParsingEngine(DatabaseType.MySQL, sql, null);
		SQLStatement ss = spe.parse();
		
		String str = le.skipParentheses(ss);
		System.out.println(str);
	}

}
