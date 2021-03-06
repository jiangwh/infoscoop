alter table ${SCHEMA_NAME}is_adminRoles rename to is_adminRoles${BACKUP_TABLE_SUFFIX};
alter table ${SCHEMA_NAME}is_adminRoles${BACKUP_TABLE_SUFFIX} rename constraint is_adminRoles_unique to is_adminRoles_unique${BACKUP_TABLE_SUFFIX};
ALTER INDEX ${SCHEMA_NAME}is_adminRoles_unique RENAME TO is_adminRoles_unique${BACKUP_TABLE_SUFFIX};

create table ${SCHEMA_NAME}is_adminRoles (
  id integer not null primary key,
  roleid varchar(256 BYTE) not null,
  name VARCHAR(256 BYTE) NOT NULL,
  permission VARCHAR(256 BYTE) NOT NULL,
  allowdelete int default 1 NOT NULL,
  constraint is_adminRoles_unique unique (roleid)
);

insert into ${SCHEMA_NAME}is_adminRoles 
  select id, roleid, name, permission, allowdelete
  from ${SCHEMA_NAME}is_adminroles${BACKUP_TABLE_SUFFIX};

UPDATE ${SCHEMA_NAME}is_adminroles 
  SET permission='["menu", "menu_tree", "search", "widget", "defaultPanel", "portalLayout", "i18n", "properties", "proxy", "admins", "forbiddenURL", "authentication"]' 
  WHERE roleid='root'
