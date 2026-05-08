package ra.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDTO {
    @NotNull(message = "Vui lòng chọn Khoa/Ngành")
    private Long departmentId;

    @NotNull(message = "Vui lòng chọn Giảng viên")
    private Long teacherId;

    @NotNull(message = "Vui lòng chọn ngày tư vấn")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Không được chọn ngày trong quá khứ")
    private LocalDate bookingDate;

    @NotNull(message = "Vui lòng chọn khung giờ")
    private Long timeSlotId;

    @NotEmpty(message = "Vui lòng nhập nội dung cần tư vấn")
    private String note;
}
