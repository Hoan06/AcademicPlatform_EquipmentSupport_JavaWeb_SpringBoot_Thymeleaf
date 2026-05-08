package ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "borrowing_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BorrowingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "borrowing_record_id")
    private BorrowingRecord borrowingRecord;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    private Integer quantity;
}
