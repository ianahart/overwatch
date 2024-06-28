/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      gridTemplateColumns: {
        chat: '1fr 2fr 1fr',
        'chat-mobile': '1fr',
      },
      animation: {
        slidemenu: 'slidemenu 0.35s ease-in-out',
        slidedown: 'slidedown 0.35s ease-in-out',
        'bounce-short': 'bounce 1s ease-in-out 2',
      },
      keyframes: {
        slidemenu: {
          '0%': { transform: 'translateY(20px)', opacity: 0 },
          '100%': { transform: 'translateY(0px)', opacity: 1 },
        },
        slidedown: {
          '0%': { transform: 'translateY(-20px)', opacity: 0 },
          '100%': { transform: 'translateY(0px)', opacity: 1 },
        },
      },
      fontFamily: {
        display: ['Anton'],
        body: ['Nunito'],
      },
    },
  },
  plugins: [],
};
