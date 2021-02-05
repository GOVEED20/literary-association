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

const submitTask = async (taskId, state) => {
    Object.keys(state).forEach((key) => {
        if (key === 'editors') {
            state[key] = JSON.stringify(state[key].map(v => {
                return v
            }))
        }
    })

    const payload = {
        id: taskId,
        formFields: Object.entries(state).map((value) => {
            return {
                fieldId: value[0],
                fieldValue: value[1]
            }
        })
    }

    const response = await axios.post(`${BASE_URL}/task`, payload)
    return response.data
}

const userTaskService = {
    getActiveTasks,
    getTask,
    submitTask
}

export default userTaskService
