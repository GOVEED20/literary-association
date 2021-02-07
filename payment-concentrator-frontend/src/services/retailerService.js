import axios from 'axios'

const GATEWAY_BASE_URL = process.env.REACT_APP_GATEWAY_BASE_URL

const local_logout = () => {
    window.localStorage.removeItem('token')
    window.localStorage.removeItem('role')
    window.localStorage.removeItem('subject')
}

export const registerRetailer = async (retailerData) => {
    const formData = new FormData()
    formData.append('retailerData', new Blob([JSON.stringify(retailerData)], { type: 'application/json' }))
    const response = await axios.post(`${GATEWAY_BASE_URL}/register`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
    return response.data
}

export const logoutUser = async () => {
    await axios.get(`${GATEWAY_BASE_URL}/logout`)
    local_logout()
}