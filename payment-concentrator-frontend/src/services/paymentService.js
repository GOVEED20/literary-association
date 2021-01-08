import axios from 'axios';

const PC_BASE_URL = process.env.PC_BASE_URL // TODO: It gives undefined

export const getAvailableServices = async () => {
    const response = await axios.get(`http://localhost:8080/api/payment-services`)
    return response.data
}

export const getPaymentServiceRegistrationFields = async (serviceName) => {
    const response = await axios.get(`http://localhost:8080/api/payment-services/${serviceName}/registration-fields`)
    return response.data;
}