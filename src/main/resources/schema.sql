DROP TABLE IF EXISTS county_result;
CREATE TABLE county_result
(
    county_fips varchar(10) not null primary key,
    state_name varchar(100),
    county_name varchar(100),
    votes_gop bigint,
    votes_dem bigint,
    total_votes bigint,
    diff double,
    per_gop double,
    per_dem double,
    per_point_diff double
);
