import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Table } from 'react-bootstrap'
import { getBooks } from '../reducers/bookReducer'
import BookListItem from './BookListItem'

const BookList = () => {
    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(getBooks())
    }, [dispatch])

    const books = useSelector(state => state.books.list)

    return (
        <div>
            <h2>Books</h2>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ISBN</th>
                    <th>Title</th>
                    <th>Publisher</th>
                    <th>Year</th>
                    <th/>
                </tr>
                </thead>
                <tbody>
                {
                    books.map(book =>
                            <BookListItem key={book.id} id={book.id} ISBN={book.isbn} publisher={book.publisher} title={book.title}
                                          year={book.year}/>)
                }
                </tbody>
            </Table>
        </div>
    )
}

export default BookList