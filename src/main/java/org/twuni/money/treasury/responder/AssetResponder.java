package org.twuni.money.treasury.responder;

import java.io.File;

import org.twuni.common.net.http.responder.FileResponder;

public class AssetResponder extends FileResponder {

	private static final String DIRECTORY_NAME = "assets";

	public AssetResponder() {
		super( new File( AssetResponder.class.getClassLoader().getResource( DIRECTORY_NAME ).getPath() ) );
	}

}
