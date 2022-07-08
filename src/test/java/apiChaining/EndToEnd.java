package apiChaining;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.json.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEnd {
	
	Response response;
	String baseURI = "http://3.85.227.102:8088/employees";
	
	@Test
	public void EndToEndChaining() {
		
		response = GetAllMethod();
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Get Request Completed");
		
		response = PostMethod("Neha","Mali","100000","neha@mali.com");
		Assert.assertEquals(response.getStatusCode(), 201);
		JsonPath Jpath =response.jsonPath();
		int EmpId = Jpath.get("id");
        System.out.println("Post Request Completed");
        
        response = PutMethod(EmpId,"Neha","Naik","100000","neha@naik.com");
        Assert.assertEquals(response.getStatusCode(), 200);
        Jpath =response.jsonPath();
        Assert.assertEquals(Jpath.get("lastName"), "Naik");
        System.out.println("Put Request Completed");
        
        response = DeleteMethod(EmpId);
        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println("Delete Request Completed");
        
        response = GetMethod(EmpId);
        Assert.assertEquals(response.getStatusCode(), 400);
        Jpath =response.jsonPath();
        Assert.assertEquals(Jpath.get("message"), "Entity Not Found");
        System.out.println(Jpath.get("message"));

	}
	
	public Response GetAllMethod() {
		RestAssured.baseURI = baseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get();
		return response;
	}
	
	public Response PostMethod(String firstName, String lastName, String salary, String email) {
		RestAssured.baseURI = baseURI;
		JSONObject jobj =  new JSONObject();
		jobj.put("firstName", firstName);
		jobj.put("lastName", lastName);
		jobj.put("salary", salary);
		jobj.put("email", email);
		
		RequestSpecification request = RestAssured.given();
		Response response = request.contentType(ContentType.JSON)
									.accept(ContentType.JSON)
									.body(jobj.toString())
									.post();
		return response;
	}
	
	public Response PutMethod(int EmpId,String firstName, String lastName, String salary, String email) {
		RestAssured.baseURI = baseURI;
		JSONObject jobj =  new JSONObject();
		jobj.put("id", EmpId);
		jobj.put("firstName", firstName);
		jobj.put("lastName", lastName);
		jobj.put("salary", salary);
		jobj.put("email", email);
		
		RequestSpecification request = RestAssured.given();
		Response response = request.contentType(ContentType.JSON)
									.accept(ContentType.JSON)
									.body(jobj.toString())
									.put("/"+ EmpId);
		return response;	
	}
	
	public Response DeleteMethod(int EmpId) {
		
		RestAssured.baseURI = baseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.delete("/" + EmpId);
		return response;
	}
	
	public Response GetMethod(int EmpId) {
		
		RestAssured.baseURI = baseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get("/" + EmpId);
		return response;
	}

}
