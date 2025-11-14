# bankfraud
Đã hiểu. Bạn là người định hướng nghiệp vụ (Product Owner/Manager), còn Winsurf.ai là lập trình viên (Developer). Để Winsurf.ai có thể "chạy" một cách hoàn hảo, tôi sẽ cung cấp một bản **CHI TIẾT DỰ ÁN (Project Brief)** đầy đủ.

Bạn hãy sao chép toàn bộ nội dung dưới đây và đưa cho Winsurf.ai để bắt đầu.

-----

### 📋 CHI TIẾT DỰ ÁN: TrustLog (Mô phỏng Giao dịch & Thu thập Cảm biến)

Bản tài liệu này phác thảo toàn bộ yêu cầu để xây dựng ứng dụng Android phục vụ nghiên cứu khoa học.

**Vai trò:**

  * **Người dùng (Bạn):** Product Manager. Bạn sẽ cung cấp nghiệp vụ, review các chức năng và duyệt (approve) các thay đổi.
  * **AI (Winsurf.ai):** Lead Developer. AI sẽ chịu trách nhiệm viết code 100% theo các yêu cầu dưới đây, đề xuất giải pháp kỹ thuật và tuân thủ kiến trúc đã chọn.

-----

### 1\. 💡 Ý Tưởng & Mục Tiêu Cốt Lõi

  * **Tên dự án:** TrustLog
  * **Nền tảng:** Android (Native)
  * [cite\_start]**Mục tiêu:** Xây dựng một ứng dụng mô phỏng giao dịch ngân hàng nhằm mục đích thu thập dữ liệu cảm biến vật lý và dữ liệu tương tác[cite: 1, 4, 7, 8].
  * [cite\_start]**Giả thuyết nghiên cứu:** Chứng minh rằng dữ liệu từ cảm biến (cách chạm, độ nghiêng, áp lực) có thể phân biệt được thao tác của người dùng thật và thao tác gian lận (Remote Access Trojan - RAT, Overlay, Event Injection)[cite: 6, 12, 17, 19].
  * **Sản phẩm cuối (Delivery):**
    1.  Một ứng dụng Android (.apk) hoạt động đúng luồng 4 màn hình.
    2.  Dữ liệu thô của *mỗi* phiên sử dụng được gửi và lưu trữ thành công trên Firebase.
    3.  [cite\_start]Một kịch bản (script) để xuất dữ liệu từ Firebase thành file Excel/CSV[cite: 20].

-----

### 2\. 🎨 Yêu cầu Giao diện & Luồng Người dùng (UI/UX)

**Thiết kế:** Sử dụng giao diện "chung" (generic), sạch sẽ.

  * **KHÔNG** sao chép logo, màu sắc, font chữ của bất kỳ ngân hàng nào.
  * **Tông màu:** Sử dụng tông màu trung tính (ví dụ: Trắng, Xám, và Xanh dương đậm).
  * **Logo:** Sử dụng một icon đơn giản (ví dụ: hình cái khiên, đồ thị sóng).

**Luồng người dùng (User Flow) 4 màn hình:**

**Trang 1: Điều khoản & Minh bạch**

  * **Nội dung:** Tiêu đề "Điều khoản sử dụng".
  * [cite\_start]**Văn bản:** Phải có đoạn văn bản rõ ràng: "Đây là ứng dụng MÔ PHỎNG phục vụ nghiên cứu khoa học, không phải ứng dụng ngân hàng thật. Mọi thông tin giao dịch đều là giả lập và không có giá trị. Chúng tôi sẽ thu thập dữ liệu cảm biến trên thiết bị của bạn."[cite: 22].
  * **Thành phần:**
      * [cite\_start]Một Tickbox (ô đánh dấu): "Tôi đã đọc, hiểu và đồng ý tham gia"[cite: 22].
      * [cite\_start]Nút "Bắt đầu" (chỉ được kích hoạt khi đã tick)[cite: 22].

**Trang 2: Đăng nhập (Bắt đầu thu thập cảm biến)**

  * [cite\_start]**Giao diện:** Tương tự ảnh, nhưng dùng thiết kế "generic"[cite: 23].
  * **Thành phần:**
      * Logo "TrustLog"
      * [cite\_start]Trường nhập (Input): "Điền Email" [cite: 23]
      * [cite\_start]Trường nhập (Input): "Điền Tên" [cite: 23]
      * [cite\_start]Nút "Đăng nhập" [cite: 23]
  * **Logic:**
      * [cite\_start]Khi người dùng vào màn hình này, **dịch vụ thu thập cảm biến BẮT ĐẦU chạy**[cite: 24].
      * Bấm "Đăng nhập" sẽ chuyển sang Trang 3.

