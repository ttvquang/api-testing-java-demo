package countries;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
}
