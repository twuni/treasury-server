package org.twuni.money.treasury;

public class Configuration {

	private static final org.twuni.common.Configuration config = new org.twuni.common.Configuration( "treasury" );

	public static int getPort() {
		return config.getInt( "server.port" );
	}

	public static String getDomain() {
		return config.getString( "server.domain" );
	}

	public static int getTokenStrength() {
		return config.getInt( "token.strength" );
	}

	public static String getDatabaseUrl() {
		return config.getString( "jdbc.url" );
	}

	public static String getDatabaseUsername() {
		return config.getString( "jdbc.username" );
	}

	public static String getDatabasePassword() {
		return config.getString( "jdbc.password", "" );
	}

	public static int getDatabaseConnectionPoolSize() {
		return config.getInt( "jdbc.pool_size" );
	}

}
