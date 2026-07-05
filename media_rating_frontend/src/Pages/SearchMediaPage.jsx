import MediaInfo from "../Components/MediaInfo";

function SearchMediaPage({media, mediaTitle}){
    // Collect all the relevant media
    
    const matchingTitles = media.filter(singleMedia => 
        singleMedia.title.toLowerCase().includes(mediaTitle.toLowerCase()));

    return (
        <div>
            <h2>Search Page Template</h2>

            <ol>
                {matchingTitles.map((singleMedia) => (
                    <MediaInfo
                        key={singleMedia.uniqueId}
                        dataObject={singleMedia}
                    ></MediaInfo>
                ))}
            </ol>
        </div>
    );
}

export default SearchMediaPage