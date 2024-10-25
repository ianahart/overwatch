package com.hart.overwatch.commentvote.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VoteTypeValidator implements ConstraintValidator<ValidVoteType, String> {

    @Override
    public void initialize(ValidVoteType constraintAnnotation) {}

    @Override
    public boolean isValid(String voteType, ConstraintValidatorContext context) {
        return "UPVOTE".equals(voteType) || "DOWNVOTE".equals(voteType);
    }
}
