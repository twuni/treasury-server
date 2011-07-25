package org.twuni.money.treasury.responder;

import org.twuni.common.Factory;
import org.twuni.common.net.http.request.Request;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.net.http.response.Response;
import org.twuni.common.net.http.response.Status;
import org.twuni.common.persistence.Connection;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.responder.behavior.Evaluate;

public class Evaluator implements Responder {

	private final Factory<Treasury> treasuryFactory;
	private final Connection connection;

	public Evaluator( Factory<Treasury> treasuryFactory, Connection connection ) {
		this.treasuryFactory = treasuryFactory;
		this.connection = connection;
	}

	@Override
	public Response respondTo( Request request ) {
		Evaluate evaluate = new Evaluate( request.getContent(), treasuryFactory );
		connection.run( evaluate );
		return new Response( Status.OK, "application/json", evaluate.getResult() );
	}

}
