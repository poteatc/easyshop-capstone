# Spring Easyshop Commerce Application

## Overview
This application is a Spring-based e-commerce platform that allows users to interact with products, categories, profiles, and a shopping cart system through REST APIs. The backend implements a layered architecture consisting of DAOs (Data Access Objects), services, and controllers to ensure modularity, scalability, and maintainability.

---

## Table of Contents
1. [Spring Framework Overview](#spring-framework-overview)
2. [DAO (Data Access Object) Layer](#dao-data-access-object-layer)
   - [DAO Interfaces](#dao-interfaces)
   - [MySQL DAO Implementations](#mysql-dao-implementations)
   - [MySqlDaoBase Class](#mysqldaobase-class)
3. [RestControllers](#restcontrollers)
4. [BasicDataSource Configuration](#basicdatasource-configuration)
5. [JWT Bearer Authentication](#jwt-bearer-authentication)
6. [Frontend Overview](#frontend-overview)
7. [Errors](#errors)
8. [Interesting Code](#interesting-code)

---

## Spring Framework Overview
Spring is a powerful Java framework used in this application to provide dependency injection, transaction management, and seamless integration with other technologies. Here, it is utilized for:
- **Dependency Injection:** Managed via the `@Component` annotation, ensuring decoupled and testable code.
- **Data Access Integration:** Utilizing Spring's DataSource management to configure and manage database connections.
- **RESTful APIs:** Enabling easy creation of RestControllers for handling HTTP requests and responses.

---

## DAO (Data Access Object) Layer

### DAO Interfaces
DAO interfaces define the operations supported for each model in the system. They decouple the business logic from the persistence layer, enabling easier testing and swapping of underlying implementations.

#### Example:
- `ProductDao` provides methods for CRUD operations on products.
- `CategoryDao` provides methods for managing product categories.
- `ProfileDao` handles user profile persistence.
- `ShoppingCartDao` manages shopping cart functionality.

---

### MySQL DAO Implementations
The MySQL implementations (e.g., `MySqlProductDao`, `MySqlCategoryDao`) are responsible for:
- Executing SQL queries.
- Mapping `ResultSet` data to Java objects.
- Handling CRUD operations with precise database interaction.

#### Example: `MySqlProductDao`
- **Methods:**
  - `search`: Fetches products based on criteria such as category, price range, and color.
  - `create`: Inserts a new product into the database.
  - `update`: Updates an existing product's details.
  - `delete`: Removes a product from the database.

These classes use prepared statements for security and efficiency, and return domain objects such as `Product` or `Category`.

---

### MySqlDaoBase Class
`MySqlDaoBase` serves as the base class for all MySQL DAO implementations. It provides the shared functionality of acquiring a database connection via a `DataSource`.

---

## RestControllers
RestControllers expose the application's functionality through HTTP endpoints. Each RestController corresponds to a specific module or functionality, such as products, categories, or shopping carts.

- **Annotations Used:**
  - `@RestController`: Indicates that the class handles HTTP requests.
  - `@RequestMapping`: Maps HTTP paths to specific methods.
  - `@RequestBody` and `@PathVariable`: Handle request bodies and URL parameters respectively.

This layer interacts with the service or DAO layers to process client requests and return JSON responses.

## Controllers Overview

This project contains several REST controllers that handle HTTP requests for managing categories, products, user profiles, shopping carts, and authentication. Below is a brief description of each controller and its main functionalities:

### CategoriesController
This controller manages operations related to product categories. 
- **Endpoint:** `/categories`
- **Key Features:**
  - Fetch all categories: `GET /categories`
  - Get a category by ID: `GET /categories/{id}`
  - Retrieve all products within a category: `GET /categories/{categoryId}/products`
  - Add a new category (Admin only): `POST /categories`
  - Update a category (Admin only): `PUT /categories/{id}`
  - Delete a category (Admin only): `DELETE /categories/{id}`

### ProductsController
This controller provides endpoints for searching and managing products.
- **Endpoint:** `/products`
- **Key Features:**
  - Search products with optional filters: `GET /products`
  - Fetch product details by ID: `GET /products/{id}`
  - Add a new product (Admin only): `POST /products`
  - Update a product (Admin only): `PUT /products/{id}`
  - Delete a product (Admin only): `DELETE /products/{id}`

### ProfileController
Handles profile management for authenticated users.
- **Endpoint:** `/profile`
- **Key Features:**
  - Get the profile of the currently logged-in user: `GET /profile`

### ShoppingCartController
Manages the shopping cart for logged-in users.
- **Endpoint:** `/cart`
- **Key Features:**
  - Retrieve the current user's shopping cart: `GET /cart`
  - Add a product to the shopping cart: `POST /cart/products/{productId}`
  - (Planned) Update a product in the cart: `PUT /cart/products/{productId}`
  - (Planned) Clear all products from the cart: `DELETE /cart`

### AuthenticationController
Handles user authentication and registration.
- **Endpoints:**
  - `/login` - User login
  - `/register` - User registration
- **Key Features:**
  - User authentication using JWT tokens.
  - Registration of new users and creation of corresponding profiles.

---

Each controller includes appropriate security annotations, such as `@PreAuthorize`, to enforce role-based access control where required. Additionally, error handling is implemented to return meaningful HTTP status codes for various scenarios.


---

## BasicDataSource Configuration
The application uses Apache Commons DBCP's `BasicDataSource` for database connection pooling, configured via `application.properties`. This ensures efficient connection reuse and optimal performance.

#### Key Configuration Properties:
- `spring.datasource.url`: Specifies the database URL.
- `spring.datasource.username` and `spring.datasource.password`: Database credentials.
- `spring.datasource.driver-class-name`: JDBC driver used for the database.

The `DatabaseConfig` class provides a central place for configuring the database connection pool in a Spring-based application. By using Spring's @Configuration annotation, the class is treated as a configuration class, and the @Bean annotation allows it to define the BasicDataSource as a Spring-managed bean. This approach provides flexibility and makes the application more maintainable by keeping the configuration in one place and allowing for easy updates to the database connection details.

---

## JWT Bearer Authentication
JSON Web Token (JWT) is used to secure the application through stateless authentication. Here's how it works:
1. **User Login:** A valid username and password return a signed JWT token.
2. **Bearer Token:** The client includes the token in the `Authorization` header for subsequent requests.
3. **Token Validation:** Middleware or filters validate the token for authenticity and expiration.

### Benefits of JWT:
- Scalability: Stateless nature eliminates server-side session storage.
- Security: Prevents unauthorized access to protected resources.

#### Implementation in the Application:
- Token generation is done upon successful login.
- Spring Security's filters handle the validation of JWT tokens for protected endpoints.

---

## Project Highlights
- **Modular Architecture:** Separation of concerns through controllers, services, and DAOs.
- **Scalability:** Designed for extensibility with minimal coupling.
- **Security:** Implemented JWT for robust authentication.
- **Efficiency:** Connection pooling via `BasicDataSource`.

---

## Getting Started
1. Configure the database credentials in `application.properties`.
2. Build the project using Maven/Gradle.
3. Run the Spring Boot application.
4. Access the REST APIs via Postman or similar tools.

---

## Frontend Overview

The frontend of this application is built using basic HTML, CSS, and JavaScript. It is designed to display a list of products that can be filtered based on categories and price ranges. Users can register and log in to access additional features, such as adding products to their shopping cart. Admin users are granted additional permissions to manage the product categories and product listings.

### Key Features

1. **Product Display and Category Filtering**
   - The main page of the website features a dropdown list of categories that users can select from. The dropdown is populated dynamically from the backend, which provides a list of all available product categories.
   - Once a category is selected, the page fetches and displays products belonging to that category. This list of products can be filtered further based on price range using two sliders (minimum and maximum price).

2. **Price Filtering with Sliders**
   - Two price sliders allow users to set a minimum and maximum price for filtering the displayed products. As the sliders are adjusted, the product list is dynamically updated to reflect the price range.
   - The minimum and maximum price filters are connected to the backend, ensuring that only products within the selected price range are displayed.

3. **User Registration and Login**
   - Users can register through the `POST` request to the `/register` endpoint using Postman or a similar API testing tool.
   - After registering, users can log in via the website by providing their username and password. Upon successful login, a JSON Web Token (JWT) is returned, which is stored in the browser's local storage for subsequent requests.
   - Once logged in, users can add products to their shopping cart and view their cart.

4. **Admin-Only Features**
   - Users with the **Admin** role can add new categories and products to the system.

5. **Admin Permissions**
   - Only users with an **Admin** role are allowed to add, update, or delete categories and products. This restriction is enforced via the backend by checking the user's role using Spring Security. If a non-admin user attempts to access these endpoints, they are blocked, ensuring proper security.

    --TODO--
6. **Shopping Cart**
   - Logged-in users can view and modify their shopping cart. They can add products to the cart by clicking an "Add to Cart" button next to each product.
   - The cart is displayed on a separate page or section of the website. It shows all the products the user has added, along with the option to remove items or proceed to checkout.
   


### Frontend Interaction with Backend API

The frontend communicates with the backend API to fetch product and category data, as well as to manage user authentication and shopping cart actions. Below is an outline of the key API interactions:

1. **Fetching Categories**
   - The category dropdown list is populated with data from the `/categories` endpoint. This is a `GET` request that returns a list of all available categories.

2. **Fetching Products by Category**
   - When a category is selected from the dropdown, a `GET` request is sent to the `/categories/{categoryId}/products` endpoint. This request fetches all products associated with the selected category.

3. **Product Filtering by Price**
   - The frontend sends a `GET` request to the `/products` endpoint with query parameters for category, minimum price, and maximum price. These filters allow users to narrow down the product list based on their preferences.

4. **User Registration**
   - The registration form on the website sends a `POST` request to the `/register` endpoint to create a new user account. This request includes the user's username, password, and role.
   
5. **User Login**
   - The login form sends a `POST` request to the `/login` endpoint, where the backend authenticates the user's credentials and returns a JWT (JSON Web Token). This token is stored in the browser's local storage and is included in the headers of future API requests to authenticate the user.

6. **Adding Products to the Cart**
   - When a logged-in user clicks the "Add to Cart" button for a product, a `POST` request is sent to the `/cart/products/{productId}` endpoint, which adds the selected product to the user's cart.

7. **Admin Actions (Add Category/Product)**
   - Admin users can add new categories and products through forms on the website. These forms send `POST` requests to the `/categories` and `/products` endpoints. Admin authentication is required for these actions.

### User Experience (UX)

- The homepage of the website displays a clean layout with a header containing the category dropdown, price sliders, and a login/register button.
- After logging in, the user can see their cart and the products they have added. There is also an option to log out.

### Example Workflow

1. **Browse Products:**
   - User selects a category from the dropdown, and the product list is updated.
   - User adjusts the price sliders to filter products by price range.

2. **Login:**
   - User clicks the login button and enters their credentials.
   - After a successful login, the user is redirected back to the homepage with the option to add products to the cart.

--TODO--
3. **Add Product to Cart:**
   - User clicks "Add to Cart" on a product, and it is added to their shopping cart.
   - The user can view and modify their cart at any time.


### Conclusion

The frontend of the application is a simple, functional website that allows users to browse products, filter them by category and price, and manage their shopping cart. Users can register and log in, while Admin users can manage product categories and listings. The application leverages a basic HTML, CSS, and JavaScript structure, interacting with a RESTful API for dynamic content and user management.

> ![image](https://github.com/user-attachments/assets/69e0a394-adb3-4eac-8b60-210091385d49)

> ![image](https://github.com/user-attachments/assets/77d6864d-545c-4846-8d2a-d2e724e6e83e)

> ![image](https://github.com/user-attachments/assets/3733c883-ecd4-4cd6-9e00-97cd322386e1)


--- 

## Errors
![image](https://github.com/user-attachments/assets/17618a90-3d13-423f-bd61-c16349073ec9)

- Fixed by changing create() to update() method.

![image](https://github.com/user-attachments/assets/6b14e4b6-1cd4-44b3-88e6-da64411ed41c)

- Caused sliders on frontend to filter incorrectly.. the SQL query and PreparedStatement was only checking for one parameter called price instead of two: minPrice and maxPrice.

---
## Interesting Code

