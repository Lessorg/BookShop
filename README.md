
# Bookshop Application 📘✨

Welcome to the Bookshop—a classic yet modern approach to mastering Java and Spring Boot!
This project is more than a training exercise; it’s a api solution for a online bookstore.
Whether you’re a bibliophile searching for your next read 📚 or a store manager organizing your catalog 📦, this app provides a seamless experience.

# About the Project 💻📖

This project’s mission is straightforward: to create a solid api foundation for an online bookstore,
simplifying operations while enhancing the user experience.

Using technologies like Java and Spring Boot, the app delivers essential features:
>✅ Secure user authentication
>✅ Efficient inventory management
>✅ Seamless shopping cart integration

# Domain Models 🏷️
- Each domain model represents an essential component of the system, enabling seamless operations for users and administrators alike:
- User 🧑‍🤝‍🧑: Captures customer data and login details for personalized experiences.
- Role 🛡️: Defines user permissions (e.g., "Customer" or "Administrator").
- Book 📖: Represents the store’s main product, with details like title, author, price, and category.
- Category 🗂️: Groups books into thematic sections for easier discovery.
- Shopping Cart 🛒: Temporarily stores books a user plans to purchase.
- Shopping Cart Item 📦: Tracks individual book selections and quantities in the cart.
- Order 📤: Represents completed purchases, bundling books into transactions.
- Order Item 📝: Details specific books within an order, including quantity and pricing.
- UML diagram:
![photo_2025-01-15_14-02-57](https://github.com/user-attachments/assets/8ae194ed-fce0-426d-9e1c-850b59096d72)
# Core Functionalities ⚙️
Roles🎭:
- Users:
	- 🛒 Explore and purchase books effortlessly.
	- 📜 Enjoy features like personalized shopping carts and order history.
- Admins:
	- 📚 Manage books, categories, and orders with ease.
	-  🔍 Oversee operations to ensure smooth functionality.

For users: 
- 🔑 Register & Login: Create and access accounts securely.
- 🔍 Browse & Search: Discover books by category or search by title.
- 🛒 Shopping Cart: Add, update, or remove books before checkout.
- 💳 Checkout: Complete purchases with clear order summaries.

For admins:
- 📖 Inventory Management: Add, update, or delete books.
- 🗂️ Category Management: Organize books into logical categories.
- 📦 Order Processing: Track and manage customer orders.

# Technology Stack 🛠️
Core Application Framework:
- 🌱 Spring Boot: For rapid, scalable backend development.
- 🔒 Spring Security: To secure user data and operations.
- 🗂️ Spring Data JPA: For seamless database interactions.

Database Management:
- 🛢️ SQL Database: Reliable data storage.
- 🔄 Liquibase: For managing database versions and migrations.

Data Transformation & Communication:
- 🚀 MapStruct: Simplifies object-to-object mapping.
- 🔑 JWT (JSON Web Tokens): Secure user authentication and session handling.

Development & Documentation:
- 📜 Swagger: Interactive and comprehensive API documentation.
-  ⚠GlobalExceptionHandler: For consistent and robust error management.

# 📝 API Endpoints
- Authentication Controller
	- POST /auth/registration - Register a new user
	- POST /auth/login - Login a user
- Book Controller
	- GET /books - Get all books
	- GET /books/{id} - Get book by ID
	- GET /books/search - Search books by parameters
	- POST /books - Create a new book (Admin only)
	- PUT /books/{id} - Update a book (Admin only)
	- DELETE /books/{id} - Delete a book (Admin only)
- Category Controller
	- GET /categories - Get all categories
	- GET /categories/{id} - Get category by ID
	- GET /categories/{id}/books - Get all books by category ID
	- POST /categories - Create a new category (Admin only)
	- PUT /categories/{id} - Update a category (Admin only)
	- DELETE /categories/{id} - Delete a category (Admin only)
- Shopping Cart Controller
	- GET /cart - Get user's shopping cart
	- POST /cart - Add a book to the cart
	- PUT /cart/items/{cartItemId} - Update quantity of a cart item
	- DELETE /cart/items/{cartItemId} - Remove an item from the cart
- Order Controller
	- POST /orders - Place an order from the shopping cart
	- GET /orders - Get user's order history
	- GET /orders/{orderId}/items - Get all items in a specific order
	- GET /orders/{orderId}/items/{itemId} - Get details of a specific order item
	- PATCH /orders/{id} - Update the status of an order (Admin only)

## Postman Collection

To help you interact with the API endpoints easily, a [**Postman Collection**](https://lll555-8521.postman.co/workspace/LLL-Workspace~db36debb-724e-4965-8f5f-5bad78fb42cf/collection/39194239-1e0f45b3-3e90-420d-9b3f-bf34aaa97e92?action=share&creator=39194239&active-environment=39194239-5710a84a-a04c-4c22-9142-e8b091576b0b) is available.

## Video Guide

For a detailed walkthrough of how the API works, refer to this [**Video Guide**](https://colya-reznick.wistia.com/medias/w6x6tcavoh).

# 🖥️🐳 How to Run the Bookshop Application Locally
Follow these steps to set up and run the Bookshop application on your local machine. With its containerized architecture using Docker, deployment and management are streamlined.

## 🛠️ Prerequisites
Ensure the following are installed on your system before proceeding:

>Java (JDK 17 or higher).
Docker & Docker Compose.
Maven (3.8+ required).
Git.
MySQL (v15): Necessary if you want to set up the database locally outside of Docker.
Postman (Optional): For API testing.

## 🏃Steps to Set Up and Run
Clone the Repository
1. git clone https://github.com/Lessorg/BookShop
2. cd BookShop  
3. Create file BookShop/.env 
5. Copy the provided .env.template file to .env and edit it with your custom configurations:

Update the .env file with your desired values (e.g., database connection details, port configurations).
```sh
cp .env.example .env  
```
Build the application using Maven:
```sh
mvn clean install  
Run the Application with Docker Compose
```
Start the application along with its dependencies (like the database):
```sh
docker -compose up -d  
```
Access the Application
To shut down the application and its containers:
```sh
docker -compose down  
```
API Documentation: Access Swagger UI at http://localhost:8080/swagger-ui.html.
Test the APIs using Postman or any HTTP client tool.

## 📜 Troubleshooting
- Docker Issues: Ensure Docker Desktop is running. Restart Docker if any containers fail to start.
- Port Conflicts: Check if the ports specified in the .env file (e.g., 8080 for the app, 3306 for MySQL) are available. Modify them in .env if necessary.
- Enjoy your journey with the Bookshop application! 📚


