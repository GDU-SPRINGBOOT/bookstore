import { memo, useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { formatCurrency } from "../../../utils/format_tien"
import "./styles.scss"
import axios from "axios"
import Image from '../../../assets/meo-chien-binh-tap-6-thoi-khac-tam-toi_128863_1.jpg'
import Cookies from 'universal-cookie'
import Header from "../theme/header"
import Footer from "../theme/footer"
const Cart = () => {
    const [cart, setCart] = useState();
    const [totalBooks, setTotalBooks] = useState(0);
    const [totalPrice, setTotalPrice] = useState(0);
    const [amount, setAmount] = useState(0)
    const [selectedBooks, setSelectedBooks] = useState([]);
    const navigate = useNavigate()
    const handleCheckboxChange = (event) => {
        const { value, checked } = event.target;
        let sum = 0
        // Nếu checkbox được tích vào
        if (checked) {
            setSelectedBooks((prevSelectedBooks) => [...prevSelectedBooks, value]);
            // Tìm sản phẩm trong giỏ hàng có id là value
            console.log('value: ', value)
            console.log('cart: ', cart.products[0])
            const arr = cart.products
            const foundProduct = arr.find(product => product.idcartitem === parseInt(value));
            // Nếu sản phẩm được tìm thấy, cập nhật totalBooks bằng số lượng của sản phẩm đó
            console.log('foundbook: ', foundProduct)
            if (foundProduct) {
                setTotalBooks((prevTotalBooks) => prevTotalBooks + foundProduct.quantity);
                setTotalPrice((prevTotalPrice) => prevTotalPrice + (foundProduct.quantity * foundProduct.price));

                // sum += foundProduct.quantity
            }
        } else { // Nếu checkbox bị bỏ chọn
            setSelectedBooks((prevSelectedBooks) =>
                prevSelectedBooks.filter((bookId) => bookId !== value)
            );
            // Tìm sản phẩm trong giỏ hàng có id là value
            const foundProduct = cart.products.find(product => product.idcartitem === parseInt(value));
            // Nếu sản phẩm được tìm thấy, cập nhật totalBooks bằng số lượng của sản phẩm đó
            if (foundProduct) {
                setTotalBooks((prevTotalBooks) => prevTotalBooks - foundProduct.quantity);
            }
        }
    };

    const handleSubmitOrder = () => {
        const isLoggedIn = cookies.get('token')
        if (selectedBooks.length < 1) {
            alert('Vui lòng chọn sản phẩm để đặt hàng')
            return
        }
        if (isLoggedIn) {
            navigate('/createorder', { state: { selectedBooks } });
        } else {
            alert('Bạn chưa đăng nhập. Vui lòng đăng nhập để tiếp tục.');
        }
    };
    const cookies = new Cookies()
    useEffect(() => {
        getCart();
    }, []);
    const getCart = async () => {
        const isLoggedIn = cookies.get('token');
        try {
            let cartResponse;
            if (isLoggedIn) {
                axios.defaults.withCredentials = true;
                cartResponse = await axios.get('http://localhost:8080/api/client/cart/getAll', {
                    headers: {
                        'Authorization': `Bearer ${isLoggedIn}`,
                    }
                });
            } else {
                axios.defaults.withCredentials = true;
                cartResponse = await axios.get('http://localhost:8080/api/client/cart/getAll');
            }
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
            setAmount(sumQuantityBooks)
            setCart(cart1);
        } catch (error) {
            console.error('Lỗi khi kiểm tra và lấy dữ liệu giỏ hàng:', error);
        }
    };
    const Arrayproducts = []
    if (cart && cart.products) {
        cart.products.forEach((cartproduct) => {
            Arrayproducts.push({
                products: cartproduct,
            })
        });
    } else {
        console.log('Không có sản phẩm trong giỏ hàng.')
    }
    const handleAdd = async (product) => {
        const isLoggedIn = cookies.get('token')

        try {
            if (isLoggedIn) {
                axios.defaults.withCredentials = true;
                const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`, null, {
                    headers: {
                        'Authorization': `Bearer ${isLoggedIn}`
                    }
                });
                if (response.data.status === 200) {
                    console.log('Sản phẩm đã được thêm vào giỏ hàng thành công:', response.data);
                    console.log('xxx')
                    getCart()
                }
            } else {
                axios.defaults.withCredentials = true;
                const response1 = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`);
                if (response1.data.status === 200) {
                    console.log('Chưa đăng nhập sản phẩm đã được thêm vào giỏ hàng thành công:', response1.data);
                    getCart()
                }
            }
        } catch (error) {
            console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
        }
    }
    const handleSubtract = async (product) => {
        const isLoggedIn = cookies.get('token')
        const updateQuantity = product.quantity - 1
        try {
            if (isLoggedIn) {
                if (updateQuantity < 1) {
                    axios.defaults.withCredentials = true;
                    const responsedelete = await axios.post(`http://localhost:8080/api/client/cart/delete/${product.id}`, null, {
                        headers: {
                            'Authorization': `Bearer ${isLoggedIn}`,
                            'Content-Type': 'application/json'
                        }
                    });
                    if (responsedelete.data.status === 200) {
                        console.log('x')
                        getCart()
                    }
                }
                axios.defaults.withCredentials = true;
                const response = await axios.post(`http://localhost:8080/api/client/cart/uppdate/${product.id}/${updateQuantity}`, null, {
                    headers: {
                        'Authorization': `Bearer ${isLoggedIn}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.data.status === 200) {
                    console.log('Sản phẩm đã được xóa thành công:', response.data);
                    getCart()
                }
            } else {
                if (updateQuantity < 1) {
                    axios.defaults.withCredentials = true;
                    const responsedelete = await axios.post(`http://localhost:8080/api/client/cart/delete/${product.id}`);
                    if (responsedelete.data.status === 200) {
                        getCart()
                    }
                }
                axios.defaults.withCredentials = true;
                const response1 = await axios.post(`http://localhost:8080/api/client/cart/uppdate/${product.id}/${updateQuantity}`, null);
                if (response1.data.status === 200) {
                    console.log('Chưa đăng nhập sản phẩm đã được xóa giỏ hàng thành công:', response1.data);
                    getCart()
                }
            }
        } catch (error) {
            console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
        }
    }
    const handleDelete = async (product) => {
        const isLoggedIn = cookies.get('token')
        if (isLoggedIn) {
            axios.defaults.withCredentials = true
            const responsedelete = await axios.post(`http://localhost:8080/api/client/cart/delete/${product.id}`, null, {
                headers: {
                    'Authorization': `Bearer ${isLoggedIn}`,
                    'Content-Type': 'application/json'
                }
            });
            if (responsedelete.data.status === 200) {
                getCart()
            }
        }
    }
    return (
        <>
            <Header amount={amount} />
            <div className="cart">
                <div className="container">
                    <div className="cart__header">Cart</div>
                    <div className="cart__content">
                        <div className="col-xl-9 col-lg-9 container__cart_left">
                            {Arrayproducts.length > 0 ? Arrayproducts.map((product, index) => (
                                <ul className=" cart__left_content" key={index}>
                                    {cookies.get('token') && <li className="cart__content_checkbox">
                                        <input type="checkbox" id={`book-${product.products.idcartitem}`} onChange={handleCheckboxChange} value={product.products.idcartitem}></input>
                                    </li>}
                                    <li className="cart__content_pic" style={{ backgroundImage: `url(data:image/jpeg;base64,${product.products.img})` }}></li>
                                    <li className="cart__content_text">
                                        <ul>
                                            <li>{product.products.name}</li>
                                            <li>{product.products.author}</li>
                                            <li><button onClick={() => { handleDelete(product.products) }} >Delete</button></li>
                                        </ul>
                                    </li>
                                    <li className="cart__content_price">{formatCurrency(product.products.price)}/book</li>
                                    <li className="cart__content_button">
                                        <ul className="button__box">
                                            <li className="button button_down">
                                                <button onClick={() => handleSubtract(product.products)}>-</button>
                                            </li>
                                            <li className="amount">
                                                <div>
                                                    {product.products.quantity}
                                                </div>
                                            </li>
                                            <li className="button button_up" >
                                                <button onClick={() => handleAdd(product.products)}>+</button>
                                            </li>
                                        </ul>
                                    </li>
                                </ul>
                            )) : <div>No products in the cart</div>}
                        </div>

                        <div className="col-xl-3 col-lg-3 container__cart_right">
                            {Arrayproducts.length !== 0 ? (
                                cookies.get('token') ? (
                                    <ul>
                                        <li>{totalBooks} books</li>
                                        <li>{formatCurrency(totalPrice)}</li>
                                        <li>(Shipping not included)</li>
                                        <button onClick={() => handleSubmitOrder()}>Order</button>
                                    </ul>
                                ) : (
                                    <div style={{backgroundColor: 'white', height: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                                        <div>Please log in to place an order</div>
                                    </div>
                                )
                            ) : null}
                        </div>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}
export default memo(Cart)