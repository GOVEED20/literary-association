import React, {useEffect, useState} from "react";
import {startProcess} from "../services/processService";
import {getRegistrationFields} from "../services/readerRegistrationService";
import Form from "./Form";

const Register = () => {
    const [processId, setProcessId] = useState(null)
    const [formFields, setFormFields] = useState(null)

    useEffect(() => {
        if (window.localStorage.getItem("processID") == null) {
            const processID = startProcess("Reader_registration")

            window.localStorage.setItem("processID", processID)
            setProcessId(processID)
        }
        setFormFields(getRegistrationFields(processId))
    }, [processId])

    return (
        <div>
            <h2>Reader registration</h2>
            <Form form={formFields}/>
        </div>
    )
}

export default Register;
