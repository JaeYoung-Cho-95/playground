package young.playground.config.response;

import jakarta.servlet.http.HttpServletRequest;
import java.net.BindException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handlerBodyValidation(MethodArgumentNotValidException ex) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Validation Failed");
    pd.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
        .map(e -> Map.of("field", e.getField(),
            "message", e.getDefaultMessage(),
            "rejectedValue", String.valueOf(e.getRejectedValue()))).toList()
    );
    return ResponseEntity.badRequest().body(pd);
  }

  @ExceptionHandler({BindException.class, ConstraintViolationException.class})
  public ResponseEntity<ProblemDetail> handleParamValidation(Exception ex) {
    var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Constraint Violation");
    return ResponseEntity.badRequest().body(pd);
  }

  @ExceptionHandler(CustomErrorException.class)
  public ResponseEntity<ProblemDetail> handleBusiness(CustomErrorException ex, HttpServletRequest req) {
    log.error("Business error [{}] uri={} msg={}", ex.getCode(), req.getRequestURI(), ex.getMessage(), ex);

    var status = ex.getCode().status();
    var pd = ProblemDetail.forStatus(status);
    pd.setTitle("Business Error");
    pd.setDetail(ex.getMessage());
    pd.setProperty("code", ex.getCode().name());
    Object traceId = req.getAttribute("traceId");
    if (traceId != null) pd.setProperty("traceId", traceId);
    return ResponseEntity.status(status).body(pd);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex, HttpServletRequest req) {
    log.error("Unexpected error uri={}", req.getRequestURI(), ex);
    var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    pd.setTitle("Internal Server Error");
    pd.setDetail("예상치 못한 오류가 발생했습니다.");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
  }
}
