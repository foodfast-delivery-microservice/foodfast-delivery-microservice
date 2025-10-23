/* Tạo các database mà các service cần.
(Lưu ý: tên phải khớp chính xác với docker-compose.yml)
*/
CREATE DATABASE IF NOT EXISTS productservice;
CREATE DATABASE IF NOT EXISTS orderservice;
CREATE DATABASE IF NOT EXISTS userservice;

/* Tạo user 'user' với mật khẩu 'password' cho user-service.
(Dùng '@'%' để cho phép user này kết nối từ bất kỳ container nào trong mạng Docker)
*/
CREATE USER 'user'@'%' IDENTIFIED BY 'password';

/* Cấp toàn bộ quyền cho user 'user' trên database 'userservice' */
GRANT ALL PRIVILEGES ON userservice.* TO 'user'@'%';

/* (Bạn không cần tạo user cho 'product' và 'order' vì chúng đang dùng 'root',
mặc dù dùng user riêng cho mỗi service sẽ bảo mật hơn)
*/
FLUSH PRIVILEGES;