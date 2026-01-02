package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Collections;

@Component
public final class GithubClient {
  @Value("${github.api.url:https://api.github.com}")
  private String GITHUB_API_URL;
  private static final String FORK_KEY = "fork";
  private static final String NAME_KEY = "name";
  private static final String OWNER_KEY = "owner";
  private static final String LOGIN_KEY = "login";
  private static final String COMMIT_KEY = "commit";
  private static final String SHA_KEY = "sha";

  private final RestTemplate restTemplate;

  public GithubClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<GithubRepository> fetchRepositories(String username) {
    try {
      String url = String.format("%s/users/%s/repos", GITHUB_API_URL, username);


      ResponseEntity<List<Map<String, Object>>> response = mapToResponse(url);

      List<Map<String, Object>> responseBody = response.getBody();
      if (responseBody == null) {
        return Collections.emptyList();
      }

      return responseBody.stream()
              .filter(repo -> !Boolean.TRUE.equals(repo.get(FORK_KEY)))
              .map(this::mapToRepository)
              .toList();

    } catch (HttpClientErrorException.NotFound _) {
      throw new UserNotFoundException("User not found: " + username);
    }
  }

  private GithubRepository mapToRepository(Map<String, Object> repositoryData) {
    String name = (String) repositoryData.get(NAME_KEY);
    Map<String, Object> owner = (Map<String, Object>) repositoryData.get(OWNER_KEY);
    String ownerLogin = (String) owner.get(LOGIN_KEY);

    List<GithubBranch> branches = fetchBranches(ownerLogin, name);

    return new GithubRepository(name, ownerLogin, branches);
  }

  private List<GithubBranch> fetchBranches(String owner, String repository) {
    String url = String.format("%s/repos/%s/%s/branches", GITHUB_API_URL, owner, repository);


    ResponseEntity<List<Map<String, Object>>> response = mapToResponse(url);

    List<Map<String, Object>> responseBody = response.getBody();
    if (responseBody == null) {
      return Collections.emptyList();
    }

    return responseBody.stream()
            .map(branchData -> {
              String branchName = (String) branchData.get(NAME_KEY);
              Map<String, Object> commit = (Map<String, Object>) branchData.get(COMMIT_KEY);
              String lastCommitSha = (String) commit.get(SHA_KEY);

              return new GithubBranch(branchName, lastCommitSha);
            })
            .toList();
  }

  private ResponseEntity<List<Map<String, Object>>> mapToResponse(String url) {
    return restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
    );
  }
}