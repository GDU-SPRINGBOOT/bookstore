import React, { memo, useEffect, useState } from 'react'
import Header from '../users/theme/header'
import Footer from '../users/theme/footer'
import Cookies from 'universal-cookie'
import axios from 'axios'
import Image from '../../assets/meo-chien-binh-tap-6-thoi-khac-tam-toi_128863_1.jpg'
import './style.scss'
import { formatCurrency } from '../../utils/format_tien'
const Account = () => {
    const cookies = new Cookies()
    const [amount, setAmount] = useState(0)
    const [orders, setOrders] = useState()
    const getCart = async () => {
        const isLoggedIn = cookies.get('token');
        console.log('token:', isLoggedIn)
        try {
            let cartResponse;
            if (isLoggedIn) {
                axios.defaults.withCredentials = true;
                // Người dùng đã đăng nhập
                cartResponse = await axios.get('http://localhost:8080/api/client/cart/getAll', {
                    headers: {
                        'Authorization': `Bearer ${isLoggedIn}`,
                    }
                });
                console.log('da dang nhap')
            } else {
                // Người dùng chưa đăng nhập
                axios.defaults.withCredentials = true;
                cartResponse = await axios.get('http://localhost:8080/api/client/cart/getAll');
            }

            const cartData = cartResponse.data;
            let sumQuantityBooks = 0;
            const cart1 = {
                products: cartData.data.map(cartProduct => {
                    sumQuantityBooks += cartProduct.quantity
                    return {
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
            setAmount(sumQuantityBooks)
        } catch (error) {
            console.error('Lỗi khi kiểm tra và lấy dữ liệu giỏ hàng:', error);
        }
    };
    useEffect(() => {
        getCart()
    }, [])
    useEffect(() => {
        getOrder()
    }, [])
    const handleCancel = async (orderId) =>{
        const token = cookies.get('token')
        axios.defaults.withCredentials = true
        const response = await axios.post(`http://localhost:8080/api/client/order/${orderId}`,null,{
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
        if(response.data.status) {

            getOrder()
        }
    }
    const getOrder = async () => {
        const isLoggedIn = cookies.get('token')
        if (isLoggedIn) {

            try {
                axios.defaults.withCredentials = true
                const response = await axios.get(`http://localhost:8080/api/client/order/getAll`, {
                    headers: {
                        Authorization: `Bearer ${isLoggedIn}`
                    }
                })
                if (response.data.status === 200) {
                    setOrders(response.data.data)

                }
            } catch (e) {
                console.error('Lỗi khi lấy đơn hàng:', e);
            }
        }
    }
    const handleDelivered = () => {

    }

    return (
        <>
            <Header amount={amount} />
            {cookies.get('token') ?
                <div className='container' style={{minHeight: '550px'}}>
                    <div className='box_user'>
                        <div className='box_user_name'>
                            <div style={{marginRight: '10px'}}>Name:</div>
                            <div>{cookies.get('name')}</div>
                        </div>
                        <div className='box_user_email'>

                            <div style={{marginRight: '10px'}}>Email:</div>
                            <div>{cookies.get('username')}</div>
                        </div>
                        <div className='box_user_sdt'>

                            <div style={{marginRight: '10px'}}>Phone:</div>
                            <div>0582132246</div>
                        </div>
                        <div className='box_user_bird'>

                            <div style={{marginRight: '10px'}}>Birthday:</div>
                            <div>25/02/2003</div>
                        </div>
                        <div className='box_user_address'>

                            <div style={{marginRight: '10px'}}>Address:</div>
                            <div>Phuoc Tan 1, Tan Hung, Ba Ria City, BRVT Province</div>
                        </div>
                    </div>
                    <div className='box_user_orders'>

                        {orders && 
                        <div className='box_user_orders_header'>All orders</div>
                        }
                        {orders && 
                        
                            <ul style={{width: '100%', display: 'flex', justifyContent: 'flex-end', borderBottom: '1px solid black'}}>
                                <li style={{width: '8%', textAlign: 'center'}}>Quantity</li>
                                <li style={{width: '12%', textAlign: 'center'}}>Price</li>
                                <li style={{width: '12%', textAlign: 'center'}}>Total</li>
                            </ul>}
                        <div className='box_user_orders_body'>
                            {orders && orders.map((order, index) => (
                                <div className='box_book' style={{borderBottom: '1px solid black'}}>
                                    <div className='book_img' style={{ backgroundImage: `url(${Image})` }}></div>
                                    <div className='book_detail'>
                                        <div className='detail'>
                                            <div className='name_author'>
                                                <div>{order.booklist[0].name}</div>
                                                <div>{order.booklist[0].author}</div>
                                            </div>
                                            <div className='book_quantity' style={{textAlign: 'center'}}>{(order.payment / order.booklist[0].price)}</div>
                                            <div className='price_payment' >
                                                <div style={{textAlign: 'center'}}>{formatCurrency((order.payment / (order.payment / order.booklist[0].price)) + (order.payment % (order.payment / order.booklist[0].price)))}</div>
                                                <div style={{textAlign: 'center'}}>{formatCurrency(order.payment)}</div>
                                            </div>
                                        </div>
                                        <div className='box_status'>
                                            <div className='status'>
                                                <div style={{marginRight: '10px'}}>Condition:</div>
                                                {order.status === 'PENDING' ?
                                                <div>PENDING</div> : 
                                                order.status === 'PROCESSING' ? 
                                                <div>PROCESSING</div>:
                                                order.status === 'DELIVERED' ? 
                                                <div>DELIVERED</div>:
                                                <div>CANCELLED</div>
                                                }
                                            </div>
                                            <div>
                                                {order.status === 'PENDING' &&
                                                <button onClick={()=> handleCancel(order.id)}>cancel</button>
                                                }
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))
                            }
                        </div>

                    </div>
                </div>
                : <div>you're not signed in</div>}
            <Footer />
        </>
    )
}
export default memo(Account)