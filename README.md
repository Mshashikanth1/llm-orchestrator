# (Refy Technical take-home assignment)
# Wrapper Service : To support , Maintain , and Access different LLM's


In this project,  i have taken the advantage of Ollama which can run 
the various llms  and provide a platform to download the open source llms
with a package manager:

# Limitations:

```
1. user authentication
2. robust session management
3. proper rag implementation with vector db
4. data base for capturing the information
5. better data structures & algorithms for accuracy 
```

# to setup ollama ( LLMS runing infra locally)
```
docker pull ollama/ollama
docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
```

# to pull the desired open source llm  in ollama docker cli user this
```
ollama pull mistral
```

# BUILD , PACKAGE & RUN

```

to build the project locally:
mvn clean install

to package the project;
mvn clean pacage

to run the orchestrator proj. locally:

method -1 : mvn sping-boot:run
method -2 :  java -r <path to jar pacage file>  
method -3 : run main function directly from intellij



```


# API Doc



1. api to check what are all the LLMs available in ollama server
```
curl --location 'http://localhost:8080/api/v1/ai/list-models' 
```
response:
```
{
    "models": [
        {
            "name": "llama3:latest"
        },
        {
            "name": "mistral:latest"
        }
    ],
    "activeLLM": "llama3",
    "statusCodeValue": 200,
    "statusCode": "OK"
}
```




3. api to change the LLM
```
curl --location --request POST 'http://localhost:8080/api/v1/ai/select/mistral' 
```
response:
```
{
    "headers": {},
    "body": {
        "models": [
            {
                "name": "llama3:latest"
            },
            {
                "name": "mistral:latest"
            }
        ],
        "activeLLM": "mistral"
    },
    "statusCodeValue": 200,
    "statusCode": "OK"
}
```




2. api to chat with the llm:
```
curl --location 'http://localhost:8080/api/v1/ai/generate?promptMessage=who%20are%20you' \
```
response:
```
{
    "message": "I am LLaMA,an AI assistant developed by Meta AI 
                that can understand and respond to human input in a conversational manner.
                I'm not a human, but a computer program designed to simulate conversation and answer questions to the best of my ability based on my training data.
                I was trained on a massive corpus of text from the internet,
                Ihich allows me to generate responses to a wide range of topics and questions.
                My training data includes a vast amount of text from various sources, including books, articles, and websites.
                I'm constantly learning and improving my responses based on the interactions I have with users like you. 
                So, please feel free to ask me anything, and I'll do my best to provide a helpful and accurate response!",
    "statusCodeValue": 200,
    "statusCode": "OK"
}
```


3. api to upload the files:
```
curl --location 'http://localhost:8080/api/v1/ai/upload' \
--form 'files=@"/Users/shashikanth/Downloads/shashikanthResume.pdf"' \
--form 'files=@"/Users/shashikanth/Downloads/newCTC.pdf"'
```
resonse:

```
{
    "message": "files uploaded sucessfully",
    "statusCodeValue": 200,
    "statusCode": "OK"
}

```


4. api to get summary:
```
curl --location 'http://localhost:8080/api/v1/ai/summary/30' \
```

resonse 
```

{
    "message": "Here is a summary of Shashi Kanth Varma's documents in 30 words:\n\n
                Software Engineer with experience in Java, Spring Boot, and Postgres. 
                Skilled in Python, Django, and Google Cloud Platforms. 
                Contributed to projects like Retrieval Augmented Generation and Social Media Backend.",
                
    "statusCodeValue": 200,
    "statusCode": "OK"
}

```

5. api to set criteria:
```
curl --location 'http://localhost:8080/api/v1/ai/set-criteria

```

resonse:

```
{
    "message": "criteria updated successfully",
    "statusCodeValue": 200,
    "statusCode": "OK"
}

```

6. api to reset criteria:
```
curl --location 'http://localhost:8080/api/v1/ai/reset-criteria

```

resonse:

```
{
    "message": "criteria updated successfully",
    "statusCodeValue": 200,
    "statusCode": "OK"
}

```

