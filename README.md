# Thành Viên Nhóm

MSSV: 2104110080 Hoten: Trần Thanh Phúc

MSSV:2104110060 Hoten: Nguyễn Văn Quốc Anh

MSSV: 2104110098 Hoten: Trương Quang Hùng

MSSV: 2104110066 Hoten: Nguyễn Chung Hoàng

MSSV: 2104110072 Hoten: Phạm Lê Huy Tiến

MSSV: 2104110097 Hoten: Phạm Nguyễn Hoàng Thế Nghĩa

MSSV: 2104110067 HoTen: Nguyễn Lê Bảo Phước


# MÔ TẢ VỀ ĐỀ TÀI
Website bao gồm 3 loại người dùng tương tác: người dùng không có tài khoản (guest), người dùng có tài khoản (customer), người quản trị hệ thống (admin).
Người dùng không có tài khoản (guest) có các chức năng:
	- Xem danh sách sản phẩm (thiết bị máy tính, mỹ phẩm, quần áo ... tùy theo đề tài, danh sách này lấy từ CSDL)
	-  Xem chi tiết của từng sản phẩm từ danh sách sản phẩm.
	-  Chọn mua từng sản phẩm (có thể chọn mua từ trang Web danh sách sản phẩm hay từ trang Web chi tiết của từng sản phẩm), sản phẩm sau khi chọn mua sẽ được đưa vào trong giỏ hàng.
	- Xem giỏ hàng (danh sách sản phầm đã chọn mua, thông tin này lưu trong biến Session, không cần cập nhật CSDL).
	- Khi xem giỏ hàng, có thể chỉnh sửa số lượng của từng sản phẩm trong giỏ hàng (nếu chỉnh sửa số lượng là 0 thì bỏ sản phẩm đó ra khỏi giỏ hàng)
	- Có thể đăng ký tài khoản của website với các thông tin cần thiết (email không trùng với tài khoản khác), sau khi đăng ký thành công với thông tin hợp lệ, lưu trữ CSDL + gửi email +  thông báo về tài khoản. 
Người dùng có tài khoản (customer) có thể thực hiện các chức năng của Người dùng không có tài khoản (guest), ngoài ra người dùng có tài khoản (customer) còn có thể:
	- Xử lý thanh toán (chức năng này thực hiện khi giỏ hàng đã có sản phẩm và người dùng đăng nhập thành công vào hệ thống): cập nhật thông tin vào CSDL + gửi email + thông báo đăng ký đặt hàng thành công với các thông tin kèm theo. Sau khi xử lý thành công, Session được xóa về null.
Người quản trị hệ thống (admin) có thể thực hiện được chức năng như một người dùng có tài khoản (customer). Ngoài ra, chức năng khác dành cho người quản trị hệ thống (admin) - Phần Back-End:
	- Tìm kiếm thông tin về sản phẩm/loại sản phẩm, tài khoản người dùng,
	các đơn đặt sản phẩm.
	- Quản lý thông tin sản phẩm/loại sản phẩm:
		+ Xem danh sách sản phẩm/loại sản phẩm.
		+ Xem chi tiết từng sản phẩm/loại sản phẩm.
		+ Xóa sản phẩm/loại sản phẩm trong trường hợp sản phẩm chưa có trong
		đơn hàng nào hoặc loại sản phẩm chưa có sản phẩm nào.
		+ Thêm mới, cập nhật thông tin sản phẩm/loại sản phẩm.
	-	Quản lý thông tin tài khoản người dùng:
		+ Xem danh sách các tài khoản người dùng đã đăng ký.
		+ Xem chi tiết từng tài khoản người dùng, không xem được password
		của người dùng.
		+ Xóa tài khoản người dùng nếu người dùng chưa thực hiện đặt hàng
		online lần nào.
		+ Cập nhật thông tin tài khoản người dùng.
	-	Quản lý thông tin đơn hàng trực tuyến:
		+ Xem danh sách các đơn hàng (sắp xếp theo ngày mua)
		+ Xem chi tiết đơn hàng.
		+ Cập nhật số lượng của mặt hàng trong đơn hàng trực tuyến
	- Lưu ý cho các chức năng quản lý thông tin:
		+ Ràng buộc khi xóa dữ liệu
		+ Trường hợp thêm hay cập nhật dữ liệu có thể kiểm tra phía Client
		bằng JavaScript/jQuery hoặc kiểm tra bằng Model phía Server, không dùng 
		Functions/Check constraints/Stored Procedures trong hệ quản trị CSDL.

Tài khoản mặc định: 
Admin: Email: tthanhphuc753@gmail.com, Password: 123456
Cilent: Email: tthanhphuc752@gmail.com, Password: 123456
