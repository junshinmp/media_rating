import React, {useState} from "react";

function Header({setCurrentView, mediaTitle, setMediaTitle}){

    const goToReview = () => {
        setCurrentView('ReviewForm')
    }

    const goToHome = () => {
        setCurrentView('Home')
    }

    const searchButton = () => {
        setCurrentView('SearchPage')
    }

    return(
        <header>
            <h1>
            Welcome to Media Reviewer!
            </h1>

            <nav>
                <input 
                    type="text" 
                    value={mediaTitle} 
                    placeholder="Search for some Media" 
                    onChange={setMediaTitle}
                />&nbsp;
                <button onClick={searchButton}>Search</button>

                <br/>

                <button onClick={goToHome}>Home</button> 

                <br />

                <button onClick={goToReview}>Create a Review</button>

            </nav>
            <hr></hr>
        </header>
    );
}

export default Header