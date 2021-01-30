import axios from 'axios';

const local_logout = () => {
    window.localStorage.removeItem('token')
    window.localStorage.removeItem('role')
    window.localStorage.removeItem('subject')
}

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

export const logoutUser = async () => {
    //await axios.get(`http://localhost:8080/logout`)
    local_logout()
}