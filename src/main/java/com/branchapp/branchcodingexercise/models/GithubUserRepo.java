package com.branchapp.branchcodingexercise.models;

import lombok.*;


/**
 * Data response class
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GithubUserRepo {
    private String name;
    private String url;
}
