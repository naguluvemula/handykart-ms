package org.tcs.hackathon.prd.resource;

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
import org.tcs.hackathon.prd.model.Product;

import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import javax.enterprise.context.ApplicationScoped;

@Path("/product")
@Consumes("application/json")
@Produces("application/json")
@Liveness
@Readiness
@ApplicationScoped
public class ProductResource {
	
		
		private AtomicLong counter = new AtomicLong(0);
	
	    @GET
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "productListFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedProductList", description = "How many times the item list has been performed.")
	    @Timed(name = "productListTimer", description = "A measure of how long it takes to return the item list.", unit = MetricUnits.MILLISECONDS)
	    public List<Product> list() {
	    	//final Long invocationNumber = counter.getAndIncrement();
	    	//maybeFail(String.format("Product List () invocation #%d failed", invocationNumber));
	        return Product.listAll();
	    }
	    
	    /* Method to test the fault Tolerance*/
	    /*
	    private void maybeFail(String failureLogMessage) {
	        if (new Random().nextBoolean()) {
	            System.out.println(failureLogMessage);
	            throw new RuntimeException("Resource failure.");
	        }
	    }*/
	    
	    
	    public List<Product> productListFallBack() {
	    	List<Product> dummyProdList = new ArrayList<Product>();
	    	Product dummyProduct =  new Product();
	    	dummyProduct.setItemId(000);
	    	dummyProduct.setDescription("Product service is currently down try after some time");
	    	dummyProdList.add(dummyProduct);
	        return dummyProdList;
	    }
	    
	    
	    @GET
	    @Path("/{id}")
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "productByIdFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedProductIdSearch", description = "How many times the item search by id is performed.")
	    @Timed(name = "productIdSearchTimer", description = "A measure of how long it takes to return a particular item", unit = MetricUnits.MILLISECONDS)
	    public Product get(@PathParam("id") int id) {
	        return Product.findByItemId(id);
	    }
	    
	    public Product productByIdFallBack(@PathParam("id") int id) {
	    	Product dummyProduct =  new Product();
	    	dummyProduct.setItemId(000);
	    	dummyProduct.setDescription("Product service is currently down try after some time");
	        return dummyProduct;
	    }
	    
	    

	    @POST
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "productCreationFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedProductCreation", description = "How many times a new item was created.")
	    @Timed(name = "productCreationTimer", description = "A measure of how long it takes to create a item", unit = MetricUnits.MILLISECONDS)
	    public Response create(Product Product) {
	        Product.persist();
	        return Response.status(201).build();
	    }
	    
	    public Response productCreationFallBack(Product Product) {
	    	return Response.status(500).build();
	    }
	    
	    

	    @PUT
	    @Path("/{id}")
	    public void update(@PathParam("id") String id, Product Product) {
	        Product.update();
	    }


	    @GET
	    @Path("/search/title/{title}")
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "productTitleSearchFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedProductTitleSearch", description = "How many times a item is searched on basis for Title.")
	    @Timed(name = "productTitleSearchTimer", description = "A measure of how long it takes to search a item on basis for Title", unit = MetricUnits.MILLISECONDS)
	    public List<Product> searchTitle(@PathParam("title") String title) {
	        return Product.findByTitle(title);
	    }
	    
	    public List<Product> productTitleSearchFallBack(@PathParam("title") String title) {
	    	List<Product> dummyProdList = new ArrayList<Product>();
	    	Product dummyProduct =  new Product();
	    	dummyProduct.setItemId(000);
	    	dummyProduct.setDescription("Product service is currently down try after some time");
	    	dummyProdList.add(dummyProduct);
	        return dummyProdList;
	    }
	    
	    
	    @GET
	    @Path("/search/cat/{category}")
	    @Retry(maxRetries = 4)
	    @Timeout(250)
	    @Fallback(fallbackMethod = "productCategorySearchFallBack")
	    @CircuitBreaker(requestVolumeThreshold = 4)
	    @Counted(name = "performedProductCategorySearch", description = "How many times a item is searched on basis of Cateogry.")
	    @Timed(name = "productCategorySearchTimer", description = "A measure of how long it takes to search a item on basis of Cateogry", unit = MetricUnits.MILLISECONDS)
	    public List<Product> searchCateogry(@PathParam("category") String category) {
	        return Product.findByCategory(category);
	    }
	    
	    public List<Product> productCategorySearchFallBack(@PathParam("category") String category) {
	    	List<Product> dummyProdList = new ArrayList<Product>();
	    	Product dummyProduct =  new Product();
	    	dummyProduct.setItemId(000);
	    	dummyProduct.setDescription("Product service is currently down try after some time");
	    	dummyProdList.add(dummyProduct);
	        return dummyProdList;
	    } 
	    
	    @GET
	    @Path("/count")
	    public Long count() {
	        return Product.count();
	    }
	    
	    @DELETE
	    @Path("/{id}")
	    public void delete(@PathParam("id") String id) {
	        Product product = Product.findById(new ObjectId(id));
	        product.delete();
	    }
	    
	    @DELETE
	    @Path("/custom/{id}")
	    public void deleteCustomId(@PathParam("id") int id) {
	        Product product = Product.findByItemId(id);
	        product.delete();
	    }    
	
}
