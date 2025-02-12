package com.wakefernfoodcorp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class TaskQueueTest {
	@Test
	public void testAddTask() {
		TaskQueue queue = new TaskQueue();
		queue.addTask("A", 1, List.of());
		assertEquals(1, queue.getExecutionOrder().size());
	}

	@Test
	public void testRegisterNode() {
		TaskQueue queue = new TaskQueue();
		queue.registerNode("Node-1");
		assertTrue(queue.isNodeRegistered("Node-1"), "Node-1 should be registered");
	}
	
	@Test
	public void testExecutionOrder() {
		TaskQueue queue = new TaskQueue();
		queue.addTask("A", 3, List.of());
		queue.addTask("B", 2, List.of("A"));
		queue.addTask("C", 1, List.of("A"));
		queue.addTask("D", 3, List.of("B", "C"));
		assertEquals(List.of("A", "B", "C", "D"), queue.getExecutionOrder());
	}
	
	@Test
	public void testInvalidTaskWithNullTaskId() {
		TaskQueue queue = new TaskQueue();
		assertThrows(NullPointerException.class, () -> queue.addTask(null, 3, List.of()));
	}
	
	@Test
	public void testInvalidTaskWithNullPriority() {
		TaskQueue queue = new TaskQueue();
		Integer priority = null;
		assertThrows(NullPointerException.class, () -> queue.addTask("ZZZ", priority, List.of()));
	}
	
	@Test
	public void testInvalidTaskWithNullDependencies() {
		TaskQueue queue = new TaskQueue();
		assertThrows(NullPointerException.class, () -> queue.addTask("ZZZ", 77, null));
	}
	
}