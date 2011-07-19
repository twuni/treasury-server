package org.twuni.money.treasury;

import java.io.IOException;

import org.twuni.common.crypto.rsa.Transformer;
import org.twuni.money.common.Repository;
import org.twuni.money.common.Token;

public class Message {

	public static Message parse( String source, Repository<String, Token> tokens ) {

		try {

			String [] parts = source.split( "\n" );
			Token token = tokens.findById( parts[0] );
			String message = decrypt( token, parts[1] );

			return new Message( token, message );

		} catch( IOException exception ) {
			throw new RuntimeException( exception );
		}

	}

	private static String decrypt( Token token, String message ) throws IOException {
		Transformer action = new Transformer( token.getActionKey() );
		Transformer owner = new Transformer( token.getOwnerKey() );
		return owner.decrypt( action.decrypt( message ) );
	}

	private final Token token;
	private final String content;

	private Message( Token token, String content ) {
		this.token = token;
		this.content = content;
	}

	public String encrypt( String content ) throws IOException {
		Transformer action = new Transformer( token.getActionKey().getPublicKey() );
		Transformer owner = new Transformer( token.getOwnerKey().getPublicKey() );
		return action.encrypt( owner.encrypt( content ) );
	}

	public Token getToken() {
		return token;
	}

	public String getContent() {
		return content;
	}

}
