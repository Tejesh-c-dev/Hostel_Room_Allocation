package singleroom;

// Warden - approves allocation, cancellation, and swap requests
public class Warden {
    String id;
    String name;
    
    public Warden(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Approve allocation
    public boolean approveAllocation(Student student, Room room) {
        if (!room.isEligible(student)) {
            System.out.println("[Warden " + id + "] REJECTED " + student.getId() + " for " + room.getId());
            return false;
        }
        System.out.println("[Warden " + id + "] APPROVED " + student.getId() + " -> " + room.getId());
        return true;
    }
    
    // Approve cancellation
    public boolean approveCancellation(Student student) {
        System.out.println("[Warden " + id + "] APPROVED cancellation for " + student.getId());
        return true;
    }
    
    // Approve swap
    public boolean approveSwap(Student s1, Student s2, Room r1, Room r2) {
        if (!r1.isEligible(s2) || !r2.isEligible(s1)) {
            System.out.println("[Warden " + id + "] REJECTED swap: ineligible");
            return false;
        }
        System.out.println("[Warden " + id + "] APPROVED swap: " + s1.getId() + " <-> " + s2.getId());
        return true;
    }
    
    public String getId() { 
        return id; 
    }
    
    public String getName() { 
        return name; 
    }
}