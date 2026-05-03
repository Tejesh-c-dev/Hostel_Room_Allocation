package singleroom;

public class HostelSystem {

    public static void main(String[] args) {
    System.out.println("HOSTEL ROOM ALLOCATION SYSTEM\n");

    // 1) Create wardens and allocation manager
        Warden warden = new Warden("W1", "Mr. Kumar");
        Warden warden2 = new Warden("W2", "Dr. Sharma");
    System.out.println("Wardens: " + warden.getId() + " (" + warden.getName() + "), "
        + warden2.getId() + " (" + warden2.getName() + ")");
        AllocationManager manager = new AllocationManager(warden, warden2);

    // 2) Add rooms
    System.out.println("\nAdding Rooms");
    manager.addRoom("R101", "M", "CSE", 1);
    manager.addRoom("R102", "M", "CSE", 1);
    manager.addRoom("R201", "F", "ANY", 2);
    manager.addRoom("R202", "M", "ECE", 3);
        manager.printAllRooms();

    // 3) Create students
    System.out.println("\nCreating Students");
    Student s1 = new Student("S101", "Exchange", 3.5, "M", "CSE", 10);
    Student s2 = new Student("S102", "Senior", 3.8, "M", "CSE", 20);
    Student s3 = new Student("S103", "FirstYear", 3.2, "M", "CSE", 5);
    Student s4 = new Student("S104", "Senior", 3.6, "F", "ECE", 15);
    Student s5 = new Student("S105", "Exchange", 3.9, "F", "CSE", 25);
    Student s6 = new Student("S106", "Senior", 3.7, "M", "ECE", 30);

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        System.out.println(s5);
        System.out.println(s6);

    // 4) Apply for rooms
        System.out.println("\n--- Applying for Rooms ---");
    manager.applyForRoom(s1, "R101");
    manager.applyForRoom(s2, "R101");
    manager.applyForRoom(s3, "R101");
    manager.applyForRoom(s4, "R201");
    manager.applyForRoom(s5, "R201");
    manager.applyForRoom(s6, "R202");
        manager.printAllRooms();

    // 5) Cancellation and automatic waitlist promotion
        System.out.println("\n--- Testing Cancellation ---");
        manager.cancelAllocation("S101");
        manager.printAllRooms();

    // 6) Swap tests (invalid + valid)
        System.out.println("\n--- Testing Room Swap ---");
        Student s7 = new Student("S107", "Senior", 3.9, "M", "CSE", 35);
    manager.applyForRoom(s7, "R102");
        manager.printAllRooms();

    manager.swapRooms("S106", "S105");
        manager.printAllRooms();

    manager.swapRooms("S102", "S107");
        manager.printAllRooms();

    // 7) Print final waitlist for one room
        System.out.println("\n--- FINAL WAITLISTS ---");
        String[] w1 = manager.getWaitlist("R101");
        System.out.print("R101 Waitlist: ");
        for (int i = 0; i < w1.length; i++) {
            System.out.print(w1[i] + " ");
        }
        System.out.println(w1.length == 0 ? "EMPTY" : "");
    }
}