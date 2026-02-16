package resume.miles.blog.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private Long id;

   
    private Long userId;

    
    private Long categoryId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    // Slug is often auto-generated, but useful to return in the DTO
    private String slug;

    private String image;

    private String name;

    @Size(max = 500, message = "Summary cannot exceed 500 characters")
    private String summary;

    @NotBlank(message = "Content is required")
    private String content;

    // Defaulting to 1 (Draft) if null
    private Integer status = 1;

    private LocalDateTime publishedAt;
    
    // Read-only fields (good for returning data to UI)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
