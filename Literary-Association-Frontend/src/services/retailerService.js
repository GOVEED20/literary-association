import axios from 'axios'

const BASE_URL = process.env.REACT_APP_BASE_URL

export const getAvailableServices = async () => {
    const response = await axios.get(`${BASE_URL}/retailer/payment-services`)
    return response.data
}

export const getPaymentServiceRegistrationFields = async (serviceName) => {
    const response = await axios.get(`${BASE_URL}/retailer/${serviceName}/registration-fields`)
    return response.data
}

export const registerRetailer = async (retailerData) => {
    const response = await axios.post(`${BASE_URL}/retailer`, retailerData)
    return response.data
}

export const getPaymentServicesForRetailer = async (retailerName) => {
    const response = await axios.get(`${BASE_URL}/${retailerName}/payment-service`)
    return response.data
}

const retailerService = {
    getAvailableServices,
    getPaymentServiceRegistrationFields,
    registerRetailer,
    getPaymentServicesForRetailer
}

export default retailerService