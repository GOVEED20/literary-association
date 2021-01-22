import axios from 'axios'

const BASE_URL = process.env.REACT_APP_BASE_URL

const getActiveTasks = async (username) => {
    const response = await axios.get(`${BASE_URL}/task/${username}/active`)
    return response.data
}

const getTask = async (id) => {
    const response = await axios.get(`${BASE_URL}/task/${id}`)
    return response.data
}

const userTaskService = {
    getActiveTasks,
    getTask
}

export default userTaskService