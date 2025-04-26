import { render, screen, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import Upload from '../../../src/components/Shared/Upload';

describe('Upload component', () => {
  const renderComponent = () => {
    const handleUpdateAttachment = vi.fn();
    render(
      <Upload
        title="Upload your file"
        maxFileSize={5000}
        maxFileSizeWord="5KB"
        error=""
        value={null}
        handleUpdateAttachment={handleUpdateAttachment}
        fieldName="file"
      />
    );
    return { handleUpdateAttachment, user: userEvent.setup() };
  };

  it('should render the component with no file', () => {
    renderComponent();
    expect(screen.getByText('Upload your file')).toBeInTheDocument();
    expect(screen.getByTestId('file-input')).toBeInTheDocument();
  });

  it('should display the filename after file is selected', () => {
    const handleUpdateAttachment = vi.fn();
    render(
      <Upload
        title="Upload your file"
        maxFileSize={5000}
        maxFileSizeWord="5KB"
        error=""
        value={new File(['file'], 'file.txt')}
        handleUpdateAttachment={handleUpdateAttachment}
        fieldName="file"
      />
    );
    expect(screen.getByText('file.txt')).toBeInTheDocument();
  });

  it('should call handleUpdateAttachment with error when file size exceeds the limit', async () => {
    const handleUpdateAttachment = vi.fn();
    render(
      <Upload
        title="Upload your file"
        maxFileSize={5000} // 5KB
        maxFileSizeWord="5KB"
        error=""
        value={null}
        handleUpdateAttachment={handleUpdateAttachment}
        fieldName="file"
      />
    );
    const fileInput = screen.getByTestId('file-input');
    const largeFile = new File(['a'.repeat(6000)], 'large-file.txt', { type: 'text/plain' }); // 6KB

    fireEvent.change(fileInput, { target: { files: [largeFile] } });

    expect(handleUpdateAttachment).toHaveBeenCalledWith('file', 'Attachment exceeds the 5KB limit', 'error');
  });

  it('should call handleUpdateAttachment with file when size is within limit', async () => {
    const handleUpdateAttachment = vi.fn();
    render(
      <Upload
        title="Upload your file"
        maxFileSize={5000} // 5KB
        maxFileSizeWord="5KB"
        error=""
        value={null}
        handleUpdateAttachment={handleUpdateAttachment}
        fieldName="file"
      />
    );
    const fileInput = screen.getByTestId('file-input');
    const smallFile = new File(['a'.repeat(2000)], 'small-file.txt', { type: 'text/plain' }); // 2KB

    fireEvent.change(fileInput, { target: { files: [smallFile] } });

    expect(handleUpdateAttachment).toHaveBeenCalledWith('file', smallFile, 'value');
  });

  it('should clear the file when clear button is clicked', async () => {
    const handleUpdateAttachment = vi.fn();
    render(
      <Upload
        title="Upload your file"
        maxFileSize={5000}
        maxFileSizeWord="5KB"
        error=""
        value={new File(['file'], 'file.txt')}
        handleUpdateAttachment={handleUpdateAttachment}
        fieldName="file"
      />
    );
    const clearButton = screen.getByTestId('clear-file');
    const user = userEvent.setup();
    await user.click(clearButton);
    expect(handleUpdateAttachment).toHaveBeenCalledWith('file', null, 'value');
  });

  it('should display an error message if error prop is passed', () => {
    const handleUpdateAttachment = vi.fn();
    render(
      <Upload
        title="Upload your file"
        maxFileSize={5000}
        maxFileSizeWord="5KB"
        error="File size is too large"
        value={null}
        handleUpdateAttachment={handleUpdateAttachment}
        fieldName="file"
      />
    );
    expect(screen.getByText('File size is too large')).toBeInTheDocument();
  });

  it('should handle drag and drop file upload', () => {
    const handleUpdateAttachment = vi.fn();
    render(
      <Upload
        title="Upload your file"
        maxFileSize={5000}
        maxFileSizeWord="5KB"
        error=""
        value={null}
        handleUpdateAttachment={handleUpdateAttachment}
        fieldName="file"
      />
    );

    const dropArea = screen.getByTestId('file-input').parentElement;
    const file = new File(['test content'], 'test.jpg', { type: 'image/jpeg' });

    const dataTransfer = {
      files: [file],
      items: [
        {
          kind: 'file',
          type: file.type,
          getAsFile: () => file,
        },
      ],
    };

    fireEvent.drop(dropArea!, { dataTransfer });

    expect(handleUpdateAttachment).toHaveBeenCalledWith('file', file, 'value');
  });

  it('should handle file upload and preview', async () => {
    vi.useFakeTimers();

    const readAsDataURLMock = vi.fn();
    const fileReaderMock = {
      readAsDataURL: readAsDataURLMock,
      result: 'data:image/jpeg;base64,mockbase64data',
      onloadend: null,
    };

    vi.spyOn(global, 'FileReader').mockImplementation(() => fileReaderMock as unknown as FileReader);

    readAsDataURLMock.mockImplementation(() => {
      setTimeout(() => {
        if (fileReaderMock.onloadend) {
          //@ts-ignore
          fileReaderMock.onloadend({ target: fileReaderMock } as unknown as ProgressEvent<FileReader>);
        }
      }, 0);
    });

    const handleUpdateAttachment = vi.fn();
    render(
      <Upload
        title="Upload your file"
        maxFileSize={5000}
        maxFileSizeWord="5KB"
        error=""
        value={null}
        handleUpdateAttachment={handleUpdateAttachment}
        fieldName="file"
      />
    );

    const fileInput = screen.getByTestId('file-input');
    const file = new File(['test image content'], 'test.jpg', { type: 'image/jpeg' });

    fireEvent.change(fileInput, { target: { files: [file] } });

    vi.runAllTimers();

    expect(handleUpdateAttachment).toHaveBeenCalledWith('file', file, 'value');

    expect(readAsDataURLMock).toHaveBeenCalledWith(file);

    vi.restoreAllMocks();
    vi.useRealTimers();
  });
});
