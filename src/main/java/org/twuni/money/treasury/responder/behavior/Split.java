package org.twuni.money.treasury.responder.behavior;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.twuni.common.Adapter;
import org.twuni.common.orm.Behavior;
import org.twuni.common.orm.Session;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.common.TreasuryService;
import org.twuni.money.treasury.Message;
import org.twuni.money.treasury.repository.TokenRepository;

public class Split implements Behavior {

	private final String message;
	private final Adapter<Collection<Token>, String> adapter;
	private String result;

	public Split( String message, Adapter<Collection<Token>, String> adapter ) {
		this.message = message;
		this.adapter = adapter;
	}

	@Override
	public void perform( Session session ) {

		try {

			TokenRepository repository = new TokenRepository( session );
			Treasury treasury = new TreasuryService( 2048, "home.twuni.org", repository );
			Message message = Message.parse( this.message, repository );
			Set<Token> tokens = treasury.split( message.getToken(), Integer.parseInt( message.getContent() ) );

			this.result = message.encrypt( adapter.adapt( tokens ) );

		} catch( NumberFormatException exception ) {
			throw new RuntimeException( exception );
		} catch( IOException exception ) {
			throw new RuntimeException( exception );
		}

	}

	public String getResult() {
		return result;
	}

}
