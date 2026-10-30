# Online Toy Store Management System

A complete Java Spring Boot web application built for the university Object Oriented Programming (SE1020) project.

## Features & Tech Stack
- **Framework**: Java 17, Spring Boot 3.x, Spring Web (MVC)
- **Frontend**: Thymeleaf, Bootstrap 5 (CDN), HTML5, CSS3
- **Storage**: Plain `.txt` files using Java file I/O (No Database, No JPA/Hibernate).
- **Security**: SHA-256 Password Hashing.

## Setup & Run Instructions

### Prerequisites
- JDK 17 or higher
- Maven 3.x

### How to Run
1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code).
2. Run `mvn spring-boot:run` from the terminal, or execute `ToyStoreApplication.main()`.
3. Open a browser and navigate to `http://localhost:8080`.

### Default Login Credentials (seeded on first run)
- **Admin**: Username: `admin`, Password: `admin123`
- **Regular Customer**: Username: `kamal`, Password: `password`
- **Premium Customer**: Username: `janith`, Password: `password`

## Data Storage Files Format

All data is stored in the `data/` directory (created automatically at the project root).
The delimiter used is a pipe `|`.

- `users.txt`: `id | username | passwordHash | email | role (REGULAR/PREMIUM) | imageFilename`
- `admins.txt`: `id | username | passwordHash | email | role (ADMIN) | permissions`
- `toys.txt`: `id | name | brand | category | ageGroup | price | stockQuantity | type | imageFilename`
- `orders.txt`: `id | userId | itemsSummary(toyId:qty,) | totalAmount | deliveryAddress | status | orderDate`
- `suppliers.txt`: `id | name | contact | brandsSupplied | type (LOCAL/INTERNATIONAL) | imageFilename`
- `admin_logs.txt`: `adminId | action | timestamp`

## Image Handling
- Images are stored on disk in `uploads/<component>/` with unique filenames formatted as `<ID>_<UUID>.<ext>`.
- Only the filename is stored in `.txt` records (never raw binary data).
- Served statically at `/uploads/**` via `WebConfig` (`WebMvcConfigurer`).
- Validates file formats (`jpg`, `jpeg`, `png`, `webp`) and maximum size of 5MB.
- Old image files are automatically deleted upon update or deletion.
- Fallback SVG placeholders are provided when no image exists.

## Components & OOP Concepts

The project is structured by **Component** (not by layer) to ensure separation of concerns per group member.

| Component | Package / Prefix | Data File | Key OOP Concepts Demonstrated |
|-----------|-----------------|-----------|--------------------------------|
| **1. User** | `com.toystore.component1_user` <br> URL: `/users` | `users.txt` | **Encapsulation**: Private fields with getters/setters.<br>**Inheritance**: `AdminUser` and `RegularUser` extend `User`.<br>**Polymorphism**: Overridden `getRole()`. |
| **2. Toy** | `com.toystore.component2_toy` <br> URL: `/toys` | `toys.txt` | **Encapsulation**: `Toy` fields.<br>**Inheritance**: `EducationalToy`, `ElectronicToy`, `PlushToy`, `NonElectronicToy` extend `Toy`.<br>**Polymorphism**: Overridden `displayDetails()` & `getSafetyInfo()`. |
| **3. Order** | `com.toystore.component3_order` <br> URL: `/orders` | `orders.txt` | **Abstraction**: Stock validation hidden in service.<br>**Polymorphism**: Discount/Delivery calculation differs by user role (Premium vs Regular). |
| **4. Admin** | `com.toystore.component4_admin` <br> URL: `/admin` | `admins.txt` | **Encapsulation**: `Admin` and `ActivityLog` classes.<br>**Inheritance**: `Admin` extends `User`.<br>**Abstraction**: `AdminOperations` interface hides implementation details. |
| **5. Supplier** | `com.toystore.component5_supplier` <br> URL: `/suppliers` | `suppliers.txt` | **Inheritance**: `LocalSupplier` and `InternationalSupplier` extend `Supplier`.<br>**Polymorphism**: Different calculations for `getDeliveryTimeEstimate()` and `getRestockCost()`. |
