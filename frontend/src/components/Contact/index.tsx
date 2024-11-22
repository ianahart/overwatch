import { AiOutlineMail } from 'react-icons/ai';

const Contact = () => {
  return (
    <div className="flex items-center flex-col mx-auto max-w-[600px] w-full">
      <h3 className="text-2xl font-display font-bold text-gray-400 my-2">Contact</h3>
      <p className="text-gray-400">
        Have questions or need assistance? We’re here to help! Whether you’re looking for support, have feedback to
        share, or just want to say hello, feel free to reach out to us. We’d love to hear from you!
      </p>
      <div className="my-4 flex items-center text-gray-400">
        <AiOutlineMail className="mr-4 text-2xl" />
        <p className="text-green-400">codeoverwatch@codeoverwatch.com</p>
      </div>
      <p>
        <a className="underline text-blue-400" href="mailto:codeoverwatch@codeoverwatch.com">
          Get in touch
        </a>
      </p>
    </div>
  );
};

export default Contact;
