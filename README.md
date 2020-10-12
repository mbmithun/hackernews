# HackerNews
A Spring boot application to fetch stories and comments from Hackernews and exposes three different endpoints and are described below. The application also makes use of in-memory caching so as to reduce the number of hits to the [data server](https://hacker-news.firebaseio.com/) and stores all the served top-stories in the mongo-db.

### API Endpoints

```
GET /api/v0/top-stories
GET /api/v0/top-comments?storyId=<story-id>
GET /api/v0/past-stories?page=0&size=10         // Page and size params are optional. Defaults are 0 and 10 respectively
```  
 
 ## Environment
 
 - Java 8
 - Maven
 - Spring Boot
 - Caffeine Cache
 - MongoDB
 - Docker
 - Docker-Compose
 
 
The application code is production ready and no changes would be needed. The server port exposed is 8080 and the server address is the host address/localhost.
