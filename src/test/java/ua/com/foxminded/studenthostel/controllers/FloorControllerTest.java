package ua.com.foxminded.studenthostel.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.studenthostel.controllers.handlers.ExceptionController;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.service.FloorService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class FloorControllerTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private FloorController floorController;

    @Autowired
    private ExceptionController exceptionController;

    @Mock
    private FloorService floorService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(floorController, exceptionController)
                .build();
    }

    @Test
    public void insert_GET_ShouldReturnInsertFormView_WhenConditionComplete() throws Exception {
        mockMvc.perform(get("/floors/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("floors/floor-insert"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfMessage_WhenEntryInserted() throws Exception {
        Mockito.when(floorService.insert(getNullIdFloor())).thenReturn(ONE);

        mockMvc.perform(post("/floors/insert")
                .param("name", "Testname"))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Adding completed."))
                .andExpect(model().attribute("id", "New ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfError_WhenEntryNotInserted() throws Exception {
        Mockito.when(floorService.insert(getNullIdFloor())).thenThrow(DaoException.class);

        mockMvc.perform(post("/floors/insert")
                .param("name", "Testname"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(floorService.getById(ONE)).thenReturn(getFloor());

        mockMvc.perform(get("/floors/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("floors/floor-info"))
                .andExpect(model().attribute("floor", getFloor()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(floorService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/floors/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Floor> floors = Collections.singletonList(getFloor());
        Mockito.when(floorService.getAll(0, 10)).thenReturn(floors);

        mockMvc.perform(get("/floors/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("floors/floors-list"))
                .andExpect(model().attribute("floors", floors));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(floorService.getAll(0, 10)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/floors/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(floorService.getById(ONE)).thenReturn(getFloor());

        mockMvc.perform(get("/floors/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("floors/floor-update"))
                .andExpect(model().attribute("floor", getFloor()));
    }

    @Test
    public void update_GET_ShouldReturnErrorView_WhenEntryNotExist() throws Exception {
        Mockito.when(floorService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/floors/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(floorService.update(getFloor())).thenReturn(getFloor());

        mockMvc.perform(post("/floors/update/1")
                .param("name", "Testname")
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Updated ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(floorService.update(getFloor())).thenThrow(DaoException.class);

        mockMvc.perform(post("/floors/update/1")
                .param("name", "Testname")
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void delete_ShouldReturnViewOfMessage_WhenEntryDeleted() throws Exception {
        Mockito.doNothing().when(floorService).deleteById(ONE);

        mockMvc.perform(post("/floors/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Deleting complete"))
                .andExpect(model().attribute("id", "Deleted floor id = " + ONE))
                .andExpect(view().name("message"));
    }

    static Floor getFloor() {
        Floor floor = getNullIdFloor();
        floor.setId(BigInteger.ONE);

        return floor;
    }

    static Floor getNullIdFloor() {
        Floor floor = new Floor();
        floor.setName("Testname");

        return floor;
    }
}
