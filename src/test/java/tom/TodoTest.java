package tom;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void constructor_initialState() {
        Todo todo = new Todo("read book");

        assertEquals("read book", todo.getDescription());
        assertFalse(todo.isDone());
        assertEquals(" ", todo.getStatus()); // 未完成时 status icon 是空格
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void markAndUnmark_changesStatusAndToString() {
        Todo todo = new Todo("read book");

        todo.mark();
        assertTrue(todo.isDone());
        assertEquals("X", todo.getStatus());
        assertEquals("[T][X] read book", todo.toString());

        todo.unmark();
        assertFalse(todo.isDone());
        assertEquals(" ", todo.getStatus());
        assertEquals("[T][ ] read book", todo.toString());
    }
}