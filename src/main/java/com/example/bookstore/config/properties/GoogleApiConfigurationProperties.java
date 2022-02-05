package com.example.bookstore.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "api.google")
public class GoogleApiConfigurationProperties {
    @NotBlank(message = "'api.google.base-url' must not be blank")
    private final String baseUrl;

    @NotBlank(message = "'api.google.path-book-get' must not be blank")
    private final String pathBookGet;

    @NotBlank(message = "'api.google.param-max-results-book-get' must not be blank")
    private final String paramMaxResultsBookGet;

    public GoogleApiConfigurationProperties(String baseUrl,
                                            String pathBookGet,
                                            String paramMaxResultsBookGet) {
        this.baseUrl = baseUrl;
        this.pathBookGet = pathBookGet;
        this.paramMaxResultsBookGet = paramMaxResultsBookGet;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPathBookGet() {
        return pathBookGet;
    }

    public String getParamMaxResultsBookGet() {
        return paramMaxResultsBookGet;
    }

    @Override
    public String toString() {
        return "GoogleApiConfigurationProperties{" +
                "baseUrl='" + baseUrl + '\'' +
                ", pathBookGet='" + pathBookGet + '\'' +
                ", paramMaxResultsBookGet='" + paramMaxResultsBookGet + '\'' +
                '}';
    }
}
