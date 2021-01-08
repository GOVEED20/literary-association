import React, {useEffect, useState} from "react";
import {getAvailableServices, getPaymentServiceRegistrationFields} from "../services/paymentService";
import ServiceForm from "./ServiceForm"
import {Form as BootstrapForm, Button} from "react-bootstrap";
import axios from "axios";

const Register = () => {

    const [availableServices, setAvailableServices] = useState([])
    const [state, setState] = useState({})
    const [checkServices, setCheckServices] = useState({})
    const [formFields, setFormFields] = useState(null)

    useEffect( () => {
        const getPaymentServices = async () => {
            setAvailableServices(await getAvailableServices())
        }
        getPaymentServices().then();
    }, [])

    useEffect(() => {
        const createState = () => {
            let newState = {...state};
            let newCheckServices = {...checkServices};
            let newFormFields = {...formFields};
            availableServices.map(service => {
                newState = {...newState}
                newState[service] = null;
                newCheckServices = {...newCheckServices}
                newCheckServices[service] = false;
                newFormFields = {...newFormFields}
                formFields[service] = null;
            })
            setState(newState)
            setCheckServices(newCheckServices)
            setFormFields(newFormFields)
        }

        createState();

    }, [availableServices])

    const checkChanged = (e) => {
        const serviceName = e.target.id;
        const newCheckServices = {...checkServices}
        newCheckServices[serviceName] = e.target.checked
        setCheckServices(newCheckServices)
        const newFormFields = {...formFields}
        getPaymentServiceRegistrationFields(serviceName).then(res => {
            newFormFields[serviceName] = res
            setFormFields(newFormFields)
        })
    }

    const onSubmit = () => {
        console.log("Form submitted")
    }

    return (
        <div>
            <h1>Retailer registration</h1>
            <BootstrapForm onSubmit={onSubmit}>
                <BootstrapForm.Label>Email address: </BootstrapForm.Label>
                <BootstrapForm.Control type="text" placeholder="Enter retailer name" />
                {
                    availableServices.map((service) =>
                        <div key={service}>
                            <BootstrapForm.Check type="checkbox" id={service} label={service} onChange={(e) => checkChanged(e)}/>
                            {checkServices[service] && <ServiceForm serviceName={service} formFields={formFields[service]}/>}
                        </div>
                    )
                }
                <Button variant="primary" type="submit">Submit</Button>
            </BootstrapForm>
        </div>
    )
}

export default Register;