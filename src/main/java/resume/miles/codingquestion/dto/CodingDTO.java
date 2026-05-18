package resume.miles.codingquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.codingans.dto.CodingAnsDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingDTO {
    private Long id;
    private String token;
    private String questionData;
    private Integer status;
    private String aiCost;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CodingAnsDto> answers;
}
