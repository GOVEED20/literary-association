import axios from 'axios';
import jwt_decode from 'jwt-decode'

const local_login = (token, role, subject) => {
    window.localStorage.setItem('token', JSON.stringify(token))
    window.localStorage.setItem('role', JSON.stringify(role))
    window.localStorage.setItem('subject', JSON.stringify(subject))
}

export const login = async (loginData) => {
    console.log(loginData)
    const formData = new FormData();
    formData.append("loginData", new Blob([JSON.stringify(loginData)], {type: "application/json"}));
    const response = await axios.post(`http://localhost:8080/api/login`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
    const token = response.data
    const role = jwt_decode(token).role[0]
    const subject = jwt_decode(token).sub // username
    local_login(token, role, subject)
}