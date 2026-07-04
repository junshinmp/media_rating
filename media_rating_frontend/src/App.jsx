import { useState } from 'react'
import Header from './Components/Header.jsx'
import ReviewForm from './Components/ReviewForm.jsx'
import WelcomeDash from './Components/WelcomeDash.jsx'

function App() {

  const [currentView, setCurrentView] = useState('Home');

  return (
    <>
    <Header setCurrentView = {setCurrentView}></Header>
      <main>
        {currentView === 'Home' && <WelcomeDash></WelcomeDash>}
        {currentView === 'ReviewForm' && <ReviewForm></ReviewForm>}
      </main>
    </>
  );
}

export default App
