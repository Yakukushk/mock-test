package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/github")
public class GitHubController {

  private final GithubService githubService;

  public GitHubController(GithubService githubService) {
    this.githubService = githubService;
  }

  @GetMapping("{username}/repositories")
  public List<GithubRepository> getRepositories(@PathVariable String username) {
    return githubService.githubRepositories(username);
  }

}
