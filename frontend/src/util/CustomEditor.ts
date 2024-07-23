import { Editor, Transforms, Element } from 'slate';
import {
  TCustomElement,
  TListElement,
  TListItemElement,
  THeadingSize,
  TListType,
  TParagraphElement,
  THeadingElement,
} from '../types';

const CustomEditor = {
  isParagraphActive(editor: Editor) {
    const [match] = Editor.nodes(editor, {
      match: (n) => Element.isElement(n) && (n as TParagraphElement).type === 'paragraph',
    });
    return !!match;
  },

  isHeadingActive(editor: Editor, size: THeadingSize) {
    const [match] = Editor.nodes(editor, {
      match: (n) =>
        Element.isElement(n) && (n as THeadingElement).type === 'heading' && (n as THeadingElement).size === size,
    });
    return !!match;
  },

  isAlignmentActive(editor: Editor, alignment: 'left' | 'center' | 'right' | 'justify') {
    const [match] = Editor.nodes(editor, {
      match: (n) => Element.isElement(n) && (n as any).align === alignment,
    });
    return !!match;
  },
  isUnderlineActive(editor: Editor) {
    const marks = Editor.marks(editor);
    return marks ? marks.underline === true : false;
  },

  isItalicActive(editor: Editor) {
    const marks = Editor.marks(editor);
    return marks ? marks.italic === true : false;
  },

  isListActive(editor: Editor) {
    const [match] = Editor.nodes(editor, { match: (n) => Element.isElement(n) && n.type === 'list' });
    return !!match;
  },

  isBoldMarkActive(editor: Editor) {
    const marks = Editor.marks(editor);
    return marks ? marks.bold === true : false;
  },

  isCodeBlockActive(editor: Editor) {
    const [match] = Editor.nodes(editor, {
      match: (n) => Element.isElement(n) && n.type === 'code',
    });

    return !!match;
  },

  toggleUnderlineMark(editor: Editor) {
    const isActive = CustomEditor.isUnderlineActive(editor);
    if (isActive) {
      Editor.removeMark(editor, 'underline');
    } else {
      Editor.addMark(editor, 'underline', true);
    }
  },

  toggleBoldMark(editor: Editor) {
    const isActive = CustomEditor.isBoldMarkActive(editor);
    if (isActive) {
      Editor.removeMark(editor, 'bold');
    } else {
      Editor.addMark(editor, 'bold', true);
    }
  },

  toggleItalicMark(editor: Editor) {
    const isActive = CustomEditor.isItalicActive(editor);
    if (isActive) {
      Editor.removeMark(editor, 'italic');
    } else {
      Editor.addMark(editor, 'italic', true);
    }
  },

  toggleCodeBlock(editor: Editor) {
    const isActive = CustomEditor.isCodeBlockActive(editor);
    Transforms.setNodes(
      editor,
      { type: isActive ? undefined : 'code' },
      { match: (n) => Element.isElement(n) && Editor.isBlock(editor, n) }
    );
  },
  toggleList(editor: Editor, listType: TListType) {
    const isListActive = CustomEditor.isListActive(editor);

    Transforms.unwrapNodes(editor, { match: (n) => Element.isElement(n) && (n as any).type === 'list', split: true });

    if (!isListActive) {
      Transforms.wrapNodes(editor, { type: 'list', children: [], listType } as TListElement, {
        match: (n) => Element.isElement(n) && Editor.isBlock(editor, n),
      });

      Transforms.setNodes(editor, { type: 'list-item' } as TListItemElement, {
        match: (n) => Element.isElement(n) && Editor.isBlock(editor, n),
      });
    } else {
      Transforms.setNodes(editor, { type: 'paragraph' } as TParagraphElement, {
        match: (n) => Element.isElement(n) && (n as any).type === 'list-item',
      });
    }
  },
  toggleAlignment(editor: Editor, alignment: 'left' | 'center' | 'right') {
    const isActive = CustomEditor.isAlignmentActive(editor, alignment);

    Transforms.setNodes<TCustomElement>(
      editor,
      { align: isActive ? undefined : alignment },
      { match: (n) => Element.isElement(n) && Editor.isBlock(editor, n) }
    );
  },
  toggleHeading(editor: Editor, size: THeadingSize) {
    const isActive = CustomEditor.isHeadingActive(editor, size);

    if (isActive) {
      Transforms.setNodes<TParagraphElement>(
        editor,
        { type: 'paragraph' },
        { match: (n) => Element.isElement(n) && Editor.isBlock(editor, n) }
      );
    } else {
      Transforms.setNodes<THeadingElement>(
        editor,
        { type: 'heading', size },
        { match: (n) => Element.isElement(n) && Editor.isBlock(editor, n) }
      );
    }
  },
  toggleParagraph(editor: Editor) {
    const isActive = CustomEditor.isParagraphActive(editor);
    if (isActive) {
      Transforms.setNodes<THeadingElement>(
        editor,
        { type: 'heading', size: 'h1' },
        { match: (n) => Element.isElement(n) && Editor.isBlock(editor, n) }
      );
    } else {
      Transforms.setNodes<TParagraphElement>(
        editor,
        { type: 'paragraph' },
        { match: (n) => Element.isElement(n) && Editor.isBlock(editor, n) }
      );
    }
  },
};

export default CustomEditor;
