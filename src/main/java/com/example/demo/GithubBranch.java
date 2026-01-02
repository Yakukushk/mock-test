package com.example.demo;

public class GithubBranch {
  private final String name;
  private final String lastCommitSha;

  public GithubBranch(String name, String lastCommitSha) {
    this.name = name;
    this.lastCommitSha = lastCommitSha;
  }

  public String getName() {
    return name;
  }

  public String getLastCommitSha() {
    return lastCommitSha;
  }
}
