http://localhost:8080/api/auth/register
body :JSON
{
"firstName": "tthanhphuc",
"lastName": "Tran",
"email": "tthanhphuc753@gmail.com",
"password": "123456"
}

http://localhost:8080/api/auth/authenticate
body:JSON
{
"email": "tthanhphuc753@gmail.com",
"password": "123456"
}
