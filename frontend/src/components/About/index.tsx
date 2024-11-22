const About = () => {
  return (
    <div className="max-w-[650px] w-full flex flex-col items-center mx-auto p-2">
      <h2 className="text-gray-400 font-bold font-display text-2xl">About Us</h2>
      <p className="leading-7 my-2 text-gray-400">
        At <span className="font-bold text-green-400">Overwatch</span>, we’re passionate about helping developers
        elevate their skills and create high-quality code. Whether you’re a seasoned programmer or just starting your
        journey, our platform connects you with expert reviewers who provide detailed, actionable feedback on your
        projects. We believe that the best way to grow as a developer is through constructive critique, collaboration,
        and continuous learning.
      </p>
      <p className="leading-7 my-2 text-gray-400">
        Our mission is simple: to create a supportive and thriving community where developers can share their work, gain
        insights from experienced professionals, and build better software. With Overwatch, you’re not just getting a
        code review—you’re unlocking opportunities to refine your craft, tackle challenges, and achieve your goals.
        Let’s write better code together.
      </p>
    </div>
  );
};

export default About;
