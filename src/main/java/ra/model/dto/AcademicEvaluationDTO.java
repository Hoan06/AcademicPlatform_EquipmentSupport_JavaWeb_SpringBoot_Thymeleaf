package ra.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.model.entity.MentoringSession;
import ra.model.enum_status.BorrowingStatus;
import ra.model.enum_status.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcademicEvaluationDTO {
    private Long sessionId;

    @NotNull(message = "Bạn chưa chọn phòng lab")
    private Long labRoomId;

    @NotBlank(message = "Đánh giá không được để trống")
    private String comment;

    @NotNull(message = "Hãy chọn điểm đánh giá")
    private Integer performance;

    @Valid
    @NotEmpty(message = "Vui lòng chọn ít nhất một thiết bị")
    private List<EquipmentItemDTO> equipments = new ArrayList<>();
}
