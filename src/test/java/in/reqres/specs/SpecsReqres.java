package in.reqres.specs;

import config.Project;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static in.reqres.helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;


public class SpecsReqres {
    public static RequestSpecification request = with()
            .filter(withCustomTemplates())
            .baseUri(Project.config.apiBaseUrl())
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(BODY)
            .expectStatusCode(200)
            .build();
}