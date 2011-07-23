package org.twuni.money.treasury;

import org.twuni.common.Factory;
import org.twuni.money.common.Repository;
import org.twuni.money.common.Token;
import org.twuni.money.common.Treasury;
import org.twuni.money.common.TreasuryService;

public class TreasuryFactory implements Factory<Treasury> {

	private final String baseUrl;
	private final int keyStrength;

	public TreasuryFactory( String baseUrl, int keyStrength ) {
		this.baseUrl = baseUrl;
		this.keyStrength = keyStrength;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public Treasury createInstance( Object... args ) {
		return new TreasuryService( keyStrength, baseUrl, (Repository<String, Token>) args[0] );
	}

}
