import React, { useEffect, useState } from 'react'
import { startProcess } from '../services/processService'
import { getRegistrationFields, sendRegistrationData } from '../services/registrationService'
import Form from './Form'
import { Spinner } from 'react-bootstrap'

const ReaderRegistration = () => {
    const [processId, setProcessId] = useState(null)
    const [formFields, setFormFields] = useState(null)

    useEffect(() => {
        const getProcessIdAndFormFields = async () => {
            setProcessId(await getProcessId())

            if (processId) {
                setFormFields(await getFormFields(processId))
            }
        }

        const getProcessId = async () => {
            let id = JSON.parse(window.localStorage.getItem('processID'))
            id = id === null ? await startProcess('Reader_registration') : id
            window.localStorage.setItem('processID', JSON.stringify(id))

            return id
        }

        const getFormFields = async (id) => {
            return await getRegistrationFields(id)
        }

        getProcessIdAndFormFields().then()
    }, [processId])

    useEffect(() => {
        window.localStorage.clear()
    }, [])

    const onSubmit = async (state) => {
        await sendRegistrationData(processId, state)
    }

    if (!formFields) {
        return (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        )
    }

    return (
        <div>
            <h2>Reader registration</h2>
            <Form form={formFields} onSubmit={onSubmit}/>
        </div>
    )
}

export default ReaderRegistration
