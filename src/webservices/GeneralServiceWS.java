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
import java.util.regex.Matcher; 
import java.util.regex.Pattern;

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
//      This regex checks for any special characters in the username but allows underscore and must be 3-30 characters		
	
		Pattern userNameCheck = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_]{2,29}$", Pattern.CASE_INSENSITIVE);
		Matcher mu = userNameCheck.matcher(userName);
		boolean boolUName = mu.find();
		
//		userPasswordCheck regex will enforce these rules:
//
//			At least one upper case English letter, (?=.*?[A-Z])
//			At least one lower case English letter, (?=.*?[a-z])
//			At least one digit, (?=.*?[0-9])
//			At least one special character, (?=.*?[#?!@$%^&*-])
//			Minimum eight in length .{8,} (with the anchors)
		
		Pattern userPasswordCheck = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
		Matcher mp = userPasswordCheck.matcher(userPassword);
		boolean boolPass = mp.find();
		if (boolUName){
//			Display that the username has a special character and must be changed
		   System.out.println("There is a special character in my string");
		   return Response.serverError().entity("Your username must be betweeen 3 and 30 characters long and may not contain a special character.").build();
		}
		else if (!boolPass){
//			Display that the password does not meet the criteria
		   System.out.println("Password does not meet the criteria");
		   
		   return Response.serverError().entity("Password must be at least 8 characters long and have at least 1 upper and lowercase letter,1 number and a special character.").build();
		}else{
			// both username and password are acceptable and it then send them to the server to be checked
		try {
			System.out.println("All is fine");
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

}
