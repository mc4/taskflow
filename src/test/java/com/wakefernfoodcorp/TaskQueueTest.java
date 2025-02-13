package com.wakefernfoodcorp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskQueueTest {

	private TaskQueue taskQueue;

	@BeforeEach
	void setUp() {
		taskQueue = new TaskQueue();
	}

	// Test adding a task with dependencies
	@Test
	public void testAddTask() {
		taskQueue.addTask("A", 1, Collections.emptyList());
		assertEquals(1, taskQueue.getExecutionOrder().size());
	}

	@Test
	void testAddTaskWithDependencies() {
		taskQueue.addTask("A", 1, Collections.emptyList());
		taskQueue.addTask("B", 1, Collections.singletonList("A"));

		List<String> order = taskQueue.getExecutionOrder();
		assertEquals(List.of("A", "B"), order);
	}

	// Test registering a node
	@Test
	public void testRegisterNode() {
		taskQueue.registerNode("Node-1");
		assertTrue(taskQueue.isNodeRegistered("Node-1"), "Node-1 should be registered");
	}

	// Test task execution order based on priority
	@Test
	void testTaskPriority() {
		taskQueue.addTask("A", 2, Collections.emptyList());
		taskQueue.addTask("B", 1, Collections.emptyList());
		List<String> order = taskQueue.getExecutionOrder();
		assertEquals(List.of("A", "B"), order);
	}

	// Test execution order with multiple dependencies
	@Test
	public void testExecutionOrder() {
		taskQueue.addTask("A", 3, Collections.emptyList());
		taskQueue.addTask("B", 2, List.of("A"));
		taskQueue.addTask("C", 1, List.of("A"));
		taskQueue.addTask("D", 3, List.of("B", "C"));
		assertEquals(List.of("A", "B", "C", "D"), taskQueue.getExecutionOrder());
	}

	@Test
	void testExecutionOrderWithMultipleDependencies() {
		taskQueue.addTask("A", 1, Collections.emptyList());
		taskQueue.addTask("B", 2, Collections.singletonList("A"));
		taskQueue.addTask("C", 3, List.of("A", "B"));

		List<String> order = taskQueue.getExecutionOrder();
		assertEquals(List.of("A", "B", "C"), order);
	}

	@Test
	public void testExecutionOrderWithTwoTaskAndOneDependency() {
		taskQueue.addTask("B", 2, List.of());
		taskQueue.addTask("C", 1, List.of("B"));
		assertEquals(List.of("B", "C"), taskQueue.getExecutionOrder());
	}

	// Testing creating a Task with null constructor parameters
	@Test
	public void testInvalidTaskWithNullTaskId() {
		assertThrows(NullPointerException.class, () -> taskQueue.addTask(null, 3, List.of()));
	}

	@Test
	public void testInvalidTaskWithNullPriority() {
		Integer priority = null;
		assertThrows(NullPointerException.class, () -> taskQueue.addTask("ZZZ", priority, List.of()));
	}

	@Test
	public void testInvalidTaskWithNullDependencies() {
		assertThrows(NullPointerException.class, () -> taskQueue.addTask("ZZZ", 77, null));
	}

	@Test
	public void testEmptyQueue() {
		List<String> order = taskQueue.getExecutionOrder();
		assertTrue(order.isEmpty(), "Execution order should be empty");
	}

	@Test
	public void testRemoveTask() {
		taskQueue.addTask("A", 1, Collections.emptyList());
		taskQueue.removeTask("A");
		assertFalse(taskQueue.getExecutionOrder().contains("A"));
	}

	@Test
	public void testUpdateTask() {
		taskQueue.addTask("A", 1, Collections.emptyList());
		taskQueue.updateTask("A", 2, Collections.emptyList());
		List<String> order = taskQueue.getExecutionOrder();
		assertTrue(order.indexOf("A") < order.size());
	}

	private void setupTaskQueueForNodeFailureTests() {
		taskQueue.registerNode("Node-1");
		taskQueue.registerNode("Node-2");
		taskQueue.addTask("A", 1, Collections.emptyList());
		taskQueue.addTask("B", 2, Collections.singletonList("A"));
		taskQueue.addTask("C", 3, List.of("A", "B"));
	}

	// Test handling task dependencies when a node fails
	@Test
	public void testNodeFailureWithDependenciesByCounting() {
		setupTaskQueueForNodeFailureTests();

		taskQueue.assignTasksToNodes();
		taskQueue.handleNodeFailure("Node-1");

		// Verify that failed tasks are reassigned
		taskQueue.assignTasksToNodes();

		assertEquals(taskQueue.getAvailableNodeIds().size(), 1);
	}

	@Test
	public void testNodeFailureWithDependenciesByNodeId() {
		setupTaskQueueForNodeFailureTests();
		taskQueue.assignTasksToNodes();
		taskQueue.handleNodeFailure("Node-1");

		// Verify that failed tasks are reassigned
		taskQueue.assignTasksToNodes();
		assertEquals(taskQueue.getAvailableNodeIds().toString(), "[Node-2]");
	}

	@Test
	void testMissingDependencyShouldThrowException() {
		taskQueue.addTask("A", 3, Collections.emptyList());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			taskQueue.addTask("B", 2, List.of("A", "X"));
		});

		assertEquals("Dependency X does not exist", exception.getMessage());
	}

}