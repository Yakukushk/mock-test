package com.example.demo;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GithubClientIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  private static WireMockServer wireMockServer;

  @BeforeAll
  static void startWireMock() {
    wireMockServer = new WireMockServer(8089);
    wireMockServer.start();
    WireMock.configureFor("localhost", 8089);
  }

  @AfterAll
  static void stopWireMock() {
    wireMockServer.stop();
  }

  @Test
  void shouldReturnRepositoriesWithoutForks() throws Exception {
    stubFor(WireMock.get(urlEqualTo("/users/testuser/repos"))
            .willReturn(okJson("""
                [
                  {
                    "name": "repo1",
                    "fork": false,
                    "owner": { "login": "testuser" }
                  },
                  {
                    "name": "forked-repo",
                    "fork": true,
                    "owner": { "login": "testuser" }
                  }
                ]
                """)));

    stubFor(WireMock.get(urlEqualTo("/repos/testuser/repo1/branches"))
            .willReturn(okJson("""
                [
                  {
                    "name": "main",
                    "commit": { "sha": "abc123" }
                  }
                ]
                """)));

    mockMvc.perform(get("/github/testuser/repositories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].repositoryName").value("repo1"))
            .andExpect(jsonPath("$[0].ownerLogin").value("testuser"))
            .andExpect(jsonPath("$[0].branches[0].name").value("main"))
            .andExpect(jsonPath("$[0].branches[0].lastCommitSha").value("abc123"));
  }
}
