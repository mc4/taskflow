package com.wakefernfoodcorp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class TaskQueue {

	private final Map<String, Task> taskMap = new HashMap<>();
	private final Map<String, List<String>> dependencyGraph = new HashMap<>();
	private final PriorityQueue<Task> taskQueue = new PriorityQueue<>();
	private final Map<String, Node> nodes = new HashMap<>();

	public void addTask(String taskId, int priority, List<String> dependencies) {
		for (String dependency : dependencies) {
			if (!taskMap.containsKey(dependency)) {
				throw new IllegalArgumentException("Dependency " + dependency + " does not exist");
			}
		}
		Task task = new Task(taskId, priority, dependencies);
		taskMap.put(taskId, task);
		dependencyGraph.put(taskId, dependencies);
		taskQueue.add(task);
	}

	public List<String> getExecutionOrder() {
		List<String> executionOrder = new ArrayList<>();
		Set<String> executedTasks = new HashSet<>();
		PriorityQueue<Task> readyQueue = new PriorityQueue<>(taskQueue);

		while (!readyQueue.isEmpty()) {
			Task task = readyQueue.poll();
			if (executedTasks.containsAll(task.getDependencies())) {
				executionOrder.add(task.getTaskId());
				executedTasks.add(task.getTaskId());
			} else {
				readyQueue.add(task);
			}
		}
		return executionOrder;
	}

	public void removeTask(String taskId) {
	    Objects.requireNonNull(taskId, "Task ID cannot be null");
		taskMap.remove(taskId);
		dependencyGraph.remove(taskId);
		taskQueue.removeIf(task -> task.getTaskId().equals(taskId));
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
		nodes.put(nodeId, new Node(nodeId));
	}

	public boolean isNodeRegistered(String nodeId) {
		return nodes.containsKey(nodeId);
	}

}
