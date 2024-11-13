package io.github.mattae.snl.test.core;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class ValidationError {
    private String field;
    private String message;
}
