package org.twuni.money.treasury.responder.behavior;

import java.io.IOException;

import org.twuni.common.Factory;
import org.twuni.common.orm.Session;
import org.twuni.common.orm.Transaction;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.Message;
import org.twuni.money.treasury.repository.TokenRepository;

public class Evaluate implements Transaction {

	private final Factory<Treasury> treasuryFactory;
	private final String content;

	private String result;

	public Evaluate( String content, Factory<Treasury> treasuryFactory ) {
		this.content = content;
		this.treasuryFactory = treasuryFactory;
	}

	@Override
	public void perform( Session session ) {

		try {

			TokenRepository repository = new TokenRepository( session );
			Treasury treasury = treasuryFactory.createInstance( repository );
			Message message = Message.parse( content, repository );
			this.result = message.encrypt( Integer.toString( treasury.getValue( message.getToken() ) ) );

		} catch( IOException exception ) {
			throw new RuntimeException( exception );
		}

	}

	public String getResult() {
		return result;
	}

}
