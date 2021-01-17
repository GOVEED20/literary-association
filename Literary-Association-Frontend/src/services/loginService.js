import axios from 'axios'

const BASE_URL = process.env.REACT_APP_BASE_URL

const login = async (username, password) => {
    const response = await axios.post(`${BASE_URL}/login`, { username, password })
    return response.data
}

const logout = async () => {
    await axios.get(`${BASE_URL}/logout`)
}

const loginService = {
    login,
    logout
}

export default loginService