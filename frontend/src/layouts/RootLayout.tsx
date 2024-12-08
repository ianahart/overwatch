import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { useEffect, useState } from 'react';
import { Session } from '../util/SessionService';
import DevelopmentModePopup from '../components/Shared/DevelopmentModePopup';

const RootLayout = () => {
  const [isModalOpen, setIsModalOpen] = useState(() => {
    return Session.getItem('devWarning') !== 'false';
  });

  const handleCloseModal = (): void => {
    setIsModalOpen(false);
    Session.setDynamicItem('devWarning', 'false');
  };

  useEffect(() => {
    if (isModalOpen) {
      Session.setDynamicItem('devWarning', 'true');
    }
  }, [isModalOpen]);

  return (
    <div className="root-layout flex flex-col h-screen">
      <Navbar />
      <main className="relative">
        {isModalOpen && <DevelopmentModePopup handleCloseModal={handleCloseModal} />}
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default RootLayout;
