package io.github.mattae.snl.core.api.services.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static io.github.mattae.snl.core.api.services.errors.ErrorConstants.PROBLEM_BASE_URL;

public class DataValidationException extends ErrorResponseException {
    public DataValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, ProblemDetailWithCause.ProblemDetailWithCauseBuilder.instance()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withType(URI.create(PROBLEM_BASE_URL + "/bad-data"))
                .withTitle("Bad Request")
                .withDetail(message)
                .build(), null);
    }
}
