package singleroom;

// Manages all room allocations, waitlists, and swaps
public class AllocationManager {
    private Room[] rooms;
    private int roomCount;
    private Warden[] wardens;
    private int wardenIndex;
    
    public AllocationManager(Warden warden) {
        this(new Warden[] { warden });
    }

    public AllocationManager(Warden... wardens) {
        this.rooms = new Room[100];
        this.roomCount = 0;
        this.wardens = wardens;
        this.wardenIndex = 0;
    }

    private Warden getNextWarden() {
        Warden warden = wardens[wardenIndex];
        wardenIndex = (wardenIndex + 1) % wardens.length;
        return warden;
    }

    // Beginner-friendly room API: pass only room properties needed by allocation rules.
    public void addRoom(String roomId, String gender, String departmentTag, int capacity) {
        if (capacity < 1 || capacity > 3) {
            System.out.println("ERROR: Capacity must be 1, 2, or 3");
            return;
        }

        Room room = new Room(roomId, gender, departmentTag, capacity);
        rooms[roomCount] = room;
        roomCount++;
        System.out.println("Added " + room.toString());
    }

    // Backward-compatible wrapper for old calls from previous versions.
    public void addRoom(String roomId, String type, int capacity, String departmentTag, String gender) {
        int resolvedCapacity = capacity;
        if ("Single".equals(type)) resolvedCapacity = 1;
        if ("Double".equals(type)) resolvedCapacity = 2;
        if ("Triple".equals(type)) resolvedCapacity = 3;
        addRoom(roomId, gender, departmentTag, resolvedCapacity);
    }
    
    // Find a room by ID
    public Room findRoom(String roomId) {
        for (int i = 0; i < roomCount; i++) {
            if (rooms[i].getId().equals(roomId)) {
                return rooms[i];
            }
        }
        return null;
    }
    
    // Find a student's current room
    public Room findStudentRoom(String studentId) {
        for (int i = 0; i < roomCount; i++) {
            Student[] occupants = rooms[i].getOccupants();
            for (int j = 0; j < occupants.length; j++) {
                if (occupants[j] != null && occupants[j].getId().equals(studentId)) {
                    return rooms[i];
                }
            }
        }
        return null;
    }
    
    // Check if student already has a room
    public boolean studentHasRoom(Student student) {
        return student.getCurrentRoomId() != null;
    }

    private Student findStudentById(Room room, String studentId) {
        Student[] occupants = room.getOccupants();
        for (int i = 0; i < occupants.length; i++) {
            if (occupants[i].getId().equals(studentId)) {
                return occupants[i];
            }
        }
        return null;
    }

    // Beginner-friendly allocation API.
    public void applyForRoom(Student student, String roomId) {
        String studentId = student.getId();

        // Policy: A student can hold only ONE room at a time
        if (studentHasRoom(student)) {
            System.out.println("ERROR: " + studentId + " already has room " + student.getCurrentRoomId());
            return;
        }
        
        Room room = findRoom(roomId);
        if (room == null) {
            System.out.println("ERROR: Room " + roomId + " not found");
            return;
        }
        
        // Warden approval
        if (!getNextWarden().approveAllocation(student, room)) {
            return;
        }
        
        // Try to allocate
        if (room.hasSpace()) {
            room.assignStudent(student);
            System.out.println(studentId + " allocated to " + roomId);
        } else {
            // Room full, add to waitlist
            room.addToWaitlist(student);
            System.out.println(studentId + " added to waitlist of " + roomId);
        }
    }

    // Backward-compatible wrapper for old calls from previous versions.
    public void applyForRoom(String studentId, Student student, String roomId) {
        applyForRoom(student, roomId);
    }
    
    // Cancel allocation and promote next from waitlist
    public void cancelAllocation(String studentId) {
        Room room = findStudentRoom(studentId);
        
        if (room == null) {
            System.out.println("ERROR: Student " + studentId + " not found in any room");
            return;
        }
        
        System.out.println("\n--- Cancelling " + studentId + " from " + room.getId() + " ---");

        // Get the student and ask warden for approval
        Student student = findStudentById(room, studentId);

        if (student != null) {
            getNextWarden().approveCancellation(student);
        }

        // Remove from occupants
        room.removeStudent(studentId);
        if (student != null) {
            student.setCurrentRoomId(null);
        }
        System.out.println(studentId + " removed from " + room.getId());

        // Try to promote next from waitlist
        Student nextStudent = room.getNextFromWaitlist();
        if (nextStudent != null) {
            room.assignStudent(nextStudent);
            System.out.println(nextStudent.getId() + " promoted from waitlist to " + room.getId());
        } else {
            System.out.println("No eligible student in waitlist");
        }
    }
    
    // Swap two students' rooms
    public void swapRooms(String studentId1, String studentId2) {
        Room room1 = findStudentRoom(studentId1);
        Room room2 = findStudentRoom(studentId2);
        
        if (room1 == null || room2 == null) {
            System.out.println("ERROR: One or both students not found in any room");
            return;
        }
        
        if (room1.equals(room2)) {
            System.out.println("ERROR: Both students in same room, cannot swap");
            return;
        }

        // Find the actual student objects
        Student student1 = findStudentById(room1, studentId1);
        Student student2 = findStudentById(room2, studentId2);

        if (student1 == null || student2 == null) {
            System.out.println("ERROR: Students not found");
            return;
        }
        
        // Get warden approval
        if (!getNextWarden().approveSwap(student1, student2, room1, room2)) {
            return;
        }
        
        // Perform swap
        room1.removeStudent(studentId1);
        room2.removeStudent(studentId2);
        
        room1.assignStudent(student2);
        room2.assignStudent(student1);
        
        System.out.println("SWAP COMPLETE: " + studentId1 + " -> " + room2.getId() + 
                          ", " + studentId2 + " -> " + room1.getId());
    }
    
    // Get waitlist for a room (as array of student IDs)
    public String[] getWaitlist(String roomId) {
        Room room = findRoom(roomId);
        if (room == null) {
            return new String[0];
        }

        Student[] waitlist = room.getWaitlist();
        String[] result = new String[waitlist.length];
        for (int i = 0; i < waitlist.length; i++) {
            result[i] = waitlist[i].getId();
        }
        return result;
    }
    
    // Get occupants of a room (as array of student IDs)
    public String[] getRoomOccupants(String roomId) {
        Room room = findRoom(roomId);
        if (room == null) {
            return new String[0];
        }

        Student[] occupants = room.getOccupants();
        String[] result = new String[occupants.length];
        for (int i = 0; i < occupants.length; i++) {
            result[i] = occupants[i].getId();
        }
        return result;
    }
    
    // Print all room status
    public void printAllRooms() {
        System.out.println("\n===== HOSTEL STATUS =====");
        for (int i = 0; i < roomCount; i++) {
            rooms[i].printStatus();
        }
    }
}