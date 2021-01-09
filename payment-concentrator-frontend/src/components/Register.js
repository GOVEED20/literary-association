import React, {useEffect, useState} from "react";
import {getAvailableServices, getPaymentServiceRegistrationFields} from "../services/paymentService";
import ServiceForm from "./ServiceForm"
import {Button, Form as BootstrapForm} from "react-bootstrap";

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
            availableServices.forEach(service => {
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

    const createServiceFormState = (formFields) => {
        const reducer = (accumulator, currentValue) => {
            const field = currentValue.name
            let value = null
            switch (currentValue.validationConstraints['type']) {
                case "select":
                    if (currentValue.validationConstraints['multiple']) {
                        value = []
                    } else {
                        value = ""
                    }
                    break
                case "checkbox":
                    value = false
                    break
                default:
                    value = ""
            }
            const newAccumulator = {...accumulator}
            newAccumulator[field] = value
            return newAccumulator
        }
        return formFields.reduce(reducer, {})
    }

    const changeState = (e, serviceName) => {
        const newState = {...state}
        const fieldName = e.target.id
        const fieldValue = e.target.value
        if (Array.isArray(newState[serviceName][fieldName])) {
            if (fieldValue === "") {
                return
            }

            if (newState[serviceName][fieldName].includes(fieldValue)) {
                newState[serviceName][fieldName] = newState[serviceName][fieldName].filter(v => v !== fieldValue)
            } else {
                newState[serviceName][fieldName] = [...newState[serviceName][fieldName], fieldValue]
            }
        } else if (typeof newState[serviceName][fieldName] === "boolean") {
            newState[serviceName][fieldName] = !newState[serviceName][fieldName]
        } else {
            newState[serviceName][fieldName] = fieldValue
        }
        setState(newState)
    }

    const checkChanged = (e) => {
        const serviceName = e.target.id;
        const newCheckServices = {...checkServices}
        newCheckServices[serviceName] = e.target.checked
        setCheckServices(newCheckServices)
        const newFormFields = {...formFields}
        const newState = {...state}
        getPaymentServiceRegistrationFields(serviceName).then(res => {
            newFormFields[serviceName] = res
            setFormFields(newFormFields)
            newState[serviceName] = createServiceFormState(res)
            setState(newState)
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
                            {checkServices[service] &&
                                <ServiceForm
                                    serviceName={service}
                                    formFields={formFields[service]}
                                    onChange={(e) => changeState(e, service)}/>
                            }
                        </div>
                    )
                }
                <Button variant="primary" type="submit">Submit</Button>
            </BootstrapForm>
        </div>
    )
}

export default Register;