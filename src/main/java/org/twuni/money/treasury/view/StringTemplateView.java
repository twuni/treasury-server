package org.twuni.money.treasury.view;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

public class StringTemplateView {

	private static final String DIRECTORY_NAME = "templates";

	private final StringTemplateGroup group = new StringTemplateGroup( DIRECTORY_NAME, getClass().getClassLoader().getResource( DIRECTORY_NAME ).getPath() );

	private final String name;
	private final Map<String, ?> model;

	public StringTemplateView( String name, Map<String, ?> model ) {
		this.name = name;
		this.model = model;
	}

	@Override
	public String toString() {
		StringTemplate template = group.lookupTemplate( name );
		template.setAttributes( model );
		return template.toString();
	}

}
