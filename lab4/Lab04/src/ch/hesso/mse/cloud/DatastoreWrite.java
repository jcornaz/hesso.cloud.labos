package ch.hesso.mse.cloud;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Simple servlet that use the google datastore api
 * 
 * @see http://mse-cloud.s3-website-eu-west-1.amazonaws.com/labs/paas.html
 * @author Jonathan Cornaz
 */
@SuppressWarnings("serial")
public class DatastoreWrite extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/plain");

		String kind = request.getParameter("_kind");

		if (kind == null || kind.isEmpty()) {
			response.getWriter().println("No entity kind specified !");
		} else {

			String key = request.getParameter("_key");
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Entity entity;

			if (key == null || key.isEmpty()) {
				entity = new Entity(kind);
			} else {
				try {
					entity = datastore.get(KeyFactory.createKey(kind, Long.parseLong(key)));
				} catch (EntityNotFoundException e) {
					response.getWriter().println("Invalid key for the specified entity kind !");
					entity = null;
				} catch(NumberFormatException e )
				{
					response.getWriter().println("not numeric key !");
					entity = null;
				}
			}

			if (entity != null) {

				@SuppressWarnings("unchecked")
				Enumeration<String> i = request.getParameterNames();
				while (i.hasMoreElements()) {
					String parameter = i.nextElement();
					if (!parameter.startsWith("_")) {
						String value = request.getParameter(parameter);
						entity.setProperty(parameter, value);
					}
				}

				datastore.put(entity);
				
				response.getWriter().println("Entity saved.");
			}
		}
	}
}
