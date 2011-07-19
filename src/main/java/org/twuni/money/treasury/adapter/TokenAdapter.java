package org.twuni.money.treasury.adapter;

import org.twuni.common.Adapter;
import org.twuni.money.common.ShareableToken;
import org.twuni.money.common.Token;

public class TokenAdapter implements Adapter<Token, String> {

    @Override
    public String adapt( Token token ) {
    	return new ShareableToken( token ).toString();
    }
}