import React, { memo, useEffect, useState } from "react";
import axios from 'axios'
import Image from '../../../assets/meo-chien-binh-tap-6-thoi-khac-tam-toi_128863_1.jpg'
import '../findpage/style.scss'
import ReactPaginate from 'react-paginate';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { FaEye, FaShoppingCart } from "react-icons/fa";
import { formatCurrency } from "../../../utils/format_tien";
import Header from "../theme/header";
import Footer from "../theme/footer";
import Cookies from "universal-cookie";
const FindPage = () => {
    const navigate = useNavigate()
    const [searchParams] = useSearchParams();
    const searchTerm = searchParams.get('search');
    const queryString = searchParams.toString().split('='); // "category=Fantasy+&+Science+fiction"
    const category = decodeURIComponent(queryString[1].replace(/\+/g, ' '));
    const [cate, setCate] = useState([])
    const [books, setBooks] = useState([])
    const [pageNumber, setPageNumber] = useState(0); // Thêm state để lưu trang hiện tại
    const booksPerPage = 15; // Số lượng sách trên mỗi trang
    const [amount, setAmount] = useState()
    const cookies = new Cookies()
    const chonsse = (categoryname) => {
        if (Number.isNaN(Number(categoryname))) {
            if (categoryname === '') {
                const element = document.querySelector(`.all`);
                const elementselected = document.querySelector(`.selected`);
                if (elementselected) {
                    elementselected.classList.remove('selected')
                }
                if (element) {
                    // Thêm class 'selected' vào phần tử
                    element.classList.add('selected');
                    console.log('class: ', element.classList)
                }
            } else {
                const element = document.querySelector(`.${nospace(categoryname)}`);
                const elementselected = document.querySelector(`.selected`);
                if (elementselected) {
                    elementselected.classList.remove('selected')
                }
                // Kiểm tra xem phần tử có tồn tại không
                if (element) {
                    // Thêm class 'selected' vào phần tử
                    element.classList.add('selected');
                }
            }
        }
    }
    useEffect(() => {
        chonsse(category)
    }, [category])
    const getCates = async () => {
        try {
            axios.defaults.withCredentials = true;
            const response = await axios.get('http://localhost:8080/api/client/categories')
            setCate(response.data)
        } catch (e) {
            console.error('error', e)
        }
    }
    const getBooksByWord = async (searchTerm) => {
        try {
            axios.defaults.withCredentials = true;
            const response = await axios.get(`http://localhost:8080/api/client/book/search?keyword=${searchTerm}`)
            setBooks(response.data.data)
        } catch (error) {
            if (error.response && error.response.status === 404) {
                setBooks([]);
            }
            console.error('error', error)
        }
    }
    const getBooks = async () => {
        try {
            axios.defaults.withCredentials = true;
            const response = await axios.get('http://localhost:8080/api/client/book/getAll')
            setBooks(response.data.data)
        } catch (e) {
            console.error('error: ', e)
        }
    }

    useEffect(() => {
        getCates()
        if (searchTerm) {
            const e = document.querySelector('.selected')
            if(e) {
                e.classList.remove('selected')
            }
                getBooksByWord(searchTerm)

        } else {
            if (category) {
                getBooksByCateOfParams(category)
            } else {
                getBooks()
            }
        }
    }, [searchTerm, category])

    const pageCount = Math.ceil(books.length / booksPerPage);

    const handlePageChange = ({ selected }) => {
        console.log('selected: ', selected)
        setPageNumber(selected);
    };
    const getBooksByCateOfParams = async (category) => {
        if (category === 'all') {
            getBooks()
            return
        }
        axios.defaults.withCredentials = true;
        const response = await axios.get('http://localhost:8080/api/client/book/getAll')
        const booksbycate = response.data.data.filter((item) => item.categories.includes(category))
        setBooks(booksbycate)
    }
    const handleAdd = async (product) => {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        const isLoggedIn = cookies.get('token')
        axios.defaults.withCredentials = true;
        try {
            if (isLoggedIn) {
                // Gửi request POST đến API endpoint để thêm sản phẩm vào cơ sở dữ liệu
                const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`, null, {
                    // Đặt các headers cần thiết cho request, ví dụ như Authorization header nếu cần
                    headers: {
                        'Authorization': `Bearer ${isLoggedIn}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.data.status === 200) {// Xử lý response từ API nếu thành công
                    console.log('Sản phẩm đã được thêm vào giỏ hàng thành công:', response.data);
                    getCart()
                    updateCartItemCount(response.data.status)
                }
            } else {

                const response1 = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`);
                if (response1.data.status === 200) {
                    getCart()
                    console.log('Chưa đăng nhập sản phẩm đã được thêm vào giỏ hàng thành công:', response1.data);
                    updateCartItemCount(response1.data.status)
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
    const getCart = async () => {
        const isLoggedIn = cookies.get('token');
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
    const updateAmount = (amount) => {
        if (amount > 99) {
            setAmount('+99')
        } else {
            setAmount(amount)
        }
    }
    const nospace = (str) => {
        const validClassName = str.replace(/[^\w-]/g, '');
        return validClassName
    }
    return (
        <>
            <Header amount={amount} />
            <div className="container">
                <div className="content">
                    <ul className="content_left">
                        <li className="content_left_headers">Directory</li>
                        <li className="all cate" onClick={() => { navigate(`/find?category=all`); handlePageChange({ selected: 0 }) }}>All</li>
                        {cate && cate.map((item, index) => (
                            <li className={`${nospace(item.name)} cate`} key={index} onClick={() => { navigate(`/find?category=${item.name}`); handlePageChange({ selected: 0 }) }}>{item.name}</li>
                        ))}
                    </ul>
                    <div className="content_right">
                        <div className="content_right_header">
                            {/* <div className="content_right_header_amount s15">{booksPerPage}</div>
                            {booksPerPage === 15 ? <div className="content_right_header_amount s20">20</div> : <div className="content_right_header_amount">15</div>} */}
                        </div>
                        <ul className="content_right_list">
                            {books.length > 0 ? books
                                .slice(pageNumber * booksPerPage, (pageNumber + 1) * booksPerPage) // Chia mảng sách thành các trang
                                .map((item, index) => (
                                    <li key={index} className="book_box">
                                        <ul key={index}>
                                            <li className="book_img" style={{ backgroundImage: `url(data:image/jpeg;base64,${item.image})` }}>
                                                <ul className="featured__item_pic_hover">
                                                    <li>
                                                        <button onClick={() => navigate(`/detail?id=${item.id}`)}>
                                                            <FaEye />
                                                        </button>
                                                    </li>
                                                    <li>
                                                        <button onClick={() => handleAdd(item)}>

                                                            <FaShoppingCart />
                                                        </button>
                                                    </li>
                                                </ul>
                                            </li>
                                            <li className="name">{item.name} vvv vvv</li>
                                            <li className="author">{item.author}</li>
                                            <li className="price">{formatCurrency(item.price)}</li>
                                        </ul>
                                    </li>
                                )): <li style={{marginLeft: '40%', marginTop: '30%', fontSize: '18px'}}>Can't find the book you want</li>}
                        </ul>
                        <ReactPaginate
                            previousLabel={'<<'}
                            nextLabel={'>>'}
                            breakLabel={'...'}
                            pageCount={pageCount}
                            marginPagesDisplayed={2}
                            pageRangeDisplayed={5}
                            onPageChange={handlePageChange}
                            containerClassName={'pagination'}
                            activeClassName={'active'}
                            previousClassName={'paginate__previous'}
                            nextClassName={'paginate__next'}
                            breakClassName={'paginate__break'}
                            pageClassName={'paginate__page'}
                            previousLinkClassName={'paginate__previous-link'}
                            nextLinkClassName={'paginate__next-link'}
                            breakLinkClassName={'paginate__break-link'}
                            pageLinkClassName={'paginate__page-link'}
                            forcePage={pageNumber}
                        />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}
export default memo(FindPage)
