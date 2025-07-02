package PIA.userside.Repositories;


import PIA.userside.Models.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UsersideRepository {

    private final JdbcTemplate jdbc;

    public UsersideRepository(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    // REPOSITORY ADDITION
    public String addCandidate(Candidate candidate) {
        String sql = """
            INSERT INTO Candidates (
                candidateExpectedGraduateYear,
                candidateName,
                candidateSurname,
                candidateBirthDay,
                candidateSex,
                candidateEmail,
                candidatePhone,
                candidateEnglishLevel,
                candidateCurrentYear,
                candidateGPA,
                candidateUniversity,
                candidateMajor
            ) VALUES (?, ?, ?, ?, ?::sex, ?, ?, ?::english_level, ?::current_year, ?, ?, ?)
        """;

        try {
            jdbc.update(sql,
                    candidate.getCandidateExpectedGraduateYear(),
                    candidate.getCandidateName(),
                    candidate.getCandidateSurname(),
                    candidate.getCandidateBirthDay(),
                    candidate.getCandidateSex(),
                    candidate.getCandidateEmail(),
                    candidate.getCandidatePhone(),
                    candidate.getCandidateEnglishLevel(),
                    candidate.getCandidateCurrentYear(),
                    candidate.getCandidateGPA(),
                    candidate.getCandidateUniversity(),
                    candidate.getCandidateMajor()
            );
            return "REPOSITORY::addCandidate::Success";
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::addCandidate::Failed to insert candidate: " + e.getMessage(), e);
        }
    }

    public String addJob(Job job) {
        String sql = """
            INSERT INTO Jobs (
                jobName,
                jobStatus
            ) VALUES (?, ?::job_status)
        """;

        try {
            jdbc.update(sql,
                    job.getJobName(),
                    job.getJobStatus()
            );
            return "REPOSITORY::addJob::Success";
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::addJob::Failed to insert job: " + e.getMessage(), e);
        }
    }

    public String addCity(City city) {
        String sql = """
            INSERT INTO Cities (
                cityName
            ) VALUES (?)
        """;

        try {
            jdbc.update(sql, city.getCityName());
            return "REPOSITORY::addCity::Success";
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::addCity::Failed to insert city: " + e.getMessage(), e);
        }
    }

    public String addMajor(Major major) {
        String sql = """
            INSERT INTO Majors (
            majorName
            ) VALUES (?)
        """;

        try {
            jdbc.update(sql, major.getMajorName());
            return "REPOSITORY::addMajor::Success";
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::addMajor::Failed to insert major: " + e.getMessage(), e);
        }
    }

    public String addUniversity(University university) {
        String sql = """
            INSERT INTO Universities (
            universityName
            ) VALUES (?)
        """;
        try {
            jdbc.update(sql, university.getUniversityName());
            return "REPOSITORY::addUniversity::Success";
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::addUniversity::Failed to insert University: " + e.getMessage(), e);
        }
    }

    // Relations
    public String addJobCity(JobCity jobCity) {
        String sql = """
            INSERT INTO JobsCities (
                jobId,
                cityId
            ) VALUES (?, ?)
        """;

        try {
            jdbc.update(sql,
                    jobCity.getJobId(),
                    jobCity.getCityId()
            );
            return "REPOSITORY::addJobCity::Success";
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::addJobCity::Failed to insert job-city relation: " + e.getMessage(), e);
        }
    }

    public String addCandidateJob(CandidateJob cj) {
        String sql = """
            INSERT INTO CandidatesJobs (
                candidateId,
                jobId,
                applicationStatus,
                applicationDate,
                cityId
            ) VALUES (?, ?, ?::application_status, ?, ?)
        """;

        try {
            jdbc.update(sql,
                    cj.getCandidateId(),
                    cj.getJobId(),
                    cj.getStatus(),
                    cj.getApplicationDate(),
                    cj.getCityId()
            );
            return "REPOSITORY::addCandidateJob::Success";
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::addCandidateJob::Failed to insert candidate-job relation: " + e.getMessage(), e);
        }
    }

    // REPOSITORY GET
    public Integer getJob(String jobName) {
        String sql = "SELECT jobId FROM Jobs WHERE jobName = ?";

        try {
            List<Integer> results = jdbc.query(
                    sql,
                    (rs, rowNum) -> rs.getInt("jobId"),
                    jobName
            );

            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::getJob::Database error: " + e.getMessage(), e);
        }
    }

    public Integer getUniversity(String universityName) {
        String sql = "SELECT universityId FROM Universities WHERE universityName = ?";

        try {
            List<Integer> results = jdbc.query(
                    sql,
                    (rs, rowNum) -> rs.getInt("universityId"),
                    universityName
            );

            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::getUniversity::Database error: " + e.getMessage(), e);
        }
    }

    public Integer getMajor(String majorName) {
        String sql = "SELECT majorId FROM Majors WHERE majorName = ?";

        try {
            List<Integer> results = jdbc.query(
                    sql,
                    (rs, rowNum) -> rs.getInt("majorId"),
                    majorName
            );

            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::getMajor::Database error: " + e.getMessage(), e);
        }
    }

    public Integer getCity(String cityName) {
        String sql = "SELECT cityId FROM Cities WHERE cityName = ?";

        try {
            List<Integer> results = jdbc.query(
                    sql,
                    (rs, rowNum) -> rs.getInt("cityId"),
                    cityName
            );

            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::getCity::Database error: " + e.getMessage(), e);
        }
    }

    public Integer getCandidateId(String email) {
        String sql = "SELECT candidateId FROM Candidates WHERE candidateEmail = ?";

        try {
            List<Integer> results = jdbc.query(
                    sql,
                    (rs, rowNum) -> rs.getInt("candidateId"),
                    email
            );

            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::getCandidateId::Database error: " + e.getMessage(), e);
        }
    }

    // REPOSITORY GET TABLE
    public List<String> getMajors() {
        String sql = "SELECT majorName FROM majors";
        try {
            return jdbc.query(sql, (resultSet, rowNum) -> resultSet.getString("majorName"));
        } catch (DataAccessException e) {
            throw new RuntimeException("REPOSITORY::getMajors::Error while fetching majors: " + e.getMessage(), e);
        }
    }

    public List<String> getUniversities() {
        String sql = "SELECT universityName FROM Universities";
        try {
            return jdbc.query(sql, (resultSet, rowNum) -> resultSet.getString("universityName"));
        }  catch (DataAccessException e) {
            throw new RuntimeException("REPOSITORY::getUniversities::Error while fetching cities: " + e.getMessage(), e);
        }
    }

}
