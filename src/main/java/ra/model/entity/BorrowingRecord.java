package ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.model.enum_status.BorrowingStatus;

import java.util.List;

@Entity
@Table(name = "borrowing_records")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "session_id")
    private MentoringSession session;

    @Enumerated(EnumType.STRING)
    private BorrowingStatus status;

    @OneToMany(mappedBy = "borrowingRecord", cascade = CascadeType.ALL)
    private List<BorrowingDetail> details;
}
