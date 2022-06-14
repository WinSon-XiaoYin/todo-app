## Simple Task Management tool
This is a simple task management tool, and it implemented both client and server services. It uses Java + Spring Boot + H2 Database to provide backend service, all thet tasks will be stored into H2 Database. For the client service, it uses Python to provide command line user interface, then users can manage tasks via executing tasks.py file with parameters.

### High Level Architecture
![img.png](architecture.png)

### Restful APIs
Swagger url: `http://localhost:8080/swagger-ui/index.html`
1. `POST /v1/api/tasks`    - _Create TODO task_
2. `GET /v1/api/tasks` - _Retrieve all tasks_
3. `GET /v1/api/tasks/{id}` - _Inquiry task details_
4. `PATCH /v1/api/tasks/{id}` - _Update task status (including a simple state machine inside)_

### How to run this project
#### _**1. Prerequisites**_
Ubuntu 18.04, JDK 8/11, Maven 3, H2 Database, Python3

#### _**2. Launch backend service**_
1. `mvn clean`

2. `mvn package`

3. `java -jar target/TODO-0.0.1-SNAPSHOT.jar`

or executing shell script to start: `./start.sh`
and pressing Ctrl-C to stop


#### _**3. Using Command Line Client to interact**_
Install dependency
`pip3 install -r requirements.txt`

*Usage:*
1. Help
`python3 tasks.py -h`

2. Add task
`python3 tasks.py add <summary> <due_date>`

3. List all tasks
`python3 tasks.py list`

4. List all tasks that will expire today
`python3 tasks.py list --expiring-today`
or
`python3 tasks.py list -et`

5. Complete task
`python3 tasks.py done <task_id>`

   
### Simple State Machine for task status
```mermaid
stateDiagram-v2
    direction LR
    [*] --> TODO: submit task 
    TODO-->INPROGRESS: start task
    INPROGRESS-->DONE: complete task
    DONE-->INPROGRESS: rollback task
    INPROGRESS-->TODO: rollback task
    TODO-->DONE: complete task
```


### Sequence Diagram
```mermaid
sequenceDiagram
    title: Simple Task Management Tool

    participant User
    participant Command Line Interface
    participant Backend Application
    participant H2 Database

    note right of User: Task creation flow
    User->>Command Line Interface: creating todo task with task summary and task due date
    Command Line Interface->>Backend Application: POST /v1/api/tasks to create todo task
    Backend Application->>Backend Application: due date checking
    alt if due date format is invalid
        Backend Application-->>Command Line Interface: return 400 Bad Request with invalid due date format error message
        Command Line Interface-->>User: show invalid due date format error message
    else
        alt if due date is earlier than today
            Backend Application-->>Command Line Interface: return 406 Not Applicable with task due date must be later than or equals to today error message
        Command Line Interface-->>User: show task due date must be later than or equals to today error message
        else
            Backend Application->>H2 Database: save todo task into DB
            H2 Database-->>Backend Application: 
            Backend Application-->>Command Line Interface: return 201 created and task detail url in the header
            Command Line Interface-->>User: return Success message
        end
    end
    

    

    note right of User: Task list flow
    User->>Command Line Interface: input command "task list"
    Command Line Interface->>Backend Application: GET /v1/api/tasks to retrieve all the tasks
    Backend Application->>H2 Database: get tasks from DB
    H2 Database-->>Backend Application: 
    Backend Application-->>Command Line Interface: return 200 and task list
    Command Line Interface-->>User: show task list

    note right of User: Complete task flow
    User->>Command Line Interface: input command "done <task_id>"
    Command Line Interface->>Backend Application: PATCH /v1/api/tasks/<task_id> with action: "DONE" to update task status
    Backend Application->>H2 Database: get task from DB
    H2 Database-->>Backend Application: 
    alt if task is not found
        Backend Application-->>Command Line Interface: return 404 Task is not found
        Command Line Interface-->>User: show task is not found error message
    end
    Backend Application->>Backend Application: Check whether a valid action for current task
    alt if invalid action for current task
        Backend Application-->>Command Line Interface: return 406 Not Applicable
        Command Line Interface-->>User: show invlid action error message
    else
        Backend Application->>H2 Database: update task status to done
        H2 Database-->>Backend Application: 
        Backend Application-->>Command Line Interface: return 200
        Command Line Interface-->>User: show Success message
    end
```




