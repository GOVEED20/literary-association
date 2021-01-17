import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_URL

const getActiveTasks = async (username) => {
    const response = await axios.get(`${BASE_URL}/task/${username}/active`)
    return response.data
}

export default {
    getActiveTasks
}