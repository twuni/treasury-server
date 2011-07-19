package org.twuni.money.treasury.responder.behavior;

import java.io.IOException;

import org.twuni.common.Adapter;
import org.twuni.common.orm.Behavior;
import org.twuni.common.orm.Session;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.common.TreasuryService;
import org.twuni.money.treasury.Message;
import org.twuni.money.treasury.repository.TokenRepository;

public class Merge implements Behavior {

	private final String message;
	private final Adapter<Token, String> adapter;

	private String result;

	public Merge( String message, Adapter<Token, String> adapter ) {
		this.message = message;
		this.adapter = adapter;
	}

	@Override
	public void perform( Session session ) {

		try {

			TokenRepository repository = new TokenRepository( session );
			Treasury treasury = new TreasuryService( 2048, "home.twuni.org", repository );

			Message a = Message.parse( message, repository );
			Message b = Message.parse( a.getContent(), repository );

			if( !b.getContent().equals( a.getToken().getActionKey().getPublicKey() ) ) {
				throw new IllegalArgumentException( "Nested message must match its parent's public key." );
			}

			Token merged = treasury.merge( a.getToken(), b.getToken() );

			this.result = b.encrypt( a.encrypt( adapter.adapt( merged ) ) );

		} catch( IOException exception ) {
			throw new RuntimeException( exception );
		}

	}

	public String getResult() {
		return result;
	}

}