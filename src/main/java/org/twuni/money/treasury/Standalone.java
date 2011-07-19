package org.twuni.money.treasury;

import org.twuni.common.Factory;
import org.twuni.common.net.http.Method;
import org.twuni.common.net.http.Server;
import org.twuni.common.net.http.responder.ExceptionHandler;
import org.twuni.common.net.http.responder.RequestMapping;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.orm.Session;
import org.twuni.common.orm.Transaction;
import org.twuni.common.orm.jdbc.Connection;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.repository.PrivateKeyRepository;
import org.twuni.money.treasury.repository.TokenRepository;
import org.twuni.money.treasury.responder.Creator;
import org.twuni.money.treasury.responder.Merger;
import org.twuni.money.treasury.responder.Splitter;

public class Standalone {

	public static void main( String [] args ) {

		Connection connection = createConnection();
		Factory<Treasury> treasuryFactory = createTreasuryFactory();
		Responder responder = createResponder( treasuryFactory, connection );

		Server server = new Server( Configuration.getPort(), responder );

		server.start();

	}

	private static Factory<Treasury> createTreasuryFactory() {
		return new TreasuryFactory( Configuration.getDomain(), Configuration.getTokenStrength() );
	}

	private static Connection createConnection() {
		return createConnection( Configuration.getDatabaseUrl(), Configuration.getDatabaseUsername(), Configuration.getDatabasePassword(), Configuration.getDatabaseConnectionPoolSize() );
	}

	private static Connection createConnection( String url, String username, String password, int poolSize ) {
		Connection connection = new Connection( url, username, password, poolSize );
		createSchema( connection );
		return connection;
	}

	private static void createSchema( Connection connection ) {

		connection.run( new Transaction() {

			@Override
			public void perform( Session session ) {
				PrivateKeyRepository.create( session );
				TokenRepository.create( session );
			}

		} );

	}

	private static ExceptionHandler createResponder( Factory<Treasury> treasuryFactory, Connection connection ) {

		RequestMapping mapping = new RequestMapping();

		mapping.map( Method.POST, "/treasury/create", new Creator( treasuryFactory, connection ) );
		mapping.map( Method.POST, "/treasury/merge", new Merger( treasuryFactory, connection ) );
		mapping.map( Method.POST, "/treasury/split", new Splitter( treasuryFactory, connection ) );

		return new ExceptionHandler( mapping );

	}

}
