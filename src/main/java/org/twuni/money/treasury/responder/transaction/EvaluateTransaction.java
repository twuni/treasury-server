package org.twuni.money.treasury.responder.transaction;

import java.io.IOException;

import org.twuni.common.crypto.rsa.PublicKey;
import org.twuni.common.crypto.rsa.Transformer;
import org.twuni.common.persistence.Session;
import org.twuni.common.persistence.Transaction;
import org.twuni.common.persistence.exception.ObjectNotFoundException;
import org.twuni.money.treasury.Message;
import org.twuni.money.treasury.repository.TokenRepository;

public class EvaluateTransaction implements Transaction {

	private final String content;

	private String result;

	public EvaluateTransaction( String content ) {
		this.content = content;
	}

	@Override
	public void perform( Session session ) {

		try {

			TokenRepository repository = new TokenRepository( session );
			try {
				Message message = Message.parse( content, repository );
				this.result = message.encrypt( Integer.toString( message.getToken().getValue() ) );
			} catch( ObjectNotFoundException exception ) {
				PublicKey publicKey = PublicKey.deserialize( content.split( "\n" )[0] );
				Transformer transformer = new Transformer( publicKey );
				this.result = transformer.encrypt( "0" );
			}

		} catch( IOException exception ) {
			throw new RuntimeException( exception );
		}

	}

	public String getResult() {
		return result;
	}

}
