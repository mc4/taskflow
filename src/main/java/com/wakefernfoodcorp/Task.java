package com.wakefernfoodcorp;

import java.util.List;
import java.util.Objects;

public class Task implements Comparable<Task> {

	private String taskId;
	private int priority;
	private List<String> dependencies;

	public Task(String taskId, int priority, List<String> dependencies) {
		this.taskId = taskId;
		this.priority = priority;
		this.dependencies = dependencies;
	}

	@Override
	public int compareTo(Task other) {
		return Integer.compare(other.priority, this.priority);
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dependencies, priority, taskId);
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
		Task other = (Task) obj;
		return Objects.equals(dependencies, other.dependencies) && priority == other.priority
				&& Objects.equals(taskId, other.taskId);
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", priority=" + priority + ", dependencies=" + dependencies + "]";
	}

}
