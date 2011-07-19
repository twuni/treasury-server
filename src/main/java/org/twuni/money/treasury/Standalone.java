package org.twuni.money.treasury;

import org.twuni.common.net.http.Method;
import org.twuni.common.net.http.Server;
import org.twuni.common.net.http.responder.ExceptionHandler;
import org.twuni.common.net.http.responder.RequestMapping;
import org.twuni.common.orm.Transaction;
import org.twuni.common.orm.Session;
import org.twuni.common.orm.jdbc.Connection;
import org.twuni.money.treasury.repository.PrivateKeyRepository;
import org.twuni.money.treasury.repository.TokenRepository;
import org.twuni.money.treasury.responder.Creator;
import org.twuni.money.treasury.responder.Merger;
import org.twuni.money.treasury.responder.Splitter;

public class Standalone {

	public static void main( String [] args ) {

		Connection connection = new Connection( "jdbc:hsqldb:mem:test", "SA", "" );

		connection.run( new Transaction() {

			@Override
			public void perform( Session session ) {
				PrivateKeyRepository.create( session );
				TokenRepository.create( session );
			}

		} );

		RequestMapping mapping = new RequestMapping();

		mapping.map( Method.POST, "/treasury/create", new Creator( connection ) );
		mapping.map( Method.POST, "/treasury/merge", new Merger( connection ) );
		mapping.map( Method.POST, "/treasury/split", new Splitter( connection ) );

		Server server = new Server( 8080, new ExceptionHandler( mapping ) );

		server.start();

	}

}
