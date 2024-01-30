JavaTaskManager
---------------

A sample Java-based web API application for managing personal tasks, using Java, Maven, and Spring Boot

## Overview and Motivation

For server-side work I'm primarily a C# developer (and more recently Python).  I've done Java work in the past (as evidenced by other Github Java projects) but it had been a while (and several Java version iterations).  Hence I wanted to write a simple web API application to refresh my knowledge of latest Java and Spring Boot.

JavaTaskManager is as the name suggests... a simple web API application providing CRUD methods for personal tasks (i.e. TODO items, reminders, etc...).

## Tooling / Dependencies / Setup

I used the following tools and packages in building...

| Item | Version | Notes |
| ---- | ------- | ----- |
| JDK  | 21      | https://www.oracle.com/jp/java/technologies/downloads/#java21 |
| Maven | 3.6.3 | https://maven.apache.org/download.cgi |
| Visual Studio Code | 1.85.2 | |
| Extension Pack for Java | v0.25.15 | Visual Studio Code extension - https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack |
 
## Building

The repository contains two separate Maven Java projects...

| Folder | Description |
| ------ | ----------- |
| service | Class library that provides the core CRUD functionality for tasks via the DefaultTaskManager class |
| api | A Spring Boot which exposes the CRUD functionality via a REST API |

The 'api' project references the 'service' project via Maven, specifically the following section of the pom.xml file...

```
<dependency>
	<groupId>net.alastairwyse.taskmanager</groupId>
	<artifactId>taskmanager</artifactId>
	<version>0.9.2</version>
</dependency>
```

For the 'api' project to be able to import the 'service' project, it must exist in your local Maven repository (in 'C:\\Users\\\[username\]\\.m2\repository\\' by default on Windows).  To publish the 'service' project to your local repository, run this command from the folder containing the 'service' project 'pom.xml' file (make sure the maven 'bin' directory is included in your path first)...

```
mvn install
```

## Running Tests

In Visual Studio Code with the 'Extension Pack for Java' extension installed, the tests for both projects can be run from the test 'beaker' (conical flask??) icon in the activity bar.

## Running 

If using the Visual Studio Code, the simplest way to run is via the 'Run and Debug' icon in the activity bar (and then the 'Run and Debug' button).  The Swagger page for the application should then be available at the below URL...

```
http://localhost:8080/swagger-ui/index.html
```

## Spring Boot and ASP.NET Core Comparison

The table below compares some of the key annotations and classes used in building a Spring Boot web application, and the equivalent in ASP.NET Core...

| Java Element | Example | C# Equivalent | Purpose |
| ------------ | ------- | ------------- | ------- |
| [@OpenAPIDefinition](https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/io/swagger/v3/oas/annotations/OpenAPIDefinition.html) | | [WebApplicationBuilder.Services.AddSwaggerGen()](https://learn.microsoft.com/en-us/aspnet/core/tutorials/getting-started-with-swashbuckle?view=aspnetcore-7.0&tabs=visual-studio) | Add title, description, etc to Swagger page |
| [@Bean](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Bean.html), [@Scope](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Scope.html) | | [AddScoped(), AddSingleton()](https://learn.microsoft.com/en-us/aspnet/core/fundamentals/dependency-injection?view=aspnetcore-8.0) etc... | Dependency injection |
| [WebMvcConfigurer.addInterceptors()](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurer.html)<br/>[InterceptorRegistry.addInterceptor()](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/InterceptorRegistry.html)<br/>[HandlerInterceptor](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/HandlerInterceptor.html) | | [IApplicationBuilder.UseMiddleware<T>()](https://learn.microsoft.com/en-us/aspnet/core/fundamentals/middleware/write?view=aspnetcore-8.0) | Intercept the handler execution chain / request pipeline |
| [SpringApplication.run()](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/SpringApplication.html) |  | [WebApplication.Run()](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.builder.webapplication?view=aspnetcore-8.0) | Runs/starts the web application |
| [@ExceptionHandler](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/ExceptionHandler.html) |  | [IApplicationBuilder.UseExceptionHandler()](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.builder.exceptionhandlerextensions.useexceptionhandler?view=aspnetcore-8.0) | Specify custom exception handling |
| [ErrorController](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/web/servlet/error/ErrorController.html) |  | N/A | Override the Spring Boot 'whitelabel' error page |
| [@RestController](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html) |  | [\{ApiController\]](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.apicontrollerattribute?view=aspnetcore-8.0) | Define a controller class |
| [@RequestMapping](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RequestMapping.html) |  | [\{Route\]](https://learn.microsoft.com/en-us/aspnet/core/mvc/controllers/routing?view=aspnetcore-8.0#attribute-routing-for-rest-apis) | Map request URLs to controller methods |
| [@Tag](https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/io/swagger/v3/oas/annotations/tags/Tag.html) |  | [\[ApiExplorerSettings(GroupName)\]](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.apiexplorersettingsattribute?view=aspnetcore-8.0) | Specify the name of a group of endpoints in the Swagger page |
| [@Operation](https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/io/swagger/v3/oas/annotations/Operation.html) |  | [XML comments](https://learn.microsoft.com/en-us/aspnet/core/tutorials/getting-started-with-swashbuckle?view=aspnetcore-7.0&tabs=visual-studio#xml-comments) on controller method | Documentation in Swagger page |
| [@PostMapping](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/PostMapping.html), [@PutMapping](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/PutMapping.html), [@GetMapping](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/GetMapping.html), etc... |  | [\[HttpGet\]](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.httpgetattribute?view=aspnetcore-8.0), [\[HttpPost\]](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.httppostattribute?view=aspnetcore-8.0), etc... | Define HTTP method on controller methods |
| [@ApiResponse](https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/io/swagger/v3/oas/annotations/responses/ApiResponse.html) |  | [\[ProducesResponseType\]](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.producesresponsetypeattribute?view=aspnetcore-8.0) | Specify HTTP status codes returned by controller methods |
| [@RequestBody](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RequestBody.html) |  | [\[FromBody\]](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.frombodyattribute?view=aspnetcore-8.0) | Specify that controller method parameter should be taken from the HTTP request body |
| [@PathVariable](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/PathVariable.html) |  | [\[FromRoute\]](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.fromrouteattribute?view=aspnetcore-8.0) | Specify that controller method parameter should be taken from the request URL |
| [@SpringBootTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/context/SpringBootTest.html) |  | [WebApplicationFactory<TEntryPoint>](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.testing.webapplicationfactory-1?view=aspnetcore-8.0) | Define and run integration / wnd-to-end tests (i.e. tests which run a real instance of the web API application, and test via HTTP requests) |

## TODO

* DTO classes are a concern of the 'api' project, not the 'service' project, so should be moved to that project.