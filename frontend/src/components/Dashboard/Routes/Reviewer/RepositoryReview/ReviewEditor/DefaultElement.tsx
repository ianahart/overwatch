import { RenderElementProps } from 'slate-react';
import { TCustomElement, TParagraphElement } from '../../../../../../types';

export interface IAlignmentMapper {
  [key: string]: string;
  left: string;
  center: string;
  right: string;
}

const DefaultElement = (props: RenderElementProps) => {
  const alignmentMapper: IAlignmentMapper = {
    left: 'text-left',
    center: 'text-center',
    right: 'text-right',
  };

  const isParagraphElement = (element: TCustomElement): element is TParagraphElement => {
    return element.type === 'paragraph';
  };

  const alignment = isParagraphElement(props.element)
    ? alignmentMapper[props.element.align as keyof IAlignmentMapper]
    : '';

  return (
    <p className={`${alignment}`} {...props.attributes}>
      {props.children}
    </p>
  );
};

export default DefaultElement;
