package org.twuni.money.treasury.adapter;

import java.util.Collection;

import org.twuni.common.Adapter;

public class CollectionAdapter<T> implements Adapter<Collection<T>, String> {

	private final Adapter<T, String> adapter;

	public CollectionAdapter( Adapter<T, String> adapter ) {
		this.adapter = adapter;
	}

	@Override
	public String adapt( Collection<T> collection ) {

		StringBuilder json = new StringBuilder();

		json.append( "[" );
		for( T item : collection ) {
			json.append( adapter.adapt( item ) ).append( "," );
		}
		json.deleteCharAt( json.length() - 1 );
		json.append( "]" );

		return json.toString();

	}
}
