import { RenderElementProps } from 'slate-react';

const ListItemElement = (props: RenderElementProps) => {
  return <li {...props.attributes}>{props.children}</li>;
};

export default ListItemElement;
