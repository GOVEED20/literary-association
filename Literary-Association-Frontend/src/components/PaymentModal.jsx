import React from 'react'
import { Modal } from 'react-bootstrap'

const PaymentModal = ({ show, toggleModal, bookId }) => {
    console.log(bookId)
    return (
        <Modal show={show} onHide={toggleModal}>
            <Modal.Header closeButton>
                <Modal.Title>Purchase item</Modal.Title>
            </Modal.Header>
            <Modal.Body>Dobar dan na modal bratmoii</Modal.Body>
            <Modal.Footer/>
        </Modal>
    )
}

export default PaymentModal