package young.playground.config.response;

import lombok.Getter;

@Getter
public class CustomErrorException extends RuntimeException {
  private final ErrorCode code;

  public CustomErrorException(ErrorCode code, String message) {
    super(message);
    this.code = code;
  }

  public CustomErrorException(ErrorCode code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }
}
