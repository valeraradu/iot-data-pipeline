//Create keyspace
CREATE KEYSPACE IF NOT EXISTS TemperatureKeySpace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

//Create table
CREATE TABLE TemperatureKeySpace.TemperatureEvent (latitude text, longitude text, temp bigint, avgTemp bigint, timeStamp temp, PRIMARY KEY (lat,lng,timestamp));

//Select from table
SELECT * FROM TemperatureKeySpace.TemperatureEvent;

//Truncate table
TRUNCATE TABLE TemperatureKeySpace.TemperatureEvent;