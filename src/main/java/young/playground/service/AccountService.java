package young.playground.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import young.playground.config.response.CustomErrorException;
import young.playground.config.response.ErrorCode;
import young.playground.domain.Member;
import young.playground.dto.request.SignupRequestDto;
import young.playground.dto.response.SignupResponseDto;
import young.playground.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public SignupResponseDto signup(SignupRequestDto dto) {
    String name = dto.getName();
    String email = dto.getEmail();
    String rawPassword = dto.getPassword();

    checkExistsMember(email);
    String passwordHash = passwordEncoder.encode(rawPassword);

    try {
      Member member = saveMember(name, email, passwordHash);
      return SignupResponseDto.from(
          member.getId(),
          member.getName(),
          member.getEmail()
      );
    } catch (DataIntegrityViolationException e) {
      throw new CustomErrorException(
          ErrorCode.EMAIL_IN_USE,
          "중복된 이메일입니다. 다른 이메일로 다시 시도해주세요.",
          e
      );
    }
  }

  private Member saveMember(String name, String email, String passwordHash) {
    Member member = Member.from(name, email, passwordHash);
    return memberRepository.save(member);
  }

  public void checkExistsMember(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new CustomErrorException(
          ErrorCode.EMAIL_IN_USE,
          "중복된 이메일입니다. 다른 이메일로 다시 시도해주세요."
      );
    }
  }
}
