# Getting Started

### Frameworks

* Spring Boot
* Spring WebFlux
* WireMock
* Swagger

### How to Run

* mvn spring-boot:run

### URLs

* http://localhost:8080/user/octocat
* http://localhost:8080/swagger-ui.html

### Explanation

I went with Spring Boot as it is the main framework I am familiar with. I originally thought about using just regular
Spring MVC Rest, but I decided to try WebFlux instead. I hadn't used it in quite a while, and I wanted to brush up on
it. In large scale applications, the reactive programming model can be more performant. The mono.zip function call kicks
off both of the calls in parallel so that alone can be more formant than calling both sequentially. I also included a
swagger doc for reference and ease of use.

The testing of it was interesting. I originally was thinking about using Mockito to just mock the WebClient, but after
reviewing what all would go into it I decided to use WireMock instead. It is a bit more heavy weight, but I think it
still works well for the unit tests. It's the extensibility of being able to mock any request/response that drew me to
it.

### Architecture

I decided to go with a standard N tier architecture for this. It's pretty straight forward. I could have put the two
fetch methods into a "repository" class, but I thought that might overcomplicate it.
