package ra.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EquipmentDTO {
    private Long id;

    @NotBlank(message = "Tên thiết bị không được để trống")
    private String name;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải >= 0")
    private Integer quantity;

    @NotBlank(message = "Danh mục không được để trống")
    private String categoryName;


}
