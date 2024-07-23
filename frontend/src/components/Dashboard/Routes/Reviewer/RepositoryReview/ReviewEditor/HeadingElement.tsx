import { RenderElementProps } from 'slate-react';
import { TCustomElement, THeadingElement } from '../../../../../../types';

const HeadingElement = (props: RenderElementProps) => {
  const isHeadingElement = (element: TCustomElement): element is THeadingElement => {
    return element.type === 'heading';
  };

  const renderHeadingSize = () => {
    if (isHeadingElement(props.element)) {
      const headingElement = props.element as THeadingElement;
      switch (headingElement.size) {
        case 'h1':
          return (
            <h1 className="text-4xl" {...props.attributes}>
              {props.children}
            </h1>
          );
        case 'h2':
          return (
            <h2 className="text-3xl" {...props.attributes}>
              {props.children}
            </h2>
          );
        case 'h3':
          return (
            <h3 className="text-2xl" {...props.attributes}>
              {props.children}
            </h3>
          );
        case 'h4':
          return (
            <h4 className="text-xl" {...props.attributes}>
              {props.children}
            </h4>
          );
        default:
        case 'h1':
          return (
            <h1 className="text-4xl" {...props.attributes}>
              {props.children}
            </h1>
          );
      }
    }
  };

  return <>{renderHeadingSize()}</>;
};

export default HeadingElement;
