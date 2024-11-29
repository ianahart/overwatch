import CodeMirror from '@uiw/react-codemirror';
import { EditorView } from '@codemirror/view';
import { javascript } from '@codemirror/lang-javascript';
import { python } from '@codemirror/lang-python';
import { html } from '@codemirror/lang-html';
import { java } from '@codemirror/lang-java';
import { go } from '@codemirror/lang-go';
import { rust } from '@codemirror/lang-rust';
import { cpp } from '@codemirror/lang-cpp';
import { sql } from '@codemirror/lang-sql';
import { useState } from 'react';
import { oneDark } from '@codemirror/theme-one-dark';
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { TRootState, useCreateTeamPostMutation } from '../../../state/store';
import { IError } from '../../../interfaces';
import { nanoid } from 'nanoid';

export interface ICodeEditorProps {
  closeModal: () => void;
}

type SupportedLanguage = 'javascript' | 'python' | 'html' | 'java' | 'go' | 'rust' | 'cpp' | 'sql';

const languages: Record<SupportedLanguage, any> = {
  javascript: javascript(),
  python: python(),
  html: html(),
  java: java(),
  go: go(),
  rust: rust(),
  cpp: cpp(),
  sql: sql(),
};

const CodeEditor = ({ closeModal }: ICodeEditorProps) => {
  const params = useParams();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const teamId = Number.parseInt(params.teamId as string);
  const [language, setLanguage] = useState<SupportedLanguage>('javascript');
  const [errors, setErrors] = useState<string[]>([]);
  const [code, setCode] = useState('// Write your code here...');
  const [createTeamPost] = useCreateTeamPostMutation();

  const handleLanguageChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setLanguage(event.target.value as SupportedLanguage);
  };

  const handleEditorChange = (value: string): void => {
    setCode(value);
  };

  const applyErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      setErrors((prevState) => [...prevState, data[prop]]);
    }
  };

  const handleCreateTeamPost = (): void => {
    if (!teamId || code.trim().length === 0) {
      return;
    }
    const payload = { code, userId: user.id, token, teamId, language };

    createTeamPost(payload)
      .unwrap()
      .then((res) => {
        console.log(res);
        closeModal();
      })
      .catch((err) => {
        applyErrors(err.data);
        console.log(err);
      });
  };

  return (
    <div className="p-4">
      {errors.length > 0 && (
        <div>
          {errors.map((error) => {
            return (
              <p key={nanoid()} className="text-red-300 text-sm">
                {error}
              </p>
            );
          })}
        </div>
      )}
      <label htmlFor="language-select" className="block mb-2 text-sm font-medium text-gray-700">
        Select Language:
      </label>
      <select
        id="language-select"
        value={language}
        onChange={handleLanguageChange}
        className="mb-4 p-2 border rounded-md shadow-sm"
      >
        {Object.keys(languages).map((lang) => (
          <option key={lang} value={lang}>
            {lang}
          </option>
        ))}
      </select>

      <CodeMirror
        value={code}
        height="400px"
        extensions={[languages[language], EditorView.lineWrapping]}
        theme={oneDark}
        onChange={handleEditorChange}
        className="rounded-md border"
      />
      <div className="my-8">
        <button onClick={handleCreateTeamPost} className="px-2 py-1 rounded bg-green-400 text-gray-800 mr-5">
          Create
        </button>
        <button onClick={closeModal} className="px-2 py-1 rounded bg-blue-400 text-gray-800 ml-5">
          Cancel
        </button>
      </div>
    </div>
  );
};

export default CodeEditor;
