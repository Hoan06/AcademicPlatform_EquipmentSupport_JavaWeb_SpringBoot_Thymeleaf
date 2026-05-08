package ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.model.enum_status.SessionStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "mentoring_sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MentoringSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private User lecturer;

    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
    private String note;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private AcademicEvaluation evaluation;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private BorrowingRecord borrowingRecord;
}
