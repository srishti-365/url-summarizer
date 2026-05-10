# URL Summarizer API

A Spring Boot REST API that fetches any URL and returns an AI-generated summary using Groq LLM.

## Tech Stack
- Java 21
- Spring Boot 3.3
- Groq API (llama-3.3-70b-versatile)
- Docker
- Maven

## Running Locally

1. Clone the repo
   git clone git@github.com:YOUR_USERNAME/url-summarizer.git
   cd url-summarizer

2. Create application-local.properties
   cp src/main/resources/application.properties.example src/main/resources/application-local.properties
   Add your Groq API key inside it

3. Run the app
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

## Running with Docker

docker build -t url-summarizer .
docker run -p 8080:8080 -e GROQ_API_KEY=your_key_here url-summarizer

## API Usage

POST /api/summarize

Request:
{
"url": "https://en.wikipedia.org/wiki/Java_(programming_language)"
}

Response:
{
"url": "https://en.wikipedia.org/wiki/Java_(programming_language)",
"summary": "Java is a high-level, class-based, object-oriented programming language..."
}

## Error Responses

400 - Invalid or empty URL
500 - Failed to fetch URL or AI service error