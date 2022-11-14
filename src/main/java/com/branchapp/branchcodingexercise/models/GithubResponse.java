package com.branchapp.branchcodingexercise.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Class to model response directly from Github
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubResponse {
    private String login;
    private String name;
    private String avatarUrl;
    private String location;
    private String email;
    private String htmlUrl;
    private Instant createdAt;
}
