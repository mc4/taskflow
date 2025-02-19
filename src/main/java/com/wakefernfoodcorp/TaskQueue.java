package com.wakefernfoodcorp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class TaskQueue {

	private final Map<String, Task> taskMap = new HashMap<>();
	private final Map<String, List<String>> dependencyGraph = new HashMap<>();
	private final PriorityQueue<Task> taskQueue = new PriorityQueue<>();
	private final Map<String, Node> nodes = new HashMap<>();

	public void addTask(String taskId, int priority, List<String> dependencies) {
		if (dependencies.stream().anyMatch(dep -> !taskMap.containsKey(dep))) {
			throw new IllegalArgumentException("One or more dependencies do not exist");
		}
		Task task = new Task(taskId, priority, dependencies);
		taskMap.put(taskId, task);
		dependencyGraph.put(taskId, dependencies);
		taskQueue.add(task);
	}

	public List<String> getExecutionOrder() {
		List<String> executionOrder = new ArrayList<>();
		Map<String, Integer> inDegree = new HashMap<>();
		PriorityQueue<Task> readyQueue = new PriorityQueue<>();
		Map<String, List<String>> dependencyMap = new HashMap<>();

		for (Task task : taskQueue) {
			inDegree.put(task.getTaskId(), task.getDependencies().size());
			for (String dep : task.getDependencies()) {
				if (!taskMap.containsKey(dep)) {
					throw new IllegalStateException("Task " + task.getTaskId() + " depends on missing task " + dep);
				}
				dependencyMap.computeIfAbsent(dep, k -> new ArrayList<>()).add(task.getTaskId());
			}
		}

		for (Task task : taskQueue) {
			if (inDegree.get(task.getTaskId()) == 0) {
				readyQueue.add(task);
			}
		}

		while (!readyQueue.isEmpty()) {
			Task task = readyQueue.poll();
			executionOrder.add(task.getTaskId());

			if (dependencyMap.containsKey(task.getTaskId())) {
				for (String dependentTaskId : dependencyMap.get(task.getTaskId())) {
					inDegree.put(dependentTaskId, inDegree.get(dependentTaskId) - 1);
					if (inDegree.get(dependentTaskId) == 0) {
						readyQueue.add(taskMap.get(dependentTaskId));
					}
				}
			}
		}

		if (executionOrder.size() != taskQueue.size()) {
			throw new IllegalStateException("Circular dependency detected or some tasks are missing.");
		}

		return executionOrder;
	}

	public void removeTask(String taskId) {
		Optional.ofNullable(taskId).orElseThrow(() -> new IllegalArgumentException("Task Id cannot be null"));
		taskMap.remove(taskId);
		dependencyGraph.remove(taskId);
		taskQueue.removeIf(task -> Objects.equals(task.getTaskId(), taskId));
	}

	public void updateTask(String taskId, int priority, List<String> dependencies) {
		removeTask(taskId);
		addTask(taskId, priority, dependencies);
	}

	public void assignTasksToNodes() {
		if (nodes.isEmpty()) {
			return;
		}

		List<Node> nodeList = new ArrayList<>(nodes.values());
		int index = 0;
		for (Task task : taskQueue) {
			nodeList.get(index).assignTask(task);
			index = (index + 1) % nodeList.size();
		}
	}

	public void handleNodeFailure(String nodeId) {
		if (nodes.containsKey(nodeId)) {
			Queue<Task> failedTasks = nodes.get(nodeId).getAssignedTasks();
			while (!failedTasks.isEmpty()) {
				taskQueue.offer(failedTasks.poll());
			}
			nodes.remove(nodeId);
		}
	}

	public void registerNode(String nodeId) {
		Optional.ofNullable(nodeId).orElseThrow(() -> new IllegalArgumentException("Node Id cannot be null"));
		nodes.put(nodeId, new Node(nodeId));
	}

	public boolean isNodeRegistered(String nodeId) {
		return nodeId != null ? nodes.containsKey(nodeId) : false;
	}

	public List<String> getAvailableNodeIds() {
		return nodes.keySet().stream().collect(Collectors.toList());
	}

}
