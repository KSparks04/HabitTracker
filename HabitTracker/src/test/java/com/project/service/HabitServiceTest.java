package com.project.service;

import com.project.dto.HabitDto;
import com.project.model.Habit;
import com.project.model.HabitLog;
import com.project.model.User;
import com.project.repository.HabitLogRepository;
import com.project.repository.HabitRepository;
import com.project.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HabitServiceTest {
    @Mock
    private HabitRepository habitRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HabitLogRepository habitLogRepository;
    @InjectMocks
    HabitService  habitService;

    private User user;
    private Habit habit;
    private HabitDto habitDto;

    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        habit = new Habit();
        habit.setId(10L);
        habit.setUser(user);
        habit.setTitle("Morning Jog");
        habit.setFrequency("DAILY");
        habit.setNotes("Go for a run");
        habit.setColor("#FF5733");
        habit.setIconName("Footprints");

        habitDto = new HabitDto();
        habitDto.setId(10L);
        habitDto.setTitle("Morning Jog");
        habitDto.setFrequency("DAILY");
        habitDto.setNotes("Go for a run");
        habitDto.setColor("#FF5733");
        habitDto.setIconName("Footprints");

    }



    /**
     * Retrieves all habits connected to a user
     */
    //Perfect Case
    @Test
    void getHabitsForUser() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(habitRepository.findByUserId(1L)).thenReturn(List.of(habit));

        List<HabitDto> result =
                habitService.getHabitsForUser(email);

        assertEquals(1, result.size());

        HabitDto returned = result.get(0);

        assertEquals(10L, returned.getId());
        assertEquals("Morning Jog", returned.getTitle());
        assertEquals("DAILY", returned.getFrequency());
        assertEquals("Go for a run", returned.getNotes());
        assertEquals("#FF5733", returned.getColor());
        assertEquals("Footprints", returned.getIconName());

        verify(habitRepository).findByUserId(1L);


    }
    //User has no habits
    @Test
    void getHabitsForUserWithNoHabits(){
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(habitRepository.findByUserId(1L)).thenReturn(List.of());
        List<HabitDto> result =
                habitService.getHabitsForUser(email);

        assertNotNull(result);
        assertTrue(result.isEmpty());


    }
    @Test
    void getHabitsForUserReturnsAllHabits() {
        Habit secondHabit = new Habit();
        secondHabit.setId(11L);
        secondHabit.setUser(user);
        secondHabit.setTitle("Read 20 Pages");
        secondHabit.setFrequency("DAILY");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findByUserId(1L))
                .thenReturn(List.of(habit, secondHabit));

        List<HabitDto> result =
                habitService.getHabitsForUser(email);

        assertEquals(2, result.size());

        assertEquals("Morning Jog", result.get(0).getTitle());
        assertEquals("Read 20 Pages", result.get(1).getTitle());

        verify(habitRepository).findByUserId(1L);
    }
    @Test
    void getHabitsForUserRejectsNullEmail() {
        when(userRepository.findByEmail(null))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> habitService.getHabitsForUser(null)
        );

        verify(habitRepository, never()).findByUserId(anyLong());
    }



    //Perfect Case
    @Test
    void createHabitForUser() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.save(any(Habit.class)))
                .thenAnswer(invocation -> {
                    Habit saved = invocation.getArgument(0);
                    saved.setId(10L);
                    return saved;
                });

        HabitDto result =
                habitService.createHabitForUser(habitDto, email);

        assertEquals(10L, result.getId());
        assertEquals("Morning Jog", result.getTitle());
        assertEquals("DAILY", result.getFrequency());

        ArgumentCaptor<Habit> captor =
                ArgumentCaptor.forClass(Habit.class);

        verify(habitRepository).save(captor.capture());

        Habit savedHabit = captor.getValue();

        assertEquals(user, savedHabit.getUser());
        assertEquals("Morning Jog", savedHabit.getTitle());
        assertEquals("DAILY", savedHabit.getFrequency());
        assertTrue(savedHabit.getSaved());


    }
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "AB",
            "This habit title is much too long"
    })
    void createHabitRejectsInvalidTitles(String title) {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setTitle(title);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "ABC",
            "123456789012345678901234567890"
    })
    void createHabitAcceptsTitleLengthBoundaries(String title) {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setTitle(title);

        when(habitRepository.save(any(Habit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(
                () -> habitService.createHabitForUser(habitDto, email)
        );

        verify(habitRepository).save(any(Habit.class));
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "YEARLY",
            "HOURLY",
            "SOMETIMES"
    })
    void createHabitRejectsInvalidFrequency(String frequency) {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setFrequency(frequency);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void createHabitRejectsBlankTitle() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        habitDto.setTitle("   ");

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void createHabitRejectsNullDto() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(null, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void createHabitRejectsNullTitle() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setTitle(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void createHabitRejectsNullFrequency() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setFrequency(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(habitDto, email)
        );
    }
    //Testing current behaviour
    @Test
    void createHabitAcceptsLowercaseFrequency() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setFrequency(" daily ");

        when(habitRepository.save(any(Habit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(
                () -> habitService.createHabitForUser(habitDto, email)
        );

        verify(habitRepository).save(any(Habit.class));
    }

    @Test
    void createHabitRejectsNegativeCurrentStreak() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setCurrentStreak(-1);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void createHabitRejectsNegativeLongestStreak() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setLongestStreak(-1);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(habitDto, email)
        );
    }
    @Test
    void createHabitRejectsLongestStreakLessThanCurrentStreak() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setCurrentStreak(10);
        habitDto.setLongestStreak(5);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabitForUser(habitDto, email)
        );
    }

    @Test
    void updateHabitForUser() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitRepository.save(any(Habit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        habitDto.setTitle("Evening Jog");
        habitDto.setFrequency("WEEKLY");

        HabitDto result =
                habitService.updateHabitForUser(habitDto, email);

        assertEquals("Evening Jog", result.getTitle());
        assertEquals("WEEKLY", result.getFrequency());

        assertEquals("Evening Jog", habit.getTitle());
        assertEquals("WEEKLY", habit.getFrequency());

        verify(habitRepository).save(habit);
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "AB",
            "This habit title is much too long"
    })
    void updateHabitRejectsInvalidTitles(String title) {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setTitle(title);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "ABC",
            "123456789012345678901234567890"
    })
    void updateHabitAcceptsTitleLengthBoundaries(String title) {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setTitle(title);
        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));
        when(habitRepository.save(any(Habit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository).save(any(Habit.class));
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "YEARLY",
            "HOURLY",
            "SOMETIMES"
    })
    void updateHabitRejectsInvalidFrequency(String frequency) {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setFrequency(frequency);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void updateHabitRejectsMissingId() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        habitDto.setId(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void updateHabitRejectsNonexistentHabit() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void updateHabitRejectsNullDto() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.updateHabitForUser(null, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void updateHabitRejectsMissingHabit() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void updateHabitRejectsAnotherUsersHabit() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        habit.setUser(anotherUser);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        assertThrows(
                SecurityException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void updateHabitRejectsHabitWithoutOwner() {
        habit.setUser(null);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        assertThrows(
                SecurityException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void updateHabitRejectsBlankTitle() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        habitDto.setTitle("");

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }
    @Test
    void updateHabitPreservesExistingOwner() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitRepository.save(any(Habit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        habitDto.setTitle("Evening Jog");

        habitService.updateHabitForUser(habitDto, email);

        assertEquals(user, habit.getUser());
    }

    @Test
    void deleteHabitForUser() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        String result =
                habitService.deleteHabitForUser(10L, email);

        assertEquals("Habit deleted successfully", result);

        verify(habitLogRepository).deleteByHabitId(10L);
        verify(habitRepository).delete(habit);
    }
    @Test
    void deleteHabitRejectsMissingHabit() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> habitService.deleteHabitForUser(10L, email)
        );

        verify(habitRepository, never()).delete(any(Habit.class));
        verify(habitLogRepository, never()).deleteByHabitId(anyLong());
    }
    @Test
    void deleteHabitRejectsAnotherUsersHabit() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        habit.setUser(anotherUser);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        assertThrows(
                SecurityException.class,
                () -> habitService.deleteHabitForUser(10L, email)
        );

        verify(habitRepository, never()).delete(any(Habit.class));
        verify(habitLogRepository, never()).deleteByHabitId(anyLong());
    }
    @Test
    void deleteHabitWithNoLogs() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        assertDoesNotThrow(
                () -> habitService.deleteHabitForUser(10L, email)
        );

        verify(habitLogRepository).deleteByHabitId(10L);
        verify(habitRepository).delete(habit);
    }
    @Test
    void deleteHabitDeletesLogsBeforeHabit() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        habitService.deleteHabitForUser(10L, email);

        InOrder inOrder = inOrder(
                habitLogRepository,
                habitRepository
        );

        inOrder.verify(habitLogRepository).deleteByHabitId(10L);
        inOrder.verify(habitRepository).delete(habit);
    }

    @Test
    void toggleHabitLog() {
        LocalDateTime date = LocalDateTime.of(2026, 11, 26, 12, 0);

        habitDto.setDate(date);
        habitDto.setLogged(false);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitLogRepository.save(any(HabitLog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HabitDto result =
                habitService.toggleHabitLog(habitDto, email);

        assertTrue(result.getLogged());
        assertEquals(date, result.getDate());

        ArgumentCaptor<HabitLog> captor =
                ArgumentCaptor.forClass(HabitLog.class);

        verify(habitLogRepository).save(captor.capture());

        HabitLog savedLog = captor.getValue();

        assertEquals(habit, savedLog.getHabit());
        assertEquals(date, savedLog.getCompletedDate());
    }
    @Test
    void toggleHabitLogRejectsNullDto() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.toggleHabitLog(null, email)
        );

        verify(habitRepository, never()).findById(anyLong());
        verify(habitLogRepository, never()).save(any(HabitLog.class));
    }
    @Test
    void toggleHabitLogRejectsMissingHabit() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> habitService.toggleHabitLog(habitDto, email)
        );

        verify(habitLogRepository, never()).save(any(HabitLog.class));
    }
    @Test
    void toggleHabitLogRejectsAnotherUsersHabit() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        habit.setUser(anotherUser);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        assertThrows(
                SecurityException.class,
                () -> habitService.toggleHabitLog(habitDto, email)
        );

        verify(habitLogRepository, never()).save(any(HabitLog.class));
        verify(habitLogRepository, never()).delete(any(HabitLog.class));
    }
    //TESTING behaviour
    @Test
    void toggleHabitLogWithNoMatchingLog() {
        LocalDateTime date =
                LocalDateTime.of(2026, 11, 26, 12, 0);

        habitDto.setDate(date);
        habitDto.setLogged(true);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitLogRepository.findByHabitId(10L))
                .thenReturn(List.of());

        HabitDto result =
                habitService.toggleHabitLog(habitDto, email);

        // Current behavior: the DTO remains logged.
        assertFalse(result.getLogged());

        verify(habitLogRepository, never()).delete(any(HabitLog.class));
        verify(habitLogRepository, never()).save(any(HabitLog.class));
    }
    @Test
    void toggleHabitLogDeletesOnlyMatchingDate() {
        LocalDateTime targetDate =
                LocalDateTime.of(2026, 11, 26, 12, 0);

        LocalDateTime otherDate =
                LocalDateTime.of(2026, 11, 25, 12, 0);

        HabitLog targetLog = new HabitLog();
        targetLog.setHabit(habit);
        targetLog.setCompletedDate(targetDate);

        HabitLog otherLog = new HabitLog();
        otherLog.setHabit(habit);
        otherLog.setCompletedDate(otherDate);

        habitDto.setDate(targetDate);
        habitDto.setLogged(true);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitLogRepository.findByHabitId(10L))
                .thenReturn(List.of(targetLog, otherLog));

        HabitDto result =
                habitService.toggleHabitLog(habitDto, email);

        assertFalse(result.getLogged());

        verify(habitLogRepository).delete(targetLog);
        verify(habitLogRepository, never()).delete(otherLog);
    }
    @Test
    void toggleHabitLogDoesNotMatchDifferentTimeOnSameDay() {
        LocalDateTime targetDate =
                LocalDateTime.of(2026, 11, 26, 12, 0);

        LocalDateTime existingDate =
                LocalDateTime.of(2026, 11, 26, 0, 0);

        HabitLog existingLog = new HabitLog();
        existingLog.setHabit(habit);
        existingLog.setCompletedDate(existingDate);

        habitDto.setDate(targetDate);
        habitDto.setLogged(true);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitLogRepository.findByHabitId(10L))
                .thenReturn(List.of(existingLog));

        HabitDto result =
                habitService.toggleHabitLog(habitDto, email);


        assertFalse(result.getLogged());

        verify(habitLogRepository).delete(existingLog);
    }
    @Test
    void toggleHabitLogUsesCurrentDateWhenDateIsNull() {
        habitDto.setDate(null);
        habitDto.setLogged(false);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitLogRepository.save(any(HabitLog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HabitDto result =
                habitService.toggleHabitLog(habitDto, email);

        assertNotNull(result.getDate());
        assertTrue(result.getLogged());

        ArgumentCaptor<HabitLog> captor =
                ArgumentCaptor.forClass(HabitLog.class);

        verify(habitLogRepository).save(captor.capture());

        assertNotNull(captor.getValue().getCompletedDate());
    }
    @Test
    void toggleHabitLogDoesNotCheckForExistingLogWhenCreating() {
        LocalDateTime date =
                LocalDateTime.of(2026, 11, 26, 12, 0);

        HabitLog existingLog = new HabitLog();
        existingLog.setHabit(habit);
        existingLog.setCompletedDate(date);

        habitDto.setDate(date);
        habitDto.setLogged(false);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitLogRepository.save(any(HabitLog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        habitService.toggleHabitLog(habitDto, email);

        verify(habitLogRepository).save(any(HabitLog.class));
    }
    @Test
    void toggleHabitLogDeletesExistingLog() {
        LocalDateTime date =
                LocalDateTime.of(2026, 11, 26, 12, 0);

        HabitLog existingLog = new HabitLog();
        existingLog.setHabit(habit);
        existingLog.setCompletedDate(date);

        habitDto.setDate(date);
        habitDto.setLogged(true);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        when(habitLogRepository.findByHabitId(10L))
                .thenReturn(List.of(existingLog));

        HabitDto result =
                habitService.toggleHabitLog(habitDto, email);

        assertFalse(result.getLogged());
        assertEquals(date, result.getDate());

        verify(habitLogRepository).delete(existingLog);
        verify(habitLogRepository, never()).save(any(HabitLog.class));
    }

    @Test
    void updateHabitRejectsHabitOwnedByAnotherUser() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        habit.setUser(anotherUser);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(habitRepository.findById(10L))
                .thenReturn(Optional.of(habit));

        assertThrows(
                SecurityException.class,
                () -> habitService.updateHabitForUser(habitDto, email)
        );

        verify(habitRepository, never()).save(any(Habit.class));
    }

    @Test
    void getHabitsForUserRejectsUnknownUser() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> habitService.getHabitsForUser(email)
        );

        verify(habitRepository, never()).findByUserId(anyLong());
    }

    @Test
    void toggleHabitLogRejectsMissingHabitId() {
        habitDto.setId(null);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        assertThrows(
                IllegalArgumentException.class,
                () -> habitService.toggleHabitLog(habitDto, email)
        );

        verify(habitLogRepository, never()).save(any(HabitLog.class));
    }

}