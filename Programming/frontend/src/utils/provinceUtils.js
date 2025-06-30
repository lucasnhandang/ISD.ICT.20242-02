// Province name mapping utility
// Mapping: displayName => backendValue
const provinceMapping = {
  'Ha Noi': 'Hanoi',
  'Ho Chi Minh City': 'Hochiminh',
  'Hai Phong': 'Hai Phong',
  'Da Nang': 'Da Nang',
  'Can Tho': 'Can Tho',
  'Binh Duong': 'Binh Duong',
  'Dong Nai': 'Dong Nai',
  'Hai Duong': 'Hai Duong',
  'Thanh Hoa': 'Thanh Hoa',
  'Nghe An': 'Nghe An',
  'Thua Thien Hue': 'Thua Thien Hue',
  'Quang Ninh': 'Quang Ninh',
  'Bac Ninh': 'Bac Ninh',
  'Quang Nam': 'Quang Nam',
  'Lam Dong': 'Lam Dong',
  'Nam Dinh': 'Nam Dinh',
  'Thai Binh': 'Thai Binh',
  'Phu Tho': 'Phu Tho',
  'Bac Giang': 'Bac Giang',
  'Hung Yen': 'Hung Yen',
  'Ha Nam': 'Ha Nam',
  'Vinh Phuc': 'Vinh Phuc',
  'Ninh Binh': 'Ninh Binh',
  'Quang Binh': 'Quang Binh',
  'Quang Tri': 'Quang Tri',
  'Binh Dinh': 'Binh Dinh',
  'Binh Thuan': 'Binh Thuan',
  'Khanh Hoa': 'Khanh Hoa',
  'Ba Ria - Vung Tau': 'Ba Ria - Vung Tau',
  'Long An': 'Long An',
  'Kien Giang': 'Kien Giang',
  'Dak Lak': 'Dak Lak',
  'Ca Mau': 'Ca Mau',
  'Binh Phuoc': 'Binh Phuoc',
  'Bac Kan': 'Bac Kan',
  'Lao Cai': 'Lao Cai',
  'Lang Son': 'Lang Son',
  'Tuyen Quang': 'Tuyen Quang',
  'Yen Bai': 'Yen Bai',
  'Dien Bien': 'Dien Bien',
  'Son La': 'Son La',
  'Hoa Binh': 'Hoa Binh',
  'Lai Chau': 'Lai Chau',
  'Ha Giang': 'Ha Giang',
  'Cao Bang': 'Cao Bang',
  'Kon Tum': 'Kon Tum',
  'Gia Lai': 'Gia Lai',
  'Dak Nong': 'Dak Nong',
  'Soc Trang': 'Soc Trang',
  'Tra Vinh': 'Tra Vinh',
  'Ben Tre': 'Ben Tre',
  'Vinh Long': 'Vinh Long',
  'An Giang': 'An Giang',
  'Tien Giang': 'Tien Giang',
  'Hau Giang': 'Hau Giang',
  'Ninh Thuan': 'Ninh Thuan',
  'Phu Yen': 'Phu Yen',
  'Quang Ngai': 'Quang Ngai',
  'Bac Lieu': 'Bac Lieu'
};

// Get display names for dropdown
export const provinces = Object.keys(provinceMapping);

// Helper function to convert display name to backend value
export const getBackendValue = (displayName) => {
  return provinceMapping[displayName] || displayName;
};

// Helper function to convert backend value to display name
export const getDisplayName = (backendValue) => {
  for (const [displayName, value] of Object.entries(provinceMapping)) {
    if (value === backendValue) {
      return displayName;
    }
  }
  return backendValue; // fallback
};

// Get all province mapping for reference
export const getProvinceMapping = () => {
  return { ...provinceMapping };
}; 