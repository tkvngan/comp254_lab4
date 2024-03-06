import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.jupiter.api.Test;
import queues.LinkedQueue;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for testing the concatenate method of a LinkedQueue.
 */
public class Exercise3Test {

    /**
     * Helper factory method for creating a queue with the given elements.
     * @param elements the elements to be added to the queue
     * @return a queue with the given elements
     */
    static <E> LinkedQueue<E> createQueue(E... elements) {
        final LinkedQueue<E> queue = new LinkedQueue<>();
        for (E element : elements) {
            queue.enqueue(element);
        }
        return queue;
    }

    /**
     * Asserts that the size of the queue is as expected, and that the queue is
     * empty if and only if the expected size is 0.
     * @param queue the queue to be tested
     * @param expectedSize the expected size of the queue
     */
    static <E> void assertQueueSize(LinkedQueue<E> queue, int expectedSize) {
        assertEquals(expectedSize, queue.size());
        assertEquals(expectedSize == 0, queue.isEmpty());
    }

    /**
     * Asserts that the queue is empty.
     * @param queue the queue to be tested
     */
    static <E> void assertQueueEmpty(LinkedQueue<E> queue) {
        assertQueueSize(queue, 0);
    }

    /**
     * Asserts that the queue contains the expected elements in the expected order,
     * and that the queue is empty after the expected elements have been dequeued.
     * @param queue the queue to be tested
     * @param elements the expected elements in the expected order
     */
    static <E> void assertQueueEquals(LinkedQueue<E> queue, E... elements) {
        assertQueueSize(queue, elements.length);
        for (E element : elements) {
            assertEquals(element, queue.dequeue());
        }
    }

    /**
     * Test to concatenate two non-empty queues
     */
    @Test
    void concatenateQueues() {
        final LinkedQueue<String> targetQueue = createQueue("a", "b", "c");
        final LinkedQueue<String> sourceQueue = createQueue("x", "y", "z");
        targetQueue.concatenate(sourceQueue);
        assertQueueSize(targetQueue, 6);
        assertQueueEquals(targetQueue, "a", "b", "c", "x", "y", "z");
        assertQueueEmpty(sourceQueue);
    }

    /**
     * Test to concatenate two empty queues
     */
    @Test
    void concatenateEmptyQueues() {
        final LinkedQueue<String> targetQueue = createQueue();
        final LinkedQueue<String> sourceQueue = createQueue();
        targetQueue.concatenate(sourceQueue);
        assertQueueEmpty(targetQueue);
        assertQueueEmpty(sourceQueue);
    }

    /**
     * Test to concatenate empty queue with non empty queue
     */
    @Test
    void concatenateEmptyQueueWithNonEmptyQueue() {
        final LinkedQueue<String> targetQueue = createQueue();
        final LinkedQueue<String> sourceQueue = createQueue("x", "y", "z");
        targetQueue.concatenate(sourceQueue);
        assertQueueSize(targetQueue, 3);
        assertQueueEquals(targetQueue, "x", "y", "z");
        assertQueueEmpty(sourceQueue);
    }

    /**
     * Test to concatenate non empty queue with empty queue
     */
    @Test
    void concatenateNonEmptyQueueWithEmptyQueue() {
        final LinkedQueue<String> targetQueue = createQueue("a", "b", "c");
        final LinkedQueue<String> sourceQueue = createQueue();
        targetQueue.concatenate(sourceQueue);
        assertQueueSize(targetQueue, 3);
        assertQueueEquals(targetQueue, "a", "b", "c");
        assertQueueEmpty(sourceQueue);
    }

