import axios from 'axios'

const BASE_URL = process.env.REACT_APP_BASE_URL

const getActiveMembership = async (username) => {
    const response = await axios.get(`${BASE_URL}/membership?username=${username}`)
    return response.data
}

const initializeMembershipPayment = async (invoice) => {
    const response = await axios.post(`${BASE_URL}/membership`, invoice)
    return response.data
}

const membershipService = {
    getActiveMembership,
    initializeMembershipPayment
}

export default membershipService