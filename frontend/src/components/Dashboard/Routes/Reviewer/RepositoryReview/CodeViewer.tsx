import { useDispatch, useSelector } from 'react-redux';
import { codeToHtml } from 'shiki';
import { useCallback, useEffect, useRef } from 'react';
import DOMPurify from 'dompurify';
import { debounce } from 'lodash';

import {
  TRootState,
  setInitialRepositoryTree,
  setRepositoryPage,
  setSearchingCodeQuery,
  useLazySearchRepositoryQuery,
} from '../../../../../state/store';
import { Session } from '../../../../../util/SessionService';

const CodeViewer = () => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const githubId = Session.getItem('github_access_token') ?? '';
  const { repositoryFile, repositoryPage, repository } = useSelector((store: TRootState) => store.repositoryTree);
  const [searchRepository] = useLazySearchRepositoryQuery();
  const codeContainerRef = useRef<HTMLPreElement>(null);

  const handleOnSearch = (query: string) => {
    dispatch(setRepositoryPage(0));
    const payload = {
      token,
      githubId: Number.parseInt(githubId),
      repositoryPage: 0,
      repoName: repository.repoName,
      query,
    };
    searchRepository(payload)
      .unwrap()
      .then((res) => {
        dispatch(setInitialRepositoryTree(res.data.contents.tree));
        dispatch(setRepositoryPage(repositoryPage + 1));
        dispatch(setSearchingCodeQuery(query));
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

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

  const handleOnHighlight = useCallback(
    debounce(() => {
      if (!codeContainerRef.current) return;

      const selection = document.getSelection();
      if (selection && selection.toString().trim() && codeContainerRef.current.contains(selection.anchorNode)) {
        const highlightedText = selection.toString().trim();
        handleOnSearch(highlightedText);
      }
    }, 300),
    []
  );

  const handleOnClick = (e: React.MouseEvent<HTMLPreElement>): void => {
    const target = e.target as HTMLElement;
    console.log(target.textContent || '');
    handleOnSearch(target.textContent || '');
  };

  useEffect(() => {
    const ref = codeContainerRef.current;
    if (!ref) return;

    ref.addEventListener('mouseup', handleOnHighlight);

    return () => {
      ref.removeEventListener('mouseup', handleOnHighlight);
    };
  }, [handleOnHighlight]);

  return (
    <div
      className="rounded bg-gray-950 w-full md:flex-grow-[2] min-h-[90vh]
"
    >
      <pre onClick={handleOnClick} className="code-review-code" ref={codeContainerRef}></pre>
    </div>
  );
};

export default CodeViewer;
