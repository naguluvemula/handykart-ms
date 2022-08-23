package org.tcs.hackathon.user.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.tcs.hackathon.user.model.User;

import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import javax.enterprise.context.ApplicationScoped;

@Path("/user")
@Consumes("application/json")
@Produces("application/json")
@Liveness
@Readiness
@ApplicationScoped
public class UserResource {
	
		
		
	
	    @GET
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "userListFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedUserList", description = "How many times the user list has been performed.")
	    @Timed(name = "userListTimer", description = "A measure of how long it takes to return the user list.", unit = MetricUnits.MILLISECONDS)
	    public List<User> list() {
	        return User.listAll();
	    }
	    
	    
	    
	    
	    public List<User> userListFallBack() {
	    	List<User> dummyUserList = new ArrayList<User>();
	    	User dummyUser =  new User();
	    	dummyUser.setUserFullName("xxx");
	    	dummyUser.setUserEmailId("nothingFound@site");
	    	dummyUserList.add(dummyUser);
	        return dummyUserList;
	    }
	    
	    
	    @GET
	    @Path("/{id}")
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "userByUserEmailIdFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedUserEmailIdSearch", description = "How many times the user searched by userEmailId.")
	    @Timed(name = "userEmailIdSearchTimer", description = "A measure of how long it takes to return a user searched by userEmailId", unit = MetricUnits.MILLISECONDS)
	    public User get(@PathParam("id") String id) {
	        return User.findByUserEmailId(id);
	    }
	    
	    public User userByUserEmailIdFallBack(@PathParam("id") String id) {
	    	User dummyUser =  new User();
	    	dummyUser.setUserFullName("xxx");
	    	dummyUser.setUserEmailId("nothingFound@site");
	        return dummyUser;
	    }
	    
	    

	    @POST
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "userCreationFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedUserCreation", description = "How many times a new user was created.")
	    @Timed(name = "userCreationTimer", description = "A measure of how long it takes to create a user", unit = MetricUnits.MILLISECONDS)
	    public Response create(User user) {
			System.err.printf(user.getUserFullName()+" "+user.getUserEmailId());
	        user.persist();
	        return Response.status(201).build();
	    }
	    
	    public Response userCreationFallBack(User Product) {
	    	return Response.status(500).build();
	    }
	    
	    

	    @PUT
	    @Path("/{id}")
	    public void update(@PathParam("id") String id, User user) {
	        user.update();
	    }


	    @GET
	    @Path("/search/userFullName/{userFullName}")
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "userFullNameSearchFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedUserFullNameSearch", description = "How many times a user is searched on the basis of User Full Name.")
	    @Timed(name = "userFullNameSearchTimer", description = "A measure of how long it takes to search a user on basis of User Full Name", unit = MetricUnits.MILLISECONDS)
	    public List<User> searchUserFullName(@PathParam("userFullName") String userFullName) {
	        return User.findByUserFullName(userFullName);
	    }
	    
	    public List<User> userFullNameSearchFallBack(@PathParam("userFullName") String userFullName) {
	    	List<User> dummyUserList = new ArrayList<User>();
	    	User dummyUser =  new User();
	    	dummyUser.setUserFullName("xxx");
	    	dummyUser.setUserEmailId("nothingFound@site");
	    	dummyUserList.add(dummyUser);
	        return dummyUserList;
	    }
	    
		@GET
		@Path("/authUser/{userName}/{userPass}")
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "userAuthFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedUserAuth", description = "How many times a user is authenticated.")
	    @Timed(name = "userAuthTimer", description = "A measure of how long it takes to authorise a user", unit = MetricUnits.MILLISECONDS)
	    public User authUser(@PathParam("userName") String userName,@PathParam("userPass") String userPass) {
			//String message = "userNotAutenticated";
			return User.authUser(userName,userPass);
	    }
	    
	    public User userAuthFallBack(@PathParam("userName") String userName,@PathParam("userPass") String userPass) {
	        return null;
	    }
		
		
		
		
	    
	    @GET
	    @Path("/count")
	    public Long count() {
	        return User.count();
	    }
	    
	    @DELETE
	    @Path("/{id}")
	    public void delete(@PathParam("id") String id) {
	        User product = User.findById(new ObjectId(id));
	        product.delete();
	    }
	    
	    @DELETE
	    @Path("/custom/{userEmailId}")
	    public void deleteCustomId(@PathParam("userEmailId") String userEmailId) {
	        User userObj = User.findByUserEmailId(userEmailId);
	        System.out.println("The user is **************** : " + userObj.getUserFullName() );
	        userObj.delete();
	    }    
	
}
