# EncyclopediaApp

Wiki-like web application, developed for collaborative documentation of a specific topic (video game). In the development of the project, the Java programming language was used with the Spring Boot framework for the server side and JavaScript together with React for the client side. Data is stored in both MongoDB, for articles and categories, and in MariaDB, for user management.

## Components

### Article
The main page of an article, the structure is made using a rich text editor which adds HTML for the user. In the top right corner, there are four buttons. The "Edit" button redirects the user to the editing page, "History" allows the users to see all modifications made to the article, "Add Category" is used for adding a category and "Delete" will archieve the article, action that can only be performed by moderators and admins.

![Article - Content from https://elderscrolls.fandom.com/wiki/Unbound](https://i.imgur.com/Zj0rOfi.png)

### Article Comments
Article comments are a component of articles that allows users to discuss about the article and leave feedback about the content.

![Article Comments](https://i.imgur.com/8hsyZvd.png)

### User Profiles

Every user has a profile attached to his account, with permission to modify personal information. The data of a profile are protected and can only be modified by the owner of the profile or by an admin.

![User Profile](https://i.imgur.com/JeFMyOc.png)

### Recent changes
Recent changes represents a way for users to vizualize all changes made to articles in order to keep track of their evolution. Recent changes are sorted by the date they were made, from the most recent to the oldest.

![Recent Changes](https://i.imgur.com/GcjjxpS.png)

### Moderator Dashboard
Moderator Dashboard is the place where moderators can access their respective tools. They can create categories and vizualize deleted articles and their content.

![Moderator Dashboard](https://i.imgur.com/DT21Fsx.png)