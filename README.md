# MÔ TẢ VỀ DỰ ÁN

Website bao gồm 3 loại người dùng tương tác: người dùng không có tài khoản (guest), người dùng có tài khoản (customer), và người quản trị hệ thống (admin).

### Chức năng của Người dùng không có tài khoản (guest):
- Xem danh sách sản phẩm từ cơ sở dữ liệu.
- Xem chi tiết của từng sản phẩm.
- Chọn mua sản phẩm và thêm vào giỏ hàng.
- Xem giỏ hàng, chỉnh sửa số lượng sản phẩm.
- Đăng ký tài khoản với thông tin cần thiết.

### Chức năng của Người dùng có tài khoản (customer):
- Tất cả các chức năng của khách.
- Thực hiện thanh toán và đặt hàng.

### Chức năng của Quản trị viên hệ thống (admin):
- Tất cả các chức năng của khách hàng.
- Quản lý hệ thống.

## Phần Back-End:
- Tìm kiếm thông tin về sản phẩm/loại sản phẩm, tài khoản người dùng, các đơn đặt sản phẩm.
- Quản lý thông tin sản phẩm/loại sản phẩm:
  - Xem danh sách sản phẩm/loại sản phẩm.
  - Xem chi tiết từng sản phẩm/loại sản phẩm.
  - Xóa sản phẩm/loại sản phẩm trong trường hợp sản phẩm chưa có trong đơn hàng nào hoặc loại sản phẩm chưa có sản phẩm nào.
  - Thêm mới, cập nhật thông tin sản phẩm/loại sản phẩm.
- Quản lý thông tin tài khoản người dùng:
  - Xem danh sách các tài khoản người dùng đã đăng ký.
  - Xem chi tiết từng tài khoản người dùng, không xem được mật khẩu của người dùng.
  - Xóa tài khoản người dùng nếu người dùng chưa thực hiện đặt hàng online lần nào.
  - Cập nhật thông tin tài khoản người dùng.
- Quản lý thông tin đơn hàng trực tuyến:
  - Xem danh sách các đơn hàng (sắp xếp theo ngày mua).
  - Xem chi tiết đơn hàng.
  - Cập nhật số lượng của mặt hàng trong đơn hàng trực tuyến.
- Lưu ý cho các chức năng quản lý thông tin:
  - Ràng buộc khi xóa dữ liệu.
  - Trường hợp thêm hay cập nhật dữ liệu có thể kiểm tra phía Client bằng JavaScript/jQuery hoặc kiểm tra bằng Model phía Server, không sử dụng Functions/Check constraints/Stored Procedures trong hệ quản trị CSDL.

### Tài khoản mặc định:
- **Admin**: Email: tthanhphuc753@gmail.com, Password: 123456
- **Client**: Email: tthanhphuc752@gmail.com, Password: 123456
