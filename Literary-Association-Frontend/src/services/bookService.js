import axios from 'axios'

const BASE_URL = process.env.REACT_APP_BASE_URL

const getBooks = async () => {
    const response = await axios.get(`${BASE_URL}/book`)
    return response.data
}

const getBook = async (id) => {
    const response = await axios.get(`${BASE_URL}/book/${id}`)
    return response.data
}

const bookService = {
    getBooks,
    getBook
}

export default bookService