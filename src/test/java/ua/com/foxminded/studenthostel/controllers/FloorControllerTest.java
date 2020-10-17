package ua.com.foxminded.studenthostel.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.studenthostel.config.WebConfig;
import ua.com.foxminded.studenthostel.controllers.handlers.ExceptionController;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.service.FloorService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringJUnitWebConfig(WebConfig.class)
class FloorControllerTest {

    MockMvc mockMvc;

    @Autowired
    @InjectMocks
    FloorController floorController;
    @Autowired
    ExceptionController exceptionController;
    @Mock
    FloorService floorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(floorController, exceptionController)
                .build();
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(floorService.getById(BigInteger.ONE)).thenReturn(getFloor());

        mockMvc.perform(get("/floors/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("floors/floor-info"))
                .andExpect(model().attribute("floor", getFloor()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(floorService.getById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/floors/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Floor> floors = Collections.singletonList(getFloor());
        Mockito.when(floorService.getAll(10, 0)).thenReturn(floors);

        mockMvc.perform(get("/floors/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("floors/floors-list"))
                .andExpect(model().attribute("floors", floors));
    }
    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(floorService.getAll(10, 0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/floors/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    static Floor getFloor() {
        Floor floor = new Floor();
        floor.setId(BigInteger.ONE);
        floor.setName("Testname");

        return floor;
    }
}
