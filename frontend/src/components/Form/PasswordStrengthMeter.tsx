import { useMemo } from 'react';

export interface IPasswordStrengthMeterProps {
  password: string;
}

const PasswordStrengthMeter = ({ password }: IPasswordStrengthMeterProps) => {
  const getPasswordStrength = (password: string) => {
    const rules = [/[a-z]/, /[A-Z]/, /\d/, /[@$!%*?&]/];
    let score = 0;
    rules.forEach((rule) => {
      if (new RegExp(rule).test(password)) {
        score += 1;
      }
    });
    return score;
  };

  const activateMeter = () => {
    const score = getPasswordStrength(password);
    let opts = { color: '', text: '' };

    switch (score) {
      case 1:
        opts = { ...opts, color: 'bg-red-400', text: 'Very Weak' };
        break;
      case 2:
        opts = { ...opts, color: 'bg-yellow-400', text: 'Weak' };
        break;
      case 3:
        opts = { ...opts, color: 'bg-blue-400', text: 'Good' };
        break;
      case 4:
        opts = { ...opts, color: 'bg-green-400', text: 'Very Good' };
        break;
      default:
        null;
    }
    return opts;
  };

  const strength = useMemo(() => {
    return activateMeter();
  }, [password.length]);

  return (
    <div className="mt-4">
      <p className="text-slate-400">{strength.text}</p>
      <div className={`${strength.color} w-full h-1 border border-slate-700 mt-1`}></div>
    </div>
  );
};

export default PasswordStrengthMeter;
