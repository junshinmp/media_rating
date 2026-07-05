function MediaInfo ({ dataObject }){
    return (
        <div>
            <label><strong>{dataObject.title}</strong>&nbsp;&nbsp;Release Date:&nbsp;<strong>{dataObject.release_date}</strong>
            &nbsp;Runtime:&nbsp;{dataObject.runtime}</label>
            <p>ID: {dataObject.id}&nbsp; Rating:&nbsp;{dataObject.vote_average} &nbsp; Media Type:&nbsp;{dataObject.type}</p>
            <p>{dataObject.overview}</p>
        </div>
    );
}

export default MediaInfo