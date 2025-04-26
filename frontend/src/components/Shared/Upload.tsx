import { useEffect, useState } from 'react';
import { AiOutlineClose } from 'react-icons/ai';
import { FaCheck, FaPlus } from 'react-icons/fa';

export interface IUploadProps {
  title?: string;
  maxFileSize: number;
  maxFileSizeWord: string;
  error: string;
  value: File | string | null;
  handleUpdateAttachment: (name: string, value: string | File | null, attribute: string) => void;
  fieldName: string;
}

const Upload = ({
  maxFileSize,
  maxFileSizeWord,
  fieldName,
  error,
  value,
  handleUpdateAttachment,
  title = 'Upload an attachment',
}: IUploadProps) => {
  const [imagePreview, setImagePreview] = useState<string | null>(null);

  useEffect(() => {
    if (value !== null && !(value instanceof File)) {
      setImagePreview(value);
    } else {
      setImagePreview(null);
    }
  }, [value]);
  const filename = value === null ? null : (value as File).name;

  const handleOnDrop = (e: React.DragEvent<HTMLDivElement>): void => {
    e.stopPropagation();
    e.preventDefault();

    handleUpdateAttachment(fieldName, '', 'error');
    const { files } = e.dataTransfer;

    if (!files) return;
    const file = files[0];

    uploadFile(file);
  };

  const uploadFile = (file: File): void => {
    if (!canUpload(file)) {
      const error = `Attachment exceeds the ${maxFileSizeWord} limit`;
      handleUpdateAttachment(fieldName, error, 'error');
      return;
    }
    handleUpdateAttachment(fieldName, file, 'value');
    previewImage(file);
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    handleUpdateAttachment(fieldName, '', 'error');
    if (e.target.files === null) return;

    const file = e.target.files[0];
    uploadFile(file);
  };

  const canUpload = (file: File): boolean => {
    if (file.size > maxFileSize) {
      return false;
    }
    return true;
  };

  const clearFile = (): void => {
    handleUpdateAttachment(fieldName, null, 'value');
    setImagePreview(null);
  };

  const previewImage = (file: File): void => {
    const reader = new FileReader();
    reader.onloadend = () => {
      setImagePreview(reader.result as string);
    };
    reader.readAsDataURL(file);
  };

  return (
    <div>
      <p>{title}</p>
      {filename !== null && (
        <div className="flex items-center">
          <small>{filename}</small>
          <div className="ml-2 cursor-pointer" data-testid="clear-file" onClick={clearFile}>
            <AiOutlineClose className="text-yellow-400" />
          </div>
        </div>
      )}
      <div onDrop={handleOnDrop} draggable className="my-4 relative border border-gray-800 w-32 h-32 rounded">
        {imagePreview === null && (
          <div className="cursor-pointer absolute w-full flex flex-col justify-center items-center bg-gray-800 rounded p-2 z-0">
            {value !== null ? <FaCheck className="text-green-400" /> : <FaPlus />}
          </div>
        )}
        <input
          onChange={handleOnChange}
          name="avatar"
          data-testid="file-input"
          id="avatar"
          type="file"
          accept="image/*"
          className="h-full w-full absolute z-10 opacity-0"
        />
        {imagePreview && (
          <div className="my-4 absolute z-20">
            <img src={imagePreview} alt="Preview" className="w-full h-full object-cover" />
          </div>
        )}
      </div>
      {error.length > 0 && <small className="text-red-300 my-1">{error}</small>}
    </div>
  );
};

export default Upload;
