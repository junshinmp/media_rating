import { useState } from "react";

function ReviewForm() {
    
    // 1. Adjusted default states: format defaults to 'movie', rating defaults to 5 (middle of slider)
    const [mediaFormat, setMediaFormat] = useState("movie");
    const [mediaTitle, setMediaTitle] = useState("");
    const [rating, setRating] = useState(5);
    const [comments, setComments] = useState("");

    // 2. Handlers to process incoming events
    function updateMediaFormat(event){
        setMediaFormat(event.target.value);
    }

    function updateMediaTitle(event){
        setMediaTitle(event.target.value);
    }

    function updateRating(event){
        setRating(Number(event.target.value)); // Sliders return strings, cast to a Number
    }

    function updateComments(event){
        setComments(event.target.value);
    }

    // 3. Form Submission Interceptor
    function handleSubmit(event) {
        event.preventDefault(); // Prevents page from reloading

        const finalReview = {
            format: mediaFormat,
            title: mediaTitle,
            stars: rating,
            text: comments
        };

        console.log("Sending Review", finalReview);
    }

    return (
        <div style={{ padding: '20px' }}>
            <h2>Review Form: </h2>   
            <hr />

            {/* Wrap inputs inside a semantic <form> tag to enable submission */}
            <form onSubmit={handleSubmit}>

                {/* DROPDOWN SELECT LIST */}
                <h3>What type of Media was it?</h3>
                <select value={mediaFormat} onChange={updateMediaFormat}>
                    <option value="Movie">Movie</option>
                    <option value="Show">TV Show</option>
                    <option value="Game">Video Game</option>
                </select>

                <h3>What {mediaFormat} did you play/watch? </h3>
                <input 
                    type="text"
                    value={mediaTitle} 
                    placeholder="Title" 
                    onChange={updateMediaTitle} 
                    required
                />
                
                {/* RANGE SLIDER (1 - 10) */}
                <h3>What would you rate this {mediaFormat}? ({rating} / 10)</h3>
                <input 
                    type="range" 
                    min="1" 
                    max="10" 
                    value={rating} 
                    onChange={updateRating} 
                />

                <h3> Any Comments on the {mediaFormat}? </h3>
                {/* Changed to a <textarea> so users have room to type longer reviews */}
                <textarea 
                    value={comments} 
                    placeholder="Thoughts?" 
                    onChange={updateComments} 
                />

                <br /><br />

                {/* SUBMIT BUTTON */}
                <button type="submit">Submit Review</button>

            </form>
        </div>
    );
}

export default ReviewForm;