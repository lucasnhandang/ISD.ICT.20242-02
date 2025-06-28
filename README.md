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
├── Analysis/                           # Analysis & design documentation
│   ├── Business Process/
│   ├── Communication Diagram + Analysis Class Diagram/
│   ├── Detailed Design/
│   ├── Enhanced Detailed Design/
│   └── Sequence Diagram/
├── Programming/
│   ├── frontend/                       # Frontend source code (React)
│   │   ├── public/
│   │   ├── src/
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   ├── services/
│   │   │   ├── styles/
│   │   │   ├── js/
│   │   │   └── App.jsx
│   │   └── ...
│   ├── src/                            # Backend source code (Java Spring Boot)
│   │   ├── main/
│   │   │   └── java/com/hustict/aims/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       ├── utils/
│   │   │       └── Application.java    # Run this to start the server
│   │   └── test/
│   │       └── java/com/hustict/aims/
│   │           ├── controller/
│   │           ├── service/
│   │           └── ...
│   └── ...
├── Requirements/                       # Project requirements and reports
│   ├── G2-Cohesion+SRP.pdf
│   ├── G2-SOLID_Evaluate.pdf
│   ├── G2-SRS.docx
│   ├── G2-SDD.docx
│   ├── G2-UseCaseSpecs.docx
│   └── G2-TestPlan.xlsx
├── README.md
└── aims.code-workspace                 # Run this in VSCode/Eclipse/...
```

## How to run AIMS?
This project is a **Web Application** that combines **Spring Boot (Java)** for backend and **React (JavaScript)** for frontend.

### Tech Stack
- **Backend**: Java Spring Boot
- **Frontend**: React
- **Database**: PostgreSQL deployed on Supabase
- **Architecture**: Client-Server Web Application

### Requirements
- **Java 21+** (for backend)
- **Maven** (for backend build)
- **Node.js 16+** (for frontend)
- **npm** (Node package manager)

### Setup & Run

#### 1. Clone the repository
```bash
git clone https://github.com/lucasnhandang/ISD.ICT.20242-02.git
cd ISD.ICT.20242-02/Coding
```

#### 2. Database setup
- Import the SQL scripts in `docs/G2-AIMS-CreateDB.sql` and `docs/G2-AIMS-DataForDB.sql` into your PostgreSQL instance.
- Update database connection info in `src/main/resources/application.properties` if needed.

#### 3. Run Backend (Spring Boot)
```bash
cd src
mvn spring-boot:run
```
- The backend API will be available at: http://localhost:8080

#### 4. Run Frontend (React)
```bash
cd frontend
npm install
npm start
```
- The frontend will be available at: http://localhost:3000

#### 5. Access the Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api/*

---

## Ngrok Configuration for Payment Gateway

### What is Ngrok?
Ngrok is a tool that creates secure tunnels to expose your local server to the internet. In this project, ngrok is used to expose the local Spring Boot server so that VNPay payment gateway can send callbacks to our application.

### Why Ngrok is Needed?
- VNPay payment gateway needs to send payment callbacks to our server
- These callbacks include payment success/failure notifications
- Since our server runs locally, we need ngrok to make it accessible from the internet

### Current Ngrok Configuration
The project is currently configured with the following ngrok URL:
```
https://89cf-42-114-34-135.ngrok-free.app
```

This URL is used in the following endpoints:
- **Return URL**: `${app.ngrok.url}/api/payment/vnpay-return`
- **IPN URL**: `${app.ngrok.url}/api/payment/vnpay-ipn`

### How to Set Up Ngrok

#### 1. Install Ngrok
```bash
# Download from https://ngrok.com/download
```

#### 2. Sign Up for Free Account
- Go to https://ngrok.com/
- Create a free account
- Get your authtoken from the dashboard

#### 3. Authenticate Ngrok
```bash
ngrok config add-authtoken YOUR_AUTH_TOKEN
```

#### 4. Start Ngrok Tunnel
```bash
# Expose port 8080 (Spring Boot server)
ngrok http 8080
```

After running this command, ngrok will provide you with a public URL like:
```
Forwarding    https://abc123-def456.ngrok-free.app -> http://localhost:8080
```

### How to Update Ngrok URL for Other Developers

#### Step 1: Get Your Ngrok URL
When you start ngrok, you'll get a URL like:
```
https://your-unique-id.ngrok-free.app
```

#### Step 2: Update Configuration
Edit the file: `Programming/src/main/resources/application.properties`

Find this line:
```properties
app.ngrok.url=https://89cf-42-114-34-135.ngrok-free.app
```

Replace it with your ngrok URL:
```properties
app.ngrok.url=https://your-unique-id.ngrok-free.app
```

#### Step 3: Restart the Application
After updating the configuration, restart your Spring Boot application:
```bash
# Stop the current server (Ctrl+C)
# Then restart
mvn spring-boot:run
```

### Important Notes

#### 1. Ngrok URL Changes
- **Free ngrok accounts**: URL changes every time you restart ngrok
- **Paid ngrok accounts**: Can have fixed subdomains
- You must update the configuration file each time the URL changes

#### 2. Security Considerations
- Ngrok exposes your local server to the internet
- Only use it for development/testing
- Never use ngrok in production environments
- Be careful with sensitive data when using ngrok

#### 3. VNPay Configuration
- VNPay sandbox environment accepts ngrok URLs
- Make sure your ngrok URL is accessible (test by visiting it in browser)
- The URL must be HTTPS (ngrok provides this automatically)

#### 4. Troubleshooting
If payment callbacks are not working:
1. Check if ngrok is running: `ngrok http 8080`
2. Verify the URL in `application.properties` matches your ngrok URL
3. Test the URL in browser: `https://your-ngrok-url.ngrok-free.app/api/health`
4. Check ngrok dashboard for any errors
5. Restart both ngrok and Spring Boot application

### Alternative Solutions for Production
For production deployment, consider:
- **Cloud hosting**: Deploy to AWS, Azure, or Google Cloud
- **Domain name**: Use a proper domain name instead of ngrok
- **Load balancer**: Use services like Cloudflare or AWS ALB
- **VPN**: Set up VPN for secure communication

---

## Report Content
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
