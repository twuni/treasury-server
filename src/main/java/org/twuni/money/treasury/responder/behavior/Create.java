package org.twuni.money.treasury.responder.behavior;

import org.twuni.common.Adapter;
import org.twuni.common.Factory;
import org.twuni.common.orm.Session;
import org.twuni.common.orm.Transaction;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.repository.TokenRepository;

public class Create implements Transaction {

	private final Factory<Treasury> treasuryFactory;
	private final String content;
	private final Adapter<Token, String> adapter;

	private String result;

	public Create( String content, Factory<Treasury> treasuryFactory, Adapter<Token, String> adapter ) {
		this.content = content;
		this.treasuryFactory = treasuryFactory;
		this.adapter = adapter;
	}

	@Override
	public void perform( Session session ) {
		TokenRepository repository = new TokenRepository( session );
		Treasury treasury = treasuryFactory.createInstance( repository );
		this.result = adapter.adapt( treasury.create( Integer.parseInt( content ) ) );
	}

	public String getResult() {
		return result;
	}

}
