// package resume.miles.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class MvcConfig implements WebMvcConfigurer {
//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         // This maps the URL "/uploads/**" directly to the physical file path on your server
//         registry.addResourceHandler("/uploads/**")
//                 .addResourceLocations("file:./src/main/resources/static/uploads/"); 
//                 // NOTE: Using "file:./src/..." works if you run the jar from the project root.
//                 // If that fails, use the FULL ABSOLUTE PATH from your screenshot:
//                 // .addResourceLocations("file:/htdocs/goodmoodnodeadminapi.goodmood.solutions/src/main/resources/static/uploads/");
//     }
// }






package resume.miles.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = Paths.get("uploads")
                .toAbsolutePath()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}
