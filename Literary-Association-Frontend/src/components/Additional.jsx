import React from 'react'
import WorkingPaper from './WorkingPaper'
import Comment from './Comment'

const Additional = ({ isComment, content }) => {
    let i = 0
    return (
        <>
            {
                content.map(contentObject => {
                    return !isComment ?
                        <WorkingPaper key={++i} content={contentObject}/>
                        :
                        <Comment key={++i} content={contentObject}/>
                })
            }
        </>
    )
}

export default Additional