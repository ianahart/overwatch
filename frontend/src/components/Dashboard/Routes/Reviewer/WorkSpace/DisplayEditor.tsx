import { BsTextLeft } from 'react-icons/bs';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useMemo, useState } from 'react';
import { ITodoCard } from '../../../../../interfaces';
import { TRootState, useUpdateTodoCardMutation, updateTodoListTodoCard } from '../../../../../state/store';
import DetailsEditor from './DetailsEditor';
import { BaseEditor, Descendant, createEditor } from 'slate';
import { TCustomElement, TCustomText } from '../../../../../types';
import { Editable, ReactEditor, Slate, withReact } from 'slate-react';

export interface IDisplayEditorProps {
  details: any;
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

const DisplayEditor = ({ details }: IDisplayEditorProps) => {
  const [editor] = useState(() => withReact(createEditor()));

  const initialValue: Descendant[] = useMemo(() => {
    if (details && details.length > 0) {
      try {
        return JSON.parse(details);
      } catch (e) {
        console.error('Error parsing details:', e);
      }
    }

    const storedContent = localStorage.getItem('details') ?? '';
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
  }, [details]); // Add dependency on details

  return (
    <Slate editor={editor} initialValue={initialValue}>
      <Editable className="custom-editor" readOnly={true} />
    </Slate>
  );
};

export default DisplayEditor;
