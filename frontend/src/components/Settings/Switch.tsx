export interface ISwitchProps {
  switchToggled: boolean;
  handleSwitchToggled: (switchToggled: boolean) => void;
}

const Switch = ({ switchToggled = false, handleSwitchToggled }: ISwitchProps) => {
  return (
    <label className="relative flex justify-between items-center group p-2 text-xl">
      <input
        onChange={(e) => handleSwitchToggled(e.target.checked)}
        checked={switchToggled}
        type="checkbox"
        className="absolute left-1/2 -translate-x-1/2 w-full h-full peer appearance-none rounded-md"
      />
      <span className="w-16 h-10 flex items-center flex-shrink-0 ml-4 p-1 bg-gray-300 rounded-full duration-300 ease-in-out peer-checked:bg-green-400 after:w-8 after:h-8 after:bg-white after:rounded-full after:shadow-md after:duration-300 peer-checked:after:translate-x-6 group-hover:after:translate-x-1"></span>
    </label>
  );
};

export default Switch;
