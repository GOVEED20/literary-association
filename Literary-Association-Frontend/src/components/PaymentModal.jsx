import React from 'react'
import { Modal, Row } from 'react-bootstrap'
import PurchaseForm from './PurchaseForm'
import { useSelector } from 'react-redux'
import CurrencyFormat from 'react-currency-format'

const PaymentModal = ({ show, toggleModal }) => {
    const book = useSelector(state => state.books.shown)

    return (
        <Modal show={show} onHide={toggleModal}>
            <Modal.Header closeButton>
                <Modal.Title>Purchase item</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Row style={{ marginLeft: 'auto', marginRight: 'auto' }}>
                    <i>{book.title}</i>
                    &nbsp;
                    <CurrencyFormat value={book.price} displayType={'text'} thousandSeparator={true} prefix={'$'}
                                    renderText={value => <i>{value}</i>}/>
                </Row>
                <PurchaseForm toggleModal={toggleModal}/>
            </Modal.Body>
        </Modal>
    )
}

export default PaymentModal