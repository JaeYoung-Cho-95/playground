package young.playground.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import young.playground.dto.request.SignupRequestDto;
import young.playground.dto.response.SignupResponseDto;
import young.playground.service.AccountService;


@RestController
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public SignupResponseDto signup(@Valid @RequestBody SignupRequestDto dto) {
    return accountService.signup(dto);
  }
}
