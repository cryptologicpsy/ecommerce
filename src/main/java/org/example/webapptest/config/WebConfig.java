package org.example.webapptest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${product.image.folder}")
    private String productImageFolder;

    @Value("${uploaded.image.folder}")
    private String uploadedImageFolder;

    

    


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	 registry.addResourceHandler("/downloadFile/**")
         .addResourceLocations("file:" + productImageFolder);
    	
        registry.addResourceHandler("/uploaded/**")
                .addResourceLocations("file:" + uploadedImageFolder);
        
        
        
        
        
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    
}