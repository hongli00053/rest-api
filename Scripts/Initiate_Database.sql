
-- Use this script as the combination initial script including recommended scripts from professor.
-- log in with root user.
DROP DATABASE IF EXISTS acmemedical;
CREATE DATABASE acmemedical DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE acmemedical;

-- Make sure you already have the user cst8277 with the password of cst8277, otherwise create one with statement CREATE USER 'cst8277'@'localhost' IDENTIFIED BY 'cst8277';
GRANT ALL PRIVILEGES ON acmemedical.* TO 'cst8277'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE physician (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    created DATETIME,
    updated DATETIME,
    version BIGINT
);

CREATE TABLE patient (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    year_of_birth INT,
    home_address VARCHAR(100),
    height_cm INT,
    weight_kg INT,
    smoker TINYINT(1),
    created DATETIME,
    updated DATETIME,
    version BIGINT
);

CREATE TABLE medicine (
    medicine_id INT AUTO_INCREMENT PRIMARY KEY,
    drug_name VARCHAR(50),
    manufacturer_name VARCHAR(50),
    dosage_information VARCHAR(100),
    chemical_name VARCHAR(50),
    generic_name VARCHAR(50),
    created DATETIME,
    updated DATETIME,
    version BIGINT
);

CREATE TABLE prescription (
    physician_id INT NOT NULL,
    patient_id INT NOT NULL,
    medicine_id INT NOT NULL,
    number_of_refills INT,
    prescription_information VARCHAR(100),
    created DATETIME,
    updated DATETIME,
    version BIGINT,
    PRIMARY KEY (physician_id, patient_id, medicine_id),
    FOREIGN KEY (physician_id) REFERENCES physician(id),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id)
);

CREATE TABLE medical_school (
    school_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    public TINYINT(1),
    created DATETIME,
    updated DATETIME,
    version BIGINT
);

CREATE TABLE medical_training (
    training_id INT AUTO_INCREMENT PRIMARY KEY,
    school_id INT,
    start_date DATETIME,
    end_date DATETIME,
    active TINYINT(1),
    created DATETIME,
    updated DATETIME,
    version BIGINT,
    FOREIGN KEY (school_id) REFERENCES medical_school(school_id)
);

CREATE TABLE medical_certificate (
    certificate_id INT AUTO_INCREMENT PRIMARY KEY,
    physician_id INT,
    training_id INT,
    signed TINYINT(1),
    created DATETIME,
    updated DATETIME,
    version BIGINT,
    FOREIGN KEY (physician_id) REFERENCES physician(id),
    FOREIGN KEY (training_id) REFERENCES medical_training(training_id)
);

CREATE TABLE security_user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  password_hash VARCHAR(256) NOT NULL,
  username VARCHAR(100) NOT NULL UNIQUE,
  physician_id INT,
  created DATETIME,
  updated DATETIME,
  version BIGINT,
  FOREIGN KEY (physician_id) REFERENCES physician(id)
);

CREATE TABLE security_role (
  role_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL UNIQUE
);

CREATE TABLE user_has_role (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES security_user(user_id),
  FOREIGN KEY (role_id) REFERENCES security_role(role_id)
);
CREATE INDEX idx_physician_name ON physician(last_name, first_name);
CREATE INDEX idx_patient_name ON patient(last_name, first_name);
CREATE INDEX idx_medicine_name ON medicine(drug_name);
CREATE INDEX idx_training_school ON medical_training(school_id);
CREATE INDEX idx_certificate_physician ON medical_certificate(physician_id);
ALTER TABLE physician AUTO_INCREMENT = 1;
ALTER TABLE medical_school AUTO_INCREMENT = 1;
ALTER TABLE medical_training AUTO_INCREMENT = 1;
ALTER TABLE medical_certificate AUTO_INCREMENT = 1;
ALTER TABLE medicine AUTO_INCREMENT = 1;
ALTER TABLE patient AUTO_INCREMENT = 1;



-- delete data before inserting it.
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE user_has_role;
TRUNCATE TABLE security_user;
TRUNCATE TABLE security_role;

