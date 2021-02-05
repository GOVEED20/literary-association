import React, { useEffect, useState } from 'react'
import { Button, Col, Form, Row, Spinner } from 'react-bootstrap'
import { useSelector } from 'react-redux'
import bookService from '../services/bookService'
import retailerService from '../services/retailerService'
import transactionService from '../services/transactionService'

const PurchaseForm = ({ toggleModal }) => {
    const book = useSelector(state => state.books.shown)
    const username = useSelector(state => state.user.subject)
    const [retailers, setRetailers] = useState([])
    const [retailer, setRetailer] = useState('')
    const [paymentServices, setPaymentServices] = useState([])
    const [paymentService, setPaymentService] = useState('')

    useEffect(() => {
        bookService.getRetailersForBook(book.id).then(result => setRetailers(result))
    }, [])

    useEffect(() => {
        if (retailer !== '') {
            retailerService.getPaymentServicesForRetailer(retailer).then(result => setPaymentServices(result))
        }
    }, [retailer])

    const onRetailerChange = ({ target }) => setRetailer(target.value)
    const onPaymentServiceChange = ({ target }) => setPaymentService(target.value)

    const onSubmit = (event) => {
        event.preventDefault()

        const invoiceItem = {
            id: book.id,
            quantity: 1
        }
        const invoiceItems = [invoiceItem]
        const invoice = {
            retailer,
            paymentMethod: paymentService,
            subscription: false,
            invoiceItems,
            user: username
        }
        transactionService.initializeTransaction(invoice).then(result => {
            window.open(result, '_blank')
            toggleModal()
        })
    }

    if (!retailers) {
        return (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        )
    }

    return (
        <Form onSubmit={onSubmit}>
            <Form.Group as={Row}>
                <Col>
                    <Form.Label>Choose retailer</Form.Label>
                </Col>
                <Col>
                    <Form.Control as='select' onChange={onRetailerChange}>
                        <option value=''>-</option>
                        {
                            retailers.map(r => <option key={r} value={r}>{r}</option>)
                        }
                    </Form.Control>
                </Col>
            </Form.Group>
            <Form.Group as={Row}>
                <Col>
                    <Form.Label>Choose payment service</Form.Label>
                </Col>
                <Col>
                    <Form.Control as='select' disabled={retailer === ''} onChange={onPaymentServiceChange}>
                        <option value=''>-</option>
                        {
                            paymentServices.map(r => <option key={r} value={r}>{r}</option>)
                        }
                    </Form.Control>
                </Col>
            </Form.Group>
            <Form.Group as={Row}>
                <Col>
                    <Button type='submit'>Purchase</Button>
                </Col>
            </Form.Group>
        </Form>
    )
}

export default PurchaseForm