# GitHubRepoInfoServer
# Version 1.1
what changed:
* When we ask for the name of an organization that does not exist, an error system is thrown and status code 500 is returned, I fixed the bug by adding handling of a status code that is incorrect. 
* I changed the name of the function RepositoryInfo to repositoryInfo
* I changed the printing to logger when exiting the repository info function


# Running the GitHub Repository Info Server

Follow these steps to run the GitHub Repository Info Server and access the Swagger documentation:

Open a Terminal or Command Prompt:
------------
On your computer, open a terminal or command prompt window. You can usually find this by searching for "Terminal" (macOS/Linux) or "Command Prompt" (Windows) in your operating system's search bar.

Navigate to the JAR File Directory:
------------
In the terminal or command prompt, use the cd command to navigate to the directory where you downloaded the JAR file which can be found in "release" directory. For example:

cd /path/to/directory
Replace /path/to/directory with the actual path to the directory containing the JAR file.

Run the JAR File:
------------
Enter the following command to run the GitHub Repository Info Server:

java -jar GitHubRepoInfoServer.jar

Access Swagger Documentation:
------------
After the server has started, open a web browser and go to the following URL:

http://localhost:8080/swagger-ui/index.html
This URL will open the Swagger UI interface, allowing you to explore the API documentation for the GitHub Repository Info Server.

Interact with the API:
------------
In the Swagger UI, you can browse the available API endpoints, send requests, and view responses. Use the provided input fields to test different scenarios and see the expected results.

![image](https://github.com/shirMax/GitHubRepoInfoServer/assets/110455848/4d166b05-85f4-4d95-acf5-b0c9017d4e86)

![image](https://github.com/shirMax/GitHubRepoInfoServer/assets/110455848/053c0845-f210-4d16-a217-2bad4a2e0fd0)


![image](https://github.com/shirMax/GitHubRepoInfoServer/assets/110455848/eb5fa2eb-edbc-40a0-b0e4-9fcdd399a80d)
