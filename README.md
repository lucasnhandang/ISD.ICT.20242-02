# ISD.ICT.20242-02
Capstone Project: AIMS Software for ITSS Software Development course in 20242 semester.


## Member
| Student ID | Name           | Role        |
|------------|----------------|-------------|
| 20225990   | Dang Van Nhan  | Team Leader |
| 20225979   | Ha Viet Khanh  | Member      |
| 20226003   | Ho Bao Thu     | Member      |
| 20226061   | Tran Cao Phong | Member      |
| 20225991   | Nguyen Lan Nhi | Member      |

## Project Structure
This repository is organized as follows:

```
ISD.ICT.20242-02/
├── .gitignore
├── README.md                        
├── AIMS/                           # Source code directory
├── Analysis/                        
│   ├── Business Process/ 
│   │           
│   ├── Communication Diagram + Analysis Class Diagram/
│   │   └── ... (each member will have their own folder with their assigned UC)
│   │           
│   ├── Detailed Design/            
│   │   ├── Data Modeling/          # Database Design for AIMS
│   │   ├── Interface Design/       # Subsystem + Interface for "Pay order" UC
│   │   └── Use Cases/              # Detailed diagrams for each use case
│   │       │ ...
│   │       └── General Combined Package.png # Class Diagram of AIMS
│   │           
│   └──Sequence Diagram/           # Sequence diagrams for each use case
│   │       └── ... (each member will have their own folder with their assigned UC)
│   │           
└── Requirements/                   
    ├── Group2-SRS.docx             # Software Requirements Specification
    └── Group2-SDD.docx             # Software Design Description (will be updated)
```

## Report content
This section outlines the tasks assigned to each team member on a weekly basis.
<details>
  <summary> W3: 24/02/2025 - 02/03/2025 </summary>

| **Name**          | **Assigned Tasks**                            | **Review Use Case**                                                                          | **Requirements**          |
|------------------|-----------------------------------------------|----------------------------------------------------------------------------------------------|---------------------------|
| **Dang Van Nhan** | Add/Update product (Product Manager)          | View product details (Customer/Product Manager) & Cancel order (Customer/VNPay)              | UC Diagram                |
| **Ha Viet Khanh** | Pay order (Customer/VNPay) & Create user (Administrator)                  | Place order (Customer) & Reject order (Product Manager)                                      | Introduction              |
| **Ho Bao Thu**    | Place order (Customer) & Reject order (Product Manager)         | Add/Update product (Product Manager)                                                         | UC Diagram + Business Process |
| **Tran Cao Phong**| View product details (Customer/Product Manager) & Cancel order (Customer/VNPay) | Place rush order (Customer) & Approve order (Product Manager) | Performance & Supportability |
| **Nguyen Lan Nhi**| Place rush order (Customer) & Approve order (Product Manager)               | Pay order (Customer/VNPay) & Create user (Administrator)                                     | Reliability & Usability   |

</details>

<details>
  <summary> W4: 03/03/2025 - 09/03/2025 </summary>

| **Name**          | **Assigned Tasks: Draw Sequence Diagram for UC**                                | 
|------------------|---------------------------------------------------------------------------------|
| **Dang Van Nhan** | Add/Update product (Product Manager)                                            | 
| **Ha Viet Khanh** | Pay order (Customer/VNPay) & Create user (Administrator)                        |
| **Ho Bao Thu**    | Place order (Customer) & Reject order (Product Manager)                         | 
| **Tran Cao Phong**| View product details (Customer/Product Manager) & Cancel order (Customer/VNPay) |
| **Nguyen Lan Nhi**| Place rush order (Customer) & Approve order (Product Manager)                   |

</details>

<details>
  <summary> W5: 10/03/2025 - 16/03/2025 </summary>

| **Name**          | **Communication Diagram + Analysis Class Diagram for UC**                       |
|------------------|---------------------------------------------------------------------------------|
| **Dang Van Nhan** | Add/Update product (Product Manager)                                            |     
| **Ha Viet Khanh** | Pay order (Customer/VNPay) & Create user (Administrator)                        |  
| **Ho Bao Thu**    | Place order (Customer) & Reject order (Product Manager)                         |  
| **Tran Cao Phong**| View product details (Customer/Product Manager) & Cancel order (Customer/VNPay) |   
| **Nguyen Lan Nhi**| Place rush order (Customer) & Approve order (Product Manager)                   |  

</details>

<details>
  <summary> W6: 17/03/2025 - 23/03/2025 </summary>

| **Name**          | **Detailed Class Diagram for UC**                                               |
|------------------|---------------------------------------------------------------------------------|
| **Dang Van Nhan** | Add/Update product (Product Manager)                                            |     
| **Ha Viet Khanh** | Pay order (Customer/VNPay) & Create user (Administrator)                        |  
| **Ho Bao Thu**    | Place order (Customer) & Reject order (Product Manager)                         |  
| **Tran Cao Phong**| View product details (Customer/Product Manager) & Cancel order (Customer/VNPay) |   
| **Nguyen Lan Nhi**| Place rush order (Customer) & Approve order (Product Manager)                   |  

</details>

<details>
  <summary> W7: 31/03/2025 - 06/04/2025 </summary>

| **Name**          | **Task**                                                                        |
|------------------|---------------------------------------------------------------------------------|
| **Dang Van Nhan** | Class diagram for interface & subsystem                                            |     
| **Ha Viet Khanh** | Class design for all elements in the subsystem (see section 2. in the week 6 & Class Design sheet)                        |  
| **Ho Bao Thu**    | Class diagram for subsystem                         |  
| **Tran Cao Phong**| Operation design for all operations in the interface (Table 2, Parameter, Exception) |   
| **Nguyen Lan Nhi**| Interaction diagram (recommend sequence diagram) for each operation in the interface                   |  

</details>

<details>
  <summary>W8: 07/04/2025 - 13/04/2025</summary>

| **Student Name**   | **Task**                                 |
|--------------------|------------------------------------------|
| **Ha Viet Khanh**          | ERD for AIMS                             |
| **Ho Bao Thu**            | DB Schema Script                         |
| **Dang Van Nhan**           | DB Detail Design (File report)           |
| **Tran Cao Phong**          | Relational Schema for AIMS               |
| **Nguyen Lan Nhi**            | Front-end for the UC View Product Detail |

</details>