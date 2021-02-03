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

const downloadBook = async (title, downloadUrl) => {
    const response = await axios.get(downloadUrl ? downloadUrl : `${BASE_URL}/book/${title}/download`, { responseType: 'blob' })
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.style = 'display: none'
    link.href = url
    link.setAttribute('download', `${title}.pdf`)
    document.body.appendChild(link)
    link.click()
    window.URL.revokeObjectURL(url)
}

const bookService = {
    getBooks,
    getBook,
    downloadBook
}

export default bookService