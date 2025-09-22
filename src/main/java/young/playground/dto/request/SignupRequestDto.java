package young.playground.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignupRequestDto {
  @NotBlank @Size(max = 10)
  private String name;

  @NotBlank @Email
  private String email;

  @NotBlank @Size(min = 8, max = 64)
  private String password;
}
