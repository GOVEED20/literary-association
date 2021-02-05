import React, { useEffect, useState } from 'react'
import membershipService from '../services/membershipService'
import { useDispatch, useSelector } from 'react-redux'
import { Button, Col, Form, Row } from 'react-bootstrap'
import retailerService from '../services/retailerService'
import { setNotification } from '../reducers/notificationReducer'

const Membership = () => {
    const username = useSelector(state => state.user.subject)
    const [membership, setMembership] = useState(null)
    const [paymentServices, setPaymentServices] = useState([])
    const [invoiceState, setInvoiceState] = useState({ paymentMethod: '', subscription: false })
    const dispatch = useDispatch()

    useEffect(() => {
        membershipService.getActiveMembership(username).then(result => {
            setMembership(result)
            result && retailerService.getPaymentServicesForRetailer(result.retailer).then(services => setPaymentServices(services))
        })
    }, [])

    if (!membership) {
        return (
            <h2>No pending membership transactions</h2>
        )
    }

    const onPaymentServiceChange = ({ target }) => setInvoiceState({ ...invoiceState, paymentMethod: target.value })
    const onSubscriptionCheck = () => {
        if (!invoiceState['subscription']) {
            setInvoiceState({ subscription: !invoiceState['subscription'], paymentMethod: 'paypal-service' })
        } else {
            setInvoiceState({ ...invoiceState, subscription: !invoiceState['subscription'] })
        }
    }

    const onSubmit = (event) => {
        event.preventDefault()

        const invoice = {
            paymentMethod: invoiceState['paymentMethod'],
            subscription: invoiceState['subscription'],
            membershipTransactionId: membership.transactionId
        }
        try {
            membershipService.initializeMembershipPayment(invoice).then(result => {
                window.open(result, '_blank')
            })
        } catch (e) {
            dispatch(setNotification(e, 'error', 3500))
        }
    }

    return (
        <Form onSubmit={onSubmit} style={{ width: '60%', marginTop: '2%' }}>
            <Form.Group as={Row}>
                <Col>
                    <Form.Label>Subscription</Form.Label>
                </Col>
                <Col>
                    <Form.Check onChange={onSubscriptionCheck} value={invoiceState['subscription']} type='checkbox'/>
                </Col>
            </Form.Group>
            <Form.Group as={Row}>
                <Col>
                    <Form.Label>Choose payment service</Form.Label>
                </Col>
                <Col>
                    <Form.Control as='select' disabled={invoiceState['subscription']}
                                  onChange={onPaymentServiceChange}
                                  value={invoiceState['paymentMethod']}>
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