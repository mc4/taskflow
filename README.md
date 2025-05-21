# Task Queue

A Java-based **Task Queue** system that supports task scheduling, execution, and error handling with dependency and priority management. Designed to distribute tasks across multiple nodes, handle node failures gracefully, and dynamically manage tasks.

---

## Features

- **Task Definition**:  
  - Unique task identifiers  
  - Priority levels (integer-based)  
  - Dependencies between tasks

- **Task Execution**:  
  - Determines valid execution order respecting dependencies and priority  
  - Detects circular dependencies and missing tasks

- **Distributed Execution**:  
  - Supports multiple nodes for task assignment  
  - Handles node failures and reassigns tasks dynamically

- **Dynamic Task Management**:  
  - Add, update, and remove tasks at runtime

---

## Usage

### Adding Tasks

```java
TaskQueue queue = new TaskQueue();
queue.addTask("A", 3, Collections.emptyList());
queue.addTask("B", 2, List.of("A"));
queue.addTask("C", 1, List.of("A"));
queue.addTask("D", 3, List.of("B", "C"));

```
### Getting Execution Order

```java
List<String> order = queue.getExecutionOrder();
// Example output: [A, B, C, D]
```

### Node Management

```java
queue.registerNode("Node-1");
queue.assignTasksToNodes();
queue.handleNodeFailure("Node-1");
```

## Installation & Build
This project can be imported as a standard Java project in Eclipse or IntelliJ IDEA. For dependency management and build automation, you can convert it into a Maven project.

To run tests, use JUnit 5.
