package com.branchapp.branchcodingexercise.controllers;

import com.branchapp.branchcodingexercise.controllers.GithubController;
import com.branchapp.branchcodingexercise.models.ErrorResponse;
import com.branchapp.branchcodingexercise.models.GithubUser;
import com.branchapp.branchcodingexercise.models.GithubUserRepo;
import com.branchapp.branchcodingexercise.services.GithubRestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = GithubController.class)
@Import(GithubRestService.class)
public class GithubControllerTests {
    @Autowired
    private WebTestClient webClient;

    private final GithubUser expectedFieldMapping = GithubUser.builder()
            .userName("jeff-gastontech")
            .avatar("https://avatars.githubusercontent.com/u/105401793?v=4")
            .displayName(null)
            .geoLocation(null)
            .email(null)
            .url("https://github.com/jeff-gastontech")
            .createdAt(Instant.parse("2022-05-12T01:31:51Z"))
            .repos(List.of(
                    GithubUserRepo.builder().name("aws-step-functions-glue").url("https://github.com/jeff-gastontech/aws-step-functions-glue").build(),
                    GithubUserRepo.builder().name("branch-coding-exercise").url("https://github.com/jeff-gastontech/branch-coding-exercise").build(),
                    GithubUserRepo.builder().name("EngineeringAsyncChallenge").url("https://github.com/jeff-gastontech/EngineeringAsyncChallenge").build(),
                    GithubUserRepo.builder().name("four-card").url("https://github.com/jeff-gastontech/four-card").build(),
                    GithubUserRepo.builder().name("media-player-example").url("https://github.com/jeff-gastontech/media-player-example").build(),
                    GithubUserRepo.builder().name("news-homepage").url("https://github.com/jeff-gastontech/news-homepage").build(),
                    GithubUserRepo.builder().name("product-feedback-app").url("https://github.com/jeff-gastontech/product-feedback-app").build(),
                    GithubUserRepo.builder().name("spring-cloud-function").url("https://github.com/jeff-gastontech/spring-cloud-function").build(),
                    GithubUserRepo.builder().name("user-management").url("https://github.com/jeff-gastontech/user-management").build()
            ))
            .build();

    @Test
    void testGetUserByUsernameExpectGeneralSuccess() {
        webClient.get().uri("/user/{username}", "jeff-gastontech")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GithubUser.class);
    }

    @Test
    void testGetUserByUsernameExpectFieldMappingSuccess() {
        webClient.get().uri("/user/{username}", "jeff-gastontech")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GithubUser.class)
                .isEqualTo(expectedFieldMapping);
    }

    /**
     * Can be flakey if you check for repo or just user due to the calls being parallel
     */
    @Test
    void testGetUserByUsernameExpectErrorFailure() {
        webClient.get().uri("/user/{username}", "jeff-gastontech123")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class);
    }
}
