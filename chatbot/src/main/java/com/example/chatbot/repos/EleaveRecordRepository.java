package com.example.chatbot.repos;

import com.example.chatbot.models.EleaveRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EleaveRecordRepository extends JpaRepository<EleaveRecord, Integer> {
    
    @Query("SELECT l FROM EleaveRecord l WHERE l.employee.employeeId = :employeeId AND l.leaveDate BETWEEN :startDate AND :endDate")
    List<EleaveRecord> findByEmployeeIdAndLeaveDateBetween(@Param("employeeId") String employeeId, 
                                                          @Param("startDate") LocalDate startDate, 
                                                          @Param("endDate") LocalDate endDate);
    
    @Query("SELECT l FROM EleaveRecord l WHERE l.leaveDate BETWEEN :startDate AND :endDate AND l.status = 'Approved'")
    List<EleaveRecord> findApprovedLeavesByDateBetween(@Param("startDate") LocalDate startDate, 
                                                      @Param("endDate") LocalDate endDate);
    
    @Query("SELECT l FROM EleaveRecord l WHERE l.leaveDate BETWEEN :startDate AND :endDate")
    List<EleaveRecord> findByLeaveDateBetween(@Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);
}
