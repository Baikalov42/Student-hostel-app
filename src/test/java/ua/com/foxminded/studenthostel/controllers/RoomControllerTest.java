package ua.com.foxminded.studenthostel.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.dto.RoomDTO;
import ua.com.foxminded.studenthostel.service.EquipmentService;
import ua.com.foxminded.studenthostel.service.RoomService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.studenthostel.controllers.EquipmentControllerTest.getEquipment;
import static ua.com.foxminded.studenthostel.controllers.FloorControllerTest.getFloor;

@SpringJUnitWebConfig(WebConfig.class)
class RoomControllerTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private RoomController roomController;

    @Autowired
    private ExceptionController exceptionController;

    @Mock
    private RoomService roomService;

    @Mock
    private EquipmentService equipmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(roomController, exceptionController)
                .build();
    }
    @Test
    public void insert_GET_ShouldReturnInsertFormView_WhenConditionComplete() throws Exception {
        mockMvc.perform(get("/rooms/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/room-insert"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfMessage_WhenEntryInserted() throws Exception {
        Mockito.when(roomService.insert(getNullIdRoom())).thenReturn(ONE);

        mockMvc.perform(post("/rooms/insert")
                .param("name", "Testname")
                .param("floorId", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Adding completed."))
                .andExpect(model().attribute("id", "New ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfError_WhenEntryNotInserted() throws Exception {
        Mockito.when(roomService.insert(getNullIdRoom())).thenThrow(DaoException.class);

        mockMvc.perform(post("/rooms/insert")
                .param("name", "Testname")
                .param("floorId", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(roomService.getDTOById(BigInteger.ONE))
                .thenReturn(getRoomDTO());

        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/room-info"))
                .andExpect(model().attribute("roomDTO", getRoomDTO()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(roomService.getDTOById(BigInteger.ONE))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Room> rooms = Collections.singletonList(getRoom());
        Mockito.when(roomService.getAll(10, 0))
                .thenReturn(rooms);

        mockMvc.perform(get("/rooms/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/rooms-list"))
                .andExpect(model().attribute("rooms", rooms));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(roomService.getAll(10, 0))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/rooms/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByEquipment_ShouldReturnViewListOfEquipments_WhenEquipmentsExists() throws Exception {
        List<Equipment> equipments = Collections.singletonList(getEquipment());
        Mockito.when(equipmentService.getAll(10, 0))
                .thenReturn(equipments);

        mockMvc.perform(get("/rooms/byEquipment/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/rooms-by-equipment"))
                .andExpect(model().attribute("equipments", equipments));
    }

    @Test
    public void getAllByEquipment_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(equipmentService.getAll(10, 0))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/rooms/byEquipment/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByEquipmentResult_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<RoomDTO> rooms = Collections.singletonList(getRoomDTO());
        Mockito.when(roomService.getAllByEquipment(getEquipment().getId()))
                .thenReturn(rooms);

        mockMvc.perform(get("/rooms/byEquipment/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/rooms-list"))
                .andExpect(model().attribute("rooms", rooms));
    }

    @Test
    public void getAllByEquipmentResult_ShouldReturnViewOfError_WhenEntriesNotExists() throws Exception {

        Mockito.when(roomService.getAllByEquipment(getEquipment().getId()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/rooms/byEquipment/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(roomService.getById(ONE)).thenReturn(getRoom());

        mockMvc.perform(get("/rooms/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("rooms/room-update"))
                .andExpect(model().attribute("room", getRoom()));
    }

    @Test
    public void update_GET_ShouldReturnErrorView_WhenEntryNotExist() throws Exception {
        Mockito.when(roomService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/rooms/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(roomService.update(getRoom())).thenReturn(true);

        mockMvc.perform(post("/rooms/update/1")
                .param("name", "Testname")
                .param("floorId", ONE.toString())
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Updated ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(roomService.update(getRoom())).thenThrow(DaoException.class);

        mockMvc.perform(post("/rooms/update/1")
                .param("name", "Testname")
                .param("floorId", ONE.toString())
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void delete_ShouldReturnViewOfMessage_WhenEntryDeleted() throws Exception {
        Mockito.when(roomService.deleteById(ONE)).thenReturn(true);

        mockMvc.perform(post("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Deleting complete"))
                .andExpect(model().attribute("id", "Deleted room id = " + ONE))
                .andExpect(view().name("message"));
    }

    static Room getNullIdRoom() {
        Room room = new Room();
        room.setFloorId(BigInteger.ONE);
        room.setName("Testname");

        return room;
    }
    static Room getRoom() {
        Room room = getNullIdRoom();
        room.setId(BigInteger.ONE);

        return room;
    }

    static RoomDTO getRoomDTO() {

        Room room = getRoom();

        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setFloor(getFloor());
        roomDTO.setStudentsCount(ONE.intValue());
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());

        return roomDTO;
    }

}
