import axios from 'axios';

export const registerRetailer = async (retailerData) => {
    const formData = new FormData();
    formData.append("retailerData", new Blob([JSON.stringify(retailerData)], {type: "application/json"}));
    const response = await axios.post(`http://localhost:8080/api/register`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
    return response.data
}