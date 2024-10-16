/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./public/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        navbarGreen: 'rgb(67, 92, 72)',
        hoverNavbar1: 'rgb(218, 165, 32)',
        hoverNavbar2: 'rgb(189, 183, 107)',
        navbarIcon: 'rgb(35, 55, 39)', 
        backgroundGreen: 'rgb(141, 146, 125)',
        complementaryBackgroundGreen: 'rgb(114, 109, 130)',
        customGray: {
          900: '#202225',
          800: '#2f3136',
          700: '#36393f',
          600: '#4f545c',
          400: '#d4d7dc',
          300: '#e3e5e8',
          200: '#ebedef',
          100: '#f2f3f5',
        },
      }
    },
  },
  plugins: [],
}