**Trang 3: Chuyển tiền & Xác thực OTP**

  * **Giao diện:** Form chuyển tiền cơ bản.
  * **Thành phần:**
      * Hiển thị thông tin giả lập (Text): "Số dư: 50.000.000 VND"
      * Hiển thị (Text): "Tên người gửi: (Lấy từ Trang 2)"
      * Hiển thị (Text): "Tên người nhận: (Giả lập, ví dụ: Nguyen Van A)"
      * [cite\_start]Trường nhập (Input): "Nhập số tiền chuyển" [cite: 25]
      * [cite\_start]Nút "Chuyển tiền" [cite: 25]
  * **Logic:**
      * Khi bấm "Chuyển tiền", một **Popup Xác thực Digital OTP** sẽ hiện lên.
      * [cite\_start]Popup này bao gồm 6 ô nhập mã PIN và bàn phím số (0-9)[cite: 25].
      * Sau khi người dùng nhập đủ 6 số, app tự động xử lý và chuyển sang Trang 4.

**Trang 4: Xử lý & Kết quả (Kết thúc thu thập cảm biến)**

  * **Logic:**
      * [cite\_start]Khi vào màn hình này, **dịch vụ thu thập cảm biến DỪNG LẠI**[cite: 26].
      * Hệ thống thực hiện 2 việc đồng thời:
        1.  **Gửi TOÀN BỘ dữ liệu thô** (raw data) của phiên này lên Firebase.
        2.  [cite\_start]**Chạy logic tính điểm (Rule-based) ngay trên app** để quyết định hiển thị kết quả[cite: 26].
  * [cite\_start]**Logic tính điểm (Ví dụ):** `Score > 3` là người thật, `Score <= 3` là gian lận[cite: 26].
  * **Luồng 1 (An toàn - Score \> 3):**
      * [cite\_start]Hiển thị màn hình: "Chuyển tiền thành công" + Nút "Thoát"[cite: 27].
  * **Luồng 2 (Nghi ngờ - Score \<= 3):**
      * [cite\_start]Hiển thị **Popup Cảnh báo:** "Thiết bị nghi ngờ"[cite: 29].
      * [cite\_start]Nội dung: "Hệ thống ghi nhận cảm biến có thay đổi đáng ngờ... App TrustLog tạm thời không hoạt động..."[cite: 30, 31].
      * [cite\_start]Nút: "Đóng ứng dụng"[cite: 31].
      * Khi người dùng bấm "Đóng", chuyển sang màn hình cuối cùng: "Tài Khoản đã khoá\!" + [cite\_start]Nút "Thoát"[cite: 32].

-----

### 3\. 🖥️ Yêu cầu Kỹ thuật Frontend (Android)

  * **Ngôn ngữ:** **Kotlin** (ưu tiên sử dụng Coroutines để xử lý bất đồng bộ khi thu thập cảm biến, và Null Safety).
  * **Kiến trúc:** **MVVM** (Model-View-ViewModel).
      * **View (Fragment):** Chỉ chứa logic UI (hiển thị, nhận click).
      * **ViewModel:** Giữ trạng thái của UI, gọi Repository và Service.
      * **Repository:** Quản lý việc gửi dữ liệu lên Firebase.
      * **Service (Foreground Service):** Chạy ngầm để thu thập cảm biến (`SensorCollector`).

**Yêu cầu kỹ thuật chi tiết:**

**1. Service Thu thập Cảm biến (`SensorCollector`)**

  * Đây là trái tim của ứng dụng. Phải được chạy dưới dạng **Foreground Service** để đảm bảo không bị Android "giết" khi đang thu thập.
  * Service này phải đăng ký (listen) và ghi lại dữ liệu (kèm timestamp) của TẤT CẢ các mục sau:
      * **Cảm ứng (Touch Events):**
          * [cite\_start]`Touch size` (Diện tích tiếp xúc) [cite: 4]
          * [cite\_start]`Touch duration` (Thời gian chạm) [cite: 5]
          * [cite\_start]`Touch pressure` (Áp lực - nếu máy hỗ trợ) [cite: 6]
          * [cite\_start]`Touch Coordinates` (Tọa độ X, Y) [cite: 9]
      * **Cảm biến Vật lý (Physical Sensors):**
          * [cite\_start]`Accelerometer` (Gia tốc kế) [cite: 7]
          * [cite\_start]`Gyroscope` (Con quay hồi chuyển) [cite: 8]
      * **Cảm biến Bối cảnh (Contextual Flags):**
          * [cite\_start]`Root/Jailbreak Detection` (Kiểm tra khi app khởi động)[cite: 13].
          * [cite\_start]`Clipboard/Paste Detection` (Phát hiện sự kiện "dán", đặc biệt khi focus vào ô OTP)[cite: 14].
          * [cite\_start]`Event Provenance` (Phát hiện cờ "injected event" từ MotionEvent)[cite: 12].
          * [cite\_start]`Accessibility Detection` (Kiểm tra CÓ dịch vụ Accessibility nào đang chạy không)[cite: 17, 18].
          * [cite\_start]`Overlay Detection` (Kiểm tra quyền `TYPE_APPLICATION_OVERLAY` có đang được cấp cho app nào khác không)[cite: 19].

