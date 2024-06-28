import Chat from './Chat';
import Profile from './Profile';
import Connections from './Connections';

const Connects = () => {
  return (
    <div className="border min-h-full chat-grid">
      <section className="border border-red-400">
        <Connections />
      </section>
      <section className="border border-blue-400">
        <Chat />
      </section>
      <section className="border border-yellow-400">
        <Profile />
      </section>
    </div>
  );
};

export default Connects;
