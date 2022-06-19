package com.ldnhat.springbootblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private ApiKey apiKey(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    // set api info
    private ApiInfo apiInfo(){
        return new ApiInfo("Spring blog rest apis",
                "Spring Boot Blog REST API Documentation",
                "1",
                "Terms of service",
                new Contact("Le Duc Nhat", "https://github.com/ldnhat19ce/spring-boot-blog",
                        "nhatkt21@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList()
                );
    }


    // cấu hình của swagger chủ yếu thông qua docket bean
    @Bean
    public Docket api(){
        // sử dụng swagger 2
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())

                //cho phép Swagger truy cập API được bảo mật bằng OAuth bằng cách sử dụngAuthorization Code
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                //SecurityScheme, nó sẽ dùng để diễn tả cách mà
                // PI được bảo mật (Basic Authentication, OAuth2, …)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        //Sử dụng any() cho cả 2 sẽ tạo documentation cho toàn bộ API khả dụng thông qua Swagger
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth(){
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }

}
