import { nanoid } from '@reduxjs/toolkit';
import { useState } from 'react';
import { AiFillStar, AiOutlineStar } from 'react-icons/ai';
import { useLocation } from 'react-router-dom';
import Avatar from '../Shared/Avatar';
import ActionReview from './ActionReview';

const CreateReview = () => {
  const { authorId, reviewerId, avatarUrl, fullName } = useLocation().state;

  return (
    <ActionReview
      action="create"
      authorId={authorId}
      reviewerId={reviewerId}
      avatarUrl={avatarUrl}
      fullName={fullName}
    />
  );
};

export default CreateReview;
