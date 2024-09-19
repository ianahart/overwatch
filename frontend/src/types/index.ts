import { ITodoCard, ICustomFieldTypeOption } from '../interfaces';

export type TCustomFieldValue = string | ICustomFieldTypeOption;

export type TCustomText = {
  text: string;
  bold: boolean;
  italic?: boolean;
  underline?: boolean;
};

export type THeadingSize = 'h1' | 'h2' | 'h3' | 'h4';

export type TListType = 'ordered' | 'unordered';

export type THeadingElement = { type: 'heading'; children: TCustomText[]; size: THeadingSize };

export type TParagraphElement = { type: 'paragraph'; children: TCustomText[]; align?: 'left' | 'center' | 'right' };

export type TCodeElement = { type: 'code'; children: TCustomText[] };

export type TListItemElement = { type: 'list-item'; children: TCustomText[] };

export type TListElement = { type: 'list'; listType: TListType; children: TListItemElement[] };

export type TCustomElement = TParagraphElement | TCodeElement | TListElement | TListItemElement | THeadingElement;

export function isListElement(element: TCustomElement): element is TListElement {
  return element.type === 'list';
}

export type TPureTodoCard = Omit<ITodoCard, 'todoListId' | 'userId' | 'todoListTitle'>;
