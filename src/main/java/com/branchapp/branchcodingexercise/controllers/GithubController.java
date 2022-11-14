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

    /**
     * Method to find Github user and repos belonging to them and return them as a remapped user object
     *
     * @param username
     * @return
     */
    @GetMapping("/user/{username}")
    public Mono<GithubUser> findUserAndRepos(@PathVariable @NotBlank @Pattern(regexp = "^[a-z\\d](?:[a-z\\d]|-(?=[a-z\\d])){0,38}$",
            message = "Github username may only contain alphanumeric characters or hyphens. " +
                    "Github username cannot have multiple consecutive hyphens. " +
                    "Github username cannot begin or end with a hyphen. " +
                    "Maximum is 39 characters.") String username) {
        return githubRestService.findGithubUserInfoAndRepos(username);
    }

    /**
     * Exception handler for handling any exceptions that may occur. Could be narrowed down to only handle webClient exceptions though.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> serverExceptionHandler(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(400).body(ErrorResponse.builder().errorMessage(ex.getMessage()).build());
    }

}
