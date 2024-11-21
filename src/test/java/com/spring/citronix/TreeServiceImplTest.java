package com.spring.citronix;

import com.spring.citronix.domain.Field;
import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Tree;
import com.spring.citronix.repository.TreeRepository;
import com.spring.citronix.service.imp.TreeServiceImpl;
import com.spring.citronix.web.errors.tree.TreeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TreeServiceImplTest {

    @Mock
    private TreeRepository treeRepository;

    private TreeServiceImpl treeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        treeService = new TreeServiceImpl(treeRepository);
    }

    private Field createDummyField() {
        Farm farm = new Farm();
        farm.setId(UUID.randomUUID());
        farm.setArea(10000);

        Field field = new Field();
        field.setId(UUID.randomUUID());
        field.setArea(5000);
        field.setFarm(farm);

        Tree tree1 = new Tree();
        tree1.setId(UUID.randomUUID());
        tree1.setField(field);

        Tree tree2 = new Tree();
        tree2.setId(UUID.randomUUID());
        tree2.setField(field);

        field.setTrees(Arrays.asList(tree1, tree2));

        return field;
    }


    @Test
    public void testSaveTreeDensityExceeds() {

        Field field = createDummyField();
        List<Tree> existingTrees = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            Tree tree = new Tree();
            tree.setId(UUID.randomUUID());
            tree.setField(field);
            existingTrees.add(tree);
        }

        when(treeRepository.findByFieldId(field.getId())).thenReturn(existingTrees);

        Tree newTree = new Tree();
        newTree.setId(UUID.randomUUID());
        newTree.setField(field);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> treeService.save(newTree));
        assertTrue(exception.getMessage().contains("Tree density exceeds"));
    }

    @Test
    public void testFindByIdTreeFound() {
        Field field = createDummyField();
        Tree tree = new Tree();
        tree.setId(UUID.randomUUID());
        tree.setField(field);

        when(treeRepository.findById(tree.getId())).thenReturn(Optional.of(tree));

        Optional<Tree> foundTree = treeService.findById(tree.getId());

        assertTrue(foundTree.isPresent());
        assertEquals(tree.getId(), foundTree.get().getId());
    }

    @Test
    public void testFindByIdTreeNotFound() {
        UUID treeId = UUID.randomUUID();

        when(treeRepository.findById(treeId)).thenReturn(Optional.empty());

        Optional<Tree> foundTree = treeService.findById(treeId);

        assertFalse(foundTree.isPresent());
    }

    @Test
    public void testFindAllTrees() {
        Field field = createDummyField();

        Tree tree1 = new Tree();
        tree1.setField(field);
        Tree tree2 = new Tree();
        tree2.setField(field);

        List<Tree> trees = Arrays.asList(tree1, tree2);

        when(treeRepository.findAll()).thenReturn(trees);

        List<Tree> result = treeService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFieldIdWithPagination() {
        Field field = createDummyField();

        Tree tree1 = new Tree();
        tree1.setField(field);
        Tree tree2 = new Tree();
        tree2.setField(field);

        List<Tree> trees = Arrays.asList(tree1, tree2);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Tree> treePage = new PageImpl<>(trees);

        when(treeRepository.findByFieldId(field.getId(), pageRequest)).thenReturn(treePage);

        Page<Tree> result = treeService.findByFieldId(field.getId(), pageRequest);

        assertEquals(2, result.getContent().size());
        verify(treeRepository, times(1)).findByFieldId(field.getId(), pageRequest);
    }

    @Test
    public void testDeleteTree() {
        Field field = createDummyField();

        Tree tree = new Tree();
        tree.setId(UUID.randomUUID());
        tree.setField(field);

        treeService.delete(tree);

        verify(treeRepository, times(1)).delete(tree);
    }

    @Test
    public void testCalculateProductivityTreeNotFound() {
        UUID treeId = UUID.randomUUID();

        when(treeRepository.findById(treeId)).thenReturn(Optional.empty());

        TreeNotFoundException exception = assertThrows(TreeNotFoundException.class, () -> treeService.calculateProductivity(treeId));
        assertTrue(exception.getMessage().contains("Tree not found with ID"));
    }
}
