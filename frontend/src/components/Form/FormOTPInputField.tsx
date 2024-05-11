import { useRef } from 'react';

export interface IFormOPTInputFieldProps {
  numOfInputs: number;
  passCode: string[];
  setPassCode: (passCode: any) => void;
}

const FormOTPInputField = ({ numOfInputs, passCode, setPassCode }: IFormOPTInputFieldProps) => {
  const inputsRef = useRef<HTMLDivElement>(null);

  const goToNextPassCodeInput = (inputs: Element[], index: number) => {
    const nextInput = inputs[index + 1] as HTMLElement;
    if (index + 1 > inputs.length - 1) return;
    nextInput.focus();
  };

  const goToPrevPassCodeInput = (inputs: Element[], index: number) => {
    const prevIndex = index - 1 >= 0 ? index - 1 : 0;
    const prevInput = inputs[prevIndex] as HTMLElement;
    prevInput.focus();
    setPassCode((prevState: string[]) => prevState.filter((_, i) => i !== index));
  };

  const handlePassCodeKeyUp = (e: React.KeyboardEvent<HTMLInputElement>, index: number) => {
    if (e.key.toLowerCase() === 'backspace') {
      if (inputsRef.current === null) return;
      const inputs = Array.from(inputsRef.current?.children);
      goToPrevPassCodeInput(inputs, index);
    }
  };

  const handlePassCodeChange = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
    const { value } = e.target;
    if (inputsRef.current === null) return;
    const inputs = Array.from(inputsRef.current?.children);
    if (validatePassCode(value)) {
      updatePassCode(value);
      goToNextPassCodeInput(inputs, index);
    }
  };

  const validatePassCode = (value: string) => {
    const acceptedValues = '0123456789';
    return value.trim().length > 0 && acceptedValues.includes(value);
  };

  const updatePassCode = (value: string) => {
    setPassCode((prevState: any) => [...prevState, value]);
  };

  const goToCurrentPassCodeInput = (inputs: Element[]) => {
    const curInput = inputs[inputs.length - 1] as HTMLElement;
    curInput.focus();
  };

  const handlePastePassCode = (e: React.ClipboardEvent<HTMLInputElement>) => {
    const pastedPassCode = e.clipboardData.getData('Text');
    if (pastedPassCode.length !== numOfInputs) return;
    setPassCode(pastedPassCode.split(''));
    if (!inputsRef.current) return;
    const inputs = Array.from(inputsRef.current?.children);
    goToCurrentPassCodeInput(inputs);
  };

  return (
    <>
      <div className="text-gray-400 mt-4">
        <p>Paste in your code below:</p>
      </div>
      <div ref={inputsRef} className="flex mt-4">
        {[...Array(numOfInputs)].map((_, index) => {
          return (
            <input
              onPaste={handlePastePassCode}
              onChange={(e) => handlePassCodeChange(e, index)}
              onKeyUp={(e) => handlePassCodeKeyUp(e, index)}
              className="pl-4 text-gray-400 outline-none focus:border-green-400 text-2xl md:w-12 h-10 w-8 mr-4 border rounded-2xl border-gray-800 bg-transparent"
              key={index}
              value={passCode[index] ? passCode[index] : ''}
              maxLength={1}
            />
          );
        })}
      </div>
    </>
  );
};

export default FormOTPInputField;
