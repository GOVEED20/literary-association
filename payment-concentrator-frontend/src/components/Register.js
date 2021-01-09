import React, {useEffect, useState} from "react";
import {getAvailableServices, getPaymentServiceRegistrationFields} from "../services/paymentService";
import ServiceForm from "./ServiceForm"
import {Button, Form as BootstrapForm} from "react-bootstrap";
import {registerRetailer} from "../services/retailerService";
import {formStyle, h1Style, h4Style, mainDivStyle, serviceDivStyle, submitButtonStyle} from "../css/registerStyles";

const Register = () => {

    const [availableServices, setAvailableServices] = useState([])
    const [state, setState] = useState({})
    const [checkServices, setCheckServices] = useState({})
    const [formFields, setFormFields] = useState(null)
    const [encryptionFields, setEncryptionFields] = useState({})
    const [retailerName, setRetailerName] = useState("")

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
            let newEncryptionFields = {...encryptionFields}
            availableServices.forEach(service => {
                newState = {...newState}
                newState[service] = null;
                newCheckServices = {...newCheckServices}
                newCheckServices[service] = false;
                newFormFields = {...newFormFields}
                formFields[service] = null;
                newEncryptionFields[service] = {...newEncryptionFields}
                newEncryptionFields[service] = []
            })
            setState(newState)
            setCheckServices(newCheckServices)
            setFormFields(newFormFields)
            setEncryptionFields(newEncryptionFields)
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
        const newEncryptionFields = {...encryptionFields}
        getPaymentServiceRegistrationFields(serviceName).then(res => {
            newFormFields[serviceName] = res
            setFormFields(newFormFields)
            newState[serviceName] = createServiceFormState(res)
            setState(newState)
            res.forEach(field => {
                const encryptionField = {
                    name: field.name,
                    encrypted: field.encrypted
                }
                if (newEncryptionFields[serviceName].filter(e => e.name === field.name).length === 0) {
                    newEncryptionFields[serviceName].push(encryptionField)
                }
            })
            setEncryptionFields(newEncryptionFields)
        })
    }

    const onChangeNameHandler = (e) => {
        setRetailerName(e.target.value)
    }

    const onSubmit = async (e) => {
        e.preventDefault()
        const retailerData = {}
        retailerData['retailerName'] = retailerName
        let paymentServiceDataArray = []
        availableServices.forEach(service => {
            if (checkServices[service]) {
                const paymentServiceData = {}
                paymentServiceData['serviceName'] = service
                let formFields = []
                encryptionFields[service].forEach(field => {
                    let regField = {
                        name: field.name,
                        value: state[service][field.name],
                        encrypted: field.encrypted
                    }
                    formFields.push(regField)
                })
                paymentServiceData['data'] = formFields
                paymentServiceDataArray.push(paymentServiceData)
            }
        })
        retailerData['paymentServices'] = paymentServiceDataArray
        console.log(retailerData)
        await registerRetailer(retailerData)
    }

    const formatServiceName = (serviceName) => {
        let formattedServiceName = serviceName.slice(0, -8)
        formattedServiceName = formattedServiceName.charAt(0).toUpperCase() + formattedServiceName.slice(1)
        return formattedServiceName + ' payment service'

    }

    document.body.style.backgroundColor = "#010d3b"

    return (
        <div className={"container"} style={mainDivStyle}>
            <h1 style={h1Style}>Retailer registration</h1>
            <BootstrapForm onSubmit={(e) => onSubmit(e)} style={formStyle}>
                <BootstrapForm.Label>Retailer name: </BootstrapForm.Label>
                <BootstrapForm.Control type="text" placeholder="Enter retailer name"
                                       onChange={(e) => onChangeNameHandler(e)}/>
                <h4 style={h4Style}>Payment services</h4>
                <p>Choose which payment services you will provide to customers</p>
                {
                    availableServices.map((service) =>
                        <div key={service} style={serviceDivStyle}>
                            <BootstrapForm.Check type="checkbox" id={service} label={formatServiceName(service)}
                                                 onChange={(e) => checkChanged(e)}/>
                            {checkServices[service] &&
                                <ServiceForm
                                    serviceName={service}
                                    formFields={formFields[service]}
                                    onChange={(e) => changeState(e, service)}/>
                            }
                        </div>
                    )
                }
                <Button variant="primary" type="submit" style={submitButtonStyle}>Submit</Button>
            </BootstrapForm>
        </div>
    )
}

export default Register;