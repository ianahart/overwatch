import { Link } from 'react-router-dom';

const FooterSection = () => {
  return (
    <div className="mt-auto my-8">
      <ul className="flex items-center justify-evenly">
        <li className="text-gray-400">
          <Link to="/about">About Us</Link>
        </li>

        <li className="text-gray-400">
          <Link to="/contact">Contact</Link>
        </li>

        <li className="text-gray-400">
          <Link to="/privacy-policy">Privacy Policy</Link>
        </li>

        <li className="text-gray-400">
          <Link to="/terms-of-service">Terms of Service</Link>
        </li>
      </ul>
    </div>
  );
};

export default FooterSection;
