import { memo, useState, useEffect } from "react"
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import 'react-tabs/style/react-tabs.css';
import { FaEye, FaShoppingCart } from "react-icons/fa";
import "./style.scss"
import { Link, useNavigate } from "react-router-dom"
import axios from 'axios';
import { formatCurrency } from "../../../../src/utils/format_tien.js";
import { useMediaQuery } from 'react-responsive';
import Image from '../../../assets/meo-chien-binh-tap-6-thoi-khac-tam-toi_128863_1.jpg';
import Image1 from '../../../assets/3d.jpg';
import Image2 from '../../../assets/3dc9db62-b9b6-4f44-a584-c67eaa332c31.jpg';
import Image3 from '../../../assets/6c826edd-f554-494b-972c-9fe51cdc5291.jpg';
import Image4 from '../../../assets/banner1.jpg';
import Image5 from '../../../assets/cc52f903-4e81-48bd-9a3c-0dd02878e446.jpg';
import Image6 from '../../../assets/loai-3-01.jpg';
import Header from "../theme/header/index.js";
import Footer from "../theme/footer/index.js";
import Cookies from "universal-cookie";

const API_URL = 'http://localhost:8080/api/client/book/getAll';
const HomePage = () => {
    const cookies = new Cookies()
    const navigate = useNavigate();
    const [data, setData] = useState([]);
    const [amount, setAmount] = useState(0)
    const [cate, setCate] = useState();
    useEffect(() => {
        const fetchData = async () => {
            try {
                axios.defaults.withCredentials = true;
                const response = await axios.get(API_URL);
                setData(response.data.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, []);
    useEffect(() => {
        const fetchData = async () => {
            axios.defaults.withCredentials = true;
            try {
                const response = await axios.get('http://localhost:8080/api/client/categories');
                setCate(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);
    let categoryNames = [];
    if (cate) {
        categoryNames = cate.map(category => category.name);
    }
    const handleAdd = async (product) => {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        const isLoggedIn = cookies.get('token')
        axios.defaults.withCredentials = true;
        try {
            if (isLoggedIn) {
                const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`,null,{
                    headers: {
                        'Authorization': `Bearer ${isLoggedIn}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.data.status === 200) {
                    getCart()
                    updateAmount(amount)
                }
            } else {

                const response = await axios.post(`http://localhost:8080/api/client/cart/add/${product.id}`);
                

                if (response.data.status === 200) {// Xử lý response từ API nếu thành công
                    // console.log('Sản phẩm đã được thêm vào giỏ hàng thành công:', response.data);
                    getCart()
                    updateAmount(amount)
                }
            }
        } catch (error) {
            // Xử lý lỗi nếu request gặp vấn đề
            console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
            // Hiển thị thông báo lỗi cho người dùng hoặc xử lý lỗi khác tùy theo nhu cầu
        }
    }
    
    const responsive = {
        superLargeDesktop: {

            breakpoint: { max: 4000, min: 3000 },
            items: 5
        },
        desktop: {
            breakpoint: { max: 3000, min: 1024 },
            items: 4
        },
        tablet: {
            breakpoint: { max: 1024, min: 464 },
            items: 2
        },
        mobile: {
            breakpoint: { max: 464, min: 0 },
            items: 1
        }
    };
    const responsive1 = {
        superLargeDesktop: {

            breakpoint: { max: 4000, min: 3000 },
            items: 1
        },
        desktop: {
            breakpoint: { max: 3000, min: 1024 },
            items: 1
        },
        tablet: {
            breakpoint: { max: 1024, min: 464 },
            items: 1
        },
        mobile: {
            breakpoint: { max: 464, min: 0 },
            items: 1
        }
    };

    const featproducts = {
        all: {
            title: "Toàn bộ",
            products: []
        },
    }

    categoryNames.forEach(genre => {
        featproducts[genre] = {
            title: genre,
            products: []
        }
    })
    categoryNames.forEach(genre => {
        const topBooksInGenre = data.filter(book => Array.isArray(book.categories) && book.categories.includes(genre));
        featproducts[genre] = {
            title: genre,
            products: topBooksInGenre
        };
    });

    const isTabletOrMobile = useMediaQuery({ query: '(max-width: 768px)' });

    const bookCount = isTabletOrMobile ? 4 : 8;

    const topBooks = data.slice(0, bookCount);

    featproducts.all.products = topBooks
    window.featproducts = featproducts
    console.log('fe: ',featproducts)
    const renderSlider = () => {
        return (
            <>
            
                {categoryNames.map((item, index) => (index < 3 &&
                    <div style={{ marginBottom: 50 }} key={index}>
                        <div className="container container__categories_slider">
                            <div className="categories_slider_header">
                                <div>{item}</div>
                                <Link to={`/find?category=${item}`}>All</Link>
                            </div>
                            <div className="categories_slider_body">
                                <Carousel responsive={responsive} className="categories_slider" key={index}>
                                    {
                                        featproducts[item].products.map((item, key) => (
                                            <div key={key} >
                                                <div className="categories_slider_item" style={{paddingTop: '10px'}}>
                                                    <div className="categories_slider_pic" style={{ backgroundImage: `url(data:image/jpeg;base64,${item.image})` }} key={key}>

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
                                                    </div>
                                                    <div className="categories_slider_text">{item.name}</div>
                                                    <div className="categories_slider_text">{formatCurrency(item.price)}</div>
                                                </div>
                                            </div>
                                        ))
                                    }
                                </Carousel>
                            </div>
                        </div>
                    </div>
                ))}
               
            </>
        )
    }
    const getCart = async () => {
        const isLoggedIn = cookies.get('token');
        console.log('token:', isLoggedIn)
        try {
            let cartResponse;
            if (isLoggedIn) {
                axios.defaults.withCredentials = true;
                // Người dùng đã đăng nhập
                cartResponse = await axios.get('http://localhost:8080/api/client/cart/getAll',{
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

    useEffect(()=>{
        getCart()
    },[])
    const updateAmount = (amount) =>{
        if (amount > 99) {
            setAmount('+99')
        } else {
            setAmount(amount)
        }
    }
    return (
        <>
        <Header amount={amount}/>
            <div className="homepage">
                <div className="categories_slider_body poster container">
                    <Carousel responsive={responsive1} className="poster__slider categories_slider" autoPlay={true} // Thiết lập tự động di chuyển
        autoPlaySpeed={2000}
        infinite={true} >
                        <div className="categories_slider_pic" style={{backgroundImage: `url(${Image1})`}}></div>
                        <div className="categories_slider_pic" style={{backgroundImage: `url(${Image2})`}}></div>
                        <div className="categories_slider_pic" style={{backgroundImage: `url(${Image3})`}}></div>
                        <div className="categories_slider_pic" style={{backgroundImage: `url(${Image4})`}}></div>
                        <div className="categories_slider_pic" style={{backgroundImage: `url(${Image5})`}}></div>
                        <div className="categories_slider_pic" style={{backgroundImage: `url(${Image6})`}}></div>
                    </Carousel>
                </div>
                <div style={{ marginBottom: 50 }}>
                    {renderSlider()}
                </div>
            </div>
            <Footer/>
        </>
    )
}
export default memo(HomePage)