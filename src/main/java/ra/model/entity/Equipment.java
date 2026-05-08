package ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "equipments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer quantity;
    private String categoryName;
    private Integer available;
    private boolean isDelete;

    @OneToMany(mappedBy = "equipment")
    private List<BorrowingDetail> borrowingDetails;
}
