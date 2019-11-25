# Coding Exercise User Management

## Instructions: 
You have 3 hours to complete the assignment (partial/full).
The outcome should be a project that we will be able to import/compile/run (Github is preferred).
You should use Java.
You can use any tool/framework to complete this assignment.
The project should include unit tests with maximum coverage.
Entities should be persisted to any kind of repository/DB.
For any question, you can contact guy.zuckerman@verizonmedia.com or dima.rassin@verizonmedia.com or ary.dvoretz@verizonmedia.com 

Implement REST-full __Java__ service for user management.

The user entity should have the following fields:
* EMail (unique)
* First name
* Last name
* Password __(cannot be stored as a plain text)

The service should provide the following __RESTFUL__ API:
* Create
* Update: every field except the password
* Delete
* ChangePassword: requires to provide the previous password
* GetByEmail: NOTE the password should not be returned
* Login: given email and password, preferable as basic authentication. Should return in case of success message “Welcome FirstName LastName!” otherwise “Access denied”

:exclamation: Bonus:
The project should be built as a docker image.
