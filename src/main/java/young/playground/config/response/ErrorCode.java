package young.playground.config.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  EMAIL_IN_USE(HttpStatus.CONFLICT),
  INVALID_INPUT(HttpStatus.BAD_REQUEST),
  DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

  private final HttpStatus status;

  ErrorCode(HttpStatus status) {
    this.status = status;
  }

  public HttpStatus status() {
    return status;
  }
}