    /**
     * Compares the runtime behaviors of the optimized concatenate method with
     * the non-optimized concatenate method. The comparison is based on the mean,
     * standard deviation, and correlation of the runtimes of the two methods.
     * This method asserts that the runtime of the optimized method is much faster,
     * less variable, and less sensitive to the number of elements to be concatenated,
     * than that of the non-optimized method. It also asserts that the runtime
     * complexity of the non-optimized method is linear, i.e. O(n), whereas the
     * runtime complexity of the optimized method is constant, i.e. O(1).
     *
     * <p>
     * Note, this test is time sensitive and must be run with the -Xint flag to
     * disable the JIT compiler to avoid the effects of JIT compilation altering
     * the timing results. The test is also sensitive to the system load, and
     * the test may fail due to unexpected reasons. Therefore, it is recommended
     * to run the test multiple times to get a better understanding of the runtime
     * behaviors of the two methods.
     */
    @Test
    void analyzeRuntimeBehaviorsOfOptimizedAndNonOptimizedConcatenation() {
        final int sampleSize = 1000;
        final int sampleIncrement = 10;
        final double[] x = new double[sampleSize];  // number of elements to be concatenated
        final double[] y1 = new double[sampleSize]; // runtime of optimized method
        final double[] y2 = new double[sampleSize]; // runtime of non-optimized method
        System.out.print("Runtime Behavior Analysis:\n\n");
        System.out.printf("%15s %20s %20s\n", "", "Optimized", "Non-optimized");
        System.out.printf("%15s %20s %20s\n", "Count", "Runtime(ns)", "Runtime(ns)");
        System.out.printf("%15s %20s %20s\n", "------", "-------------", "-----------");
        for (int i = 0; i < sampleSize; i++) {
            final int count = (i + 1) * sampleIncrement;
            final long optimizedRuntime = executeConcatenationWithTiming(count, this::optimizedConcatenate);
            final long nonOptimizedRuntime = executeConcatenationWithTiming(count, this::nonOptimizedConcatenate);
            System.out.printf("%,15d %,20d %,20d\n", count, optimizedRuntime, nonOptimizedRuntime);
            x[i] = count;
            y1[i] = optimizedRuntime;
            y2[i] = nonOptimizedRuntime;
        }

        Mean mean = new Mean();
        StandardDeviation std = new StandardDeviation();
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        final double mean_y1 = mean.evaluate(y1);
        final double mean_y2 = mean.evaluate(y2);
        final double std_y1 = std.evaluate(y1);
        final double std_y2 = std.evaluate(y2);
        final double correlation_x_y1 = correlation.correlation(x, y1);
        final double correlation_x_y2 = correlation.correlation(x, y2);
        System.out.println();
        System.out.printf("%15s %,20.4f %,20.4f\n", "Mean:", mean_y1, mean_y2);
        System.out.printf("%15s %,20.4f %,20.4f\n", "STD:", std_y1, std_y2);
        System.out.printf("%15s %,20.4f %,20.4f\n", "Correlation:", correlation_x_y1, correlation_x_y2);

        assertTrue(mean_y1 < mean_y2);
        assertTrue(std_y1 < std_y2);
        assertTrue(correlation_x_y1 < correlation_x_y2);

        final double O_n_threshold = 0.95;
        final double O_1_threshold = 0.35;
        assertTrue(correlation_x_y1 < O_1_threshold);
        assertTrue(correlation_x_y2 > O_n_threshold);

        System.out.print(
            """
                
                Conclusion:
                 
                1. The runtime of the optimized method is much faster than that of the
                non-optimized method as seen by comparing their means.
                """);
        System.out.print(
            """
                
                2. The runtime of the optimized method is less variable than that of
                the non-optimized method as seen by by comparing their standard deviations.
                This suggests that the runtime of the optimized method is not sensitive to
                the number of elements to be concatenated, whereas the runtime of non-optimized
                method is very sensitive to the number of elements to be concatenated.
                """);
        System.out.print(
            """
                
                3. The runtime of the non-optimized method has a very high correlation with
                the number of elements to be concatenated. The correlation coefficient
                is close to 1, which means its runtime complexity is linear, i.e. O(n).
                Whereas the correlation coefficient of the optimized method is very small,
                meaning that its runtime complexity is constant, i.e. O(1).
                """);
    }

    /**
     * Concatenates the contents of the source list into the target list using
     * the optimized concatenate method developed according to EXERCISE 3.
     *
     * @param target the list into which the elements of the source are to be
     *               concatenated
     * @param source the list from which the elements are to be concatenated; it
     *               is emptied by this method
     */
    private void optimizedConcatenate(LinkedQueue<Integer> target, LinkedQueue<Integer> source) {
        target.concatenate(source);
    }

    /**
     * Concatenates the contents of the source list into the target list without
     * using the optimized concatenate method. It is implemented using a simple
     * while loop that dequeues elements from the source and enqueues them into
     * the target.
     *
     * @param target the list into which the elements of the source are to be
     *               concatenated
     * @param source the list from which the elements are to be concatenated; it
     *               is emptied by this method
     */
    private void nonOptimizedConcatenate(LinkedQueue<Integer> target, LinkedQueue<Integer> source) {
        while (!source.isEmpty()) {
            target.enqueue(source.dequeue());
        }
    }

    /**
     * Returns the runtime of executing the supplied concatenation method with
     * the given count of elements.
     *
     * @param count         the number of elements to be concatenated
     * @param concatenation the concatenation method to be executed
     * @return the runtime of executing the supplied concatenation method in
     * nanoseconds
     */
    private long executeConcatenationWithTiming(int count, BiConsumer<LinkedQueue<Integer>, LinkedQueue<Integer>> concatenation) {
        /* Create two empty queues */
        final LinkedQueue<Integer> source = createQueue();
        final LinkedQueue<Integer> target = createQueue();
        for (int i = 0; i < count; i++) {       // Populate the source queue with
            source.enqueue(i);                  // count number of elements
        }
        final long start = System.nanoTime();   // Save the start time
        concatenation.accept(target, source);   // Execute the concatenation
        return System.nanoTime() - start;       // Return the runtime
    }

}
