import React, {useEffect, useState} from "react";
import axios from 'axios';

const Register = () => {
    const [formFields, setFormFields] = useState([]);
    const [taskId, setTaskId] = useState("");
    const [genres, setGenres] = useState([]);
    const [userData, setUserData] = useState({
        username: "",
        password: "",
        name: "",
        surname: "",
        country: "",
        city: "",
        email: "",
        genres: [],
        beta_reader: false,
        beta_genres: []
    });

    useEffect(() => {
        axios
            .get('http://localhost:9090/api/register/form-fields')
            .then(response => {
                setFormFields(response.data.formFields);
                setTaskId(response.data.taskId);
            });

        // get genres
        axios
            .get('http://localhost:9090/api/genres')
            .then(response => {
                setGenres(response.data);
            })
    }, []);

    const setInput = (field) => {
        if (field.id === 'password') {
            return <input id={field.id} name={field.id}
                          type='password' required pattern={field.properties.pattern} onChange={handleChange}/>
        }
        if (field.id === 'genres') {
            return <select id={field.id} name={field.id} required multiple onChange={handleChange}>
                {genres.map(genre =>
                    <option value={genre}>
                        {genre.charAt(0).toUpperCase() + genre.slice(1)}
                    </option>
                )}
            </select>
        }
        if (field.id === "email") {
            return <input id={field.id} name={field.id}
                          type='text' required pattern={field.properties.pattern} onChange={handleChange}/>
        }
        if (field.type.name === "string") {
            return <input id={field.id} name={field.id}
                          type='text' required onChange={handleChange}/>
        }
        if (field.type.name === "boolean") {
            return <input id={field.id} name={field.id}
                          type='checkbox' onChange={handleChange}/>
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (name === "beta_reader") {
            setUserData({...userData, [name]: e.target.checked });
        }
        else if (name === "genres" || name === "beta_genres") {
            let values = Array.from(e.target.selectedOptions, option => option.value);
            setUserData({...userData, [name]: values});
        }
        else {
            setUserData({...userData, [name]: value });
        }
    };

    const register = (e) => {
        e.preventDefault();
        if (!userData["beta_reader"]) {
            userData["beta_genres"] = []
        }
        userData["genres"] = "[" + userData["genres"].map(g => `"${g}"`).join(',') + "]";
        userData["beta_genres"] = "[" + userData["beta_genres"].map(gr => `"${gr}"`).join(',') + "]";
        console.log(userData["beta_genres"]);
        let data = [];
        for (let property in userData) {
            data.push({fieldId : property, fieldValue : userData[property]});
        }
        let regData = { "taskID": taskId, "formFields": data };
        axios
            .post('http://localhost:9090/api/register', regData)
            .then(response => {
                console.log(response)
            });
    };

    return (
        <div>
            <h3>Register</h3>
            <form onSubmit={register} method="POST">
                {
                    formFields.map(field => {
                        return (
                            <div className="row">
                                <div className="input-field col s12">
                                    <label htmlFor={field.id}>{field.label}</label>
                                    <br/>
                                    {setInput(field)}
                                </div>
                            </div>
                        )
                    })
                }
                {
                    userData['beta_reader'] ?
                        <div className="input-field col s12">
                        <label htmlFor="beta_genres">Select genres for beta reader</label>
                            <br/>
                            <select id="beta_genres" name="beta_genres" required multiple onChange={handleChange}>
                        {genres.map(genre =>
                            <option value={genre}>
                                {genre.charAt(0).toUpperCase() + genre.slice(1)}
                            </option>
                        )}
                        </select></div>: null
                }
                <br/>
                <input className="btn" type="Submit" defaultValue="Register" />
            </form>
        </div>
    );
};

export default Register;
