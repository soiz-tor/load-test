import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;


import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class WMSSimulation extends Simulation{

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://127.0.0.1:8082");

    FeederBuilder.FileBased<Object> goodsFeeder = jsonFile("goods.json").queue();
//    FeederBuilder.FileBased<Object> loginFeeder = jsonFile("login.json").queue();
//    FeederBuilder.FileBased<Object> registerFeeder = jsonFile("register.json").queue();

    ScenarioBuilder goodsScn = scenario("GoodsSimulation")
            .feed(goodsFeeder)
            .exec(http("addGoodsRequest")
                    .post("/goods")
                    .header("content-type", "application/json")
                    .body(StringBody(
                            "{ \"goodsNo\":\"#{goodsNo}\",\"imgUrl\":\"#{imgUrl}\",\"name\":\"#{name}\",\"janNo\":\"#{janNo}\",\"type\":\"#{type}\",\"unit\":\"#{unit}\",\"price\":#{price}}"
                    )).asJson()
                    .check(status().is(200))
                    .check(jmesPath("code").ofInt().is(0)))
            .pause(Duration.ofSeconds(5))
            .exec(http("detailGoodsRequest1")
                    .get("/goods/detail/#{goodsNo}")
                    .header("content-type", "application/json")
                    .check(status().is(200))
                    .check(jmesPath("code").ofInt().is(0)))
            .pause(Duration.ofSeconds(5))
            .exec(http("updateGoodsRequest")
                    .put("/goods")
                    .header("content-type", "application/json")
                    .body(StringBody(
                            "{\"goodsNo\":\"#{goodsNo}\",\"imgUrl\":\"#{imgUrl}\",\"name\":\"#{name}\",\"janNo\":\"#{janNo}\",\"type\":\"#{name}\",\"unit\":\"#{unit}\",\"price\":#{price}}"
                    )).asJson()
                    .check(status().is(200))
                    .check(jmesPath("code").ofInt().is(0)))
            .pause(Duration.ofSeconds(5))
            .exec(http("detailGoodsRequest2")
                    .get("/goods/detail/#{goodsNo}")
                    .header("content-type", "application/json")
                    .check(status().is(200))
                    .check(jmesPath("code").ofInt().is(0)))
            .pause(Duration.ofSeconds(5))
            .exec(http("listGoodsRequest")
                    .get("/goods/all")
                    .header("content-type", "application/json")
                    .queryParam("page", 1)
                    .queryParam("size", 10)
                    .check(status().is(200))
                    .check(jmesPath("code").ofInt().is(0)))
            .pause(Duration.ofSeconds(5))
            .exec(http("deleteGoodsRequest")
                    .delete("/goods")
                    .header("content-type", "application/json")
                    .body(StringBody(
                            "{\"goodsNoList\":[\"#{goodsNo}\"]}"
                    )).asJson()
                    .check(status().is(200))
                    .check(jmesPath("code").ofInt().is(0)))
            ;
    {
        setUp(goodsScn.injectOpen(atOnceUsers(10)).protocols(httpProtocol));
//        setUp(scn.injectOpen(atOnceUsers(10)).protocols(httpProtocol).andThen(scn2.injectOpen(rampUsersPerSec(1).to(10).during(60)).protocols(httpProtocol)));
    }

}
