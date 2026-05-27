package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationTest {
    private Notification notification;
    private Volunteer volunteer;

    @BeforeEach
    public void setup() {
        volunteer = new Volunteer();
        notification = new Notification("Hello, I am a new Notification", volunteer);
    }

    @Test
    public void testNotificationConstructor() {
        assertNotNull(notification);
        assertEquals("Hello, I am a new Notification", notification.getDescription());

    }

    @Test
    public void testEmptyNotificationConstructor() {
        notification = new Notification();
        assertNull(notification.getDescription());
    }

    @Test
    public void testSettersAndGetters() {
        notification.setDescription("new_notification_message");
        Volunteer volunteerTest = new Volunteer();
        notification.setVolunteer(volunteerTest);
        assertEquals("new_notification_message", notification.getDescription());
        assertEquals(volunteerTest, notification.getVolunteer());
    }


}
