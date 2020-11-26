package tests;

import org.json.JSONObject;
import org.junit.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestNGCRUD 
{
	//Declare global objects
	public RequestSpecification req;
	public Response res;
	public JsonPath jp;
	public JSONObject jo;
	
	//Annotated methods
	@BeforeMethod
	public void common()
	{
		//Register end-point of Restful service under testing(SUT)
		RestAssured.baseURI="https://jsonplaceholder.typicode.com";
		//Define a default HTTP request
		req=RestAssured.given(); //Define request object
	}
	
	@Test(priority=1)
	public void getPosts()
	{
		res=req.request(Method.GET,"/posts"); //submit request to service via HTTP method
		int sc=res.getStatusCode();
		String rb=res.getBody().asString();
		if(sc==200 && !rb.equals(""))
		{
			Reporter.log("posts resource is working correctly");
			Assert.assertTrue(true);
		}
		else
		{
			Reporter.log("posts resource is not working correctly");
			Assert.assertTrue(false);
		}
	}
	
	@Test(priority=2)
	public void getSpecifiedPost()
	{
		res=req.request(Method.GET,"/posts/1"); //submit request to service via HTTP method
		int sc=res.getStatusCode();
		jp=res.jsonPath();
		int id=jp.get("id");
		if(sc==200 && id==1)
		{
			Reporter.log("Specific post resource is working correctly");
			Assert.assertTrue(true);
		}
		else
		{
			Reporter.log("Specific post resource is not working correctly");
			Assert.assertTrue(false);
		}
	}
	
	@Test(priority=3)
	public void createpost() throws Exception
	{
		jo=new JSONObject();
		jo.put("userId",10);
		jo.put("id",101);
		jo.put("title","magnitia");
		jo.put("body","Students are very good");
		req.header("Content-Type","application/json");
		req.body(jo.toString());
		res=req.request(Method.POST,"/posts"); //submit request to service via HTTP method
		int sc=res.getStatusCode();
		if(sc==201)
		{
			Reporter.log("New post creation worked correctly");
			Assert.assertTrue(true);
		}
		else
		{
			Reporter.log("New post creation did not work correctly");
			Assert.assertTrue(false);
		}
	}
	
	@Test(priority=4, dependsOnMethods= {"createpost"})
	public void updatepost() throws Exception
	{
		jo=new JSONObject();
		jo.put("userID",10);
		jo.put("id",101);
		jo.put("title","magnitia");
		jo.put("body","Students were listening properly");
		req.body(jo.toString());
		res=req.request(Method.PUT,"/posts/1");
		int sc=res.getStatusCode();
		if(sc==200)
		{
			Reporter.log("Existing post updation worked correctly");
			Assert.assertTrue(true);
		}
		else
		{
			Reporter.log("Existing post updation did not work correctly");
			Assert.assertTrue(false);
		}
	}
	
	@Test(priority=5, dependsOnMethods= {"createpost","updatepost"})
	public void deletepost()
	{
		res=req.request(Method.DELETE,"/posts/101");
		int sc=res.getStatusCode();
		if(sc==200)
		{
			Reporter.log("Existing post deletion worked correctly");
			Assert.assertTrue(true);
		}
		else
		{
			Reporter.log("Existing post deletion did not work correctly");
			Assert.assertTrue(false);
		}
	}
}
