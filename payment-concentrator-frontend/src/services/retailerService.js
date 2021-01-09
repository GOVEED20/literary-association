import axios from 'axios';

export const registerRetailer = async (retailerData) => {
    const response = await axios.post(`http://localhost:8080/api/register`, retailerData)
    return response.data
}