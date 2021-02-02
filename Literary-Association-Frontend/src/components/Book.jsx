import React, { useEffect } from 'react'
import { useRouteMatch } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { Button, Col, Row, Spinner } from 'react-bootstrap'
import { setBook } from '../reducers/bookReducer'

const Book = () => {
    const dispatch = useDispatch()
    const book = useSelector(state => state.books.shown)

    const idMatch = useRouteMatch('/dashboard/books/:id')

    useEffect(() => {
        if (!book) {
            idMatch && dispatch(setBook(idMatch.params.id))
        }
    }, [])

    if (!book) {
        return (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        )
    }

    return (
        <Row style={{ width: '50%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto' }}>
            <Col>
                <Row>Title:</Row>
                <Row>ISBN:</Row>
                <Row>Genre:</Row>
                <Row>Publisher:</Row>
                <Row>City:</Row>
                <Row>Year:</Row>
                <Row>Synopsis:</Row>
            </Col>
            <Col>
                <Row><b>{book.title}</b></Row>
                <Row>{book.isbn}</Row>
                <Row>{book.genreEnum}</Row>
                <Row>{book.publisher}</Row>
                <Row>{book.place}</Row>
                <Row>{book.year}</Row>
                <Row><i>{book.synopsis}</i></Row>
                <Row><Button type='button'>{book.price === 0 ? 'Download' : 'Purchase'}</Button></Row>
            </Col>
        </Row>
    )
}

export default Book