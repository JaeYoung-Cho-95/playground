package young.playground.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import young.playground.dto.request.SignupRequestDto;
import young.playground.dto.response.SignupResponseDto;
import young.playground.service.AccountService;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AccountController.class)
class AccountControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private AccountService accountService;

  @Test
  @DisplayName("회원가입 테스트")
  void postSignup() throws Exception {
    //given
    Map<String, Object> body = Map.of(
        "name", "홍길동",
        "email", "user@example.com",
        "password", "P@ssw0rd!"
    );
    String json = objectMapper.writeValueAsString(body);

    SignupResponseDto mockedResponse = mock(SignupResponseDto.class);
    given(accountService.signup(any(SignupRequestDto.class)))
        .willReturn(mockedResponse);

    //when
    mockMvc.perform(
        post("/signup")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )
        .andExpect(status().isCreated());

    //then
    then(accountService).should().signup(any(SignupRequestDto.class));
  }
}