import React, { memo, useEffect, useState } from 'react';
import Image from '../../../assets/meo-chien-binh-tap-6-thoi-khac-tam-toi_128863_1.jpg'
import { useSearchParams} from 'react-router-dom';
import axios from 'axios';
import "react-multi-carousel/lib/styles.css";
import '../../../../src/pages/users/bookdetail/style.scss'
import Header from '../theme/header';
import Footer from '../theme/footer';
import Cookies from 'universal-cookie';
import { formatCurrency } from '../../../utils/format_tien';
const BookDetail = () => {
    const [book, setBook] = useState()
    const [cart, setCart] = useState();
    const [quantity, setQuantity] = useState(1)
    const [searchParams] = useSearchParams();
    const searchTerm = searchParams.get('id');
    const [bookSomeGenre, setBookSomeGenre] = useState()
    const [amount, setAmount] = useState()
    const getBooksByWord = async () => {
        console.log('searchTerm: ',searchTerm)
        try {
            axios.defaults.withCredentials = true;
            const response = await axios.get(`http://localhost:8080/api/client/book/finbyId/${searchTerm}`);
            setBook(response.data.data);
        } catch (error) {
            // Xử lý lỗi từ API
            console.error("Error fetching books:", error);
            // Gán giá trị rỗng cho book hoặc thực hiện một xử lý khác tùy thuộc vào yêu cầu của bạn
            setBook([]);
        }
    };
    useEffect(() => {
        // Gọi hàm async bên trong useEffect
        const fetchData = async () => {
            await getBooksByWord();
        };
    
        fetchData();
        
    }, [searchTerm]);
    const getBooks = async () => {
        axios.defaults.withCredentials = true;
        const response = await axios.get(`http://localhost:8080/api/client/book/getAll`);
        
        const genre = [];
        if (book) {
            book.categoriesSet.forEach((item) => {
                genre.push(item.name);
            });
        }
    
        const bs = genre.map((item) => {
            return response.data.data.filter((b) => {
                return b.categories.includes(item);
            });
        });
    
        // Gộp các mảng thành một mảng duy nhất
        const mergedArray = bs.reduce((accumulator, currentValue) => {
            return accumulator.concat(currentValue);
        }, []);
    
        // Đảm bảo mergedArray không rỗng trước khi gán
        if (mergedArray.length > 0) {
            setBookSomeGenre(mergedArray);
        }
       
    };
   
    useEffect(()=>{
        getBooks()
        console.log('bsg: ',bookSomeGenre)
    },[searchParams])
    useEffect(() => {
        getCart();
    }, []);
    
    const getCart = async () => {
        const isLoggedIn = cookies.get('token')

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
            } else {
                // Người dùng chưa đăng nhập
                axios.defaults.withCredentials = true;
                cartResponse = await axios.get('http://localhost:8080/api/client/cart/getAll');
            }

            const cartData = cartResponse.data;
            console.log('cartData:', cartData);
            let sumQuantityBooks =0
            const cart = {
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
            setCart(cart);
            setAmount(sumQuantityBooks)
        } catch (error) {
            console.error('Lỗi khi kiểm tra và lấy dữ liệu giỏ hàng:', error);
        }
    };
    const cookies = new Cookies()
    const handleAddToCart = async (product, quantity) => {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        const isLoggedIn = cookies.get('token')

        axios.defaults.withCredentials = true;
        try {
            if (isLoggedIn) {
                if (quantity === 1) {
                    // Gửi request POST đến API endpoint để thêm sản phẩm vào cơ sở dữ liệu
                    const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`,null,{
                        // Đặt các headers cần thiết cho request, ví dụ như Authorization header nếu cần
                        headers: {
                            'Authorization': `Bearer ${isLoggedIn}`,
                            'Content-Type': 'application/json'
                        }
                    });

                    if (response.data.status === 200) {
                        console.log('Sản phẩm đã được thêm vào giỏ hàng thành công:', response.data);
                        updateCartItemCount(response.data.status)
                        getCart()
                    }
                } else {
                    let oldQuantity
                    const existingProduct = cart.products.find(item => item.id === product.id)
                    if (existingProduct) {
                        oldQuantity = existingProduct.quantity
                        const response = await axios.post(`http://localhost:8080/api/client/cart/uppdate/${product.id}/${oldQuantity + quantity}`,null, {
                            // Đặt các headers cần thiết cho request, ví dụ như Authorization header nếu cần
                            headers: {
                                'Authorization': `Bearer ${isLoggedIn}`,
                                'Content-Type': 'application/json'
                            }
                        });
                        if (response.data.status === 200) {
                            updateCartItemCount(response.data.status)
                            getCart()
                        }
                    } else {
                        const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`,null,{
                            // Đặt các headers cần thiết cho request, ví dụ như Authorization header nếu cần
                            headers: {
                                'Authorization': `Bearer ${isLoggedIn}`,
                                'Content-Type': 'application/json'
                            }
                        });
                        if (response.data.status === 200) {
                            const response = await axios.post(`http://localhost:8080/api/client/cart/uppdate/${product.id}/${quantity}`, null,{
                                // Đặt các headers cần thiết cho request, ví dụ như Authorization header nếu cần
                                headers: {
                                    'Authorization': `Bearer ${isLoggedIn}`,
                                    'Content-Type': 'application/json'
                                }
                            });
                            if (response.data.status === 200) {
    
                                updateCartItemCount(response.data.status)
                                getCart()
                            }
                        }
                    }
                }
            } else {

                if (quantity === 1) {
                    // Gửi request POST đến API endpoint để thêm sản phẩm vào cơ sở dữ liệu
                    const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`);

                    if (response.data.status === 200) {
                        console.log('Sản phẩm đã được thêm vào giỏ hàng thành công:', response.data);
                        updateCartItemCount(response.data.status)
                        getCart()
                    }
                } else {
                    let oldQuantity
                    const existingProduct = cart.products.find(item => item.id === product.id)
                    if (existingProduct) {
                        oldQuantity = existingProduct.quantity
                        const response = await axios.post(`http://localhost:8080/api/client/cart/uppdate/${product.id}/${oldQuantity + quantity}`);
                        if (response.data.status === 200) {
    
                            updateCartItemCount(response.data.status)
                            getCart()
                        }
                    } else {
                        const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`);
                        if (response.data.status === 200) {
                            const response = await axios.post(`http://localhost:8080/api/client/cart/uppdate/${product.id}/${quantity}`);
                            if (response.data.status === 200) {
    
                                updateCartItemCount(response.data.status)
                                getCart()
                            }
                        }
                    }
                }
            }
        } catch (error) {
            // Xử lý lỗi nếu request gặp vấn đề
            console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
            // Hiển thị thông báo lỗi cho người dùng hoặc xử lý lỗi khác tùy theo nhu cầu
        }
    }
    const updateCartItemCount = (status) => {
        if (status === 200) {
            alert('Successfull', 'Đã thêm sản phẩm vào giỏ hàng')
        }
    };
    console.log('book: ',book)
    return (
        <>
        <Header amount={amount}/>
        <div className='container' style={{minHeight: '550px'}}>
            {book && <ul className='content book'>
                <li className='image' style={{ backgroundImage: `url(data:image/jpeg;base64,${book.bookImage})` }}></li>
                <li className='book_detail'>
                    <ul className='detail'>
                        <li className='name'>{book.name}</li>
                        <li className='book_author'>{book.author}</li>
                        <li className='book_price'>{formatCurrency(book.price)}</li>
                        <li className='categories'>
                            <div>categories:</div>
                            <ul>
                                {book.categoriesSet.map((item, index)=>(
                                    <li key={index}>{item.name}</li>
                                ))}
                            </ul>
                        </li>
                        <li className='button'>
                            <ul className='button_box'>
                                <li>
                                    <button onClick={() => setQuantity(quantity - 1)}>-</button>
                                </li>
                                <li className='amount'>{quantity}</li>
                                <li>
                                    <button onClick={() => setQuantity(quantity + 1)}>+</button>
                                </li>
                            </ul>
                        </li>
                        <li className='button_addtocart'>
                            <button onClick={() => handleAddToCart(book, quantity)}>Thêm vào giỏ hàng</button>
                        </li>
                        <li className='description'>{book.description}</li>
                    </ul>
                </li>
            </ul>}
        </div>
        <Footer/>
        </>
    )
}
export default memo(BookDetail)