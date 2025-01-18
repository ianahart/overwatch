import { useSlate } from 'slate-react';
import {
  MdFormatBold,
  MdCode,
  MdFormatItalic,
  MdFormatUnderlined,
  MdFormatListBulleted,
  MdFormatListNumbered,
  MdFormatAlignLeft,
  MdFormatAlignCenter,
  MdFormatAlignRight,
} from 'react-icons/md';
import { LuHeading1, LuHeading2, LuHeading3, LuHeading4 } from 'react-icons/lu';
import { FaParagraph } from 'react-icons/fa';
import { useState } from 'react';
import { CiSaveUp2, CiViewList } from 'react-icons/ci';
import CustomEditor from '../../../../../../util/CustomEditor';
import ToolTip from '../../../../../Shared/ToolTip';
import ClickAway from '../../../../../Shared/ClickAway';
import { IMinFeedbackTemplate } from '../../../../../../interfaces';
import { BsTrash } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import { TRootState } from '../../../../../../state/store';

export interface IToolbarProps {
  onSaveTemplate?: () => void;
  utilizeTemplate?: (feedbackTemplateId: number) => void;
  deleteTemplate?: (feedbackTemplateId: number) => void;
  templates?: IMinFeedbackTemplate[];
}

const Toolbar = ({
  onSaveTemplate = () => {},
  templates = [],
  utilizeTemplate = () => {},
  deleteTemplate = () => {},
}: IToolbarProps) => {
  const { user } = useSelector((store: TRootState) => store.user);
  const [activeButton, setActiveButton] = useState<string>('paragraph');
  const [clickAwayOpen, setClickAwayOpen] = useState(false);
  const editor = useSlate();

  const handleActiveButtonClick = (buttonName: string, action: Function, arg?: string) => {
    setActiveButton(buttonName);
    arg === undefined ? action(editor) : action(editor, arg);
  };

  const styleActiveButton = (currentActiveBtn: string) => {
    return activeButton === currentActiveBtn ? 'text-green-400' : 'text-gray-400';
  };

  const handleOnSaveTemplate = () => {
    onSaveTemplate();
  };

  const handleCloseClickAway = () => {
    setClickAwayOpen(false);
  };

  return (
    <div className="flex flex-wrap mb-0 bg-gray-800 p-2 rounded-t max-w-full">
      <MdFormatUnderlined
        className={`editor-btn ${styleActiveButton('underline')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('underline', CustomEditor.toggleUnderlineMark);
        }}
      />
      <MdFormatItalic
        className={`editor-btn ${styleActiveButton('italic')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('italic', CustomEditor.toggleItalicMark);
        }}
      />
      <MdFormatBold
        className={`editor-btn ${styleActiveButton('bold')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('bold', CustomEditor.toggleBoldMark);
        }}
      />
      <MdCode
        className={`editor-btn ${styleActiveButton('code')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('code', CustomEditor.toggleCodeBlock);
        }}
      />
      <MdFormatListBulleted
        className={`editor-btn ${styleActiveButton('unordered')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('unordered', CustomEditor.toggleList, 'unordered');
        }}
      />
      <MdFormatListNumbered
        className={`editor-btn ${styleActiveButton('ordered')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('ordered', CustomEditor.toggleList, 'ordered');
        }}
      />
      <MdFormatAlignLeft
        className={`editor-btn ${styleActiveButton('left')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('left', CustomEditor.toggleAlignment, 'left');
        }}
      />
      <MdFormatAlignCenter
        className={`editor-btn ${styleActiveButton('center')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('center', CustomEditor.toggleAlignment, 'center');
        }}
      />
      <MdFormatAlignRight
        className={`editor-btn ${styleActiveButton('right')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('right', CustomEditor.toggleAlignment, 'right');
        }}
      />
      <LuHeading1
        className={`editor-btn ${styleActiveButton('h1')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('h1', CustomEditor.toggleHeading, 'h1');
        }}
      />
      <LuHeading2
        className={`editor-btn ${styleActiveButton('h2')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('h2', CustomEditor.toggleHeading, 'h2');
        }}
      />
      <LuHeading3
        className={`editor-btn ${styleActiveButton('h3')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('h3', CustomEditor.toggleHeading, 'h3');
        }}
      />
      <LuHeading4
        className={`editor-btn ${styleActiveButton('h4')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('h4', CustomEditor.toggleHeading, 'h4');
        }}
      />
      <FaParagraph
        className={`editor-btn ${styleActiveButton('paragraph')}`}
        onMouseDown={(event) => {
          event.preventDefault();
          handleActiveButtonClick('paragraph', CustomEditor.toggleParagraph);
        }}
      />
      {user.role === 'REVIEWER' && (
        <>
          <ToolTip message="Save as template">
            <CiSaveUp2 onClick={handleOnSaveTemplate} className="mr-2 editor-btn bg-gray-900" />
          </ToolTip>
          <div className="relative w-[120px]">
            <ToolTip message="Your templates">
              <CiViewList onClick={() => setClickAwayOpen(true)} className="mr-2 editor-btn bg-gray-900" />
            </ToolTip>
            {clickAwayOpen && (
              <ClickAway onClickAway={handleCloseClickAway}>
                <div className="absolute top-5 left-0 bg-stone-950 z-10 p-2 w-full rounded">
                  <p className="mb-2 text-xs">Your Templates</p>
                  {templates.map((template) => {
                    return (
                      <div key={template.id} className="flex justify-between my-2 rounded">
                        <p onClick={() => utilizeTemplate(template.id)} className="text-xs cursor-pointer">
                          Template{template.id}
                        </p>
                        <BsTrash onClick={() => deleteTemplate(template.id)} className="cursor-pointer" />
                      </div>
                    );
                  })}
                </div>
              </ClickAway>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default Toolbar;
