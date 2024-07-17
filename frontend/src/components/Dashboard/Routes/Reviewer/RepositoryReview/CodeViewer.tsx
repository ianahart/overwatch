import { useSelector } from 'react-redux';
import { codeToHtml } from 'shiki';
import { useEffect, useRef } from 'react';
import DOMPurify from 'dompurify';
import { TRootState } from '../../../../../state/store';

const CodeViewer = () => {
  const { repositoryFile } = useSelector((store: TRootState) => store.repositoryTree);
  const codeContainerRef = useRef<HTMLPreElement>(null);

  useEffect(() => {
    const highlightCode = async () => {
      if (!repositoryFile.content || !repositoryFile.language || !codeContainerRef.current) return;

      const tokens = await codeToHtml(repositoryFile.content, {
        lang: repositoryFile.language,
        theme: 'vitesse-dark',
      });

      const sanitizedTokens = DOMPurify.sanitize(tokens);
      codeContainerRef.current.innerHTML = sanitizedTokens;
    };

    highlightCode();
  }, [repositoryFile.content, repositoryFile.language]);

  return (
    <div
      className="rounded bg-gray-950 w-full md:flex-grow-[2] min-h-[90vh]
"
    >
      <pre className="code-review-code" ref={codeContainerRef}></pre>
    </div>
  );
};

export default CodeViewer;
