import React from 'react'

const Comment = ({ content }) => {
    return (
        <>
            <h6><b>Comment:</b></h6> <p>{content}</p><br/>
        </>
    )
}

export default Comment