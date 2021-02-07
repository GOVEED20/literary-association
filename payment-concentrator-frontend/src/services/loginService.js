import axios from 'axios';
import jwt_decode from 'jwt-decode'

const GATEWAY_BASE_URL = process.env.REACT_APP_GATEWAY_BASE_URL

const local_login = (token, role, subject) => {
    window.localStorage.setItem('token', JSON.stringify(token))
    window.localStorage.setItem('role', JSON.stringify(role))
    window.localStorage.setItem('subject', JSON.stringify(subject))
}

export const login = async (loginData) => {
    const formData = new FormData();
    formData.append("loginData", new Blob([JSON.stringify(loginData)], {type: "application/json"}));
    const response = await axios.post(`${GATEWAY_BASE_URL}/login`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
    const token = response.data
    const role = jwt_decode(token).role[0]
    const subject = jwt_decode(token).sub
    local_login(token, role, subject)
}