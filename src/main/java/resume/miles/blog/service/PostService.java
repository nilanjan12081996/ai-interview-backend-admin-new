package resume.miles.blog.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import resume.miles.blog.dto.PostDTO;
import resume.miles.blog.entity.PostEntity;
import resume.miles.blog.helper.BlogImageHelper;
import resume.miles.blog.mapper.PostMapper;
import resume.miles.blog.repository.PostRepository;
import resume.miles.blog.repository.specification.BlogSpecification;
import resume.miles.blog.utills.SlugUtil;


@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SlugUtil slugUtil;
    @Autowired
    private BlogImageHelper blogImageHelper;
    @Value("${urls.baseUrl}")
    private String baseUrl;

    public PostDTO saveAndEdit(PostDTO postDTO,Long id,String fullname){
        long total = postRepository.count();
        String slugName = postDTO.getTitle()+(total+1);
        String rawSlug = slugUtil.makeSlug(slugName);
        PostEntity postEntity = PostMapper.insertPostEntityConvert(postDTO, rawSlug, id , fullname);
        PostEntity saveDataPost = postRepository.save(postEntity);
        PostDTO convertDTO = PostMapper.insertPostDTOConvert(saveDataPost);
        return convertDTO;
    }
    public boolean publish(Long id){
        PostEntity entity = postRepository.findById(id).orElseThrow(()->new RuntimeException("post not found"+1));
        if(entity.getStatus() == 1){
            throw new RuntimeException("already publish");
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setPublishedAt(now);
        entity.setStatus(1);
        postRepository.save(entity);
        return true;
    }


    public boolean uploadImage(Long id, MultipartFile file) throws IOException{
        PostEntity entity = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog ID " + id + " not found"));


        if (file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }

        String url =  blogImageHelper.saveFile(file);
        entity.setImage(url);
        postRepository.save(entity);
        return true;
    }


    public List<PostDTO> list(Long id,String slug){
        Specification<PostEntity> spc = null;
        spc = spc == null ? Specification.where(BlogSpecification.joinTable(id,slug)) : spc.and(BlogSpecification.joinTable(id,slug));

        List<PostEntity>  post =spc == null ? postRepository.findAll():postRepository.findAll(spc);

        List<PostDTO> postDt = post.stream()
            .map(q -> PostMapper.insertPostDTOConvertResponse(q,baseUrl)) 
            .collect(Collectors.toList());

        return postDt;
    }
}
