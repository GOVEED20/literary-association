import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_URL

export const startProcess = async (processName) => {
    const response = await axios.post(`${BASE_URL}/process/start`, processName, {headers: {"Content-Type": "text/plain"}})
    return response.data
}
