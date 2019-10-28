package com.ewp.crm.service.interfaces;

import com.ewp.crm.models.Client;
import com.ewp.crm.models.SocialProfile.SocialNetworkType;
import com.ewp.crm.models.Student;
import com.ewp.crm.models.dto.all_students_page.StudentDto;

import java.util.List;
import java.util.Optional;

public interface StudentService extends CommonService<Student> {

    List<Student> getStudentsWithoutSocialProfileByType(List<SocialNetworkType> excludeSocialProfiles);

    Optional<Student> addStudentForClient(Client client);

    List<Student> getStudentsByStatusId(Long id); // status из табл student_status: {Auto-generated, новый студент}

    /**
     * Возвращает список дтошек для страницы all-student-table (/student/all)
     * по названию статуса клиента: {Новые, На пробных, Слился и тд}.
     *
     * @param statusNames строка с  названиями статусов клиентов через зяпятую.
     * Таблицы: client, status, status_clients.
     */
    List<StudentDto> getStudentsByClientStatusNames(String statusNames);

    List<Student> getStudentsWithTodayNotificationsEnabled();

    List<Student> getStudentsWithTodayTrialNotificationsEnabled();

    void detach(Student student);

    Optional<Student> getStudentByClientId(Long clientId);

    void save(Student student);

    void resetColors();

    Student getStudentByEmail(String email);

    //TODO удалить или оставить?
    List<StudentDto> getStudentDtoForAllStudentsPage();

}
