-- Connection Pin Sequence
CREATE SEQUENCE IF NOT EXISTS connection_pin_sequence;
ALTER SEQUENCE connection_pin_sequence RESTART WITH 1;

-- Connection Sequence
CREATE SEQUENCE IF NOT EXISTS connection_sequence;
ALTER SEQUENCE connection_sequence RESTART WITH 1;

-- Location Sequence
CREATE SEQUENCE IF NOT EXISTS location_sequence;
ALTER SEQUENCE location_sequence RESTART WITH 1;

-- Profile Sequence
CREATE SEQUENCE IF NOT EXISTS profile_sequence;
ALTER SEQUENCE profile_sequence RESTART WITH 1;

CREATE SEQUENCE IF NOT EXISTS setting_sequence;
ALTER SEQUENCE setting_sequence RESTART WITH 1;

-- User Sequence
CREATE SEQUENCE IF NOT EXISTS _user_sequence;
ALTER SEQUENCE _user_sequence RESTART WITH 1;
