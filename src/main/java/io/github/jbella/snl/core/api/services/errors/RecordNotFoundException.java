package io.github.jbella.snl.core.api.services.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class RecordNotFoundException extends AbstractThrowableProblem {
    public RecordNotFoundException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, "Bad request", Status.NOT_FOUND, message);
    }
}
