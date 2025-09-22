package young.playground.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
public class CommunityBoard {
  @Id @GeneratedValue
  @Column(name = "community_board_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "communityBoard", cascade = CascadeType.REMOVE)
  private List<BoardComment> boardCommentList = new ArrayList<>();

  private String title;
  private String content;

  @Column(nullable = true)
  private List<String> tags;

  @CreatedDate
  @Column(name="created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name="updated_at")
  private LocalDateTime updatedAt;
}
