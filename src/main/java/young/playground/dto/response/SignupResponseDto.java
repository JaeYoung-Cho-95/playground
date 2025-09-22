package young.playground.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class SignupResponseDto {
  private final Long id;
  private final String name;
  private final String email;

  public static SignupResponseDto from(Long id, String name, String email) {
    return SignupResponseDto.builder()
        .id(id)
        .name(name)
        .email(email)
        .build();
  }
}
