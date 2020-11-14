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
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.service.EquipmentService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class EquipmentControllerTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private EquipmentController equipmentController;
    @Autowired
    private ExceptionController exceptionController;
    @Mock
    private EquipmentService equipmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(equipmentController, exceptionController)
                .build();
    }

    @Test
    public void insert_GET_ShouldReturnInsertFormView_WhenConditionComplete() throws Exception {
        mockMvc.perform(get("/equipments/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipments/equipment-insert"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfMessage_WhenEntryInserted() throws Exception {
        Mockito.when(equipmentService.insert(getNullIdEquipment())).thenReturn(ONE);

        mockMvc.perform(post("/equipments/insert")
                .param("name", "Testname"))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Adding completed."))
                .andExpect(model().attribute("id", "New ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfError_WhenEntryNotInserted() throws Exception {
        Mockito.when(equipmentService.insert(getNullIdEquipment())).thenThrow(DaoException.class);

        mockMvc.perform(post("/equipments/insert")
                .param("name", "Testname"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(equipmentService.getById(ONE)).thenReturn(getEquipment());

        mockMvc.perform(get("/equipments/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipments/equipment-info"))
                .andExpect(model().attribute("equipment", getEquipment()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(equipmentService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/equipments/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Equipment> equipments = Collections.singletonList(getEquipment());
        Mockito.when(equipmentService.getAll(0, 10)).thenReturn(equipments);

        mockMvc.perform(get("/equipments/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipments/equipments-list"))
                .andExpect(model().attribute("equipments", equipments));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(equipmentService.getAll(0, 10)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/equipments/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(equipmentService.getById(ONE)).thenReturn(getEquipment());

        mockMvc.perform(get("/equipments/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("equipments/equipment-update"))
                .andExpect(model().attribute("equipment", getEquipment()));
    }

    @Test
    public void update_GET_ShouldReturnErrorView_WhenEntryNotExist() throws Exception {
        Mockito.when(equipmentService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/equipments/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(equipmentService.update(getEquipment())).thenReturn(getEquipment());

        mockMvc.perform(post("/equipments/update/1")
                .param("name", "Testname")
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Updated ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(equipmentService.update(getEquipment())).thenThrow(DaoException.class);

        mockMvc.perform(post("/equipments/update/1")
                .param("name", "Testname")
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void delete_ShouldReturnViewOfMessage_WhenEntryDeleted() throws Exception {
        Mockito.doNothing().when(equipmentService).deleteById(ONE);

        mockMvc.perform(post("/equipments/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Deleting complete"))
                .andExpect(model().attribute("id", "Deleted equipment id = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void delete_ShouldReturnViewOfError_WhenEntryNotDeleted() throws Exception {
        doThrow(DaoException.class).when(equipmentService).deleteById(ONE);

        mockMvc.perform(post("/equipments/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

     static Equipment getEquipment() {
        Equipment equipment = getNullIdEquipment();
        equipment.setId(ONE);

        return equipment;
    }

     static Equipment getNullIdEquipment() {
        Equipment equipment = new Equipment();
        equipment.setName("Testname");

        return equipment;
    }
}
