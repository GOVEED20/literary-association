import {Toast} from "react-bootstrap";

const Toaster = ({type, message, color}) => {
    return (
            <div
                style={{
                    position: 'absolute',
                    top: 0,
                    right: 0,
                    marginTop: '15px',
                    marginRight: '15px',
                    width: '250px',
                    backgroundColor: color
                }}
            >
                <Toast>
                    <Toast.Header>
                        {
                            type === 'success' ?
                                <strong className="mr-auto">Success message</strong>
                                :
                                <strong className="mr-auto">Error message</strong>
                        }
                    </Toast.Header>
                    <Toast.Body>{message}</Toast.Body>
                </Toast>
            </div>
    );
}

export default Toaster;