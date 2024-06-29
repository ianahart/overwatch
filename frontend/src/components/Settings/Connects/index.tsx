import Chat from './Chat';
import Profile from './Profile';
import Connections from './Connections';

const Connects = () => {
  return (
    <div className=" min-h-full chat-grid">
      <section className="">
        <Connections />
      </section>
      <section className="">
        <Chat />
      </section>
      <section className="">
        <Profile />
      </section>
    </div>
  );
};

export default Connects;
