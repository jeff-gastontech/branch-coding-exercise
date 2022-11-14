package com.branchapp.branchcodingexercise.services;

import com.branchapp.branchcodingexercise.models.GithubRepoResponse;
import com.branchapp.branchcodingexercise.models.GithubResponse;
import com.branchapp.branchcodingexercise.models.GithubUser;
import com.branchapp.branchcodingexercise.models.GithubUserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GithubRestService {
    private final WebClient client;

    public GithubRestService(@Value("${githubUrl}") String githubUrl) {
        client = WebClient.create(githubUrl);
    }

    public Mono<GithubUser> findGithubUserInfoAndRepos(String username) {
        return Mono.zip(getGithubUser(username),getGithubRepos(username)).flatMap(data -> {
            GithubResponse githubResponse = data.getT1();
            List<GithubRepoResponse> githubRepos = data.getT2();
            return Mono.just(GithubUser.builder()
                    .userName(githubResponse.getLogin())
                    .displayName(githubResponse.getName())
                    .avatar(githubResponse.getAvatarUrl())
                    .geoLocation(githubResponse.getLocation())
                    .email(githubResponse.getEmail())
                    .url(githubResponse.getHtmlUrl())
                    .createdAt(githubResponse.getCreatedAt())
                    .repos(githubRepos.stream().map(repo ->
                            GithubUserRepo.builder().name(repo.getName()).url(repo.getHtmlUrl()).build()
                    ).collect(Collectors.toList()))
                    .build());
        });
    }

    private Mono<GithubResponse> getGithubUser(String username) {
        return client.get().uri(uriBuilder -> uriBuilder.pathSegment("users", username).build()).retrieve().bodyToMono(GithubResponse.class);
    }

    private Mono<List<GithubRepoResponse>> getGithubRepos(String username) {
        return client.get()
                                .uri(uriBuilder -> uriBuilder.pathSegment("users",username,"repos")
                                        .build())
                                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {});
    }

}
