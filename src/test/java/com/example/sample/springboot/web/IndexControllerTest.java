package com.example.sample.springboot.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 메인페이지_로딩() {
        //when
        String body = this.restTemplate.getForObject("/", String.class);
        //RestTemplate: Spring 3부터 지원 되었고 REST API 호출이후 응답을 받을 때까지 기다리는 동기방식이다
        //getForObject: 주어진 URL 주소로 HTTP GET 메서드로 객체로 결과를 반환받는다

        //then
        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }
}
