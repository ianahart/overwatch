const Form = () => {
  return (
    <div className="bg-slate-900 lg:w-3/6 rounded-r">
      <form>
        <h1>Create Account</h1>
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input type="email" name="email" id="email" />
        </div>
      </form>
    </div>
  );
};

export default Form;
