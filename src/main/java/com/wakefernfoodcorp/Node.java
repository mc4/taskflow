package com.wakefernfoodcorp;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Node {
	private String nodeId;
	private Queue<Task> assignedTasks = new LinkedList<>();

	public Node(String nodeId) {
		this.nodeId = nodeId;
	}

	public void assignTask(Task task) {
		assignedTasks.offer(task);
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Queue<Task> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(Queue<Task> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	@Override
	public int hashCode() {
		return Objects.hash(assignedTasks, nodeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Node other = (Node) obj;
		return Objects.equals(assignedTasks, other.assignedTasks) && Objects.equals(nodeId, other.nodeId);
	}

	@Override
	public String toString() {
		return "Node [nodeId=" + nodeId + ", assignedTasks=" + assignedTasks + "]";
	}

}
