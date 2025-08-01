# ACME Medical REST API - Group 8

## Project Overview
This project implements a comprehensive REST API system for ACME Medical with full CRUD operations, security, and extensive JUnit testing.

## Group Members
- **Group 8 Members**: [Please add actual member names here]

## Work Division
[Please specify how the work was divided among group members - who did what]

## Individual Member Contributions and Grades
[Please have each group member grade the others based on their contribution to the group work]

## Test Suite Summary

### Comprehensive JUnit Test Implementation
We have implemented **63 comprehensive JUnit tests** covering all REST endpoints and requirements:

#### Test Categories:

1. **Physician Resource Tests (7 tests)**
   - GET all physicians (admin role)
   - GET all physicians (no authentication - should fail)
   - GET physician by ID (admin role)
   - GET physician by ID (user role)
   - POST create physician (admin role)
   - POST create physician (user role - should fail)
   - GET physician with invalid ID (404 test)

2. **Patient Resource Tests (8 tests)**
   - GET all patients (admin role)
   - GET all patients (user role - should fail)
   - GET patient by ID (admin role)
   - POST create patient (admin role)
   - PUT update patient (admin role)
   - POST create patient (user role - should fail)
   - DELETE patient (admin role)
   - GET patient with invalid ID (404 test)

3. **Medical School Resource Tests (8 tests)**
   - GET all medical schools (public access)
   - GET medical school by ID
   - POST create medical school (admin role)
   - PUT update medical school (admin role)
   - PUT update medical school (user role)
   - DELETE medical school
   - POST add medical training to school (admin role)
   - GET medical school with invalid ID

4. **Medicine Resource Tests (7 tests)**
   - GET all medicines (admin role)
   - GET all medicines (user role - should fail)
   - GET medicine by ID (admin role)
   - POST create medicine (admin role)
   - PUT update medicine (admin role)
   - DELETE medicine (admin role)
   - POST create medicine (user role - should fail)

5. **Prescription Resource Tests (7 tests)**
   - GET all prescriptions (admin role)
   - GET all prescriptions (user role - should fail)
   - GET prescription by composite ID (admin role)
   - POST create prescription (admin role)
   - PUT update prescription (admin role)
   - DELETE prescription (admin role)
   - POST create prescription (user role - should fail)

6. **Medical Certificate Resource Tests (7 tests)**
   - GET all medical certificates (admin role)
   - GET all medical certificates (user role - should fail)
   - GET medical certificate by ID (admin role)
   - GET medical certificate by ID (user role)
   - POST create medical certificate (admin role)
   - PUT update medical certificate (admin role)
   - DELETE medical certificate (admin role)

7. **Medical Training Resource Tests (6 tests)**
   - GET all medical trainings (admin role)
   - GET all medical trainings (user role)
   - GET medical training by ID (admin role)
   - POST create medical training (admin role)
   - PUT update medical training (admin role)
   - POST create medical training (user role - should fail)

8. **Negative Testing (8 tests)**
   - Invalid media type requests
   - Malformed JSON requests
   - Non-existent endpoint access
   - Empty request body handling
   - Null entity POST requests
   - DELETE on non-existent resources
   - PUT with mismatched IDs
   - Very long request paths

9. **Integration and Association Tests (5 tests)**
   - Physician-Patient-Medicine associations
   - Medical School-Medical Training relationships
   - Physician with prescriptions and certificates
   - Patient with prescriptions
   - Complex entity creation with relationships

### Security Testing
- **Admin Role Tests**: Full CRUD access to all resources
- **User Role Tests**: Limited access based on business rules
- **Authentication Tests**: Unauthorized access attempts
- **Authorization Tests**: Role-based access control validation

### Technical Features Tested
- **CRUD Operations**: Create, Read, Update, Delete for all entities
- **Composite Keys**: Prescription entity with physician-patient composite key
- **Entity Relationships**: One-to-Many and Many-to-One associations
- **Lazy Loading**: Fetch strategies and LazyInitializationException handling
- **Jackson Serialization**: JSON processing with custom annotations
- **Error Handling**: HTTP status codes and error responses
- **Media Type Validation**: Content negotiation testing

### Entities Covered
- ✅ Physician
- ✅ Patient  
- ✅ MedicalSchool
- ✅ Medicine
- ✅ Prescription
- ✅ MedicalCertificate
- ✅ MedicalTraining

### Security Roles Tested
- **Admin Role** (`admin/admin`): Full system access
- **User Role** (`cst8277/8277`): Limited access based on ownership

## Running the Tests

### Prerequisites
1. Ensure the ACME Medical application is deployed and running on Payara server
2. Database should be populated with initial test data
3. Server should be accessible at `http://localhost:8080/rest-acmemedical/api/v1`

### Execute Tests
```bash
# Run all tests
mvn test

# Generate surefire report
mvn clean install test surefire-report:report site -DgenerateReports=true
```

### View Test Results
After running the Maven command above, open `target/site/surefire-report.html` in a web browser to view detailed test results.

## Test Configuration
- **Base URL**: `http://localhost:8080/rest-acmemedical/api/v1`
- **Admin Credentials**: `admin/admin`
- **User Credentials**: `cst8277/8277`
- **Test Framework**: JUnit 5
- **HTTP Client**: JAX-RS Client API
- **Authentication**: HTTP Basic Authentication

## Key Testing Patterns
1. **Positive Testing**: Valid requests with expected successful responses
2. **Negative Testing**: Invalid requests with expected error responses  
3. **Security Testing**: Role-based access control validation
4. **Boundary Testing**: Edge cases and invalid data handling
5. **Integration Testing**: Entity relationships and associations

## Notes
- All tests use the JAX-RS Client API as required
- Tests are ordered using `@TestMethodOrder(MethodOrderer.MethodName.class)`
- Comprehensive error handling and status code validation
- Tests cover both successful operations and failure scenarios
- Entity relationships and lazy loading are thoroughly tested

## Postman Collection
The project includes `REST-ACMEMedical-Sample.postman_collection.json` with working REST requests that correspond to the implemented and tested endpoints.
