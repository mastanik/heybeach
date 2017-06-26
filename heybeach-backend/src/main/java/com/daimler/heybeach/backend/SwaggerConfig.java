package com.daimler.heybeach.backend;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Primary
    @Bean
    public ApiListingScanner addExtraOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
        return new FormLoginOperations(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    public class FormLoginOperations extends ApiListingScanner {
        @Autowired
        private TypeResolver typeResolver;

        @Autowired
        public FormLoginOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
            super(apiDescriptionReader, apiModelReader, pluginsManager);
        }

        @Override
        public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
            final Multimap<String, ApiListing> def = super.scan(context);

            final List<ApiDescription> apis = new LinkedList<>();

            final List<Operation> login = new ArrayList<>();
            login.add(new OperationBuilder(new CachingOperationNameGenerator())
                    .tags(Sets.newHashSet("authentication"))
                    .method(HttpMethod.POST)
                    .uniqueId("login")
                    .parameters(Arrays.asList(new ParameterBuilder()
                                    .name("username")
                                    .description("The username")
                                    .parameterType("query")
                                    .type(typeResolver.resolve(String.class))
                                    .modelRef(new ModelRef("string"))
                                    .build(),
                            new ParameterBuilder()
                                    .name("password")
                                    .description("The password")
                                    .parameterType("query")
                                    .type(typeResolver.resolve(String.class))
                                    .modelRef(new ModelRef("string"))
                                    .build()))
                    .summary("Log in") //
                    .notes("Here you can log in")
                    .build());

            final List<Operation> logout = new ArrayList<>();
            logout.add(new OperationBuilder(new CachingOperationNameGenerator())
                    .tags(Sets.newHashSet("authentication"))
                    .method(HttpMethod.POST)
                    .uniqueId("logout")
                    .summary("Log out") //
                    .notes("Here you can log out")
                    .build());

            apis.add(new ApiDescription("/login", "Authentication documentation", login, false));
            apis.add(new ApiDescription("/logout", "Authentication documentation", logout, false));

            def.put("authentication", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                    .apis(apis)
                    .description("Custom authentication")
                    .build());

            return def;
        }
    }
}
