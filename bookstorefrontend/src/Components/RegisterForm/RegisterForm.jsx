import React, { useState } from 'react';
import api from '../../services/api'; // Import API module
import { FaUser, FaLock } from "react-icons/fa";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const RegisterForm = ({ onLoginForm }) => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate()
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            setError('Passwords do not match. Please try again.');
            console.error('Registration failed:', error.message);
            return;
        }

        try {
            axios.defaults.withCredentials = true
            const response = await axios.post('http://localhost:8080/api/auth/register',{
                'firstName': `${firstName}`,
                'lastName': `${lastName}`,
                'email': `${email}`,
                'password': `${password}`,
            });

            console.log('Registration successful:', response.data);
            alert(`Vui lòng bấm vào đường dẫn trong email ${email} để kích hoạt tài khoản`)
            navigate('/')
            // Display success message or redirect to login page
            // For example, you can set a state to display a success message
            // or navigate to the login page using history.push('/login')

        } catch (error) {
            console.error('Registration failed:', error.message);
            setError('Registration failed. Please try again.'); // Set error message state
        }
    };

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', position: 'relative'}}>

        <div className='wrapper' style={{position: 'absolute', top: '100px', backgroundColor: '#9C90D0'}}>
            <form onSubmit={(e) => handleSubmit(e)}>
                <h1>Register</h1>
                {error && <p className="error-message">{error}</p>} {/* Display error message if exists */}
                <div className="input-box">
                    <input type="text" placeholder='First Name' required value={firstName} onChange={(e) => setFirstName(e.target.value)} />
                    <FaUser className='icon' />
                </div>

                <div className="input-box">
                    <input type="text" placeholder='Last Name' required value={lastName} onChange={(e) => setLastName(e.target.value)} />
                    <FaUser className='icon' />
                </div>

                <div className="input-box">
                    <input type="email" placeholder='Email' required value={email} onChange={(e) => setEmail(e.target.value)} />
                    <FaUser className='icon' />
                </div>

                <div className="input-box">
                    <input type="password" placeholder='Password' required value={password} onChange={(e) => setPassword(e.target.value)} />
                    <FaLock className='icon' />
                </div>

                <div className="input-box">
                    <input type="password" placeholder='Confirm Password' required value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
                    <FaLock className='icon' />
                </div>

                <button type="submit">Register</button>

                <div className="register-link">
                    <p>Already have an account? <button onClick={onLoginForm}>Login</button></p>
                </div>
            </form>
        </div>
        </div>
    );
}

export default RegisterForm;
