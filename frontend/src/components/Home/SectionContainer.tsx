export interface ISectionContainerProps {
  children: React.ReactNode;
  background?: string;
}

const SectionContainer = ({ children, background = 'dark' }: ISectionContainerProps) => {
  return (
    <section className="min-h-[750px]">
      <div className={`min-h-[750px] p-2 ${background === 'light' ? 'bg-stone-50' : '#000'}`}>{children}</div>
    </section>
  );
};

export default SectionContainer;
