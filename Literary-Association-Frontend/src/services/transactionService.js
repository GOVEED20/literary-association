import axios from 'axios'

const BASE_URL = process.env.REACT_APP_BASE_URL

const initializeTransaction = async (invoice) => {
    const response = await axios.post(`${BASE_URL}/transaction`, invoice)
    return response.data
}


const transactionService = {
    initializeTransaction
}

export default transactionService