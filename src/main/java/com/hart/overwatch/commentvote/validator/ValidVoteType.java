package com.hart.overwatch.commentvote.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VoteTypeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidVoteType {
    String message() default "Vote type must be either UPVOTE or DOWNVOTE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
