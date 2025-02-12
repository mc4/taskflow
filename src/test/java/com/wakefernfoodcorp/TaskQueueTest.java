package com.wakefernfoodcorp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class TaskQueueTest {
	@Test
	void testAddTask() {
		TaskQueue queue = new TaskQueue();
		queue.addTask("A", 1, List.of());
		assertEquals(1, queue.getExecutionOrder().size());
	}

	@Test
	void testRegisterNode() {
		TaskQueue queue = new TaskQueue();
		queue.registerNode("Node-1");
		assertTrue(queue.isNodeRegistered("Node-1"), "Node-1 should be registered");
	}
}