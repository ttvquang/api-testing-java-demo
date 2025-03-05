package countries;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Option;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.hamcrest.MatcherAssert.*;


public class CountriesTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    @Test
    void getCountries() {
        RestAssured.get("/api/v1/countries").
                then().log().all()
                .statusCode(200);
    }

    @Test
    void verifyGetCountrySchema() {
        RestAssured.get("/api/v1/countries").
                then().log().all()
                .statusCode(200).
                assertThat().body(matchesJsonSchemaInClasspath("data/get-country/get-country-json-schema.json"));
    }

    @Test
    void verifyGetCountryCorrectData() {
        String expectedResponse = """
                [{"name":"Viet Nam","code":"VN"},{"name":"USA","code":"US"},{"name":"Canada","code":"CA"},{"name":"UK","code":"GB"},{"name":"France","code":"FR"},{"name":"Japan","code":"JP"},{"name":"India","code":"IN"},{"name":"China","code":"CN"},{"name":"Brazil","code":"BR"}]""";
        String expectedResponseOrder = """
                [{"name":"USA","code":"US"},{"name":"Viet Nam","code":"VN"},{"name":"Canada","code":"CA"},{"name":"UK","code":"GB"},{"name":"France","code":"FR"},{"name":"Japan","code":"JP"},{"name":"India","code":"IN"},{"name":"China","code":"CN"},{"name":"Brazil","code":"BR"}]""";

        Response response = RestAssured.get("/api/v1/countries");
        assertThat(response.statusCode(),Matchers.equalTo(200));
        assertThat(response.header("Content-Type"),Matchers.equalTo("application/json; charset=utf-8"));
        assertThat(response.header("X-Powered-By"),Matchers.equalTo("Express"));
        //2. Verify Body
        System.out.println(response.asString());
        assertThatJson(response.asString()).isEqualTo(expectedResponse);
        // Wrong order
        assertThatJson(response.asString()).when(Option.IGNORING_ARRAY_ORDER).isEqualTo(expectedResponse);
        // Extra field
        assertThatJson(response.asString()).when(Option.IGNORING_ARRAY_ORDER,Option.IGNORING_ARRAY_ORDER).isEqualTo(expectedResponse);




    }
}
