import React from 'react'

const WorkingPaper = ({ content }) => {
    return (
        <>
            <h5><b>Working paper:</b></h5>
            <ul>
                <li><b>Title:</b> {content.title}</li>
                <li><b>Synopsis:</b> <p>{content.synopsis}</p></li>
                <li><b>Genre:</b> {content.genre}</li>
                <li><b>Author:</b> {content.author}</li>
            </ul>
        </>
    )
}

export default WorkingPaper