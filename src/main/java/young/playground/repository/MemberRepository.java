package young.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import young.playground.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
  boolean existsByEmail(String email);
}
