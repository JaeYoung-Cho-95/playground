package young.playground.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import young.playground.config.response.CustomErrorException;
import young.playground.config.response.ErrorCode;
import young.playground.domain.Member;
import young.playground.dto.request.SignupRequestDto;
import young.playground.dto.response.SignupResponseDto;
import young.playground.repository.MemberRepository;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock MemberRepository memberRepository;
  @Mock PasswordEncoder passwordEncoder;

  @InjectMocks AccountService accountService;

  @Nested
  @DisplayName("checkExistsMember(email)")
  class CheckExistsMember {

    @Test
    @DisplayName("이메일이 이미 존재하면 CustomErrorException(EMAIL_IN_USE)을 던진다")
    void throws_whenEmailAlreadyExists() {
      // Given
      String email = "dup@example.com";
      given(memberRepository.existsByEmail(email)).willReturn(true);

      // When & Then
      CustomErrorException ex =
          assertThrows(CustomErrorException.class, () -> accountService.checkExistsMember(email));

      assertEquals(ErrorCode.EMAIL_IN_USE, ex.getCode());
      // existsByEmail 이 정확히 1번 호출되었는지 검증
      then(memberRepository).should(times(1)).existsByEmail(email);
      then(memberRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이메일이 존재하지 않으면 예외가 발생하지 않는다")
    void doesNothing_whenEmailNotExists() {
      // Given
      String email = "free@example.com";
      given(memberRepository.existsByEmail(email)).willReturn(false);

      // When & Then
      assertDoesNotThrow(() -> accountService.checkExistsMember(email));
      then(memberRepository).should(times(1)).existsByEmail(email);
    }
  }

  @Nested
  @DisplayName("signup(request)")
  class Signup {

    @Test
    @DisplayName("정상 요청이면 비밀번호를 해시하고 회원을 저장한 뒤 응답 DTO를 반환한다")
    void signup_success() {
      // Given
      SignupRequestDto req = new SignupRequestDto("홍길동", "hong@example.com", "rawPw#1234");

      // 비밀번호 해시 동작 모킹
      given(passwordEncoder.encode("rawPw#1234")).willReturn("hashedPw");

      // Repository.save 호출 시, 저장된 Member 를 반환하도록 모킹
      // (실제로는 JPA 가 id를 채워 반환)
      Member saved = Member.from("홍길동", "hong@example.com", "hashedPw");
      // id 필요하면 리플렉션/빌더 등으로 세팅해도 무방
      given(memberRepository.save(any(Member.class))).willReturn(saved);

      // When
      SignupResponseDto res = accountService.signup(req);

      // Then
      // 응답 필드 검증 (구현체에 맞춰 필드 수정)
      assertNotNull(res);
      assertEquals("홍길동", res.getName());
      assertEquals("hong@example.com", res.getEmail());

      // 비밀번호 해시가 원문으로 호출되었는지
      then(passwordEncoder).should(times(1)).encode("rawPw#1234");

      // 저장 시점에 넘긴 Member 값을 캡쳐해서 비즈니스 규칙 검증
      ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
      then(memberRepository).should(times(1)).save(captor.capture());
      Member toSave = captor.getValue();
      assertEquals("홍길동", toSave.getName());
      assertEquals("hong@example.com", toSave.getEmail());
      assertEquals("hashedPw", toSave.getPasswordHash()); // 평문이 아닌 해시여야 함
    }

    @Test
    @DisplayName("사전 중복 체크 로직이 있다면, 중복 이메일이면 EMAIL_IN_USE 예외를 던진다")
    void signup_fails_whenEmailAlreadyExists_precheck() {
      // Given
      SignupRequestDto req = new SignupRequestDto("홍길동", "dup@example.com", "pw");
      given(memberRepository.existsByEmail("dup@example.com")).willReturn(true);

      // When & Then
      CustomErrorException ex =
          assertThrows(CustomErrorException.class, () -> accountService.signup(req));
      assertEquals(ErrorCode.EMAIL_IN_USE, ex.getCode());

      // 저장 로직은 타지 않아야 함
      then(memberRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("동시성 등으로 save 단계에서 제약 위반이 발생하면 EMAIL_IN_USE 로 매핑해 던진다")
    void signup_fails_whenUniqueConstraintViolationAtSave() {
      // Given
      SignupRequestDto req = new SignupRequestDto("홍길동", "dup2@example.com", "pw");
      given(memberRepository.existsByEmail("dup2@example.com")).willReturn(false);
      given(passwordEncoder.encode("pw")).willReturn("hashed");
      // save 시점에서 제약 위반(유니크 키) 발생을 시뮬레이션
      given(memberRepository.save(any(Member.class)))
          .willThrow(new DataIntegrityViolationException("unique_violation"));

      // When & Then
      CustomErrorException ex =
          assertThrows(CustomErrorException.class, () -> accountService.signup(req));
      assertEquals(ErrorCode.EMAIL_IN_USE, ex.getCode());
    }
  }
}
