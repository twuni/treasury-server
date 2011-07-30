package org.twuni.money.treasury;

import java.io.File;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.twuni.common.Factory;
import org.twuni.common.config.Configuration;
import org.twuni.common.config.PropertiesConfiguration;
import org.twuni.common.net.http.Method;
import org.twuni.common.net.http.Server;
import org.twuni.common.net.http.responder.ExceptionHandler;
import org.twuni.common.net.http.responder.FileResponder;
import org.twuni.common.net.http.responder.RequestMapping;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.persistence.Connection;
import org.twuni.common.persistence.Session;
import org.twuni.common.persistence.Transaction;
import org.twuni.money.common.Treasury;
import org.twuni.money.treasury.repository.PrivateKeyRepository;
import org.twuni.money.treasury.repository.TokenRepository;
import org.twuni.money.treasury.responder.AboutResponder;
import org.twuni.money.treasury.responder.CreateResponder;
import org.twuni.money.treasury.responder.EvaluateResponder;
import org.twuni.money.treasury.responder.MergeResponder;
import org.twuni.money.treasury.responder.SplitResponder;

public class Standalone {

	private static final Logger log = LoggerFactory.getLogger( Standalone.class );

	private final Server server;
	private final Configuration config;

	public static void main( String [] args ) {
		new Standalone( PropertiesConfiguration.load( "treasury" ) ).start();
	}

	public Standalone( Configuration config ) {

		this.config = config;

		Connection connection = createConnection();
		Factory<Treasury> treasuryFactory = createTreasuryFactory();
		Responder responder = createResponder( treasuryFactory, connection );

		this.server = new Server( getPort(), responder );

	}

	public void start() {
		server.start();
	}

	private Factory<Treasury> createTreasuryFactory() {
		return new TreasuryFactory( getBaseUrl(), getTokenStrength() );
	}

	private Connection createConnection() {
		return createConnection( getDatabaseUrl(), getDatabaseUsername(), getDatabasePassword(), getDatabaseConnectionPoolSize() );
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
		mapping.map( Method.GET, "/.*", createFileResponder( "assets" ) );

		return new ExceptionHandler( mapping );

	}

	private Responder createFileResponder( String parentDirectoryName ) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource( parentDirectoryName );
		File parentDirectory = new File( resource.getPath() );
		return new FileResponder( parentDirectory );
	}

	public String getBaseUrl() {

		StringBuilder url = new StringBuilder();

		url.append( "http" );
		url.append( "://" );
		url.append( getDomain() );

		int port = getPort();
		if( port != 80 ) {
			url.append( ":" );
			url.append( port );
		}

		return url.toString();

	}

	public int getPort() {
		return config.getInt( "server.port" );
	}

	public String getDomain() {
		return config.getString( "server.domain" );
	}

	public int getTokenStrength() {
		return config.getInt( "token.strength" );
	}

	public String getDatabaseUrl() {
		return config.getString( "jdbc.url" );
	}

	public String getDatabaseUsername() {
		return config.getString( "jdbc.username" );
	}

	public String getDatabasePassword() {
		return config.getString( "jdbc.password", "" );
	}

	public int getDatabaseConnectionPoolSize() {
		return config.getInt( "jdbc.pool_size" );
	}

}
