import React from 'react'
import { startProcess } from '../services/processService'
import { Button } from 'react-bootstrap'

const MyBooks = () => {

    const startBookPublishingProcess = async () => {
        await startProcess('Book_publishing')
    }

    return (
        <div>
            <Button size="lg" variant="primary" type="submit" onClick={startBookPublishingProcess}>Add new book</Button>
        </div>
    )
}

export default MyBooks
