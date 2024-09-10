import { useCallback, useMemo, useState } from 'react';
import { useSelector } from 'react-redux';
import { createEditor, BaseEditor, Descendant } from 'slate';
import { Slate, Editable, withReact, ReactEditor, RenderElementProps, RenderLeafProps } from 'slate-react';
import CodeElement from '../RepositoryReview/ReviewEditor/CodeElement';
import { DefaultElement } from 'slate-react';
import Leaf from '../RepositoryReview/ReviewEditor/Leaf';
import CustomEditor from '../../../../../util/CustomEditor';
import ListElement from '../RepositoryReview/ReviewEditor/ListElement';
import ListItemElement from '../RepositoryReview/ReviewEditor/ListItemElement';
import { TCustomElement, TCustomText } from '../../../../../types';
import Toolbar from '../RepositoryReview/ReviewEditor/Toolbar';
import HeadingElement from '../RepositoryReview/ReviewEditor/HeadingElement';
import { TRootState } from '../../../../../state/store';

declare module 'slate' {
  interface CustomTypes {
    Editor: BaseEditor & ReactEditor;
    Element: TCustomElement;
    Text: TCustomText;
  }
}

export interface IDetailsEditorProps {
  details: any;
}

const DetailsEditor = ({ details }: IDetailsEditorProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [editor] = useState(() => withReact(createEditor()));

  const initialValue: Descendant[] = useMemo(() => {
    // Validate and parse the details JSON
    try {
      if (details && details.length > 0) {
        return JSON.parse(details);
      }
    } catch (error) {
      console.error('Error parsing details:', error);
    }

    // Validate and parse the local storage content
    const storedContent = localStorage.getItem('details');
    if (storedContent) {
      try {
        const parsedContent = JSON.parse(storedContent);
        // Check if parsedContent is an array
        if (Array.isArray(parsedContent)) {
          return parsedContent;
        }
      } catch (e) {
        console.error('Error parsing content from localStorage:', e);
      }
    }

    // Fallback initial content if parsing fails
    return [
      {
        type: 'paragraph',
        children: [{ text: 'Begin writing your details here.', bold: false }],
      },
    ];
  }, [details]);

  const renderLeaf = useCallback((props: RenderLeafProps) => {
    return <Leaf {...props} />;
  }, []);

  const handleOnChange = (value: Descendant[]) => {
    const isAstChange = editor.operations.some((op) => 'set_selection' !== op.type);
    if (isAstChange) {
      const details = JSON.stringify(value);
      localStorage.setItem('details', details);
    }
  };

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

  return (
    <Slate editor={editor} initialValue={initialValue} onChange={handleOnChange}>
      <Toolbar />
      <Editable
        className="custom-editor"
        renderLeaf={renderLeaf}
        renderElement={renderElement}
        onKeyDown={handleOnKeyDown}
      />
    </Slate>
  );
};

export default DetailsEditor;
