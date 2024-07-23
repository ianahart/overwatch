import { RenderLeafProps } from 'slate-react';

const Leaf = (props: RenderLeafProps) => {
  const styles = {
    fontWeight: props.leaf.bold ? 'bold' : 'normal',
    fontStyle: props.leaf.italic ? 'italic' : 'normal',
    textDecoration: props.leaf.underline ? 'underline' : 'unset',
  };

  return (
    <span {...props.attributes} style={styles}>
      {props.children}
    </span>
  );
};

export default Leaf;
