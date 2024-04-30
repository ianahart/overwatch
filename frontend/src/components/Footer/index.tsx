const Footer = () => {
  const year = new Date().getFullYear();
  return (
    <footer className="sticky top-[100vh]">
      <p className="font-body text-center py-2">OverWatch &copy;{year}</p>
    </footer>
  );
};

export default Footer;
