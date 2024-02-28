import org.junit.jupiter.api.Test;
import queues.LinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * A test class for testing the concatenate method of a LinkedQueue.
 */
public class Exercise3Test {

  /**
   * Tests the concatenate method of a LinkedQueue.
   */
  @Test
  void concatenateNonEmptyQueues() {
    LinkedQueue<String> q1 = new LinkedQueue<>();
    q1.enqueue("a");
    q1.enqueue("b");
    q1.enqueue("c");

    LinkedQueue<String> q2 = new LinkedQueue<>();
    q2.enqueue("x");
    q2.enqueue("y");
    q2.enqueue("z");

    q1.concatenate(q2);

    assertEquals(6, q1.size());
    assertEquals(0, q2.size());
    assertTrue(q2.isEmpty());

    assertEquals("a", q1.dequeue());
    assertEquals("b", q1.dequeue());
    assertEquals("c", q1.dequeue());
    assertEquals("x", q1.dequeue());
    assertEquals("y", q1.dequeue());
    assertEquals("z", q1.dequeue());
  }

  /**
   *
   */
  @Test
  void concatenateEmptyQueues() {
    LinkedQueue<String> q1 = new LinkedQueue<>();
    LinkedQueue<String> q2 = new LinkedQueue<>();

    q1.concatenate(q2);

    assertEquals(0, q1.size());
    assertEquals(0, q2.size());
    assertTrue(q1.isEmpty());
    assertTrue(q2.isEmpty());
  }

  /**
   * test concatenate empty queue with non empty queue
   */
  @Test
  void concatenateEmptyQueueWithNonEmptyQueue() {
    LinkedQueue<String> q1 = new LinkedQueue<>();
    LinkedQueue<String> q2 = new LinkedQueue<>();
    q2.enqueue("x");
    q2.enqueue("y");
    q2.enqueue("z");

    q1.concatenate(q2);

    assertEquals(3, q1.size());
    assertEquals(0, q2.size());
    assertTrue(q2.isEmpty());

    assertEquals("x", q1.dequeue());
    assertEquals("y", q1.dequeue());
    assertEquals("z", q1.dequeue());
  }

  /**
   * test concatenate non empty queue with empty queue
   */
  @Test
  void concatenateNonEmptyQueueWithEmptyQueue() {
    LinkedQueue<String> q1 = new LinkedQueue<>();
    q1.enqueue("a");
    q1.enqueue("b");
    q1.enqueue("c");

    LinkedQueue<String> q2 = new LinkedQueue<>();

    q1.concatenate(q2);

    assertEquals(3, q1.size());
    assertEquals(0, q2.size());
    assertTrue(q2.isEmpty());

    assertEquals("a", q1.dequeue());
    assertEquals("b", q1.dequeue());
    assertEquals("c", q1.dequeue());
  }

  /**
   * Compares the runtime of the optimized concatenate method with the runtime
   * of the non-optimized concatenate method, and asserts that the optimized
   * concatenate method is faster.
   */
  @Test
  void concatenateRuntimeFasterThanNonOptimizedConcatenation() {
    for (int count = 1000000; count <= 5000000; count += 1000000) {
      System.out.println();
      System.out.println("Count: " + count);
      final long nonOptimizedRuntime = nonOptimizedConcatenateRuntime(count);
      final long optimizedRuntime = optimizedConcatenateRuntime(count);
      System.out.println("Non-optimized runtime: " + nonOptimizedRuntime);
      System.out.println("Optimized runtime: " + optimizedRuntime);
      assertTrue(nonOptimizedRuntime > optimizedRuntime);
    }
  }

  /**
   * Concatenates the contents of the source list into the target list without
   * using the optimized concatenate method.
   * @param target the list into which the elements of the source are to be
   * @param source the list from which the elements are to be concatenated; it
   */
  private <E> void nonOptimizedConcatenate(LinkedQueue<E> target, LinkedQueue<E> source) {
    while (!source.isEmpty()) {
      target.enqueue(source.dequeue());
    }
  }

  /**
   * Returns the runtime of the non-optimized concatenate method by concatenating
   * count elements into an empty list.
   * @param count the number of elements to concatenate
   * @return the runtime of the non-optimized concatenate method
   */
  private long nonOptimizedConcatenateRuntime(int count) {
    final LinkedQueue<Integer> source = new LinkedQueue<>();
    final LinkedQueue<Integer> target = new LinkedQueue<>();
    for (int i = 0; i < count; i++) {
      source.enqueue(i);
    }
    final long start = System.currentTimeMillis();
    nonOptimizedConcatenate(target, source);
    final long end = System.currentTimeMillis();
    return end - start;
  }

  /**
   * Returns the runtime of the optimized concatenate method by concatenating
   * count elements into an empty list.
   * @param count the number of elements to concatenate
   * @return the runtime of the optimized concatenate method
   */
  private long optimizedConcatenateRuntime(int count) {
    final LinkedQueue<Integer> source = new LinkedQueue<>();
    final LinkedQueue<Integer> target = new LinkedQueue<>();
    for (int i = 0; i < count; i++) {
      source.enqueue(i);
    }
    final long start = System.currentTimeMillis();
    target.concatenate(source);
    final long end = System.currentTimeMillis();
    return end - start;
  }

}
