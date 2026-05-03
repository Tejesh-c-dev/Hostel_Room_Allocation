package singleroom;

import java.util.*;

// Base class for rooms
public class Room {
    String id;
    String gender;              // "M" or "F"
    String departmentTag;       // "ANY" = no restriction, else "CSE", "ECE", etc.
    int capacity;               // Max occupants
    Student[] occupants;        // Array of occupants
    int occupantCount;          // Number of actual occupants
    Student[] waitlist;         // Array for waitlist
    int waitlistCount;          // Number in waitlist
    
    public Room(String id, String gender, String departmentTag, int capacity) {
        this.id = id;
        this.gender = gender;
        this.departmentTag = departmentTag;
        this.capacity = capacity;
        this.occupants = new Student[capacity];
        this.occupantCount = 0;
        this.waitlist = new Student[100];  // Max 100 waiting
        this.waitlistCount = 0;
    }
    
    // Check if room has space
    public boolean hasSpace() {
        return occupantCount < capacity;
    }
    
    // Check if student can be in this room (gender + dept match)
    public boolean isEligible(Student s) {
        // Check gender match
        if (!s.getGender().equals(this.gender)) {
            return false;
        }
        
        // Check department match
        if (!this.departmentTag.equals("ANY")) {
            if (!s.getDepartment().equals(this.departmentTag)) {
                return false;
            }
        }
        
        return true;
    }
    
    // Assign student to this room (must have space)
    public void assignStudent(Student s) {
        if (hasSpace()) {
            occupants[occupantCount] = s;
            occupantCount++;
            s.setCurrentRoomId(this.id);
        }
    }
    
    // Remove student from occupants (e.g., cancellation)
    public boolean removeStudent(String studentId) {
        for (int i = 0; i < occupantCount; i++) {
            if (occupants[i].getId().equals(studentId)) {
                // Shift remaining students left
                for (int j = i; j < occupantCount - 1; j++) {
                    occupants[j] = occupants[j + 1];
                }
                occupants[occupantCount - 1] = null;
                occupantCount--;
                return true;
            }
        }
        return false;
    }
    
    // Add student to waitlist
    public void addToWaitlist(Student s) {
        if (waitlistCount < waitlist.length) {
            waitlist[waitlistCount] = s;
            waitlistCount++;
            sortWaitlist();
        }
    }
    
    // Get next eligible student from waitlist (removes them)
    public Student getNextFromWaitlist() {
        for (int i = 0; i < waitlistCount; i++) {
            Student s = waitlist[i];
            if (isEligible(s)) {
                // Remove from waitlist
                for (int j = i; j < waitlistCount - 1; j++) {
                    waitlist[j] = waitlist[j + 1];
                }
                waitlist[waitlistCount - 1] = null;
                waitlistCount--;
                return s;
            }
        }
        return null;
    }
    
    // Remove specific student from waitlist
    public void removeFromWaitlist(String studentId) {
        for (int i = 0; i < waitlistCount; i++) {
            if (waitlist[i].getId().equals(studentId)) {
                // Shift remaining left
                for (int j = i; j < waitlistCount - 1; j++) {
                    waitlist[j] = waitlist[j + 1];
                }
                waitlist[waitlistCount - 1] = null;
                waitlistCount--;
                break;
            }
        }
    }
    
    // Sort waitlist by priority rules
    private void sortWaitlist() {
        // Bubble sort - simple and easy to understand
        for (int i = 0; i < waitlistCount - 1; i++) {
            for (int j = 0; j < waitlistCount - i - 1; j++) {
                if (shouldSwap(waitlist[j], waitlist[j + 1])) {
                    // Swap
                    Student temp = waitlist[j];
                    waitlist[j] = waitlist[j + 1];
                    waitlist[j + 1] = temp;
                }
            }
        }
    }
    
    // Helper: should we swap these two students in the waitlist?
    private boolean shouldSwap(Student a, Student b) {
        // Rule 1: Priority level (lower is better)
        if (a.getPriority() != b.getPriority()) {
            return a.getPriority() > b.getPriority();
        }
        
        // Rule 2: CGPA (higher is better)
        if (a.getCgpa() != b.getCgpa()) {
            return a.getCgpa() < b.getCgpa();
        }
        
        // Rule 3: Application time (earlier is better)
        if (a.getApplicationTime() != b.getApplicationTime()) {
            return a.getApplicationTime() > b.getApplicationTime();
        }
        
        // Rule 4: Student ID (for identical CGPA and time)
        return a.getId().compareTo(b.getId()) > 0;
    }
    
    // Getters
    public String getId() { 
        return id; 
    }
    
    public String getGender() { 
        return gender; 
    }
    
    public String getDepartmentTag() { 
        return departmentTag; 
    }
    
    public int getCapacity() { 
        return capacity; 
    }
    
    public Student[] getOccupants() { 
        Student[] result = new Student[occupantCount];
        for (int i = 0; i < occupantCount; i++) {
            result[i] = occupants[i];
        }
        return result;
    }
    
    public Student[] getWaitlist() { 
        Student[] result = new Student[waitlistCount];
        for (int i = 0; i < waitlistCount; i++) {
            result[i] = waitlist[i];
        }
        return result;
    }
    
    // Print room status
    public void printStatus() {
        System.out.println("  Room " + id + " (" + occupantCount + "/" + capacity + ")");
        System.out.print("    Occupants: ");
        for (int i = 0; i < occupantCount; i++) {
            System.out.print(occupants[i].getId() + " ");
        }
        if (occupantCount == 0) System.out.print("EMPTY");
        System.out.println();
        
        System.out.print("    Waitlist(" + waitlistCount + "): ");
        for (int i = 0; i < waitlistCount; i++) {
            System.out.print(waitlist[i].getId() + "[" + waitlist[i].getType() + "] ");
        }
        if (waitlistCount == 0) System.out.print("NONE");
        System.out.println();
    }
    
    public String toString() {
        return "Room[" + id + " " + gender + " " + departmentTag + " cap:" + capacity + "]";
    }
}