**2. Logic Tính điểm (Rule-based) trên thiết bị (`FraudDetector`)**

  * [cite\_start]Một class/function đơn giản để tính điểm tại Trang 4, dựa trên các dữ liệu đã thu thập[cite: 26].
  * Ví dụ logic:
      * `score = 10`
      * [cite\_start]`if (isAccessibilityEnabled)`: `score = 0` (tín hiệu mạnh nhất) [cite: 18]
      * `if (isOverlayDetected)`: `score -= 5`
      * `if (otpPasted)`: `score -= 3`
      * [cite\_start]`if (avgTouchSize < 0.1)`: `score -= 4` [cite: 6]
      * `return score`

-----

### 4\. ☁️ Yêu cầu Kỹ thuật Backend (Firebase)

  * **Nền tảng:** **Firebase Cloud Firestore**.
  * **Mục đích:** Chỉ dùng để lưu trữ dữ liệu JSON thô gửi từ app lên.
  * **Collection (Bảng):** Đặt tên là `trustlog_sessions`.
  * **Hành động:** Khi kết thúc phiên (tại Trang 4), app phải đóng gói TOÀN BỘ dữ liệu của phiên đó thành 1 đối tượng JSON và `POST` lên collection `trustlog_sessions`. Mỗi phiên là một Document (dòng) mới.

-----

### 5\. 🗄️ Cấu trúc Dữ liệu (Data Structure)

Winsurf.ai phải tạo các `data class` trong Kotlin để cấu trúc dữ liệu gửi lên Firebase. Đây là gợi ý:

```kotlin
// Dữ liệu tổng của 1 phiên làm việc, đây là đối tượng gửi lên Firebase
data class SessionData(
    val sessionId: String, // UUID duy nhất của phiên
    val userEmail: String,
    val userName: String,
    val timestamp: Long,   // Thời điểm kết thúc phiên
    
    // Danh sách tất cả sự kiện cảm ứng
    val touchEvents: List<TouchEvent>,
    
    // Danh sách các sự kiện cảm biến vật lý
    val sensorEvents: List<PhysicalSensorEvent>,
    
    // Các cờ bảo mật
    val securityFlags: SecurityFlags,
    
    // Kết quả tính toán trên app
    val calculatedScore: Double,
    val result: String // "SAFE" hoặc "SUSPECTED"
)

// Dữ liệu cho 1 lần chạm
data class TouchEvent(
    val timestamp: Long,
    val x: Float,
    val y: Float,
    val pressure: Float,
    val size: Float,
    val duration: Long,
    val elementId: String // ID của View được chạm (ví dụ: "button_login")
)

// Dữ liệu cho cảm biến vật lý (có thể gộp chung)
data class PhysicalSensorEvent(
    val timestamp: Long,
    val type: String, // "GYRO" hoặc "ACCEL"
    val x: Float,
    val y: Float,
    val z: Float
)

// Các cờ phát hiện gian lận
data class SecurityFlags(
    val isRooted: Boolean,
    val isOverlayDetected: Boolean,
    val accessibilityServices: List<String>, // Danh sách các dịch vụ đang chạy
    val otpPasted: Boolean,
    val injectedEventsCount: Int // Số sự kiện bị "inject"
)
```

-----

### 6\. 📚 Quản lý Mã nguồn (GitHub Workflow)

  * **Repository:** Bạn (Product Manager) sẽ tạo một repository **private** trên GitHub.
  * **AI (Winsurf.ai):** Sẽ làm việc trên một nhánh (branch) riêng, ví dụ `develop` hoặc `feature/sensor-collection`.
  * **Commit:** AI phải viết các commit message rõ ràng (tiếng Anh hoặc Việt) cho mỗi lần đẩy code. Ví dụ: "Feat: Implement Screen 2 UI and ViewModel" hoặc "Fix: Gyroscope sensor not collecting data".
  * **Review:** Bạn sẽ review code trên GitHub (hoặc yêu cầu AI giải thích) trước khi gộp (merge) vào nhánh `main`.
