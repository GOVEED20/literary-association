import React, { useEffect, useState } from 'react'
import membershipService from '../services/membershipService'
import { useSelector } from 'react-redux'
import { Button, Col, Form, Row } from 'react-bootstrap'
import transactionService from '../services/transactionService'

const Membership = () => {
    const username = useSelector(state => state.user.subject)
    const [membership, setMembership] = useState(null)

    useEffect(() => {
        membershipService.getActiveMembership(username).then(result => setMembership(result))
    }, [])

    if (!membership) {
        return (
            <h2>No pending membership transactions</h2>
        )
    }

    const onSubmit = (event) => {
        event.preventDefault()

        const invoiceItem = {
            id: null,
            quantity: 1
        }
        const invoiceItems = [invoiceItem]
        const invoice = {
            retailer: null,
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

export default Membership