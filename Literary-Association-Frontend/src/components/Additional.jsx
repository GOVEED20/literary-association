import React from 'react'
import WorkingPaper from './WorkingPaper'
import Comment from './Comment'

const Additional = ({ isComment, content }) => {
    return (
        <>
            {
                content.map(contentObject => {
                    return !isComment ?
                        <WorkingPaper content={contentObject}/>
                        :
                        <Comment content={contentObject}/>
                })
            }
        </>
    )
}

export default Additional