package org.twuni.money.treasury.responder;

import org.twuni.common.Adapter;
import org.twuni.common.net.http.request.Request;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.net.http.response.Response;
import org.twuni.common.net.http.response.Status;
import org.twuni.common.orm.Connection;
import org.twuni.money.common.Token;
import org.twuni.money.treasury.adapter.TokenAdapter;
import org.twuni.money.treasury.responder.behavior.Create;

public class CreateResponder implements Responder {

	private final Adapter<Token, String> adapter = new TokenAdapter();
	private final Connection connection;

	public CreateResponder( Connection connection ) {
		this.connection = connection;
	}

	@Override
	public Response respondTo( Request request ) {
		Create create = new Create( request.getContent(), adapter );
		connection.run( create );
		return new Response( Status.OK, "application/json", create.getResult() );
	}

}
