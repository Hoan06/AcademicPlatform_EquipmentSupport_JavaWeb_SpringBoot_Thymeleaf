package ra.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    private String username;

    private String role;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private MultipartFile imageFile;
}