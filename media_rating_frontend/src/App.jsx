import { useState } from 'react'
import Header from './Components/Header.jsx'
import ReviewForm from './Components/ReviewForm.jsx'
import WelcomeDash from './Components/WelcomeDash.jsx'
import SearchMediaPage from './Pages/SearchMediaPage.jsx'

function App() {
  
  // declaration of variables, any helper functions
  const [mediaTitle, setMediaTitle] = useState("");
      function searchTitle(event){
          setMediaTitle(event.target.value)
      }

  const [currentView, setCurrentView] = useState('Home');

  // main return statement
  return (
    <>
    <Header 
      setCurrentView = {setCurrentView}
      mediaTitle = {mediaTitle}
      setMediaTitle = {searchTitle}
    ></Header>

    <main>
      {currentView === 'Home' && <WelcomeDash></WelcomeDash>}
      {currentView === 'ReviewForm' && <ReviewForm></ReviewForm>}

      {currentView === 'SearchPage' && <SearchMediaPage></SearchMediaPage>}
    </main>
    </>
  );
}

export default App
