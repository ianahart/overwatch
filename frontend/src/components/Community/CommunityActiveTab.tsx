export interface ICommunityActiveTabProps {
  tab: string;
  btnText: string;
  activeTab: string;
  handleSetActiveTab: (activeTab: string) => void;
}

const CommunityActiveTab = ({ tab, btnText, activeTab, handleSetActiveTab }: ICommunityActiveTabProps) => {
  return (
    <button
      onClick={() => handleSetActiveTab(tab)}
      className={`mx-4 ${activeTab === tab ? 'font-bold text-green-400' : 'text-gray-400 font-normal'}`}
    >
      {btnText}
    </button>
  );
};

export default CommunityActiveTab;
