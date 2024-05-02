1: Tải và cài đặt SQL Server

Truy cập liên kết https://www.microsoft.com/en-us/sql-server/sql-server-downloads để tải SQL Server
Chạy file cài đặt và làm theo các bước hướng dẫn để cài đặt SQL Server

B2: Tạo một database có tên bookstore

Mở SQL Server Management Studio (SSMS)
Kết nối với SQL Server bằng cách nhập tên máy chủ (Server name) và xác thực (Authentication)
Trong Object Explorer, click chuột phải vào "Databases" và chọn "New Database..."
Đặt tên cho database là "bookstore" và nhấn OK để tạo

B3: Thay đổi username và password trong file .yml (giả sử bạn đang sử dụng SQL Server Authentication)

Vào file application.yml trong thư mục BookStoreBackEnd
Tìm phần cấu hình database và thay đổi các giá trị tương ứng:
spring:
datasource:
url: jdbc:sqlserver://localhost;databaseName=bookstore
username: your_sql_server_username
password: your_sql_server_password

Lưu ý: Nếu bạn sử dụng Windows Authentication, bạn không cần cung cấp username và password trong file cấu hình. Thay vào
đó, bạn có thể sử dụng integratedSecurity=true trong chuỗi kết nối URL.
Sau khi hoàn tất các bước trên, bạn đã sẵn sàng để sử dụng SQL Server cho ứng dụng của mình.