TRUNCATE TABLE medical_certificate;
TRUNCATE TABLE medical_training;
TRUNCATE TABLE medical_school;

TRUNCATE TABLE prescription;
TRUNCATE TABLE medicine;
TRUNCATE TABLE patient;
TRUNCATE TABLE physician;

SET FOREIGN_KEY_CHECKS = 1;


-- Insert into physician
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (1, 'DrFirst1', 'DrLast1', '2021-08-14 00:00:00', '2016-02-14 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (2, 'DrFirst2', 'DrLast2', '2017-11-03 00:00:00', '2019-06-22 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (3, 'DrFirst3', 'DrLast3', '2014-10-26 00:00:00', '2016-10-12 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (4, 'DrFirst4', 'DrLast4', '2020-06-23 00:00:00', '2014-07-23 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (5, 'DrFirst5', 'DrLast5', '2016-04-25 00:00:00', '2013-10-13 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (6, 'DrFirst6', 'DrLast6', '2010-10-10 00:00:00', '2019-05-18 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (7, 'DrFirst7', 'DrLast7', '2018-09-13 00:00:00', '2013-02-23 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (8, 'DrFirst8', 'DrLast8', '2023-05-05 00:00:00', '2022-03-17 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (9, 'DrFirst9', 'DrLast9', '2023-01-15 00:00:00', '2017-12-13 00:00:00', 1);
INSERT INTO physician (`id`, `first_name`, `last_name`, `created`, `updated`, `version`) VALUES (10, 'DrFirst10', 'DrLast10', '2015-05-21 00:00:00', '2019-09-30 00:00:00', 1);

-- Insert into patient
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (1, 'PatientFirst1', 'PatientLast1', 2005, '100 Main St', 158, 67, 1, '2017-08-31 00:00:00', '2020-12-09 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (2, 'PatientFirst2', 'PatientLast2', 2002, '101 Main St', 180, 98, 0, '2016-04-21 00:00:00', '2022-02-09 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (3, 'PatientFirst3', 'PatientLast3', 1950, '102 Main St', 186, 61, 0, '2020-11-24 00:00:00', '2013-11-07 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (4, 'PatientFirst4', 'PatientLast4', 1973, '103 Main St', 150, 58, 0, '2015-11-13 00:00:00', '2024-07-12 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (5, 'PatientFirst5', 'PatientLast5', 1979, '104 Main St', 164, 98, 0, '2012-03-22 00:00:00', '2023-09-14 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (6, 'PatientFirst6', 'PatientLast6', 1992, '105 Main St', 176, 54, 0, '2010-05-04 00:00:00', '2022-09-03 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (7, 'PatientFirst7', 'PatientLast7', 1951, '106 Main St', 159, 80, 0, '2015-09-22 00:00:00', '2024-11-18 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (8, 'PatientFirst8', 'PatientLast8', 1964, '107 Main St', 180, 71, 0, '2012-03-10 00:00:00', '2013-01-11 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (9, 'PatientFirst9', 'PatientLast9', 2000, '108 Main St', 155, 98, 0, '2021-09-19 00:00:00', '2013-07-26 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (10, 'PatientFirst10', 'PatientLast10', 1951, '109 Main St', 179, 89, 1, '2010-12-09 00:00:00', '2014-02-07 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (11, 'PatientFirst11', 'PatientLast11', 1996, '110 Main St', 179, 99, 1, '2024-12-18 00:00:00', '2024-11-13 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (12, 'PatientFirst12', 'PatientLast12', 1962, '111 Main St', 162, 69, 0, '2013-02-13 00:00:00', '2010-08-11 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (13, 'PatientFirst13', 'PatientLast13', 1987, '112 Main St', 157, 99, 0, '2024-11-11 00:00:00', '2010-03-27 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (14, 'PatientFirst14', 'PatientLast14', 1974, '113 Main St', 150, 69, 0, '2023-01-14 00:00:00', '2017-04-28 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (15, 'PatientFirst15', 'PatientLast15', 1954, '114 Main St', 187, 65, 0, '2012-04-12 00:00:00', '2023-08-19 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (16, 'PatientFirst16', 'PatientLast16', 1993, '115 Main St', 184, 69, 0, '2014-04-18 00:00:00', '2022-09-15 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (17, 'PatientFirst17', 'PatientLast17', 1989, '116 Main St', 151, 57, 0, '2024-05-17 00:00:00', '2013-09-26 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (18, 'PatientFirst18', 'PatientLast18', 1965, '117 Main St', 176, 62, 1, '2010-10-07 00:00:00', '2020-10-28 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (19, 'PatientFirst19', 'PatientLast19', 1969, '118 Main St', 175, 92, 0, '2010-07-12 00:00:00', '2013-02-02 00:00:00', 1);
INSERT INTO patient (`patient_id`, `first_name`, `last_name`, `year_of_birth`, `home_address`, `height_cm`, `weight_kg`, `smoker`, `created`, `updated`, `version`) VALUES (20, 'PatientFirst20', 'PatientLast20', 1983, '119 Main St', 177, 72, 0, '2012-01-29 00:00:00', '2024-06-08 00:00:00', 1);

-- Insert into medicine
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (1, 'Drug1', 'Manufacturer1', '1 pills per day', 'Chemical1', 'Generic1', '2010-04-07 00:00:00', '2019-01-28 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (2, 'Drug2', 'Manufacturer2', '3 pills per day', 'Chemical2', 'Generic2', '2014-09-25 00:00:00', '2021-10-05 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (3, 'Drug3', 'Manufacturer3', '1 pills per day', 'Chemical3', 'Generic3', '2024-07-29 00:00:00', '2015-12-07 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (4, 'Drug4', 'Manufacturer4', '1 pills per day', 'Chemical4', 'Generic4', '2024-09-27 00:00:00', '2021-03-11 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (5, 'Drug5', 'Manufacturer5', '4 pills per day', 'Chemical5', 'Generic5', '2017-09-29 00:00:00', '2013-07-10 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (6, 'Drug6', 'Manufacturer6', '3 pills per day', 'Chemical6', 'Generic6', '2020-02-12 00:00:00', '2013-10-06 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (7, 'Drug7', 'Manufacturer7', '4 pills per day', 'Chemical7', 'Generic7', '2015-01-11 00:00:00', '2013-07-22 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (8, 'Drug8', 'Manufacturer8', '1 pills per day', 'Chemical8', 'Generic8', '2018-03-01 00:00:00', '2023-03-31 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (9, 'Drug9', 'Manufacturer9', '2 pills per day', 'Chemical9', 'Generic9', '2013-09-26 00:00:00', '2018-01-20 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (10, 'Drug10', 'Manufacturer10', '1 pills per day', 'Chemical10', 'Generic10', '2023-04-29 00:00:00', '2019-04-25 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (11, 'Drug11', 'Manufacturer11', '4 pills per day', 'Chemical11', 'Generic11', '2024-05-23 00:00:00', '2016-03-30 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (12, 'Drug12', 'Manufacturer12', '2 pills per day', 'Chemical12', 'Generic12', '2010-03-24 00:00:00', '2014-04-25 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (13, 'Drug13', 'Manufacturer13', '3 pills per day', 'Chemical13', 'Generic13', '2013-09-28 00:00:00', '2012-01-14 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (14, 'Drug14', 'Manufacturer14', '1 pills per day', 'Chemical14', 'Generic14', '2023-01-29 00:00:00', '2019-04-21 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (15, 'Drug15', 'Manufacturer15', '3 pills per day', 'Chemical15', 'Generic15', '2014-12-07 00:00:00', '2019-10-10 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (16, 'Drug16', 'Manufacturer16', '2 pills per day', 'Chemical16', 'Generic16', '2023-12-21 00:00:00', '2016-11-30 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (17, 'Drug17', 'Manufacturer17', '4 pills per day', 'Chemical17', 'Generic17', '2016-09-26 00:00:00', '2018-09-01 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (18, 'Drug18', 'Manufacturer18', '2 pills per day', 'Chemical18', 'Generic18', '2024-05-19 00:00:00', '2010-12-21 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (19, 'Drug19', 'Manufacturer19', '4 pills per day', 'Chemical19', 'Generic19', '2017-07-02 00:00:00', '2024-10-25 00:00:00', 1);
INSERT INTO medicine (`medicine_id`, `drug_name`, `manufacturer_name`, `dosage_information`, `chemical_name`, `generic_name`, `created`, `updated`, `version`) VALUES (20, 'Drug20', 'Manufacturer20', '3 pills per day', 'Chemical20', 'Generic20', '2023-10-11 00:00:00', '2021-07-23 00:00:00', 1);

-- Insert into prescription
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (6, 6, 16, 1, 'Take with food', '2020-10-26 00:00:00', '2012-11-27 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 18, 8, 1, 'Take with food', '2017-08-17 00:00:00', '2021-03-01 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 16, 19, 3, 'Take with food', '2022-09-02 00:00:00', '2012-04-01 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 11, 9, 3, 'Take with food', '2011-12-17 00:00:00', '2023-01-15 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 7, 3, 3, 'Take with food', '2020-01-26 00:00:00', '2017-06-19 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 16, 3, 4, 'Take with food', '2021-02-02 00:00:00', '2020-06-02 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 13, 6, 2, 'Take with food', '2022-01-03 00:00:00', '2011-06-16 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 20, 4, 0, 'Take with food', '2019-10-18 00:00:00', '2013-09-25 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (1, 20, 12, 5, 'Take with food', '2022-07-27 00:00:00', '2023-05-03 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 12, 15, 5, 'Take with food', '2022-10-28 00:00:00', '2019-03-28 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (5, 3, 18, 3, 'Take with food', '2016-09-24 00:00:00', '2021-12-01 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 14, 15, 1, 'Take with food', '2012-11-24 00:00:00', '2017-04-27 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 20, 16, 2, 'Take with food', '2019-04-13 00:00:00', '2012-06-08 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 5, 18, 3, 'Take with food', '2010-07-08 00:00:00', '2010-07-13 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (5, 11, 7, 0, 'Take with food', '2014-07-17 00:00:00', '2010-01-29 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 4, 11, 5, 'Take with food', '2020-10-26 00:00:00', '2015-09-03 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 5, 5, 5, 'Take with food', '2023-10-27 00:00:00', '2017-08-19 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 16, 12, 1, 'Take with food', '2020-05-25 00:00:00', '2010-05-24 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (1, 11, 7, 0, 'Take with food', '2011-01-12 00:00:00', '2018-04-20 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 13, 2, 4, 'Take with food', '2022-03-11 00:00:00', '2020-09-12 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 3, 5, 1, 'Take with food', '2022-03-06 00:00:00', '2014-09-10 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 3, 9, 0, 'Take with food', '2020-08-04 00:00:00', '2014-04-06 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 8, 13, 5, 'Take with food', '2015-09-11 00:00:00', '2017-10-21 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (5, 4, 13, 3, 'Take with food', '2022-09-09 00:00:00', '2015-11-07 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (5, 10, 6, 5, 'Take with food', '2012-12-17 00:00:00', '2020-02-12 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (5, 10, 9, 1, 'Take with food', '2011-01-12 00:00:00', '2020-03-09 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 9, 17, 3, 'Take with food', '2017-07-18 00:00:00', '2018-09-14 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 16, 4, 2, 'Take with food', '2023-09-28 00:00:00', '2019-12-13 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 12, 19, 0, 'Take with food', '2020-08-19 00:00:00', '2013-01-17 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 3, 1, 1, 'Take with food', '2014-05-19 00:00:00', '2019-05-17 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 11, 18, 4, 'Take with food', '2014-02-03 00:00:00', '2017-02-07 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (1, 12, 5, 0, 'Take with food', '2018-01-04 00:00:00', '2015-03-14 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 17, 13, 5, 'Take with food', '2020-04-19 00:00:00', '2023-10-21 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (5, 8, 13, 1, 'Take with food', '2017-04-20 00:00:00', '2019-08-24 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 12, 1, 5, 'Take with food', '2017-01-17 00:00:00', '2013-09-22 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 15, 13, 2, 'Take with food', '2011-05-10 00:00:00', '2021-03-27 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (6, 13, 18, 4, 'Take with food', '2010-12-25 00:00:00', '2015-01-22 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 8, 8, 5, 'Take with food', '2021-02-28 00:00:00', '2020-12-16 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (6, 14, 18, 3, 'Take with food', '2020-01-04 00:00:00', '2022-01-12 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 14, 19, 4, 'Take with food', '2018-07-21 00:00:00', '2020-11-14 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 3, 19, 3, 'Take with food', '2016-09-24 00:00:00', '2020-09-25 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 11, 13, 5, 'Take with food', '2015-01-09 00:00:00', '2012-06-10 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (5, 8, 3, 4, 'Take with food', '2013-06-13 00:00:00', '2013-08-05 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (6, 10, 15, 0, 'Take with food', '2024-08-08 00:00:00', '2012-03-27 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 3, 6, 5, 'Take with food', '2023-05-11 00:00:00', '2013-02-03 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (6, 8, 2, 5, 'Take with food', '2024-03-31 00:00:00', '2020-12-27 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 5, 17, 2, 'Take with food', '2012-08-23 00:00:00', '2014-01-10 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 19, 14, 0, 'Take with food', '2022-09-16 00:00:00', '2022-01-30 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 10, 12, 0, 'Take with food', '2012-04-12 00:00:00', '2020-01-06 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 1, 11, 3, 'Take with food', '2017-09-24 00:00:00', '2012-07-02 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (6, 3, 6, 0, 'Take with food', '2021-04-17 00:00:00', '2022-11-13 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 9, 1, 2, 'Take with food', '2024-01-21 00:00:00', '2015-01-27 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 17, 3, 1, 'Take with food', '2023-01-14 00:00:00', '2012-07-20 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 17, 19, 3, 'Take with food', '2021-02-03 00:00:00', '2020-07-22 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (1, 12, 9, 3, 'Take with food', '2010-11-09 00:00:00', '2013-10-18 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 15, 1, 0, 'Take with food', '2022-04-20 00:00:00', '2019-02-04 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 3, 11, 4, 'Take with food', '2012-10-20 00:00:00', '2024-04-23 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 12, 5, 1, 'Take with food', '2014-02-05 00:00:00', '2017-11-15 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 2, 8, 1, 'Take with food', '2023-10-10 00:00:00', '2019-08-15 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (1, 6, 3, 3, 'Take with food', '2022-07-11 00:00:00', '2011-05-24 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (6, 12, 19, 4, 'Take with food', '2015-02-06 00:00:00', '2022-12-05 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 18, 16, 4, 'Take with food', '2011-12-23 00:00:00', '2019-01-09 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 13, 15, 5, 'Take with food', '2017-12-31 00:00:00', '2014-04-20 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 7, 6, 5, 'Take with food', '2018-05-16 00:00:00', '2014-05-05 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 10, 13, 2, 'Take with food', '2012-07-29 00:00:00', '2014-10-17 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 17, 18, 4, 'Take with food', '2023-11-13 00:00:00', '2021-03-19 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 15, 16, 2, 'Take with food', '2021-11-08 00:00:00', '2017-05-03 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 6, 15, 4, 'Take with food', '2016-06-21 00:00:00', '2010-04-07 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 18, 7, 1, 'Take with food', '2014-05-31 00:00:00', '2024-03-29 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 1, 17, 4, 'Take with food', '2014-01-26 00:00:00', '2014-04-01 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 11, 13, 1, 'Take with food', '2011-09-27 00:00:00', '2016-10-22 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 7, 20, 4, 'Take with food', '2016-09-01 00:00:00', '2019-11-16 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 5, 2, 1, 'Take with food', '2020-08-01 00:00:00', '2013-11-15 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 13, 12, 1, 'Take with food', '2020-03-30 00:00:00', '2022-10-21 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (6, 13, 5, 1, 'Take with food', '2010-05-23 00:00:00', '2011-11-01 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 5, 15, 5, 'Take with food', '2011-12-16 00:00:00', '2017-09-06 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (5, 1, 8, 3, 'Take with food', '2021-02-05 00:00:00', '2015-10-14 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 4, 16, 4, 'Take with food', '2011-01-25 00:00:00', '2020-04-12 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 18, 16, 4, 'Take with food', '2015-01-21 00:00:00', '2017-12-20 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 14, 5, 3, 'Take with food', '2013-11-13 00:00:00', '2024-01-25 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 3, 10, 3, 'Take with food', '2018-02-16 00:00:00', '2010-06-29 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (8, 6, 6, 1, 'Take with food', '2018-09-21 00:00:00', '2012-07-23 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 18, 11, 4, 'Take with food', '2017-05-17 00:00:00', '2020-06-21 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 11, 8, 0, 'Take with food', '2012-10-18 00:00:00', '2014-10-23 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (1, 6, 11, 0, 'Take with food', '2024-11-28 00:00:00', '2018-05-30 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 15, 11, 3, 'Take with food', '2012-01-13 00:00:00', '2022-09-16 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 10, 2, 5, 'Take with food', '2014-06-08 00:00:00', '2017-04-07 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (4, 6, 4, 4, 'Take with food', '2013-09-17 00:00:00', '2023-10-25 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (1, 19, 16, 1, 'Take with food', '2013-10-09 00:00:00', '2019-08-31 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (1, 4, 11, 1, 'Take with food', '2021-02-25 00:00:00', '2017-03-18 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 2, 7, 3, 'Take with food', '2012-03-07 00:00:00', '2022-03-28 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 3, 5, 2, 'Take with food', '2014-08-25 00:00:00', '2018-03-15 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (2, 4, 18, 1, 'Take with food', '2014-07-21 00:00:00', '2020-11-02 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 17, 5, 5, 'Take with food', '2023-08-18 00:00:00', '2016-09-20 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (3, 17, 10, 0, 'Take with food', '2012-07-06 00:00:00', '2015-05-27 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (7, 20, 11, 2, 'Take with food', '2023-02-16 00:00:00', '2016-08-15 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (10, 8, 8, 5, 'Take with food', '2016-02-16 00:00:00', '2011-10-09 00:00:00', 1);
INSERT INTO prescription (`physician_id`, `patient_id`, `medicine_id`, `number_of_refills`, `prescription_information`, `created`, `updated`, `version`) VALUES (9, 1, 4, 5, 'Take with food', '2023-07-03 00:00:00', '2021-07-14 00:00:00', 1);

-- Insert into medical_school
INSERT INTO medical_school (`school_id`, `name`, `public`, `created`, `updated`, `version`) VALUES (1, 'MedSchool1', 1, '2023-07-08 00:00:00', '2018-12-01 00:00:00', 1);
INSERT INTO medical_school (`school_id`, `name`, `public`, `created`, `updated`, `version`) VALUES (2, 'MedSchool2', 0, '2022-11-25 00:00:00', '2011-02-27 00:00:00', 1);
INSERT INTO medical_school (`school_id`, `name`, `public`, `created`, `updated`, `version`) VALUES (3, 'MedSchool3', 0, '2021-09-13 00:00:00', '2014-01-10 00:00:00', 1);
INSERT INTO medical_school (`school_id`, `name`, `public`, `created`, `updated`, `version`) VALUES (4, 'MedSchool4', 1, '2023-09-09 00:00:00', '2022-02-11 00:00:00', 1);
INSERT INTO medical_school (`school_id`, `name`, `public`, `created`, `updated`, `version`) VALUES (5, 'MedSchool5', 0, '2016-08-01 00:00:00', '2016-10-13 00:00:00', 1);

-- Insert into medical_training
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (1, 1, '2012-07-07 00:00:00', '2019-11-06 00:00:00', 1, '2020-06-12 00:00:00', '2014-07-31 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (2, 5, '2009-02-10 00:00:00', '2019-06-23 00:00:00', 0, '2020-05-06 00:00:00', '2016-11-01 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (3, 5, '2013-10-17 00:00:00', '2023-06-13 00:00:00', 1, '2019-12-04 00:00:00', '2012-05-05 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (4, 3, '2002-07-22 00:00:00', '2018-01-26 00:00:00', 0, '2021-06-02 00:00:00', '2012-07-15 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (5, 5, '2012-06-20 00:00:00', '2024-07-13 00:00:00', 0, '2011-07-09 00:00:00', '2016-09-20 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (6, 5, '2006-08-22 00:00:00', '2020-10-07 00:00:00', 0, '2015-10-13 00:00:00', '2018-09-09 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (7, 2, '2011-07-09 00:00:00', '2023-01-14 00:00:00', 1, '2015-01-09 00:00:00', '2020-03-10 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (8, 5, '2010-07-06 00:00:00', '2016-04-01 00:00:00', 1, '2018-04-02 00:00:00', '2015-03-22 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (9, 5, '2006-04-28 00:00:00', '2017-04-03 00:00:00', 0, '2019-05-16 00:00:00', '2011-11-01 00:00:00', 1);
INSERT INTO medical_training (`training_id`, `school_id`, `start_date`, `end_date`, `active`, `created`, `updated`, `version`) VALUES (10, 4, '2015-01-26 00:00:00', '2021-11-17 00:00:00', 0, '2022-12-26 00:00:00', '2017-05-08 00:00:00', 1);

-- Insert into medical_certificate
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (1, 5, 7, 1, '2011-12-16 00:00:00', '2018-11-07 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (2, 6, 5, 1, '2019-03-27 00:00:00', '2020-11-12 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (3, 9, 10, 0, '2013-06-11 00:00:00', '2023-04-20 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (4, 6, 2, 1, '2014-12-17 00:00:00', '2024-04-12 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (5, 10, 7, 1, '2019-01-23 00:00:00', '2017-05-30 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (6, 4, 2, 0, '2011-04-26 00:00:00', '2012-04-22 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (7, 4, 9, 1, '2011-10-17 00:00:00', '2022-11-10 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (8, 5, 2, 1, '2019-08-16 00:00:00', '2019-01-04 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (9, 6, 1, 1, '2024-07-31 00:00:00', '2017-08-13 00:00:00', 1);
INSERT INTO medical_certificate (`certificate_id`, `physician_id`, `training_id`, `signed`, `created`, `updated`, `version`) VALUES (10, 9, 6, 1, '2016-11-19 00:00:00', '2023-12-02 00:00:00', 1);

-- Insert into security_role
INSERT INTO security_role (`role_id`, `name`) VALUES (1, 'ADMIN_ROLE');
INSERT INTO security_role (`role_id`, `name`) VALUES (2, 'USER_ROLE');

-- Insert into security_user
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (1, 'user1', 'hash1', 1, '2017-07-27 00:00:00', '2012-06-16 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (2, 'user2', 'hash2', 2, '2023-09-01 00:00:00', '2013-04-21 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (3, 'user3', 'hash3', 3, '2013-03-14 00:00:00', '2014-09-20 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (4, 'user4', 'hash4', 4, '2020-05-05 00:00:00', '2018-01-01 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (5, 'user5', 'hash5', 5, '2013-10-24 00:00:00', '2014-06-11 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (6, 'user6', 'hash6', 6, '2010-09-27 00:00:00', '2020-03-20 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (7, 'user7', 'hash7', 7, '2015-07-18 00:00:00', '2012-10-09 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (8, 'user8', 'hash8', 8, '2016-10-06 00:00:00', '2015-04-26 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (9, 'user9', 'hash9', 9, '2014-12-29 00:00:00', '2019-06-14 00:00:00', 1);
INSERT INTO security_user (`user_id`, `username`, `password_hash`, `physician_id`, `created`, `updated`, `version`) VALUES (10, 'user10', 'hash10', 10, '2023-06-16 00:00:00', '2017-08-25 00:00:00', 1);

-- Insert into user_has_role
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (1, 2);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (2, 1);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (3, 2);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (4, 1);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (5, 2);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (6, 2);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (7, 2);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (8, 2);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (9, 2);
INSERT INTO user_has_role (`user_id`, `role_id`) VALUES (10, 2);
