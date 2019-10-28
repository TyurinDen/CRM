package com.ewp.crm.repository.interfaces;

import com.ewp.crm.models.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends CommonGenericRepository<Student> {

    Student getStudentByClientId(Long clientId);

    List<Student> getStudentsByClientSocialProfiles_Empty();

    @Query("SELECT s FROM Student s JOIN s.client c WHERE ?1 MEMBER OF c.clientEmails")
    Student getStudentByEmail(String email);

    List<Student> getStudentsByStatusId(Long id); // status из табл student_status: {Auto-generated, новый студент}

    /**
     * Возвращает список студентов по названию статуса клиента (Новые, На пробных, Слился и тд).
     *
     * @param statusName название статуса клиента.
     * Таблицы: student, client, status, status_clients.
     */
    @Query(value = "SELECT * FROM crm.student s " +
            "LEFT JOIN client c ON s.client_id = c.client_id " +
            "LEFT JOIN status_clients sc ON sc.user_id = c.client_id " +
            "INNER JOIN status st ON sc.status_id = st.status_id WHERE st.status_name = :statusName", nativeQuery = true)
    List<Student> getStudentsByClientStatusName(@Param("statusName") String statusName);

}
