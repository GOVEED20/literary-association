import axios from 'axios';

const GATEWAY_BASE_URL = process.env.REACT_APP_GATEWAY_BASE_URL

export const getAvailableServices = async () => {
    const response = await axios.get(`${GATEWAY_BASE_URL}/payment-services`)
    return response.data
}

export const getPaymentServiceRegistrationFields = async (serviceName) => {
    const response = await axios.get(`${GATEWAY_BASE_URL}/payment-services/${serviceName}/registration-fields`)
    return response.data;
}