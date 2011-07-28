package org.twuni.money.treasury.responder;

import java.util.HashMap;
import java.util.Map;

import org.twuni.common.net.http.request.Request;
import org.twuni.common.net.http.responder.Responder;
import org.twuni.common.net.http.response.Response;
import org.twuni.common.net.http.response.Status;
import org.twuni.money.treasury.view.StringTemplateView;

public class AboutResponder implements Responder {

	private static final String VIEW_NAME = "about";

	@Override
	public Response respondTo( Request request ) {
		return new Response( Status.OK, new StringTemplateView( VIEW_NAME, getModel() ).toString() );
	}

	private Map<String, Object> getModel() {

		Map<String, Object> model = new HashMap<String, Object>();

		model.put( "title", "Awesome" );
		model.put( "content", "Something else that is cool." );

		return model;

	}

}
