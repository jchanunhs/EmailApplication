# EmailApplication
Client-Server Email Application that uses multi threading to handle multiple request

Database name = EmailDB
Tables and Attributes

Account
- varchar Email (PK)
- varchar Password
- varchar Name

Email

- varchar SendFrom - The sender
- varchar Mail - The message to recipient
- varchar Date
- varchar Time
- varchar SendTo - Reciever of the mail 

When we search for the client's email: 
String SQL_Command = "SELECT * FROM EMAIL WHERE SendTo = '" + SendTo + "'" + "ORDER BY 'Date','Time' ASC";


Description of program

Client
- Main windows contains 2 tabs for client to view their emails or send them.
- Inbox uses multi threading to refresh inbox every 5 seconds.
- Email is displayed in a JTable.
- Each row contains the sender's email address, their message and date when email was sent

Server
- Use of multi threading to accept multiple connections.
- Contains access to Database and the queries necessary to create account, login, signup, get and send emails.
