import axios from 'axios';

const BASE_URL = process.env.BASE_URL

export const getRegistrationFields = async (processId) => {
    return await axios.get(`${BASE_URL}/register/form-fields/${processId}`)
}
