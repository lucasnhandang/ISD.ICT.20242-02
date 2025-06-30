# Rush Order Eligibility Service Test

File unit test này kiểm tra tính năng xác định điều kiện hợp lệ cho đơn hàng rush order của hệ thống AIMS.

## Các Test Cases

### 1. Kiểm tra thành công

- **checkEligibility_Success_WithRushAndNormalItems**: Kiểm tra khi có cả sản phẩm rush và normal, địa chỉ Hanoi
- **checkEligibility_Success_WithOnlyRushItems**: Kiểm tra khi chỉ có sản phẩm rush, địa chỉ Hanoi  
- **checkEligibility_VerifyResponseFields**: Kiểm tra tính đúng đắn của các trường trong response

### 2. Kiểm tra exception

- **checkEligibility_AddressNotEligible_ThrowsException**: Kiểm tra exception khi địa chỉ không phải Hanoi
- **checkEligibility_NoEligibleProducts_ThrowsException**: Kiểm tra exception khi không có sản phẩm nào hỗ trợ rush
- **checkEligibility_NullRushItems_ThrowsException**: Kiểm tra exception khi rush items null
- **checkEligibility_EmptyCart_NoEligibleProducts**: Kiểm tra exception khi giỏ hàng trống

### 3. Kiểm tra case-insensitive

- **checkEligibility_AddressHaNoiCaseInsensitive_Success**: Kiểm tra với "Ha Noi" (có dấu cách)
- **checkEligibility_AddressHanoiUppercase_Success**: Kiểm tra với "HANOI" (viết hoa)

## Dependencies được Mock

- **CartSplitter**: Mock việc tách sản phẩm rush và normal
- **ProductRepository**: Mock repository sản phẩm
- **MessageService**: Mock service lấy message lỗi

## Kết quả chạy test

✅ Tất cả 9 test cases đều PASS
✅ Build SUCCESS
✅ Time elapsed: 1.134s 