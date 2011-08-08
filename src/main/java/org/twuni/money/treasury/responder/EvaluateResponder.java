package org.twuni.money.treasury.responder;

import org.twuni.common.net.http.request.Request;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.net.http.response.Response;
import org.twuni.common.net.http.response.Status;
import org.twuni.common.persistence.Connection;
import org.twuni.money.treasury.responder.transaction.EvaluateTransaction;

public class EvaluateResponder implements Responder {

	private final Connection connection;

	public EvaluateResponder( Connection connection ) {
		this.connection = connection;
	}

	@Override
	public Response respondTo( Request request ) {
		EvaluateTransaction transaction = new EvaluateTransaction( new String( request.getContent() ) );
		connection.run( transaction );
		return new Response( Status.OK, transaction.getResult() );
	}

}
