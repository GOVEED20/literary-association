import React from 'react'
import Additional from './Additional'

const AdditionalContentList = ({ additionals }) => {
    let i = 0
    return (
        <>
            {
                JSON.parse(additionals).map(additional => {
                    return <Additional key={++i} isComment={additional.isComment} content={additional.content}/>
                })
            }
        </>
    )
}

export default AdditionalContentList