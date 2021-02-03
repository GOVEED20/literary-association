import React from 'react'
import { Modal } from 'react-bootstrap'
import PurchaseForm from './PurchaseForm'

const PaymentModal = ({ show, toggleModal }) => {
    return (
        <Modal show={show} onHide={toggleModal}>
            <Modal.Header closeButton>
                <Modal.Title>Purchase item</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <PurchaseForm/>
            </Modal.Body>
        </Modal>
    )
}

export default PaymentModal