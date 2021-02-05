import React from 'react'
import { Button } from 'react-bootstrap'
import { useHistory } from 'react-router-dom'

const BookListItem = ({ id, ISBN, title, publisher, year }) => {
    const history = useHistory()

    const seeBookDetails = () => history.push(`/dashboard/books/${id}`)

    return (
        <tr>
            <td>{ISBN}</td>
            <td>{title}</td>
            <td>{publisher}</td>
            <td>{year}</td>
            <td>
                <Button onClick={seeBookDetails}>Details</Button>
            </td>
        </tr>
    )
}

export default BookListItem
