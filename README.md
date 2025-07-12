REST-ACMEMedical - Group 8
CST8277 Group Project - Jakarta EE REST Application

This is our team project for CST8277. It uses Jakarta EE 10 (JPA, JAX-RS, Security) on Payara 6, with a MySQL database. It implements full CRUD REST APIs for 7 entities, secured endpoints with ADMIN_ROLE / USER_ROLE, database-backed login using CustomIdentityStore, and 50+ JUnit + Postman tests with Maven Surefire reports.

To get started, clone the repository by running:
git clone https://github.com/Mohamad512/REST-ACMEMedical-Group-8.git
cd REST-ACMEMedical-Group-8

Then import the project into Eclipse. Use “File → Import → Maven → Existing Maven Projects”, select this folder (it should find pom.xml), and click Finish.

For the database, run the provided ACME_CREATE.sql script in MySQL Workbench (or via CLI) to create all tables and seed data. This includes SECURITY_USER with admin/admin and cst8277/8277 users, plus SECURITY_ROLE, USER_HAS_ROLE, and the domain tables like Physician, Patient, MedicalCertificate, MedicalTraining, MedicalSchool, Medicine, Prescription. Make sure to update persistence.xml if your DB password or connection differs.

To build and deploy, ensure Payara Server is running on localhost:8080. You can either deploy via Eclipse or manually copy the built WAR file from target/REST-ACMEMedical.war into Payara’s autodeploy folder. The REST APIs will be available under http://localhost:8080/REST-ACMEMedical/api/v1/.

Some example secured API calls are:
GET /api/v1/physician (accessible by ADMIN or USER)
POST /api/v1/physician (ADMIN only)
GET /api/v1/patient/1 (only the OWNER or ADMIN can see)
DELETE /api/v1/medicalschool/5 (ADMIN only)

To run all JUnit tests and generate the Maven Surefire report, execute:
mvn clean install test surefire-report:report site -DgenerateReports=true
Then open target/site/surefire-report.html in your browser to see the detailed results. Also use our Postman collection to manually test all endpoints.

For daily team workflow: always start by running git checkout main and git pull origin main to ensure you have the latest code. Then create your feature branch with git checkout -b feature/rest-api-mohammed (replace with your feature name). Make changes, then commit and push using git add ., git commit -m "Added CRUD for Physician", and git push origin feature/rest-api-mohammed. After pushing, create a Pull Request on GitHub so it can be reviewed and merged into main.

Team members are:
Chengcheng - Team Lead, GitHub, Testing, Documentation
Manaf - JPA Entities and JSON serialization
Mohammed - REST API Development
Hongli - Security & IdentityStore

It’s very important to always pull main before starting your work and to never commit directly to main. Use a feature branch and open a PR for all changes. If you change the DB schema, security logic, or major resources, notify the team immediately so everyone stays in sync.

This ensures everyone can continue development smoothly and our project will be ready for demo on time.

