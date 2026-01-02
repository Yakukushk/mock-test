package com.example.demo;

import java.util.List;

public class GithubRepository {
  private final String repositoryName;
  private final String ownerLogin;
  private final List<GithubBranch> branches;

  public GithubRepository(String repositoryName, String ownerLogin, List<GithubBranch> branches) {
    this.repositoryName = repositoryName;
    this.ownerLogin = ownerLogin;
    this.branches = branches;
  }

  public String getRepositoryName() {
    return repositoryName;
  }

  public String getOwnerLogin() {
    return ownerLogin;
  }

  public List<GithubBranch> getBranches() {
    return branches;
  }
}