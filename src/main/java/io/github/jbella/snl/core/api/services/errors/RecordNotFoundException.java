package io.github.jbella.snl.core.api.services.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static io.github.jbella.snl.core.api.services.errors.ErrorConstants.PROBLEM_BASE_URL;

public class RecordNotFoundException extends ErrorResponseException {
    public RecordNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, ProblemDetailWithCause.ProblemDetailWithCauseBuilder.instance()
                .withStatus(HttpStatus.NOT_FOUND.value())
                .withType(URI.create(PROBLEM_BASE_URL + "/not-found"))
                .withTitle("NOT_FOUND")
                .withDetail(message)
                .build(), null);
    }
}
