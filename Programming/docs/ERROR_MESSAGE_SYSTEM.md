# Hệ thống Quản lý Thông báo Lỗi

## Tổng quan

Hệ thống này đã được thiết kế để tách biệt các thông báo lỗi ra khỏi code logic, giúp dễ dàng quản lý và bảo trì. Thay vì hardcode các thông báo lỗi trong code, chúng ta sử dụng:

1. **MessageService**: Quản lý tất cả các thông báo lỗi và thành công
2. **Custom Exceptions**: Xử lý các lỗi cụ thể với thông tin chi tiết
3. **GlobalExceptionHandler**: Xử lý tất cả exceptions một cách nhất quán

## Cấu trúc

### 1. MessageService

File: `src/main/java/com/hustict/aims/service/MessageService.java`

Service này chứa tất cả các thông báo lỗi và thành công được sử dụng trong hệ thống:

```java
@Service
public class MessageService {
    // Rush Order Messages
    public static final String RUSH_ORDER_ADDRESS_ERROR = "Rush order is only available for delivery in Hanoi.";
    public static final String RUSH_ORDER_NO_ELIGIBLE_PRODUCTS = "There are no products eligible for rush order.";
    public static final String RUSH_ORDER_SUCCESS = "Rush order placed successfully";
    
    // Order Messages
    public static final String ORDER_SUCCESS = "Order placed successfully";
    public static final String ORDER_FAILED = "Order placement failed";
    
    // Validation Messages
    public static final String VALIDATION_ERROR = "Validation failed";
    public static final String INVALID_INPUT = "Invalid input provided";
    
    // Authentication Messages
    public static final String AUTH_FAILED = "Authentication failed";
    public static final String TOKEN_INVALID = "Token is invalid";
    public static final String TOKEN_EXPIRED = "Token expired";
    
    // ... và nhiều thông báo khác
}
```

### 2. Custom Exceptions

#### RushOrderException
File: `src/main/java/com/hustict/aims/exception/RushOrderException.java`

Exception này được sử dụng cho các lỗi liên quan đến rush order:

```java
public class RushOrderException extends RuntimeException {
    private final String errorCode;
    private final LocalDateTime expectedDateTime;

    public RushOrderException(String message, String errorCode, LocalDateTime expectedDateTime) {
        super(message);
        this.errorCode = errorCode;
        this.expectedDateTime = expectedDateTime;
    }
}
```

### 3. GlobalExceptionHandler

File: `src/main/java/com/hustict/aims/exception/GlobalExceptionHandler.java`

Handler này xử lý tất cả exceptions và trả về response nhất quán:

```java
@ExceptionHandler(RushOrderException.class)
public ResponseEntity<Map<String, Object>> handleRushOrderException(RushOrderException e) {
    Map<String, Object> response = new HashMap<>();
    response.put("error", "RUSH_ORDER_ERROR");
    response.put("errorCode", e.getErrorCode());
    response.put("message", e.getMessage());
    response.put("expectedDateTime", e.getExpectedDateTime());
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```

## Cách sử dụng

### 1. Trong Service

Thay vì hardcode thông báo lỗi:

```java
// ❌ Cách cũ
if (!rushOrderValidator.validateAddress(deliveryInfo)) {
    return new RushOrderResponseDTO(null, "FAILED", "Rush order is only available for delivery in Hanoi.", deliveryInfo.getExpectedDateTime());
}
```

Sử dụng MessageService và Exception:

```java
// ✅ Cách mới
@Autowired
private MessageService messageService;

if (!rushOrderValidator.validateAddress(deliveryInfo)) {
    throw new RushOrderException(
        messageService.getRushOrderAddressError(), 
        "ADDRESS_NOT_ELIGIBLE", 
        deliveryInfo.getExpectedDateTime()
    );
}
```

### 2. Trong Controller

Controller không cần xử lý exception thủ công, GlobalExceptionHandler sẽ tự động xử lý:

```java
@PostMapping("/place-rush-order")
public ResponseEntity<RushOrderResponseDTO> placeRushOrder(@RequestBody RushOrderRequestDTO request) {
    // Không cần try-catch, exception sẽ được xử lý tự động
    RushOrderResponseDTO response = rushOrderService.processRushOrder(request);
    return ResponseEntity.ok(response);
}
```

### 3. Thêm thông báo mới

Để thêm thông báo mới:

1. Thêm constant vào `MessageService`:
```java
public static final String NEW_ERROR_MESSAGE = "Your new error message";
```

2. Thêm getter method:
```java
public String getNewErrorMessage() {
    return NEW_ERROR_MESSAGE;
}
```

3. Sử dụng trong service:
```java
throw new SomeException(messageService.getNewErrorMessage());
```

## Lợi ích

1. **Tính nhất quán**: Tất cả thông báo lỗi được quản lý tập trung
2. **Dễ bảo trì**: Thay đổi thông báo chỉ cần sửa ở một nơi
3. **Đa ngôn ngữ**: Dễ dàng hỗ trợ nhiều ngôn ngữ trong tương lai
4. **Tách biệt concerns**: Logic xử lý tách biệt với thông báo
5. **Exception handling nhất quán**: Tất cả exceptions được xử lý theo cùng một format

## Ví dụ Response

### Success Response
```json
{
    "invoice": {...},
    "status": "SUCCESS",
    "message": "Rush order placed successfully",
    "expectedTime": "2024-01-15T10:30:00"
}
```

### Error Response
```json
{
    "error": "RUSH_ORDER_ERROR",
    "errorCode": "ADDRESS_NOT_ELIGIBLE",
    "message": "Rush order is only available for delivery in Hanoi.",
    "expectedDateTime": "2024-01-15T10:30:00",
    "timestamp": "2024-01-15T09:15:30",
    "status": 400
}
```

## Migration Guide

Để chuyển đổi từ hardcode messages sang hệ thống mới:

1. **Bước 1**: Thêm MessageService vào constructor của service
2. **Bước 2**: Thay thế hardcode messages bằng messageService.getXXX()
3. **Bước 3**: Thay thế return error response bằng throw exception
4. **Bước 4**: Xóa try-catch blocks không cần thiết trong controller

## Best Practices

1. **Sử dụng constants**: Luôn sử dụng constants thay vì hardcode strings
2. **Descriptive error codes**: Sử dụng error codes có ý nghĩa
3. **Consistent naming**: Đặt tên methods theo pattern getXxxError(), getXxxSuccess()
4. **Documentation**: Cập nhật tài liệu khi thêm thông báo mới
5. **Testing**: Test cả success và error scenarios 