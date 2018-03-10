/*
The MIT License (MIT)

Copyright (c) 2013 Jacob Kanipe-Illig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package webservices;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;

import tools.AMSException;

import common.user.UserPassword;

import hibernate.user.UserDAO;

/**
 * Controller class that will handle the front-end API interactions regarding
 * individual players involved with a game.
 * 
 * @author jacobhyphenated Copyright (c) 2013
 */

@Path("GetGeneralServiceWS")
public class GeneralServiceWS {

	@GET
	@Path("/RegisterNew")
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerNew(@QueryParam("userName") String userName,
			@QueryParam("userPassword") String userPassword)  {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		UserDAO dao = new UserDAO();
		try {
			json = mapper.writeValueAsString(dao
					.registerNewUser(new UserPassword(userName, userPassword)));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AMSException e) {
			return Response.serverError().entity(e.getMessage()).build();
		} 
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}

}
