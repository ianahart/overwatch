export interface IStepProps {
  stepNumber: string;
  stepTitle: string;
  stepDetails: string;
  Icon: React.FC<React.SVGProps<SVGSVGElement>>;
}

const Step = ({ stepNumber, stepTitle, stepDetails, Icon }: IStepProps) => {
  return (
    <div className="max-w-[300px] w-full">
      <div className="text-gray-400">
        <div className="flex items-center">
          <div className="h-12 w-12 bg-gray-700 flex flex-col items-center justify-center rounded-full">
            <h3 className="text-3xl font-display text-green-400">{stepNumber}</h3>
          </div>
          <Icon className="w-20 h-20 md:ml-5" />
        </div>
        <h3 className="my-2">{stepTitle}</h3>
      </div>
      <p className="my-1">{stepDetails}</p>
    </div>
  );
};

export default Step;
