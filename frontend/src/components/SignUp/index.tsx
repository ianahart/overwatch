import registerImg from '../../assets/register.jpeg';
import FormImage from '../Layout/Form/FormImage';
import Form from './Form';

const SignUp = () => {
  return (
    <section className="max-w-7xl rounded min-h-screen mx-auto">
      <div className="lg:flex-row flex w-full flex-col-reverse">
        <FormImage src={registerImg} alt="A night time landscape portrait" />
        <Form />
      </div>
    </section>
  );
};

export default SignUp;
