package server.service;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository repository;

    @InjectMocks
    private TagService service;

    private Tag tag;

    @BeforeEach
    public void setUp() {
        this.tag = new Tag("Test", 10, 10, 10, null);
    }

    @Test
    public void getAllEmptyTest() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, service.getAll().size());
        verify(repository).findAll();
    }

    @Test
    public void getAllTest() {
        when(repository.findAll()).thenReturn(List.of(tag));
        assertTrue(service.getAll().contains(tag));
        verify(repository).findAll();
    }

    @Test
    public void getByIdTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(tag));
        Optional<Tag> gotten = service.getById(1L);
        assertTrue(gotten.isPresent());
        assertEquals(tag, gotten.get());
        verify(repository).findById(1L);
    }

    @Test
    public void existsTest() {
        when(repository.existsById(2L)).thenReturn(false);
        when(repository.existsById(3L)).thenReturn(true);
        boolean isThere3 = service.exists(3L);
        boolean isThere2 = service.exists(2L);
        assertTrue(isThere3);
        assertFalse(isThere2);
    }

    @Test
    public void saveTest() {
        when(repository.saveAndFlush(tag)).thenReturn(tag);
        Tag actual = service.save(tag);
        assertEquals(tag, actual);
        verify(repository).saveAndFlush(tag);
    }

    @Test
    public void removeTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(tag));
        service.remove(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    public void noRemoveTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        service.remove(1L);
        verify(repository, times(0)).deleteById(tag.getId());
    }

    @Test
    public void testRestore() {
        when(repository.saveAndFlush(tag)).thenReturn(tag);
        service.restore(tag);
        verify(repository).saveAndFlush(tag);
    }
}
