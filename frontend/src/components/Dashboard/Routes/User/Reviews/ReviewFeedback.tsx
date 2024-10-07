import { useMemo, useState } from 'react';
import { BaseEditor, Descendant, createEditor } from 'slate';
import { TCustomElement, TCustomText } from '../../../../../types';
import { Editable, ReactEditor, Slate, withReact } from 'slate-react';

export interface IReviewFeedbackProps {
  feedback: string;
}

export interface IError {
  [index: string]: string;
}

declare module 'slate' {
  interface CustomTypes {
    Editor: BaseEditor & ReactEditor;
    Element: TCustomElement;
    Text: TCustomText;
  }
}

const ReviewFeedback = ({ feedback }: IReviewFeedbackProps) => {
  const [editor] = useState(() => withReact(createEditor()));

  const initialValue: Descendant[] = useMemo(() => {
    if (feedback && feedback.length > 0) {
      try {
        return JSON.parse(feedback);
      } catch (e) {
        console.error('Error parsing details:', e);
      }
    }

    const storedContent = localStorage.getItem('feedback') ?? '';
    try {
      return (
        JSON.parse(storedContent) || [
          {
            type: 'paragraph',
            children: [{ text: 'Begin writing your details here.', bold: false }],
          },
        ]
      );
    } catch (e) {
      console.error('Error parsing content from localStorage', e);
      return [
        {
          type: 'paragraph',
          children: [{ text: 'Begin writing your details here.', bold: false }],
        },
      ];
    }
  }, [feedback]);

  return (
    <Slate editor={editor} initialValue={initialValue}>
      <Editable className="custom-editor" readOnly={true} />
    </Slate>
  );
};

export default ReviewFeedback;
