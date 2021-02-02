import bookService from '../services/bookService'

export const getBooks = () => {
    return async dispatch => {
        const books = await bookService.getBooks()

        dispatch({
            type: 'GET_BOOKS',
            books
        })
    }
}

const reducer = (state = [], action) => {
    switch (action.type) {
    case 'GET_BOOKS':
        return action.books
    default:
        return state
    }
}

export default reducer