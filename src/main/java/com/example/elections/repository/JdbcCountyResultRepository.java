package com.example.elections.repository;

import com.example.elections.model.CountyResult;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("JdbcEngine")
@Primary
public class JdbcCountyResultRepository implements CountyResultRepository {

    private static final String SQL_INSERT = """
            insert into county_result
                (county_fips, state_name, county_name, votes_gop, votes_dem, total_votes,
                 diff, per_gop, per_dem, per_point_diff)
            values
                (:countyFips, :stateName, :countyName, :votesGop, :votesDem, :totalVotes,
                 :diff, :perGop, :perDem, :perPointDiff)
            """;

    private static final String SQL_UPDATE = """
            update county_result
                set state_name = :stateName,
                    county_name = :countyName,
                    votes_gop = :votesGop,
                    votes_dem = :votesDem,
                    total_votes = :totalVotes,
                    diff = :diff,
                    per_gop = :perGop,
                    per_dem = :perDem,
                    per_point_diff = :perPointDiff
            where county_fips = :countyFips
            """;

    private static final String SQL_DELETE =
            "delete from county_result where county_fips = :countyFips";

    private static final String SQL_EXISTS =
            "select count(*) > 0 from county_result where county_fips = :countyFips";

    private static final String SQL_FIND_ALL = """
            select county_fips, state_name, county_name, votes_gop, votes_dem, total_votes,
                   diff, per_gop, per_dem, per_point_diff
            from county_result
            """;

    private static final String SQL_FIND_BY_FIPS =
            SQL_FIND_ALL + " where county_fips = :countyFips";

    private final NamedParameterJdbcTemplate template;

    public JdbcCountyResultRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<CountyResult> findAll() {
        return template.query(SQL_FIND_ALL, countyRowMapper);
    }

    @Override
    public Optional<CountyResult> findByFips(String countyFips) {
        List<CountyResult> results = template.query(
                SQL_FIND_BY_FIPS,
                Collections.singletonMap("countyFips", countyFips),
                countyRowMapper
        );
        return results.stream().findFirst();
    }

    @Override
    public CountyResult save(CountyResult result) {
        if (result == null || result.getCountyFips() == null) {
            throw new IllegalArgumentException("countyFips is required");
        }
        Map<String, Object> params = toParams(result);
        if (existsByFips(result.getCountyFips())) {
            template.update(SQL_UPDATE, params);
        } else {
            template.update(SQL_INSERT, params);
        }
        return findByFips(result.getCountyFips()).orElse(result);
    }

    @Override
    public void deleteByFips(String countyFips) {
        template.update(SQL_DELETE, Collections.singletonMap("countyFips", countyFips));
    }

    @Override
    public boolean existsByFips(String countyFips) {
        Boolean exists = template.queryForObject(
                SQL_EXISTS,
                Collections.singletonMap("countyFips", countyFips),
                Boolean.class
        );
        return Boolean.TRUE.equals(exists);
    }

    private Map<String, Object> toParams(CountyResult result) {
        Map<String, Object> params = new HashMap<>();
        params.put("countyFips", result.getCountyFips());
        params.put("stateName", result.getStateName());
        params.put("countyName", result.getCountyName());
        params.put("votesGop", result.getVotesGop());
        params.put("votesDem", result.getVotesDem());
        params.put("totalVotes", result.getTotalVotes());
        params.put("diff", result.getDiff());
        params.put("perGop", result.getPerGop());
        params.put("perDem", result.getPerDem());
        params.put("perPointDiff", result.getPerPointDiff());
        return params;
    }

    private final RowMapper<CountyResult> countyRowMapper = (ResultSet rs, int rowNum) -> {
        CountyResult result = new CountyResult();
        result.setCountyFips(rs.getString("county_fips"));
        result.setStateName(rs.getString("state_name"));
        result.setCountyName(rs.getString("county_name"));
        result.setVotesGop(rs.getLong("votes_gop"));
        result.setVotesDem(rs.getLong("votes_dem"));
        result.setTotalVotes(rs.getLong("total_votes"));
        result.setDiff(rs.getDouble("diff"));
        result.setPerGop(rs.getDouble("per_gop"));
        result.setPerDem(rs.getDouble("per_dem"));
        result.setPerPointDiff(rs.getDouble("per_point_diff"));
        return result;
    };
}
