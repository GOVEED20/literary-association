import React, { useEffect, useState } from 'react'
import { startProcess } from '../services/processService'
import { getRegistrationFields, sendRegistrationData } from '../services/registrationService'
import Form from './Form'
import { Spinner } from 'react-bootstrap'
import { setNotification } from '../reducers/notificationReducer'
import { useDispatch } from 'react-redux'

const ReaderRegistration = () => {

    const dispatch = useDispatch()

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
            let id = JSON.parse(window.localStorage.getItem('reader_registration_process'))
            id = id === null ? await startProcess('Reader_registration') : id
            window.localStorage.setItem('reader_registration_process', JSON.stringify(id))

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
            window.localStorage.removeItem('reader_registration_process')
        } catch (e) {
            dispatch(setNotification(e.response.data, 'error', 3500))
        }
    }

    if (!formFields) {
        return (
            <Spinner className='d-flex flex-column align-items-center' animation="border" role="status">
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
