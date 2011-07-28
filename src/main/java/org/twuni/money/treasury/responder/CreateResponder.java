package org.twuni.money.treasury.responder;

import org.twuni.common.Adapter;
import org.twuni.common.Factory;
import org.twuni.common.net.http.request.Request;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.net.http.response.Response;
import org.twuni.common.net.http.response.Status;
import org.twuni.common.persistence.Connection;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.adapter.TokenAdapter;
import org.twuni.money.treasury.responder.transaction.CreateTransaction;

public class CreateResponder implements Responder {

	private final Factory<Treasury> treasuryFactory;
	private final Adapter<Token, String> adapter = new TokenAdapter();
	private final Connection connection;

	public CreateResponder( Factory<Treasury> treasuryFactory, Connection connection ) {
		this.treasuryFactory = treasuryFactory;
		this.connection = connection;
	}

	@Override
	public Response respondTo( Request request ) {
		CreateTransaction transaction = new CreateTransaction( new String( request.getContent() ), treasuryFactory, adapter );
		connection.run( transaction );
		return new Response( Status.OK, transaction.getResult() );
	}

}
