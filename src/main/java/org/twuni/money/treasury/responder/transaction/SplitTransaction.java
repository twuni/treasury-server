package org.twuni.money.treasury.responder.transaction;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.twuni.common.Adapter;
import org.twuni.common.Factory;
import org.twuni.common.persistence.Session;
import org.twuni.common.persistence.Transaction;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.Message;
import org.twuni.money.treasury.repository.TokenRepository;

public class SplitTransaction implements Transaction {

	private final Factory<Treasury> treasuryFactory;
	private final String message;
	private final Adapter<Collection<Token>, String> adapter;
	private String result;

	public SplitTransaction( String message, Factory<Treasury> treasuryFactory, Adapter<Collection<Token>, String> adapter ) {
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
			Set<Token> tokens = treasury.split( message.getToken(), Integer.parseInt( message.getContent() ) );

			this.result = message.encrypt( adapter.adapt( tokens ) );

		} catch( IOException exception ) {
			throw new RuntimeException( exception );
		}

	}

	public String getResult() {
		return result;
	}

}
