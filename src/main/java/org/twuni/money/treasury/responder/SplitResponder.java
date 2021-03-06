package org.twuni.money.treasury.responder;

import java.util.Collection;

import org.twuni.common.Adapter;
import org.twuni.common.Factory;
import org.twuni.common.net.http.request.Request;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.net.http.response.Response;
import org.twuni.common.net.http.response.Status;
import org.twuni.common.persistence.Connection;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.adapter.CollectionAdapter;
import org.twuni.money.treasury.adapter.TokenAdapter;
import org.twuni.money.treasury.responder.transaction.SplitTransaction;

public class SplitResponder implements Responder {

	private final Factory<Treasury> treasuryFactory;
	private final Adapter<Collection<Token>, String> adapter = new CollectionAdapter<Token>( new TokenAdapter() );
	private final Connection connection;

	public SplitResponder( Factory<Treasury> treasuryFactory, Connection connection ) {
		this.treasuryFactory = treasuryFactory;
		this.connection = connection;
	}

	@Override
	public Response respondTo( Request request ) {
		SplitTransaction transaction = new SplitTransaction( new String( request.getContent() ), treasuryFactory, adapter );
		connection.run( transaction );
		return new Response( Status.OK, transaction.getResult() );
	}

}
