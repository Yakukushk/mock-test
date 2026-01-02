package com.example.demo;

public final class ErrorResponse {
  private final int status;
  private final String message;

  public ErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}