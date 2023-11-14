# byte-works
The GeoByte Project is a dynamic full-stack web app that combines React and Spring Boot for efficient delivery management and optimized routes. With React on the frontend, users experience a seamless interface, while Spring Boot empowers the backend for robust delivery process handling. Security is reinforced using JWT, providing a secure environment for sensitive data.

The application offers insights into cost-efficient and expensive routes for informed decision-making. Furthermore, it utilizes an H2 in-memory database, enhancing data management and accessibility.

#### Please Note
The project is still in development not in production. For this reason it is not in the `main` branch of the repository but `develop` branch.

## Installation and Set Up
### Front-end - React
- Clone the repository
- run the command
`git clone`

https://github.com/Kanurow/byte-works
Gives you a copy of the project on your local drive. Change directory into the folder named 'frontend' and geobyte/geobyte-task folder.

- Install dependencies
Run the command below to install project dependencies

`npm install`

then start the project with 

`npm start`

- The server port will run on your local host at  `http://localhost:3000`

### Backend - Spring Boot
*Note: The project was built as a `Maven` Project not `Gradle`.
The cloned repository contains your backend code. 
Change directory (cd) into byteworks

Run the codes below from your terminal to install all dependencies 

`./mvnw clean install`

To start the project 

`./mvnw spring-boot:run`

The backend server port will run on port 8080 at the local host below 

`http://localhost:8080`
