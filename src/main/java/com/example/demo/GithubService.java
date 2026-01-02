package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GithubService {
  private final GithubClient githubClient;

  public GithubService(GithubClient githubClient) {
    this.githubClient = githubClient;
  }

  public List<GithubRepository> githubRepositories(String username) {
    return githubClient.fetchRepositories(username);
  }
}
