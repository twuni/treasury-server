package org.twuni.money.treasury.responder.behavior;

import org.twuni.common.Adapter;
import org.twuni.common.orm.Transaction;
import org.twuni.common.orm.Session;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.common.TreasuryService;
import org.twuni.money.treasury.repository.TokenRepository;

public class Create implements Transaction {

	private final String content;
	private final Adapter<Token, String> adapter;

	private String result;

	public Create( String content, Adapter<Token, String> adapter ) {
		this.content = content;
		this.adapter = adapter;
	}

	@Override
	public void perform( Session session ) {
		TokenRepository repository = new TokenRepository( session );
		Treasury treasury = new TreasuryService( 2048, "home.twuni.org", repository );
		this.result = adapter.adapt( treasury.create( Integer.parseInt( content ) ) );
	}

	public String getResult() {
		return result;
	}

}