package young.playground.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Member {
  @Id @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, length = 255)
  @JsonProperty(access = Access.READ_ONLY)
  private String passwordHash;

  @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<CommunityBoard> communityBoardList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<BoardComment> boardCommentList = new ArrayList<>();

  @CreatedDate
  @Column(name="created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name="updated_at")
  private LocalDateTime updatedAt;

  @Builder
  private Member(String name, String email, String passwordHash) {
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
  }

  public static Member from(String name, String email, String passwordHash) {
    return Member.builder()
        .name(name)
        .email(email)
        .passwordHash(passwordHash)
        .build();
  }
}
