import loginImg from '../../assets/login.jpeg';
import FormImage from '../Layout/Form/FormImage';
import Form from './Form';

const SignIn = () => {
  return (
    <section className="max-w-7xl rounded min-h-screen mx-auto">
      <div className="lg:flex-row flex w-full flex-col-reverse">
        <FormImage src={loginImg} alt="A night time landscape portrait" />
        <Form />
      </div>
    </section>
  );
};

export default SignIn;
