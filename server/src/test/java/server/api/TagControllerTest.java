package server.api;

import commons.BankAccount;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import server.service.BankAccountService;
import server.service.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagControllerTest {

    @Mock
    private TagService service;

    @InjectMocks
    TagController controller;

    private Tag t1;
    private Tag t2;


    @BeforeEach
    public void setUp() {
        this.t1 = new Tag("Test", 10, 10, 10, null);
        this.t2 = new Tag("Test", 11, 11, 11, null);
    }

    @Test
    public void getAllTest() {
        when(service.getAll()).thenReturn(new ArrayList<>());
        assertEquals(new ArrayList<>(), controller.getAll());
        verify(service).getAll();
        assertEquals(0, controller.getAll().size());
        when(service.getAll()).thenReturn(List.of(t1, t2));
        assertTrue(controller.getAll().containsAll(List.of(t1, t2)));
    }

    @Test
    public void getByIdTest() {
        when(service.getById(1L)).thenReturn(Optional.of(t1));
        ResponseEntity<Tag> found = controller.getById(1L);
        verify(service).getById(1L);
        assertEquals(HttpStatusCode.valueOf(200), found.getStatusCode());
        assertEquals(t1, found.getBody());
        ResponseEntity<Tag> notFound = controller.getById(-1L);
        assertEquals(ResponseEntity.badRequest().build(), notFound);
        assertEquals(HttpStatusCode.valueOf(400), notFound.getStatusCode());
        assertNull(notFound.getBody());
    }

    @Test
    public void addTest() {
        when(service.save(t2)).thenReturn(
                new Tag("Test", 11, 11, 11, null));
        Tag tag = controller.add(t2).getBody();
        verify(service).save(t2);
        assertEquals(t2, tag);
        assertNotSame(t2, tag);
        ResponseEntity<Tag> notFound = controller.add(null);
        assertEquals(ResponseEntity.badRequest().build(), notFound);
        assertEquals(HttpStatusCode.valueOf(400), notFound.getStatusCode());
    }

    @Test
    public void deleteTest() {
        when(service.remove(2L)).thenReturn(Optional.of(t2));
        ResponseEntity<Tag> t2 = controller.delete(2L);
        verify(service).remove(2L);
        assertEquals(HttpStatusCode.valueOf(200), t2.getStatusCode());
        ResponseEntity<Tag> notFound = controller.delete(-1L);
        assertEquals(ResponseEntity.badRequest().build(), notFound);
        assertEquals(HttpStatusCode.valueOf(400), notFound.getStatusCode());
    }

    @Test
    public void updateTest() {
        Tag updatedTag = new Tag("Updated", 1, 1, 1, null);
        when(service.getById(1L)).thenReturn(Optional.of(t1));
        when(service.save(t1)).thenReturn(updatedTag);
        ResponseEntity<Tag> t1 = controller.update(1L, new Tag("Updated", 1, 1, 1, null));
        verify(service).getById(1L);
        verify(service).save(this.t1);
        assertEquals(updatedTag.getName(), t1.getBody().getName());
        assertEquals(updatedTag.getRed(), t1.getBody().getRed());
        assertEquals(updatedTag.getBlue(), t1.getBody().getBlue());
        assertEquals(updatedTag.getGreen(), t1.getBody().getGreen());
        assertEquals(HttpStatusCode.valueOf(200), t1.getStatusCode());
        ResponseEntity<Tag> t2 = controller.update(-1L, new Tag());
        assertEquals(t2, ResponseEntity.badRequest().build());
        assertEquals(HttpStatusCode.valueOf(400), t2.getStatusCode());
    }

    @Test
    public void saveOrUpdateSTest() {
        when(service.getAll()).thenReturn(List.of(t2));
        when(service.save(t1)).thenReturn(t1);
        ResponseEntity<Tag> res = controller.saveOrUpdate(t1);
        assertEquals(t1, res.getBody());
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
        verify(service).save(t1);
    }

    @Test
    public void saveOrUpdateUTest() {
        when(service.getAll()).thenReturn(List.of(t1, t2));
        when(service.save(t1)).thenReturn(t1);
        when(service.getById(1L)).thenReturn(Optional.of(t1));
        t1.setId(1L);
        ResponseEntity<Tag> res = controller.saveOrUpdate(t1);
        assertEquals(t1, res.getBody());
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
        verify(service).save(t1);
    }

    @Test
    public void nullOrEmptyTest() {
        String n = null;
        String e = "";
        String f = "full";
        assertTrue(TagController.isNullOrEmpty(n));
        assertTrue(TagController.isNullOrEmpty(e));
        assertFalse(TagController.isNullOrEmpty(f));
    }

}