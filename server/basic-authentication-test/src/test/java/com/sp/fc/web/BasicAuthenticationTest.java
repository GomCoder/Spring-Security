package com.sp.fc.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationTest {

    @LocalServerPort
    int port; //로컬 서버 포트를 받음

    //클라이언트
    RestTemplate client = new RestTemplate();

    //greeting URL
    private String greetingUrl() {
        return "http://localhost:" + port + "/greeting";
    }

    //-----------GET 방식에서의 테스트 ----------------------

    @DisplayName("1. 인증 실패")
    @Test
    void test_1() {
        //일반적인 테스트를 작성하는 경우
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            client.getForObject(greetingUrl(), String.class);
        });

        assertEquals(401, exception.getRawStatusCode());
    }

    @DisplayName("2. 인증 성공")
    @Test
    void test_2() {
        HttpHeaders headers = new HttpHeaders(); //headers 선언
        //HttpHeaders에 있는 AUTHORIZATION에 Basic 토큰을 Base64로 Username과 Password를 인코딩함
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));
        //HttpEntity를 Header에 담음
        HttpEntity entity = new HttpEntity(null, headers);
        //greeting URL에서 GET 방식으로 Entity를 String으로 가져와 Response에 담음
        ResponseEntity<String> response = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);

        //System.out.println(response.getBody());
        assertEquals("Hello", response.getBody());
    }

    /**
     * test_2에서 작성한 테스트를 다른 방법으로 작성하는 방법
     */
    @DisplayName("3. 인증 성공")
    @Test
    void test_3() {
        //TestRestTemplate에서는 기본적으로 Basic Token을 지원한다. - 테스트 시 이용하는 것이 편리함
        //바로 객체의 기본 인증을  Basic header Token을 넣어서 Request를 날려준다. 그래서 response를 바로 받을 수 있음
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String response = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("Hello", response);
    }


    //-----------POST 방식에서의 테스트 ----------------------

    @DisplayName("4.POST 인증")
    @Test
    void test_4() {
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> response = testClient.postForEntity(greetingUrl(), "hong", String.class);

        assertEquals("Hello hong", response.getBody());

    }
}
