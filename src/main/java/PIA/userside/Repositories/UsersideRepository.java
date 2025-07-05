package PIA.userside.Repositories;

import PIA.userside.GlobalData.Application_Status;
import PIA.userside.Models.*;
import PIA.userside.Models.Inputs.Filter;
import PIA.userside.Models.Outputs.CandidateDetailsOutput;
import PIA.userside.Models.Outputs.FilterOutput;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.util.*;


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

    public String addCandidateCV(CandidateCV cv) {
        String sql = """
        INSERT INTO CandidateCVs (
            candidateId,
            cvFile,
            cvFileName
        ) VALUES (?, ?, ?)
    """;

        try {
            jdbc.update(sql,
                    cv.getCandidateId(),
                    cv.getCvFile(),
                    cv.getCvFileName()
            );
            return "REPOSITORY::addCandidateCV::Success";
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::addCandidateCV::Failed to insert candidate CV: " + e.getMessage(), e);
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

    public Integer getJobCity(Integer jobId, Integer cityId) {
        String sql = "SELECT jobId FROM JobsCities WHERE jobId = ? AND cityId = ?";

        try {
            return jdbc.queryForObject(sql, Integer.class, jobId, cityId);
        } catch (EmptyResultDataAccessException e) {
            return null; // No matching record found
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::getJobCity::Error while fetching job-city mapping: " + e.getMessage(), e);
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


    public byte[] getCandidateCV(String email) {
        String sql = """
        SELECT cvFile
        FROM CandidateCVs
        WHERE candidateId = (
            SELECT candidateId FROM Candidates WHERE candidateEmail = ?
        )
    """;

        try {
            System.out.println("Email: " + email);
            List<byte[]> results = jdbc.query(
                    sql,
                    (rs, rowNum) -> rs.getBytes("cvFile"),
                    email
            );

            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::getCandidateCV::Database error: " + e.getMessage(), e);
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

    public List<String> getJobs() {
        String sql = "SELECT jobName FROM jobs";
        try {
            return jdbc.query(sql, (resultSet, rowNum) -> resultSet.getString("jobName"));
        }   catch (DataAccessException e) {
            throw new RuntimeException("REPOSITORY::getJobs::Error while fetching cities: " + e.getMessage(), e);
        }
    }

    public List<String> getCities() {
        String sql = "SELECT cityName FROM cities";
        try{
            return jdbc.query(sql, (resultSet, rowNum) -> resultSet.getString("cityName"));
        } catch (RuntimeException e){
            throw new RuntimeException("REPOSITORY::getCities::Error while fetching cities: " + e.getMessage(), e);
        }
    }

    public CandidateDetailsOutput getCandidateDetails(String candidateEmail, String jobName) {
        String sql = """
        SELECT 
            c.candidateId, c.candidateName, c.candidateSurname, c.candidateBirthDay,
            c.candidateEmail, c.candidatePhone, u.universityName, m.majorName,
            c.candidateGPA, c.candidateSex, c.candidateCurrentYear, 
            c.candidateExpectedGraduateYear, c.candidateEnglishLevel,
            j.jobName, cj.applicationStatus,
            city.cityName
        FROM Candidates c
        JOIN CandidatesJobs cj ON c.candidateId = cj.candidateId
        JOIN Jobs j ON j.jobId = cj.jobId
        JOIN Cities city ON cj.cityId = city.cityId
        JOIN Universities u ON c.candidateUniversity = u.universityId
        JOIN Majors m ON c.candidateMajor = m.majorId
        WHERE c.candidateEmail = ? AND j.jobName = ?
    """;

        try {


            CandidateDetailsOutput details = jdbc.queryForObject(sql, (rs, rowNum) -> {
                CandidateDetailsOutput d = new CandidateDetailsOutput();
                d.setCandidateName(rs.getString("candidateName"));
                d.setCandidateSurname(rs.getString("candidateSurname"));

                Date sqlDate = rs.getDate("candidateBirthDay");
                if (sqlDate != null) {
                    d.setCandidateBirthDay(sqlDate.toLocalDate());
                }

                d.setCandidateEmail(rs.getString("candidateEmail"));
                d.setCandidatePhone(rs.getString("candidatePhone"));

                d.setCandidateUniversity(rs.getString("universityName"));
                d.setCandidateMajor(rs.getString("majorName"));
                d.setCandidateGPA(rs.getBigDecimal("candidateGPA"));

                d.setCandidateSex(rs.getString("candidateSex"));
                d.setCandidateCurrentYear(rs.getString("candidateCurrentYear"));
                d.setCandidateExpectedGraduateYear(rs.getInt("candidateExpectedGraduateYear"));
                d.setCandidateEnglishLevel(rs.getString("candidateEnglishLevel"));

                d.setJobName(rs.getString("jobName"));
                d.setCityName(rs.getString("cityName"));

                String appStatusStr = rs.getString("applicationStatus");
                if (appStatusStr != null) {
                    d.setApplicationStatus(Application_Status.valueOf(appStatusStr));
                }

                return d;
            }, candidateEmail, jobName);


            return details;

        } catch (EmptyResultDataAccessException e) {
            System.out.println("REPOSITORY::getCandidateDetails::No candidate found.");
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching candidate details: " + e.getMessage(), e);
        }
    }


    /*public List<FilterOutput> filterCandidates(List<Filter> filters) {
        try {
            StringBuilder sql = new StringBuilder("SELECT DISTINCT c.candidateEmail, c.candidateName, c.candidateSurname, ");
            sql.append("j.jobName, u.universityName, m.majorName, cj.applicationStatus ");
            sql.append("FROM Candidates c ");

            List<Object> params = new ArrayList<>();
            boolean joinUniversities = true;
            boolean joinMajors = true;
            boolean joinCandidatesJobs = true;
            boolean joinJobs = true;
            boolean joinCities = false;

            List<String> orConditions = new ArrayList<>();

            for (Filter filter : filters) {
                List<String> andConditions = new ArrayList<>();

                // <editor-fold desc="Candidate Table">
                if (filter.getCandidateAges() != null && !filter.getCandidateAges().isEmpty()) {
                    for (Integer age : filter.getCandidateAges()) {
                        andConditions.add("DATE_PART('year', AGE(c.candidateBirthDay)) = ?");
                        params.add(age);
                    }
                }

                if (filter.getCandidateMinAge() != null) {
                    andConditions.add("DATE_PART('year', AGE(c.candidateBirthDay)) >= ?");
                    params.add(filter.getCandidateMinAge());
                }

                if (filter.getCandidateMaxAge() != null) {
                    andConditions.add("DATE_PART('year', AGE(c.candidateBirthDay)) <= ?");
                    params.add(filter.getCandidateMaxAge());
                }

                if (filter.getCandidateGPAs() != null && !filter.getCandidateGPAs().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateGPAs().size(), "?"));
                    andConditions.add("c.candidateGPA IN (" + inClause + ")");
                    params.addAll(filter.getCandidateGPAs());
                }

                if (filter.getCandidateMinGPA() != null) {
                    andConditions.add("c.candidateGPA >= ?");
                    params.add(filter.getCandidateMinGPA());
                }

                if (filter.getCandidateMaxGPA() != null) {
                    andConditions.add("c.candidateGPA <= ?");
                    params.add(filter.getCandidateMaxGPA());
                }

                if (filter.getCandidateSexes() != null && !filter.getCandidateSexes().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateSexes().size(), "?"));
                    andConditions.add("c.candidateSex IN (" + inClause + ")");
                    params.addAll(filter.getCandidateSexes());
                }

                if (filter.getCandidateCurrentYears() != null && !filter.getCandidateCurrentYears().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateCurrentYears().size(), "?"));
                    andConditions.add("c.candidateCurrentYear IN (" + inClause + ")");
                    params.addAll(filter.getCandidateCurrentYears());
                }

                if (filter.getCandidateExpectedGraduateYears() != null && !filter.getCandidateExpectedGraduateYears().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateExpectedGraduateYears().size(), "?"));
                    andConditions.add("c.candidateExpectedGraduateYear IN (" + inClause + ")");
                    params.addAll(filter.getCandidateExpectedGraduateYears());
                }

                if (filter.getCandidateEnglishLevels() != null && !filter.getCandidateEnglishLevels().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateEnglishLevels().size(), "?"));
                    andConditions.add("c.candidateEnglishLevel IN (" + inClause + ")");
                    params.addAll(filter.getCandidateEnglishLevels());
                }
                // </editor-fold>

                // <editor-fold desc="University Join">
                if (filter.getCandidateUniversities() != null && !filter.getCandidateUniversities().isEmpty()) {
                    joinUniversities = true;
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateUniversities().size(), "?"));
                    andConditions.add("u.universityName IN (" + inClause + ")");
                    params.addAll(filter.getCandidateUniversities());
                }
                // </editor-fold>

                // <editor-fold desc="Major Join">
                if (filter.getCandidateMajors() != null && !filter.getCandidateMajors().isEmpty()) {
                    joinMajors = true;
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateMajors().size(), "?"));
                    andConditions.add("m.majorName IN (" + inClause + ")");
                    params.addAll(filter.getCandidateMajors());
                }
                // </editor-fold>

                // <editor-fold desc="Candidate-jobs Join">
                if ((filter.getApplicationDates() != null && !filter.getApplicationDates().isEmpty()) || filter.getApplicationMinDate() != null || filter.getApplicationMaxDate() != null) {
                    joinCandidatesJobs = true;

                    if (filter.getApplicationDates() != null && !filter.getApplicationDates().isEmpty()) {
                        String inClause = String.join(",", Collections.nCopies(filter.getApplicationDates().size(), "?"));
                        andConditions.add("cj.applicationDate IN (" + inClause + ")");
                        params.addAll(filter.getApplicationDates());
                    }

                    if (filter.getApplicationMinDate() != null) {
                        andConditions.add("cj.applicationDate >= ?");
                        params.add(filter.getApplicationMinDate());
                    }

                    if (filter.getApplicationMaxDate() != null) {
                        andConditions.add("cj.applicationDate <= ?");
                        params.add(filter.getApplicationMaxDate());
                    }
                }
                // </editor-fold>

                // <editor-fold desc="PLUS Jobs Join">
                if (filter.getJobNames() != null && !filter.getJobNames().isEmpty()) {
                    joinCandidatesJobs = true;
                    joinJobs = true;
                    String inClause = String.join(",", Collections.nCopies(filter.getJobNames().size(), "?"));
                    andConditions.add("j.jobName IN (" + inClause + ")");
                    params.addAll(filter.getJobNames());
                }
                // </editor-fold>

                // <editor-fold desc="PLUS City Join">
                if (filter.getCityNames() != null && !filter.getCityNames().isEmpty()) {
                    joinCandidatesJobs = true;
                    joinJobs = true;
                    joinCities = true;
                    String inClause = String.join(",", Collections.nCopies(filter.getCityNames().size(), "?"));
                    andConditions.add("ci.cityName IN (" + inClause + ")");
                    params.addAll(filter.getCityNames());
                }
                // </editor-fold>

                if (!andConditions.isEmpty()) {
                    orConditions.add("(" + String.join(" AND ", andConditions) + ")");
                }
            }

            // <editor-fold desc="JOINS">
            if (joinUniversities) sql.append(" JOIN Universities u ON c.candidateUniversity = u.universityId ");
            if (joinMajors) sql.append(" JOIN Majors m ON c.candidateMajor = m.majorId ");
            if (joinCandidatesJobs) sql.append(" JOIN CandidatesJobs cj ON c.candidateId = cj.candidateId ");
            if (joinJobs) sql.append(" JOIN Jobs j ON cj.jobId = j.jobId ");
            if (joinCities) sql.append(" JOIN JobsCities jc ON j.jobId = jc.jobId JOIN Cities ci ON jc.cityId = ci.cityId ");
            // </editor-fold>

            if (!orConditions.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" OR ", orConditions));
            }

            List<Map<String, Object>> results = jdbc.queryForList(sql.toString(), params.toArray());

            List<FilterOutput> outputList = new ArrayList<>();

            // <editor-fold desc="Mapping to the OutputObject">
            for (Map<String, Object> row : results) {
                FilterOutput output = new FilterOutput();
                output.setCandidateEmail((String) row.get("candidateEmail"));
                output.setCandidateName((String) row.get("candidateName"));
                output.setCandidateSurname((String) row.get("candidateSurname"));
                output.setJobName((String) row.get("jobName"));
                output.setCandidateUniversity((String) row.get("universityName"));
                output.setCandidateMajor((String) row.get("majorName"));
                output.setStatus((String) row.get("applicationStatus"));
                outputList.add(output);
            }
            // </editor-fold>
            return outputList;

        } catch (Exception e) {
            throw new RuntimeException("REPOSITORY::filterCandidates::Error while fetching candidates: " + e.getMessage(), e);
        }
    }
     */

    public List<FilterOutput> filterCandidates(List<Filter> filters) {
        try {
            System.out.println("REPOSITORY::filterCandidates::Executed.");

            StringBuilder sql = new StringBuilder("SELECT DISTINCT c.candidateEmail, c.candidateName, c.candidateSurname, ");
            sql.append("j.jobName, u.universityName, m.majorName, cj.applicationStatus, ");
            sql.append("ROUND(DATE_PART('year', AGE(c.candidateBirthDay))) AS candidateAge, c.candidateGPA, c.candidateCurrentYear ");
            sql.append("FROM Candidates c ");

            List<Object> params = new ArrayList<>();
            List<String> orConditions = new ArrayList<>();

            for (Filter filter : filters) {
                List<String> andConditions = new ArrayList<>();


                // <editor-fold desc="Main Candidate Table">
                if (filter.getCandidateMinAge() != null) {
                    andConditions.add("ROUND(DATE_PART('year', AGE(c.candidateBirthDay))) >= ?");
                    params.add(filter.getCandidateMinAge());
                }

                if (filter.getCandidateMaxAge() != null) {
                    andConditions.add("ROUND(DATE_PART('year', AGE(c.candidateBirthDay))) <= ?");
                    params.add(filter.getCandidateMaxAge());
                }

                if (filter.getCandidateMinGPA() != null) {
                    andConditions.add("c.candidateGPA >= ?");
                    params.add(filter.getCandidateMinGPA());
                }

                if (filter.getCandidateMaxGPA() != null) {
                    andConditions.add("c.candidateGPA <= ?");
                    params.add(filter.getCandidateMaxGPA());
                }

                if (filter.getCandidateSexes() != null && !filter.getCandidateSexes().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateSexes().size(), "?"));
                    andConditions.add("c.candidateSex::text IN (" + inClause + ")");
                    params.addAll(filter.getCandidateSexes().stream().map(Enum::name).toList());
                }

                if (filter.getCandidateCurrentYears() != null && !filter.getCandidateCurrentYears().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateCurrentYears().size(), "?"));
                    andConditions.add("c.candidateCurrentYear::text IN (" + inClause + ")");
                    params.addAll(filter.getCandidateCurrentYears().stream().map(Enum::name).toList());
                }

                if (filter.getCandidateExpectedGraduateYears() != null && !filter.getCandidateExpectedGraduateYears().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateExpectedGraduateYears().size(), "?"));
                    andConditions.add("c.candidateExpectedGraduateYear IN (" + inClause + ")");
                    params.addAll(filter.getCandidateExpectedGraduateYears());
                }

                if (filter.getCandidateEnglishLevels() != null && !filter.getCandidateEnglishLevels().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateEnglishLevels().size(), "?"));
                    andConditions.add("c.candidateEnglishLevel::text IN (" + inClause + ")");
                    params.addAll(filter.getCandidateEnglishLevels().stream().map(Enum::name).toList());
                }

                if (filter.getCandidateApplicationStatuses() != null && !filter.getCandidateApplicationStatuses().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateApplicationStatuses().size(), "?"));
                    andConditions.add("cj.applicationStatus::text IN (" + inClause + ")");
                    params.addAll(filter.getCandidateApplicationStatuses().stream().map(Enum::name).toList());
                }
                // </editor-fold>

                // <editor-fold desc="JOIN UNIVERSITIES">
                if (filter.getCandidateUniversities() != null && !filter.getCandidateUniversities().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateUniversities().size(), "?"));
                    andConditions.add("u.universityName IN (" + inClause + ")");
                    params.addAll(filter.getCandidateUniversities());
                }
                // </editor-fold>

                // <editor-fold desc="JOIN MAJORS">
                if (filter.getCandidateMajors() != null && !filter.getCandidateMajors().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCandidateMajors().size(), "?"));
                    andConditions.add("m.majorName IN (" + inClause + ")");
                    params.addAll(filter.getCandidateMajors());
                }
                // </editor-fold>

                List<String> applicationDateOrs = new ArrayList<>();

                // <editor-fold desc="JOIN CANDIDATE JOBS">
                if (filter.getApplicationDates() != null && !filter.getApplicationDates().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getApplicationDates().size(), "?"));
                    applicationDateOrs.add("cj.applicationDate IN (" + inClause + ")");
                    params.addAll(filter.getApplicationDates());
                }

                List<String> minMaxDateAnds = new ArrayList<>();

                if (filter.getApplicationMinDate() != null) {
                    minMaxDateAnds.add("cj.applicationDate >= ?");
                    params.add(filter.getApplicationMinDate());
                }

                if (filter.getApplicationMaxDate() != null) {
                    minMaxDateAnds.add("cj.applicationDate <= ?");
                    params.add(filter.getApplicationMaxDate());
                }

                if (!minMaxDateAnds.isEmpty()) {
                    applicationDateOrs.add("(" + String.join(" AND ", minMaxDateAnds) + ")");
                }

                if (!applicationDateOrs.isEmpty()) {
                    andConditions.add("(" + String.join(" OR ", applicationDateOrs) + ")");
                }
                // </editor-fold>

                // <editor-fold desc="JOIN JOBS">
                if (filter.getJobNames() != null && !filter.getJobNames().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getJobNames().size(), "?"));
                    andConditions.add("j.jobName IN (" + inClause + ")");
                    params.addAll(filter.getJobNames());
                }
                // </editor-fold>

                // <editor-fold desc="JOIN CITIES">
                if (filter.getCityNames() != null && !filter.getCityNames().isEmpty()) {
                    String inClause = String.join(",", Collections.nCopies(filter.getCityNames().size(), "?"));
                    andConditions.add("ci.cityName IN (" + inClause + ")");
                    params.addAll(filter.getCityNames());
                }

                //</editor-fold>

                if (!andConditions.isEmpty()) {
                    orConditions.add("(" + String.join(" AND ", andConditions) + ")");
                }
            }

            // <editor-fold desc="JOINS">
            sql.append(" JOIN Universities u ON c.candidateUniversity = u.universityId ");
            sql.append(" JOIN Majors m ON c.candidateMajor = m.majorId ");
            sql.append(" JOIN CandidatesJobs cj ON c.candidateId = cj.candidateId ");
            sql.append(" JOIN Jobs j ON cj.jobId = j.jobId ");
            sql.append(" JOIN JobsCities jc ON j.jobId = jc.jobId JOIN Cities ci ON jc.cityId = ci.cityId ");

            // </editor-fold>

            if (!orConditions.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" OR ", orConditions));
            }

            // FOR DEBUG
            System.out.println("REPOSITORY::filterCandidates::SQL String: " + sql.toString());
            System.out.println("REPOSITORY::filterCandidates::Parameters: " + Arrays.toString(params.toArray()));


            List<Map<String, Object>> results = jdbc.queryForList(sql.toString(), params.toArray());
            List<FilterOutput> outputList = new ArrayList<>();

            // <editor-fold desc="Object Creation">
            for (Map<String, Object> row : results) {
                FilterOutput output = new FilterOutput();
                output.setCandidateEmail((String) row.get("candidateEmail"));
                output.setCandidateName((String) row.get("candidateName"));
                output.setCandidateSurname((String) row.get("candidateSurname"));
                output.setJobName((String) row.get("jobName"));
                output.setCandidateUniversity((String) row.get("universityName"));
                output.setCandidateMajor((String) row.get("majorName"));
                output.setStatus((String) row.get("applicationStatus"));
                output.setCandidateAge(row.get("candidateAge") != null ? ((Number) row.get("candidateAge")).intValue() : null);
                output.setCandidateGPA(row.get("candidateGPA") != null ? new java.math.BigDecimal(row.get("candidateGPA").toString()) : null);
                output.setCandidateCurrentYear((String) row.get("candidateCurrentYear"));

                outputList.add(output);
            }
            // </editor-fold>

            return outputList;
        } catch (Exception e) {
            System.out.println("REPOSITORY::filterCandidates::SQL Exception: " + e.getMessage());
            throw new RuntimeException("REPOSITORY::filterCandidates::Error while fetching candidates: " + e.getMessage(), e);
        }
    }






}
