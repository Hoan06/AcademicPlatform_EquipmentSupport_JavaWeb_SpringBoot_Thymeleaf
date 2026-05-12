package ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "lab_rooms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LabRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "labRoom")
    private List<AcademicEvaluation> evaluations;
}
