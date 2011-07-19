package org.twuni.money.treasury.repository;

import java.util.List;

import org.twuni.common.Adapter;
import org.twuni.common.crypto.rsa.PrivateKey;
import org.twuni.common.orm.Parameterized;
import org.twuni.common.orm.Parameters;
import org.twuni.common.orm.Record;
import org.twuni.common.orm.Session;
import org.twuni.money.common.Repository;
import org.twuni.money.common.SimpleToken;
import org.twuni.money.common.Token;

public class TokenRepository implements Repository<String, Token> {

	private static final String SCHEMA = "CREATE TABLE token ( treasury varchar(128) not null, action_key_id int not null, owner_key_id int not null, value int not null, primary key (action_key_id), foreign key (action_key_id) references keypair (id), foreign key (owner_key_id) references keypair (id) );";

	private static final String FIND_ALL = "SELECT t.treasury, a.private_key AS action_key, o.private_key AS owner_key, t.value FROM token t INNER JOIN keypair a ON t.action_key_id = a.id INNER JOIN keypair o ON t.owner_key_id = o.id ORDER BY value ASC;";
	private static final String FIND_BY_ID = "SELECT t.treasury, a.private_key AS action_key, o.private_key AS owner_key, t.value FROM token t INNER JOIN keypair a ON t.action_key_id = a.id INNER JOIN keypair o ON t.owner_key_id = o.id WHERE a.public_key = ?;";
	private static final String DELETE = "DELETE FROM token WHERE action_key_id = ( SELECT id FROM keypair WHERE public_key = ? );";
	private static final String INSERT = "INSERT INTO token ( treasury, action_key_id, owner_key_id, value ) VALUES ( ?, ( SELECT id FROM keypair WHERE public_key = ? ), ( SELECT id FROM keypair WHERE public_key = ? ), ? );";

	public static void create( Session session ) {
		session.query( SCHEMA );
	}

	private final Adapter<Record, Token> adapter = new Adapter<Record, Token>() {

		@Override
		public Token adapt( Record record ) {

			String treasury = record.getString( "treasury" );
			PrivateKey actionKey = PrivateKey.deserialize( record.getString( "action_key" ) );
			PrivateKey ownerKey = PrivateKey.deserialize( record.getString( "owner_key" ) );
			int value = record.getInt( "value" );

			return new SimpleToken( treasury, actionKey, ownerKey, value );

		}

	};

	private final PrivateKeyRepository privateKeys;
	private final Session session;

	public TokenRepository( Session session ) {
		this.session = session;
		this.privateKeys = new PrivateKeyRepository( session );
	}

	@Override
	public Token findById( final String id ) {

		return session.unique( FIND_BY_ID, new Parameters() {

			@Override
			public void apply( Parameterized target ) {
				target.setParameter( 1, id );
			}

		}, adapter );

	}

	@Override
	public void save( final Token token ) {

		privateKeys.save( token.getActionKey() );
		privateKeys.save( token.getOwnerKey() );

		session.query( INSERT, new Parameters() {

			@Override
			public void apply( Parameterized target ) {
				target.setParameter( 1, token.getTreasury() );
				target.setParameter( 2, token.getActionKey().getPublicKey().serialize() );
				target.setParameter( 3, token.getOwnerKey().getPublicKey().serialize() );
				target.setParameter( 4, token.getValue() );
			}

		} );

	}

	@Override
	public void delete( final Token token ) {

		session.query( DELETE, new Parameters() {

			@Override
			public void apply( Parameterized target ) {
				target.setParameter( 1, token.getActionKey().getPublicKey().serialize() );
			}

		} );

		privateKeys.delete( token.getActionKey() );
		privateKeys.delete( token.getOwnerKey() );

	}

	@Override
	public List<Token> list( int limit ) {
		return session.query( FIND_ALL, adapter, limit );
	}

	@Override
	public List<Token> list() {
		return list( Integer.MAX_VALUE );
	}

}
