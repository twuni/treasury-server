package org.twuni.money.treasury.repository;

import java.util.List;

import org.twuni.common.Adapter;
import org.twuni.common.crypto.rsa.PrivateKey;
import org.twuni.common.persistence.Parameterized;
import org.twuni.common.persistence.Parameters;
import org.twuni.common.persistence.Record;
import org.twuni.common.persistence.Session;
import org.twuni.money.common.Repository;

public class PrivateKeyRepository implements Repository<String, PrivateKey> {

	private static final String SCHEMA = "CREATE TABLE keypair ( id int not null identity, public_key varchar(8192) not null, private_key varchar(8192) not null );";
	private static final String FIND_ALL = "SELECT private_key FROM keypair;";
	private static final String FIND_BY_ID = "SELECT private_key FROM keypair WHERE public_key = ?;";
	private static final String DELETE = "DELETE FROM keypair WHERE private_key = ?;";
	private static final String INSERT = "INSERT INTO keypair ( public_key, private_key ) VALUES ( ?, ? );";

	public static void create( Session session ) {
		session.query( SCHEMA );
	}

	private final Adapter<Record, PrivateKey> adapter = new Adapter<Record, PrivateKey>() {

		@Override
		public PrivateKey adapt( Record record ) {
			return PrivateKey.deserialize( record.getString( "private_key" ) );
		}

	};

	private final Session session;

	public PrivateKeyRepository( Session session ) {
		this.session = session;
	}

	@Override
	public PrivateKey findById( final String publicKey ) {

		return session.unique( FIND_BY_ID, new Parameters() {

			@Override
			public void apply( Parameterized target ) {
				target.setParameter( 1, publicKey );
			}

		}, adapter );

	}

	@Override
	public void save( final PrivateKey privateKey ) {

		session.query( INSERT, new Parameters() {

			@Override
			public void apply( Parameterized target ) {
				target.setParameter( 1, privateKey.getPublicKey().serialize() );
				target.setParameter( 2, privateKey.serialize() );
			}

		} );

	}

	@Override
	public void delete( final PrivateKey privateKey ) {

		session.query( DELETE, new Parameters() {

			@Override
			public void apply( Parameterized target ) {
				target.setParameter( 1, privateKey.serialize() );
			}

		} );

	}

	@Override
	public List<PrivateKey> list( int limit ) {
		return session.query( FIND_ALL, adapter, limit );
	}

	@Override
	public List<PrivateKey> list() {
		return list( Integer.MAX_VALUE );
	}

}
