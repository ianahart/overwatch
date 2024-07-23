import { RenderElementProps } from 'slate-react';
import { TCustomElement, isListElement } from '../../../../../../types';

const ListElement = (props: RenderElementProps) => {
  const element = props.element as TCustomElement;

  if (isListElement(element)) {
    return element.listType === 'unordered' ? (
      <ul className="list-disc list-inside" {...props.attributes}>
        {props.children}
      </ul>
    ) : (
      <ol className="list-decimal list-inside" {...props.attributes}>
        {props.children}
      </ol>
    );
  }
};

export default ListElement;
