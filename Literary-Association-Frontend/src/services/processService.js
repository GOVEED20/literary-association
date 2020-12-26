import axios from 'axios';

const BASE_URL = process.env.BASE_URL

export const startProcess = async (processName) => {
    return await axios.post(`${BASE_URL}/process/start`, processName)
}
