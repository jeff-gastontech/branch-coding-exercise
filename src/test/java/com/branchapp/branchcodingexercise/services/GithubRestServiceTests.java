package com.branchapp.branchcodingexercise.services;

import com.branchapp.branchcodingexercise.models.GithubRepoResponse;
import com.branchapp.branchcodingexercise.models.GithubResponse;
import com.branchapp.branchcodingexercise.models.GithubUser;
import com.branchapp.branchcodingexercise.models.GithubUserRepo;
import com.branchapp.branchcodingexercise.services.GithubRestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@WireMockTest
public class GithubRestServiceTests {

    private GithubRestService githubRestService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private int port;

    @BeforeEach
    public void setup(WireMockRuntimeInfo wmRuntimeInfo) {
        objectMapper.registerModule(new JavaTimeModule());
        // The static DSL will be automatically configured for you
        stubFor(get("/static-dsl").willReturn(ok()));

        // Instance DSL can be obtained from the runtime info parameter
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.register(get("/instance-dsl").willReturn(ok()));

        // Info such as port numbers is also available
        port = wmRuntimeInfo.getHttpPort();

        githubRestService = new GithubRestService("http://localhost:"+ port);
    }

    @Test
    public void testFindGithubUserInfoAndReposExpectValidResponse() throws JsonProcessingException {
        String username = "test";
        stubFor(
                get("/users/" + username)
                        .willReturn(
                                ok(objectMapper.writeValueAsString(
                                        GithubResponse.builder().
                                                login(username)
                                                .htmlUrl("https://localhost:"+port+"/"+ username)
                                                .email(username +"@example.com")
                                                .createdAt(Instant.parse("2011-01-25T18:44:36Z"))
                                                .location("North Pole")
                                                .avatarUrl("https://avatars.localhost.com/u/583231?v=4")
                                                .name("Santa Claus")
                                                .build()))
                                        .withHeader("Content-Type","application/json")
                        )
        );

        String repoName = "test-repo";
        stubFor(
                get("/users/"+ username +"/repos")
                        .willReturn(
                                ok(objectMapper.writeValueAsString(List.of(
                                        GithubRepoResponse
                                        .builder()
                                        .name(repoName)
                                        .htmlUrl("https://localhost:"+port+"/"+ username +"/"+ repoName).build())))
                                        .withHeader("Content-Type","application/json")
                        )
        );

        GithubUser githubUser = githubRestService.findGithubUserInfoAndRepos(username).block();

        assertEquals(objectMapper.writeValueAsString(githubUser),
                objectMapper.writeValueAsString(GithubUser.builder()
                        .userName(username)
                        .url("https://localhost:"+port+"/"+ username)
                        .email(username +"@example.com")
                        .createdAt(Instant.parse("2011-01-25T18:44:36Z"))
                        .geoLocation("North Pole")
                        .avatar("https://avatars.localhost.com/u/583231?v=4")
                        .displayName("Santa Claus")
                        .repos(List.of(
                                GithubUserRepo.builder().name(repoName).url("https://localhost:"+port+"/"+ username +"/"+ repoName).build()
                        ))
                        .build()));
    }

}
