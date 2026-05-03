package singleroom;

// Base class for all students
public class Student {
    String id;
    String type;           // "Exchange", "Senior", "FirstYear"
    double cgpa;
    String gender;         // "M" or "F"
    String department;
    int applicationTime;   // Timestamp (lower = earlier = higher priority)
    String currentRoomId;  // Can hold only ONE room at a time
    
    public Student(String id, String type, double cgpa, String gender, String department, int applicationTime) {
        this.id = id;
        this.type = type;
        this.cgpa = cgpa;
        this.gender = gender;
        this.department = department;
        this.applicationTime = applicationTime;
        this.currentRoomId = null;
    }
    
    // Get priority number (lower = higher priority)
    public int getPriority() {
        if (type.equals("Exchange")) return 0;    // Highest
        if (type.equals("Senior")) return 1;      // Medium
        if (type.equals("FirstYear")) return 2;   // Lowest
        return 999;
    }
    
    // Getters and setters
    public String getId() { 
        return id; 
    }
    
    public String getType() { 
        return type; 
    }
    
    public double getCgpa() { 
        return cgpa; 
    }
    
    public String getGender() { 
        return gender; 
    }
    
    public String getDepartment() { 
        return department; 
    }
    
    public int getApplicationTime() { 
        return applicationTime; 
    }
    
    public String getCurrentRoomId() { 
        return currentRoomId; 
    }
    
    public void setCurrentRoomId(String roomId) { 
        this.currentRoomId = roomId; 
    }
    
    public String toString() {
        return type + "[" + id + " CGPA:" + cgpa + " Time:" + applicationTime + "]";
    }
}