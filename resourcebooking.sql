CREATE DATABASE resource_booking;

USE resource_booking;

CREATE TABLE users (
    employee_id INT(5) NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    designation VARCHAR(30) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50),
    mobile_number BIGINT(11) NOT NULL,
    role ENUM('user', 'res_admin', 'admin') NOT NULL,
    PRIMARY KEY (employee_id)
);

ALTER TABLE users AUTO_INCREMENT=10000;

CREATE TABLE resources (
    resource_id INT(3) NOT NULL AUTO_INCREMENT,
    resource_name VARCHAR(30) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    capacity INT(3) NOT NULL,
    PRIMARY KEY (resource_id)
);

ALTER TABLE resources AUTO_INCREMENT=100;

CREATE TABLE admins (
    employee_id INT(5),
    resource_id INT(3),
    PRIMARY KEY(employee_id, resource_id),
    CONSTRAINT fk_admins_employee FOREIGN KEY (employee_id) REFERENCES users(employee_id),
    CONSTRAINT fk_admins_resource FOREIGN KEY (resource_id) REFERENCES resources(resource_id)
);

CREATE TABLE bookings (
    booking_id VARCHAR(20),
    employee_id INT(5) NOT NULL,
    resource_id INT(3) NOT NULL,
    title VARCHAR(30) NOT NULL,
    description VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(15) NOT NULL,
    participants INT(3) NOT NULL,
    PRIMARY KEY(booking_id),
    CONSTRAINT fk_bookings_employee FOREIGN KEY (employee_id) REFERENCES users(employee_id),
    CONSTRAINT fk_bookings_resource FOREIGN KEY (resource_id) REFERENCES resources(resource_id)
);

CREATE TABLE cabs (
    cab_number VARCHAR(15),
    resource_id INT(3) NOT NULL UNIQUE,
    driver_name VARCHAR(30) NOT NULL,
    driver_number BIGINT(11) NOT NULL,
    route VARCHAR(100) NOT NULL,
    PRIMARY KEY(cab_number),
    CONSTRAINT fk_cabs_resource FOREIGN KEY (resource_id) REFERENCES resources(resource_id)
);
