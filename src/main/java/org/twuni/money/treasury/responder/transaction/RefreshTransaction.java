package org.twuni.money.treasury.responder.transaction;

import java.io.IOException;

import org.twuni.common.Adapter;
import org.twuni.common.Factory;
import org.twuni.common.persistence.Session;
import org.twuni.common.persistence.Transaction;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.Message;
import org.twuni.money.treasury.repository.TokenRepository;

public class RefreshTransaction implements Transaction {

	private final String message;
	private final Adapter<Token, String> adapter;
	private final Factory<Treasury> treasuryFactory;

	private String result;

	public RefreshTransaction( String message, Factory<Treasury> treasuryFactory, Adapter<Token, String> adapter ) {
		this.message = message;
		this.treasuryFactory = treasuryFactory;
		this.adapter = adapter;
	}

	@Override
	public void perform( Session session ) {
		try {
			TokenRepository repository = new TokenRepository( session );
			Treasury treasury = treasuryFactory.createInstance( repository );
			Message message = Message.parse( this.message, repository );
			if( !message.getContent().equals( Integer.toString( message.getToken().getValue() ) ) ) {
				throw new IllegalArgumentException( "Encrypted message must match the token's declared value." );
			}
			Token refreshed = treasury.refresh( message.getToken() );
			this.result = message.encrypt( adapter.adapt( refreshed ) );
		} catch( IOException exception ) {
			throw new RuntimeException( exception );
		}
	}

	public String getResult() {
		return result;
	}

}
