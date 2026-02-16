package resume.miles.blog.mapper;

import resume.miles.blog.dto.PostDTO;
import resume.miles.blog.entity.PostEntity;

public class PostMapper {
    public static PostEntity insertPostEntityConvert(PostDTO postdto,String slug,Long id,String fullname){
            PostEntity entity = PostEntity.builder()
                                .categoryId(4L)
                                .title(postdto.getTitle())
                                .content(postdto.getContent())
                                .slug(slug)
                                .userId(id)
                                .name(fullname)
                                .status(0)
                                .summary(postdto.getSummary())
                                .build();

            return entity;
    }

      public static PostDTO insertPostDTOConvert(PostEntity postdto){
            PostDTO entity = PostDTO.builder()
                                .id(postdto.getId())
                                .categoryId(postdto.getCategoryId())
                                .title(postdto.getTitle())
                                .content(postdto.getContent())
                                .slug(postdto.getSlug())
                                .userId(postdto.getUserId())
                                .summary(postdto.getSummary())
                                .status(postdto.getStatus())
                                .name(postdto.getName())
                                .createdAt(postdto.getCreatedAt())
                                .updatedAt(postdto.getUpdatedAt())
                                .publishedAt(postdto.getPublishedAt())
                                .build();

            return entity;
    }

    public static PostDTO insertPostDTOConvertResponse(PostEntity postdto,String baseUrl){
            PostDTO entity = PostDTO.builder()
                                .id(postdto.getId())
                                .categoryId(postdto.getCategoryId())
                                .image(postdto.getImage() != null ? baseUrl+postdto.getImage():null)
                                .title(postdto.getTitle())
                                .content(postdto.getContent())
                                .slug(postdto.getSlug())
                                .userId(postdto.getUserId())
                                .summary(postdto.getSummary())
                                .status(postdto.getStatus())
                                .name(postdto.getName())
                                .createdAt(postdto.getCreatedAt())
                                .updatedAt(postdto.getUpdatedAt())
                                .publishedAt(postdto.getPublishedAt())
                                .build();

            return entity;
    }
}
