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
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.service.EquipmentService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringJUnitWebConfig(WebConfig.class)
class EquipmentControllerTest {

    MockMvc mockMvc;

    @Autowired
    @InjectMocks
    EquipmentController equipmentController;
    @Autowired
    ExceptionController exceptionController;
    @Mock
    EquipmentService equipmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(equipmentController, exceptionController)
                .build();
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(equipmentService.getById(BigInteger.ONE)).thenReturn(getEquipment());

        mockMvc.perform(get("/equipments/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipments/equipment-info"))
                .andExpect(model().attribute("equipment", getEquipment()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(equipmentService.getById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/equipments/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Equipment> equipments = Collections.singletonList(getEquipment());
        Mockito.when(equipmentService.getAll(10, 0)).thenReturn(equipments);

        mockMvc.perform(get("/equipments/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipments/equipments-list"))
                .andExpect(model().attribute("equipments", equipments));
    }
    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(equipmentService.getAll(10, 0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/equipments/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    static Equipment getEquipment() {
        Equipment equipment = new Equipment();
        equipment.setId(BigInteger.ONE);
        equipment.setName("Testname");

        return equipment;
    }
}
