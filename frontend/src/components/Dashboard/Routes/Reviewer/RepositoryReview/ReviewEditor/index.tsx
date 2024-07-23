import { useCallback, useMemo, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { createEditor, BaseEditor, Descendant } from 'slate';
import { Slate, Editable, withReact, ReactEditor, RenderElementProps, RenderLeafProps } from 'slate-react';
import CodeElement from './CodeElement';
import DefaultElement from './DefaultElement';
import Leaf from './Leaf';
import CustomEditor from '../../../../../../util/CustomEditor';
import ListElement from './ListElement';
import ListItemElement from './ListItemElement';
import { TCustomElement, TCustomText } from '../../../../../../types';
import Toolbar from './Toolbar';
import HeadingElement from './HeadingElement';
import { TRootState, updateRepository, useUpdateRepositoryReviewMutation } from '../../../../../../state/store';

declare module 'slate' {
  interface CustomTypes {
    Editor: BaseEditor & ReactEditor;
    Element: TCustomElement;
    Text: TCustomText;
  }
}

const ReviewEditor = () => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const [editor] = useState(() => withReact(createEditor()));
  const { repository } = useSelector((store: TRootState) => store.repositoryTree);
  const [status, setStatus] = useState(repository.status ? repository.status : 'COMPLETED');
  const [updateRepositoryReview] = useUpdateRepositoryReviewMutation();

  const initialValue: Descendant[] = useMemo(() => {
    const storedContent = localStorage.getItem('content') ?? '';
    try {
      return (
        JSON.parse(storedContent) || [
          {
            type: 'paragraph',
            children: [{ text: 'Begin writing your feedback here.', bold: false }],
          },
        ]
      );
    } catch (e) {
      console.error('Error parsing content from localStorage', e);
      return [
        {
          type: 'paragraph',
          children: [{ text: 'Begin writing your feedback here.', bold: false }],
        },
      ];
    }
  }, []);

  const renderLeaf = useCallback((props: RenderLeafProps) => {
    return <Leaf {...props} />;
  }, []);

  const renderElement = useCallback((props: RenderElementProps) => {
    switch (props.element.type) {
      case 'code':
        return <CodeElement {...props} />;
      case 'list':
        return <ListElement {...props} />;
      case 'list-item':
        return <ListItemElement {...props} />;
      case 'heading':
        return <HeadingElement {...props} />;
      default:
        return <DefaultElement {...props} />;
    }
  }, []);

  const handleOnKeyDown = (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (!event.ctrlKey) {
      return;
    }

    switch (event.key) {
      case '`': {
        event.preventDefault();
        CustomEditor.toggleCodeBlock(editor);
        break;
      }

      case 'b': {
        event.preventDefault();
        CustomEditor.toggleBoldMark(editor);
        break;
      }
    }
  };

  const handleOnChange = (value: Descendant[]) => {
    const isAstChange = editor.operations.some((op) => 'set_selection' !== op.type);
    if (isAstChange) {
      const content = JSON.stringify(value);
      localStorage.setItem('content', content);
    }
  };

  const handleOnClick = () => {
    const feedback = JSON.stringify(editor.children);
    const payload = { repositoryId: repository.id, status, feedback, token };
    updateRepositoryReview(payload)
      .unwrap()
      .then((res) => {
        const { feedback, status } = res.data;
        dispatch(updateRepository({ status, feedback }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <Slate editor={editor} initialValue={initialValue} onChange={handleOnChange}>
      <Toolbar />
      <Editable
        className="custom-editor"
        renderLeaf={renderLeaf}
        renderElement={renderElement}
        onKeyDown={handleOnKeyDown}
      />

      <div className="my-4 flex flex-col">
        <label htmlFor="status" className="block mb-2">
          Review Status
        </label>
        <select
          id="status"
          name="status"
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          className="bg-gray-900 p-2 rounded max-w-[200px]"
        >
          <option value="INCOMPLETE">In complete</option>
          <option value="INPROGRESS">In progress</option>
          <option value="COMPLETED">Completed</option>
        </select>
      </div>
      <div className="my-8">
        <button onClick={handleOnClick} className="btn max-w-[600px] w-full">
          Update Review
        </button>
      </div>
    </Slate>
  );
};

export default ReviewEditor;
