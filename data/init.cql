CREATE KEYSPACE lolDb WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 3
};

USE lolDb;

CREATE TABLE champion (
  id uuid PRIMARY KEY,
  name text,
  shortDescription text,
  role text,
  difficulty text
);

