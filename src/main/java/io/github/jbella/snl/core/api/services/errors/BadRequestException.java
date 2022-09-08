package io.github.jbella.snl.core.api.services.errors;


import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class BadRequestException extends AbstractThrowableProblem {
    public BadRequestException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, "Bad request", Status.BAD_REQUEST, message);
    }
}
