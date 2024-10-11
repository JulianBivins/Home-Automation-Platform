-- users table
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL
--     UNIQUE
     ,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    roles VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- devices table
CREATE TABLE devices (
    device_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
--    CHECK (type IN ('LIGHTS', 'UTILITY', 'SPEAKER', 'TELEVISION', 'CAMERA')),
    user_id BIGINT,
    CONSTRAINT fk_device_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

--  groups table
CREATE TABLE `groups` (
    group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
--    rule_id BIGINT,
    user_id BIGINT,
--    CONSTRAINT fk_group_rule FOREIGN KEY (rule_id) REFERENCES home_automation_rules(rule_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_group_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

--  home_automation_rules table
CREATE TABLE home_automation_rules (
    rule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT,
    event VARCHAR(50),
    group_id BIGINT,
--    CHECK (event IN ('TIME', 'PERIOD', 'BEFORE_OTHER', 'AFTER_OTHER', 'WHEN_INSERT_ON', 'WHEN_INSERT_OFF')),
    CONSTRAINT fk_rule_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rule_group FOREIGN KEY (group_id) REFERENCES `groups`(group_id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- device_rule table (devices and home_automation_rules)
CREATE TABLE device_rule (
    rule_id BIGINT,
    device_id BIGINT,
    PRIMARY KEY (rule_id, device_id),
    CONSTRAINT fk_device_rule_rule FOREIGN KEY (rule_id) REFERENCES home_automation_rules(rule_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_device_rule_device FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE ON UPDATE CASCADE
);

--  rule_device_behaviour table
CREATE TABLE rule_device_behaviour (
    rule_id BIGINT,
    device_id BIGINT,
    behaviour VARCHAR(50),
--    CHECK (behaviour IN ('ON', 'OFF', 'STAND_BY', 'TIMED')),
    PRIMARY KEY (rule_id, device_id),
    CONSTRAINT fk_behaviour_rule FOREIGN KEY (rule_id) REFERENCES home_automation_rules(rule_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_behaviour_device FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE ON UPDATE CASCADE
);













