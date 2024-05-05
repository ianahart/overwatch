export interface IFormImageProps {
  src: string;
  alt: string;
}

const FormImage = ({ src, alt }: IFormImageProps) => {
  return (
    <div className="lg:w-3/6">
      <img className="brightness-50 rounded-l" src={src} alt={alt} />
    </div>
  );
};

export default FormImage;
