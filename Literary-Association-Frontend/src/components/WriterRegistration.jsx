import React, { useEffect, useState } from 'react'
import { startProcess } from '../services/processService'
import { getRegistrationFields, sendRegistrationData } from '../services/registrationService'
import { Spinner } from 'react-bootstrap'
import Form from './Form'

const WriterRegistration = () => {
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
            let id = JSON.parse(window.localStorage.getItem('writer_registration_process'))
            id = id === null ? await startProcess('Writer_registration') : id
            window.localStorage.setItem('writer_registration_process', JSON.stringify(id))

            return id
        }

        const getFormFields = async (id) => {
            return await getRegistrationFields(id)
        }

        getProcessIdAndFormFields().then()
    }, [processId])

    const onSubmit = async (state) => {
        try {
            await sendRegistrationData(processId, state)
            window.localStorage.removeItem('writer_registration_process')
            // eslint-disable-next-line no-empty
        } catch (e) {

        }
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
            <h2>Writer registration</h2>
            <Form form={formFields} onSubmit={onSubmit}/>
        </div>
    )
}

export default WriterRegistration
