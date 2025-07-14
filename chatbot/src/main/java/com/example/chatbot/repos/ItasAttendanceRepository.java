package com.example.chatbot.repos;

import com.example.chatbot.models.ItasAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ItasAttendanceRepository extends JpaRepository<ItasAttendance, Integer> {
    
    @Query("SELECT a FROM ItasAttendance a WHERE a.employee.employeeId = :employeeId AND a.date BETWEEN :startDate AND :endDate")
    List<ItasAttendance> findByEmployeeIdAndDateBetween(@Param("employeeId") String employeeId, 
                                                       @Param("startDate") LocalDate startDate, 
                                                       @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM ItasAttendance a WHERE a.date BETWEEN :startDate AND :endDate")
    List<ItasAttendance> findByDateBetween(@Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
}
