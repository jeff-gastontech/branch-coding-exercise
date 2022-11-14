package com.branchapp.branchcodingexercise.controllers;

import com.branchapp.branchcodingexercise.services.GithubRestService;
import com.branchapp.branchcodingexercise.models.ErrorResponse;
import com.branchapp.branchcodingexercise.models.GithubUser;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@RestController
@Validated
public class GithubController {

    private GithubRestService githubRestService;

    public GithubController(GithubRestService githubRestService) {
        this.githubRestService = githubRestService;
    }

    @GetMapping("/user/{username}")
    public Mono<GithubUser> findUserAndRepos(@PathVariable @NotBlank @Pattern(regexp = "^[a-zA-Z0-9]$") String username) {
        return githubRestService.findGithubUserInfoAndRepos(username);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> serverExceptionHandler(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(400).body(ErrorResponse.builder().errorMessage(ex.getMessage()).build());
    }

}
