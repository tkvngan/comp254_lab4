import lists.LinkedPositionalList;
import lists.Position;
import lists.PositionalList;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Exercise1Test {


  /**
   * A test class for testing the functionality of a LinkedPositionalList. This
   * class extends PositionalListTest and inherits all the tests from it, and
   * provides a factory method for creating an empty list to be used for testing.
   */
  @Nested
  class LinkedPositionalListTest extends PositionalListTest {

    @Override
    protected <E> PositionalList<E> createList() {
      return new LinkedPositionalList<>();
    }
  }

}

/**
 * An abstract test class for testing the functionality of a PositionalList.
 */
abstract class PositionalListTest {

  /**
   * Factory method for creating an empty list to be used for testing.
   *
   * @return an empty list to be used for testing
   */
  abstract <E> PositionalList<E> createList();

  /**
   * An empty list to be used for testing.
   */
  final PositionalList<String> list = createList();

  /**
   * Asserts that the index of a position in a single element list is as
   * expected.
   */
  @Test
  void indexOfSingleElementList() {
    Position<String> first = list.addFirst("Java is great!");
    assertEquals(0, list.indexOf(first));
  }

  /**
   * Asserts that the index of a position in a multiple element list is as
   * expected.
   */
  @Test
  void indexOfMultipleElementList() {
    Position<String> first = list.addFirst("Hello");
    Position<String> second = list.addLast("World");
    Position<String> third = list.addLast("How are you?");
    assertEquals(0, list.indexOf(first));
    assertEquals(1, list.indexOf(second));
    assertEquals(2, list.indexOf(third));
  }

  /**
   * Asserts that the index of a position in a list with elements removed before
   * it is as expected.
   */
  @Test
  void indexOfPositionWithOtherElementsRemoved() {
    // A list of three elements with positions as javaScript, python and java
    Position<String> javaScript = list.addFirst("JavaScript is bad!");
    Position<String> python = list.addLast("Python is bad!");
    Position<String> java = list.addLast("Java is good!");

    // Assert that java is at index position 2
    assertEquals(2, list.indexOf(java));

    // Remove an element before java, and assert that java is still there with
    // index position changed to 1
    list.remove(javaScript);
    assertEquals(1, list.indexOf(java));

    // Remove an element before java, and assert that java is still there with
    // index position changed to 0
    list.remove(python);
    assertEquals(0, list.indexOf(java));
  }

  /**
   * Asserts that the index of a position in a list with elements added before
   * it is as expected.
   */
  @Test
  void indexOfPositionWithOtherElementsAdded() {
    Position<String> java = list.addFirst("Java is good!");
    assertEquals(0, list.indexOf(java));

    // Add an element before java and assert that the index position
    // of java is changed to 1
    list.addFirst("JavaScript is bad!");
    assertEquals(1, list.indexOf(java));

    // Add another element before java and assert that the
    // index position of java is now 2
    list.addFirst("Python is bad!");
    assertEquals(2, list.indexOf(java));
  }

  /**
   * Asserts that the index of a position which has been removed is -1.
   */
  @Test
  void indexOfRemovedPositionShouldThrowException() {
    Position<String> hello = list.addFirst("Hello");
    Position<String> world = list.addLast("World");

    // Remove position hello and assert that getting index of
    // hello will throw IllegalArgumentException.
    list.remove(hello);
    assertThrows(IllegalArgumentException.class, () -> list.indexOf(hello));
  }

  /**
   * Asserts that the index of a null position is -1.
   */
  @Test
  void indexOfNullPosition() {
    // Assert that the index of a null position is -1
    assertEquals(-1, list.indexOf(null));
  }
}
