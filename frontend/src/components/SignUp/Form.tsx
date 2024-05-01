import RoleQuestionField from './RoleQuestionField';

const Form = () => {
  return (
    <div className="bg-slate-900 lg:w-3/6 rounded-r p-4">
      <form className="border">
        <header className="text-center">
          <h1 className="text-2xl font-display text-green-400">Sign up for OverWatch</h1>
          <p>Create a free account or log in</p>
        </header>
        <section className="flex flex-col justify-center min-h-40">
          <RoleQuestionField />
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input type="email" name="email" id="email" />
          </div>
        </section>
      </form>
    </div>
  );
};

export default Form;
