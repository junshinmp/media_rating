import React, {useState} from "react";

function Header({setCurrentView }){

    const [mediaTitle, setMediaTitle] = useState("");
    function searchTitle(event){
        setMediaTitle(event.target.value)
    }

    const goToReview = () => {
        setCurrentView('ReviewForm')
    }

    const goToHome = () => {
        setCurrentView('Home')
    }

    return(
        <header>
            <h1>
            Welcome to Media Reviewer!
            </h1>

            <nav>
                <input type="text" value={mediaTitle} placeholder="Search for some Media" onChange={searchTitle}/>
                
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