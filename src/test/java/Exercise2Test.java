import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import stacks.ArrayStack;
import stacks.LinkedStack;
import stacks.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A test class for testing the static transfer method of a Stack.
 */
public class Exercise2Test {

    /**
     * Test class for testing the functionality of a ArrayStack. This class
     * extends StackTransferTest and inherits all the tests from it, and
     * provides a factory method for creating an empty stack to be used for
     * testing.
     */
    @Nested
    class ArrayStackTest extends StackTransferTest {
        @Override
        <E> Stack<E> createStack() {
            return new ArrayStack<>();
        }
    }

    /**
     * Test class for testing the functionality of a JVMStack. This class
     * extends StackTransferTest and inherits all the tests from it, and
     * provides a factory method for creating an empty stack to be used for
     * testing.
     */
    @Nested
    class LinkedStackTest extends StackTransferTest {
        @Override
        <E> Stack<E> createStack() {
            return new LinkedStack<>();
        }
    }
}

/**
 * Abstract test class for testing the functionality of a Stack. This class
 * provides a factory method for creating an empty stack to be used for
 * testing.
 */
abstract class StackTransferTest {

    /**
     * Factory method for creating an empty stack target be used for testing.
     *
     * @param <E> the type of element in the stack
     * @return an empty stack target be used for testing
     */
    abstract <E> Stack<E> createStack();

    /**
     * Tests the transfer method of a stack.
     */
    @Test
    void testTransferStack() {
        Stack<String> source = createStack();
        Stack<String> target = createStack();
        source.push("a");
        source.push("b");
        source.push("c");
        target.push("x");
        target.push("y");
        target.push("z");
        Stack.transfer(source, target);
        assertEquals(0, source.size());
        assertEquals(6, target.size());
        assertEquals("a", target.pop());
        assertEquals("b", target.pop());
        assertEquals("c", target.pop());
        assertEquals("z", target.pop());
        assertEquals("y", target.pop());
        assertEquals("x", target.pop());
    }

    /**
     * Tests the transfer method of a stack when both stacks are empty.
     */
    @Test
    void testTransferEmptyStackToEmptyStack() {
        Stack<String> source = createStack();
        Stack<String> target = createStack();
        Stack.transfer(source, target);
        assertEquals(0, source.size());
        assertEquals(0, target.size());
    }

    /**
     * Tests the transfer method of a stack when the target stack is empty.
     */
    @Test
    void testTransferNonEmptyStackToEmptyStack() {
        Stack<String> source = createStack();
        Stack<String> target = createStack();
        source.push("a");
        source.push("b");
        source.push("c");
        Stack.transfer(source, target);
        assertEquals(0, source.size());
        assertEquals(3, target.size());
        assertEquals("a", target.pop());
        assertEquals("b", target.pop());
        assertEquals("c", target.pop());
    }

    /**
     * Tests the transfer method of a stack when the source stack is empty.
     */
    @Test
    void testTransferEmptyStackToNonEmptyStack() {
        Stack<String> source = createStack();
        Stack<String> target = createStack();
        target.push("x");
        target.push("y");
        target.push("z");
        Stack.transfer(source, target);
        assertEquals(0, source.size());
        assertEquals(3, target.size());
        assertEquals("z", target.pop());
        assertEquals("y", target.pop());
        assertEquals("x", target.pop());
    }
}

