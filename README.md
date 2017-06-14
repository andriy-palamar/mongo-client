# mongo-client intro
Alternative mongo client, which interprets SQL queries into the mongo ones.

It`s CLI application, which interprets SQL queries, sends to MongoDB and returns response in JSON representation.

# Requirements
- JRE v.8
- MongoDB 3+

# Before run app
- create database with name 'mev'. 
If you put another name, then go to application.properties and change "spring.data.mongodb.database"

# Steps to run app
- start MongoDB connection
- run spring boot application (class with main MongoClientApplication.java)
- write queries and enjoy!

# Examples of queries

- Select * from user where name = "Andrii" or age > 20;
- SELECT * from user where age > 20 ORDER BY age limit 2 offset 1;
- SELECT name, surname from user where age > 20 ORDER BY age limit 2 offset 1;