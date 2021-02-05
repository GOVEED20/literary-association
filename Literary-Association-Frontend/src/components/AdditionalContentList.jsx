import React from 'react'
import Additional from './Additional'

const AdditionalContentList = ({ additionals }) => {
    return (
        <>
            {
                JSON.parse(additionals).map(additional => {
                    return <Additional key={additional} isComment={additional.isComment} content={additional.content}/>
                })
            }
        </>
    )
}

export default AdditionalContentList