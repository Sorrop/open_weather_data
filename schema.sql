CREATE TABLE current_reports (
  city_id INTEGER UNIQUE,
  city_name VARCHAR(128),
  country_name VARCHAR(128),
  country_code VARCHAR(2) not null,
  temperature FLOAT,
  created_at TIMESTAMP WITH TIME ZONE  NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW());


CREATE TABLE forecasts (
  city_id INTEGER UNIQUE,
  city_name VARCHAR(128),
  country_name VARCHAR(128),
  country_code VARCHAR(2) not null,
  forecasts JSON NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE  NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW());


CREATE OR REPLACE FUNCTION trigger_updated_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER updated_timestamp_current
BEFORE UPDATE ON current_reports
FOR EACH ROW
EXECUTE PROCEDURE trigger_updated_timestamp();

CREATE TRIGGER updated_timestamp_forecasts
BEFORE UPDATE ON forecasts
FOR EACH ROW
EXECUTE PROCEDURE trigger_updated_timestamp();
