package org.twuni.money.treasury;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.twuni.common.Factory;
import org.twuni.common.net.http.Method;
import org.twuni.common.net.http.Server;
import org.twuni.common.net.http.responder.ExceptionHandler;
import org.twuni.common.net.http.responder.RequestMapping;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.persistence.Connection;
import org.twuni.common.persistence.Session;
import org.twuni.common.persistence.Transaction;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.repository.PrivateKeyRepository;
import org.twuni.money.treasury.repository.TokenRepository;
import org.twuni.money.treasury.responder.AboutResponder;
import org.twuni.money.treasury.responder.AssetResponder;
import org.twuni.money.treasury.responder.CreateResponder;
import org.twuni.money.treasury.responder.EvaluateResponder;
import org.twuni.money.treasury.responder.MergeResponder;
import org.twuni.money.treasury.responder.SplitResponder;

public class Standalone {

	private static final Logger log = LoggerFactory.getLogger( Standalone.class );

	private final Server server;

	public static void main( String [] args ) {
		new Standalone().start();
	}

	public Standalone() {

		Connection connection = createConnection();
		Factory<Treasury> treasuryFactory = createTreasuryFactory();
		Responder responder = createResponder( treasuryFactory, connection );

		this.server = new Server( Configuration.getPort(), responder );

	}

	public void start() {
		server.start();
	}

	private Factory<Treasury> createTreasuryFactory() {
		return new TreasuryFactory( Configuration.getBaseUrl(), Configuration.getTokenStrength() );
	}

	private Connection createConnection() {
		return createConnection( Configuration.getDatabaseUrl(), Configuration.getDatabaseUsername(), Configuration.getDatabasePassword(), Configuration.getDatabaseConnectionPoolSize() );
	}

	private Connection createConnection( String url, String username, String password, int poolSize ) {
		Connection connection = new org.twuni.common.persistence.jdbc.Connection( url, username, password, poolSize );
		createSchema( connection );
		return connection;
	}

	private void createSchema( Connection connection ) {

		try {

			connection.run( new Transaction() {

				@Override
				public void perform( Session session ) {
					PrivateKeyRepository.create( session );
					TokenRepository.create( session );
				}

			} );

		} catch( RuntimeException exception ) {
			log.warn( String.format( "Unable to create schema: %s", exception.getMessage() ) );
		}

	}

	private ExceptionHandler createResponder( Factory<Treasury> treasuryFactory, Connection connection ) {

		RequestMapping mapping = new RequestMapping();

		mapping.map( Method.POST, "/create", new CreateResponder( treasuryFactory, connection ) );
		mapping.map( Method.POST, "/merge", new MergeResponder( treasuryFactory, connection ) );
		mapping.map( Method.POST, "/split", new SplitResponder( treasuryFactory, connection ) );
		mapping.map( Method.POST, "/value", new EvaluateResponder( treasuryFactory, connection ) );

		mapping.map( Method.GET, "/", new AboutResponder() );
		mapping.map( Method.GET, "/.*", new AssetResponder() );

		return new ExceptionHandler( mapping );

	}

}
