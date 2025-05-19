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
├── AIMS/                             
├── Analysis/                         # Analysis and design documentation
│   ├── Business Process/
│   ├── Communication Diagram + Analysis Class Diagram/
│   │   └── ... (each member has a folder for their assigned use cases)
│   ├── Detailed Design/
│   │   ├── Data Modeling/           
│   │   ├── Interface Design/        
│   │   └── Use Cases/                          # Detailed use case diagrams
│   │       └── General Combined Package.png    # Class Diagram for AIMS of Group 2
│   └── Sequence Diagram/                       # Sequence diagrams for each use case
│       └── ... (each member has a folder for their assigned use cases)
├── Programming/              
│   ├── pom.xml                      # Maven configuration (Java backend)
│   ├── main.js                      # Entry point (JavaScript frontend)
│   ├── package.json                 # Node.js project configuration
│   ├── package-lock.json
│   ├── frontend/                    # Source code for React
│   ├── node_modules/                # Frontend dependencies
│   ├── src/
│   │   └── java/
│   │       └── com/
│   │           └── hustict/
│   │               └── aims/
│   │                   ├── controller/   
│   │                   ├── model/        
│   │                   ├── repository/   
│   │                   ├── service/      
│   │                   ├── utils/        
│   │                   └── Application.java 
│   └── target/                     # Compiled output from Maven
├── Requirements/
│   ├── G2-Cohesion+SRP.pdf     # Submission for W11's task
│   ├── G2-SRS.docx             # Software Requirements Specification
│   └── G2-SDD.docx             # Software Design Description
└── Testing/                    # Testing files, unit tests, etc.

```

## How to run AIMS?
This project combines **Spring Boot (Java)** for backend and **Electron + React (JavaScript)** for frontend.

### Requirements

- **Java 17+**
- **Node.js 16+**
- **Maven** (to build backend)

### Run Application (Development Mode)

```bash
git clone https://github.com/lucasnhandang/ISD.ICT.20242-02
cd ISD.ICT.20242-02/Programming

npm install # Download Electron dependencies

mvn clean package # Build the backend .jar file
# After building, you should see the file: 
# target/aims-0.0.1-SNAPSHOT.jar

npm start # Start the app
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

<details>
  <summary>W9: 13/04/2025 - 20/04/2025</summary>

| **Student Name**   | **Task (Continue + Coding)**             |
|--------------------|------------------------------------------|
| **Ha Viet Khanh**          | ERD for AIMS                             |
| **Ho Bao Thu**            | DB Schema Script                         |
| **Dang Van Nhan**           | DB Detail Design (File report)           |
| **Tran Cao Phong**          | Relational Schema for AIMS               |
| **Nguyen Lan Nhi**            | Front-end for the UC View Product Detail |

</details>

<details>
  <summary>W10: 20/04/2025 - 04/05/2025</summary>

| **Student Name**   | **Task (Unit Test + Test Plan)** |
|--------------------|----------------------------------|
| **Ha Viet Khanh**          | Create users                     |
| **Ho Bao Thu**            | Pay order                        |
| **Dang Van Nhan**           | Create products                  |
| **Tran Cao Phong**          | View product details             |
| **Nguyen Lan Nhi**            | Place rush order                 |

</details>

<details>
  <summary>W11: 04/05/2025 - 11/05/2025</summary>

| **Student Name**   | **Task**                    |
|--------------------|-----------------------------|
| **Ha Viet Khanh**          | Evaluate Cohesion + Coupling |
| **Ho Bao Thu**            | Evaluate Cohesion + Coupling                   |
| **Dang Van Nhan**           | Evaluate Cohesion + Coupling             |
| **Tran Cao Phong**          | Evaluate Cohesion + Coupling        |
| **Nguyen Lan Nhi**            | Evaluate Cohesion + Coupling            |

</details>

<details>
  <summary>W12: 11/05/2025 - 18/05/2025</summary>

| **Student Name**   | **Task**                                         |
|--------------------|--------------------------------------------------|
| **Ha Viet Khanh**          | Evaluate Cohesion + Coupling with SOLID + Coding |
| **Ho Bao Thu**            | Evaluate Cohesion + Coupling with SOLID + Coding |
| **Dang Van Nhan**           | Evaluate Cohesion + Coupling with SOLID + Coding                    |
| **Tran Cao Phong**          | Evaluate Cohesion + Coupling with SOLID + Coding                     |
| **Nguyen Lan Nhi**            | Evaluate Cohesion + Coupling with SOLID + Coding                    |

</details>