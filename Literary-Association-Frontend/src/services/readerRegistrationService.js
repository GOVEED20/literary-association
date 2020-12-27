import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_URL

export const getRegistrationFields = async (processId) => {
    const response = await axios.get(`${BASE_URL}/register/form-fields/${processId}`)
    return response.data
}

export const sendRegistrationData = async (processID, state) => {
    Object.keys(state).map((key) => {
        if (key === "genres" || key === "beta_genres") {
            state[key] = JSON.stringify(state[key].map(v => {
                return {
                    genre: v
                }
            }))
        }
    })

    const payload = {
        processID,
        formFields: Object.entries(state).map((value) => {
            return {
                fieldId: value[0],
                fieldValue: value[1]
            }
        })
    }
    const response = await axios.post(`${BASE_URL}/register`, payload)
    return response.data
}
