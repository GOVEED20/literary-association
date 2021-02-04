import React, { useEffect, useState } from 'react'
import { useRouteMatch } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { Button, Col, Row, Spinner } from 'react-bootstrap'
import { setBook } from '../reducers/bookReducer'
import bookService from '../services/bookService'
import PaymentModal from './PaymentModal'
import CurrencyFormat from 'react-currency-format'

const Book = () => {
    const dispatch = useDispatch()
    const book = useSelector(state => state.books.shown)
    const [modalShown, setModalShown] = useState(false)

    const idMatch = useRouteMatch('/dashboard/books/:id')

    useEffect(() => {
        if (!book || book.id !== idMatch) {
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

    const buttonName = book.price === 0 ? 'Download' : 'Purchase'
    const toggleModal = () => setModalShown(!modalShown)
    const onClick = () => book.price === 0 ? bookService.downloadBook(book.title, null) : toggleModal()

    return (
        <>
            <PaymentModal show={modalShown} toggleModal={toggleModal}/>
            <Row style={{ width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto' }}>
                <Col style={{ borderRight: '1px solid #333' }}>
                    <Row>Title:</Row>
                    <Row>ISBN:</Row>
                    <Row>Genre:</Row>
                    <Row>Publisher:</Row>
                    <Row>City:</Row>
                    <Row>Year:</Row>
                    <Row>Synopsis:</Row>
                    <Row>Price:</Row>
                </Col>
                <Col/>
                <Col style={{ whiteSpace: 'nowrap' }}>
                    <Row><b>{book.title}</b></Row>
                    <Row>{book.isbn}</Row>
                    <Row>{book.genreEnum}</Row>
                    <Row>{book.publisher}</Row>
                    <Row>{book.place}</Row>
                    <Row>{book.year}</Row>
                    <Row><i>{book.synopsis}</i></Row>
                    <Row>
                        <CurrencyFormat value={book.price} displayType={'text'} thousandSeparator={true} prefix={'$'}
                                        renderText={value => <p>{value}</p>}/>
                    </Row>
                    <Row><Button onClick={onClick} type='button'>{buttonName}</Button></Row>
                </Col>
            </Row>
        </>
    )
}

export default Book