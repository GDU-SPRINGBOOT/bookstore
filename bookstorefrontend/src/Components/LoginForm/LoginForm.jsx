import React, { useState, useEffect } from 'react';
import './LoginForm.css';
import { FaUser, FaLock } from "react-icons/fa";
import api from '../../services/api'; // Import API module
import { useNavigate } from 'react-router-dom';
import Cookies from 'universal-cookie';
import axios from 'axios';

const LoginForm = ({ onRegisterForm }) => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [cart, setCart] = useState()
    const cookies = new Cookies()
    const getCart = async () => {
        try {
            let cartResponse;

            axios.defaults.withCredentials = true;
            cartResponse = await axios.get('http://localhost:8080/api/client/cart/getAll');

            const cartData = cartResponse.data;
            let sumQuantityBooks = 0;
            let sumPrice = 0;
            const cart1 = {
                products: cartData.data.map(cartProduct => {
                    sumQuantityBooks += cartProduct.quantity
                    sumPrice += cartProduct.quantity * cartProduct.book.price
                    return {
                        // idcart: cartProduct.cart.id,
                        idcartitem: cartProduct.id,
                        id: cartProduct.book.id,
                        img: cartProduct.book.bookImage,
                        name: cartProduct.book.name,
                        author: cartProduct.book.author,
                        description: cartProduct.book.description,
                        price: cartProduct.book.price,
                        quantity: cartProduct.quantity
                    };
                })
            };
            setCart(cart1);
            console.log('cart1: ', cart1)
        } catch (error) {
            console.error('Lỗi khi kiểm tra và lấy dữ liệu giỏ hàng:', error);
        }
    };
    useEffect(() => {
        getCart()
    }, [])
    const handleAddToCart = async (product, quantity) => {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        const isLoggedIn = cookies.get('token')
        console.log('token: ', isLoggedIn)
        axios.defaults.withCredentials = true;
        try {
            if (isLoggedIn) {
                for (let i = 0; i < quantity; i++) {
                    const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`, null, {
                        // Đặt các headers cần thiết cho request, ví dụ như Authorization header nếu cần
                        headers: {
                            'Authorization': `Bearer ${isLoggedIn}`,
                            'Content-Type': 'application/json'
                        }
                    });

                    if (response.data.status === 200) {
                        console.log('Sản phẩm đã được thêm vào giỏ hàng thành công:', response.data);
                        // getCart()
                    }
                }
            } else {
                for (let i = 0; i < quantity; i++) {
                    const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`, null, {
                        // Đặt các headers cần thiết cho request, ví dụ như Authorization header nếu cần
                        headers: {
                            'Authorization': `Bearer ${isLoggedIn}`,
                            'Content-Type': 'application/json'
                        }
                    });

                    if (response.data.status === 200) {
                        console.log('Sản phẩm đã được thêm vào giỏ hàng thành công:', response.data);
                        // getCart()
                    }
                }
            }
        } catch (error) {
            // Xử lý lỗi nếu request gặp vấn đề
            console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
            // Hiển thị thông báo lỗi cho người dùng hoặc xử lý lỗi khác tùy theo nhu cầu
        }
    }
    const handleDeleteItemCartSession = async (product) => {

        axios.defaults.withCredentials = true
        const responsedelete = await axios.post(`http://localhost:8080/api/client/cart/delete/${product.id}`);
        if (responsedelete.data.status === 200) {
            console.log('Sản phẩm đã được xoa khoi giỏ hàng thành công:', responsedelete.data);
        }
    }
    const handleLogin = async (e) => {
        try {

            e.preventDefault();
            axios.defaults.withCredentials = true;
            const response = await axios.post('http://localhost:8080/api/auth/authenticate', {
                "email": username,
                "password": password
            })
            console.log('respon:', response.data)
            if (response.status === 200) {
                // Lưu JSESSIONID vào cookies
                // console.log('response: ', response.headers)
                // const jsessionId = getJsessionIdFromResponse(response);
                // console.log('session: ',jsessionId)
                // cookies.set('JSESSIONID', jsessionId, { path: '/' });

                // Lưu các thông tin khác vào cookies
                cookies.set('userRole', response.data.role, { path: '/', maxAge: 604800 }); // expires in 7 days
                cookies.set('name', response.data.name, { path: '/', maxAge: 604800 }); // expires in 7 days
                cookies.set('username', response.data.username, { path: '/', maxAge: 604800 }); // expires in 7 days
                cookies.set('token', response.data.token, { path: '/', maxAge: 604800 }); // expires in 7 days

                console.log('Đăng nhập thành công');
                if (cart) {
                    cart.products.forEach((element) => {
                        handleAddToCart(element, element.quantity)
                        handleDeleteItemCartSession(element)
                    })
                }
                window.location.href = '/';
            }
        } catch (e) {
            if(e.response.status === 400) {
                const elements = document.querySelectorAll('.input-box');
                elements.forEach((element) => {
                    element.classList.add('error');
                })
            } else {
                console.log('error: ',e)
            }
            
        }
    };
    const navigate = useNavigate()
    return (<div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', position: 'relative' }} className='box'>

        <div className='wrapper' style={{ position: 'absolute', top: '100px', backgroundColor: '#9C90D0' }}>
            <form onSubmit={(e) => handleLogin(e)}>
                <h1>Login</h1>
                <div className="input-box">
                    <input
                        type="email"
                        placeholder='Email'
                        required value={username}
                        onChange={(e) => {
                            setUsername(e.target.value);
                            e.target.parentElement.classList.remove('error');
                        }} />
                    <FaUser className='icon' />
                </div>

                <div className="input-box">
                    <input
                        type="password"
                        placeholder='Password'
                        required
                        value={password}
                        onChange={(e) => {
                            setPassword(e.target.value);
                            e.target.parentElement.classList.remove('error');
                        }}
                    />
                    <FaLock className='icon' />
                </div>

                <div className="remember-forgot">
                    <label><input type='checkbox' />Remember me</label>
                    <a href="#">Forgot password?</a>
                </div>

                <button type="submit">Login</button>

                <div className="register-link">
                    <p>Don't have an account? <button onClick={() => navigate('/register')}>Register</button></p>
                </div>
            </form>
        </div>
    </div>
    );
}

export default LoginForm;
