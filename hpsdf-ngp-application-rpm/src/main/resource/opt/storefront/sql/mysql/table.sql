create table ASSETPROVIDER (id bigint(18) not null auto_increment, entityId bigint(18) not null, email varchar(255), externalId varchar(255), settlementRate double precision, commissionRate double precision, firstName varchar(255), lastName varchar(255), streetAddress varchar(255), city varchar(255), phone varchar(255), contractExpireDate datetime, homePage varchar(255), contractStartDate datetime, name varchar(30) not null unique, priority integer, country varchar(255), locale varchar(255), source varchar(255), organization varchar(255), STATUSID bigint(18), primary key (id), unique (entityId)) type=InnoDB;
create table ApiKey (id bigint(18) not null auto_increment, sgName varchar(100) not null, sgPassword varchar(100) not null, description text, primary key (id)) type=InnoDB;
create table Asset (id bigint(18) not null auto_increment, entityId bigint(18) not null, currentVersion varchar(255), authorid varchar(255), createDate datetime not null, updateDate datetime, brief text, downloadCount bigint(18) not null, externalId varchar(255), currentVersionId bigint(18), assetHomePage varchar(255), thumbnailLocation varchar(255), thumbnailMiddleLocation varchar(255), thumbnailBigLocation varchar(255), docUrl varchar(255), averageUserRating double precision, recommendOrder bigint(18), recommendStartDate datetime, recommendDueDate datetime, publishDate datetime, newArrivalDueDate datetime, recommendUpdateDate datetime, name text, source varchar(255), description text, PARENTID bigint(18), STATUSID bigint(18) not null, PROVIDERID bigint(18), primary key (id), unique (entityId)) type=InnoDB;
create table AssetBinaryVersion (id bigint(18) not null auto_increment, entityId bigint(18) not null, createDate datetime not null, updateDate datetime not null, brief text, externalId varchar(255), thumbnailLocation varchar(255), thumbnailMiddleLocation varchar(255), thumbnailBigLocation varchar(255), recommendOrder bigint(18), recommendStartDate datetime, recommendDueDate datetime, publishDate datetime, newArrivalDueDate datetime, recommendUpdateDate datetime, expireDate datetime, ownerAssetParentId bigint(18), name text, location varchar(255), fileName varchar(255), fileSize numeric(19,2), version varchar(255), description text, ASSETID bigint(18), STATUSID bigint(18), primary key (id), unique (entityId)) type=InnoDB;
create table AssetCategoryRelation (id bigint(18) not null auto_increment, CTGID bigint(18) not null, ASSETID bigint(18) not null, VERSIONID bigint(18), primary key (id)) type=InnoDB;
create table AssetLifecycleAction (id bigint(18) not null auto_increment, comments text, createDate datetime, completeDate datetime, event varchar(200), result varchar(200), submitterid text not null, ownerid text not null, processStatus text, commentType varchar(255), notificationDate datetime, description text, POSTSTATUSID bigint(18), ASSETBINARYVERSIONID bigint(18), PRESTATUSID bigint(18), ASSETID bigint(18) not null, primary key (id)) type=InnoDB;
create table AssetLifecycleActionHistory (id bigint(18) not null auto_increment, comments varchar(255), createDate datetime, externalId varchar(255), completeDate datetime, event varchar(255), result varchar(255), postStatus varchar(255), processStatus varchar(255), commentType varchar(255), notificationDate datetime, submitterId varchar(255), ownerId varchar(255), assetBinaryVersionId bigint(18), assetId bigint(18), prestatus varchar(255), source varchar(255), version varchar(255), description text, primary key (id)) type=InnoDB;
create table AssetLifecycleCommentType (id bigint(18) not null auto_increment, commentType varchar(255), commentTemplate varchar(255), primary key (id)) type=InnoDB;
create table AssetPlatformRelation (id bigint(18) not null auto_increment, ASSETID bigint(18) not null, PLATFORMID bigint(18) not null, primary key (id)) type=InnoDB;
create table AssetPrice (id bigint(18) not null auto_increment, amount numeric(19,2), currency varchar(255), ASSETID bigint(18) not null, VERSIONID bigint(18), primary key (id)) type=InnoDB;
create table AssetRating (id bigint(18) not null auto_increment, createDate datetime not null, userid varchar(128) not null, rating double precision not null, description text, ASSETID bigint(18) not null, primary key (id)) type=InnoDB;
create table AssetRestrictionRelation (id bigint(18) not null auto_increment, RESTRICTIONID bigint(18) not null, ASSETID bigint(18) not null, VERSIONID bigint(18), primary key (id)) type=InnoDB;
create table AssetTagRelation (id bigint(18) not null auto_increment, TAGID bigint(18) not null, VERSIONID bigint(18), ASSETID bigint(18) not null, primary key (id)) type=InnoDB;
create table Attribute (attributeID bigint(18) not null auto_increment, attributeName varchar(100) not null unique, attributeType integer not null, attributeDesc varchar(225), primary key (attributeID)) type=InnoDB;
create table AttributeValueChar (attributeValueID bigint(18) not null auto_increment, entityId bigint(18) not null, value text not null, attributeID bigint(18), primary key (attributeValueID)) type=InnoDB;
create table AttributeValueDate (attributeValueID bigint(18) not null auto_increment, entityId bigint(18) not null, value datetime not null, attributeID bigint(18), primary key (attributeValueID)) type=InnoDB;
create table AttributeValueDomain (id bigint(18) not null auto_increment, attributeValueID bigint(18) not null, isDefault bit not null, ATTRIBUTEID bigint(18), primary key (id)) type=InnoDB;
create table AttributeValueNumber (attributeValueID bigint(18) not null auto_increment, entityId bigint(18) not null, value float not null, attributeID bigint(18), primary key (attributeValueID)) type=InnoDB;
create table BinaryFile (id bigint(18) not null auto_increment, fileLocation varchar(255) not null unique, fileBinary longblob not null, primary key (id)) type=InnoDB;
create table Category (id bigint(18) not null auto_increment, externalId varchar(255), name varchar(100) not null, displayName varchar(255), source varchar(255), description text, PARENTID bigint(18), primary key (id)) type=InnoDB;
create table Comments (id bigint(18) not null auto_increment, createDate datetime not null, updateDate datetime, externalId varchar(255), userid varchar(128), assetVersion varchar(255), content text, title varchar(255), ASSETID bigint(18) not null, primary key (id)) type=InnoDB;
create table CommentsSensorWord (id bigint(18) not null auto_increment, updateDate datetime, sensorWord varchar(255) unique, operatorId varchar(255), primary key (id)) type=InnoDB;
create table ContentProviderOperator (id bigint(18) not null auto_increment, userid varchar(255), ASSETPROVIDERID bigint(18) not null, primary key (id)) type=InnoDB;
create table Country (id bigint(18) not null auto_increment, name varchar(100) not null, primary key (id)) type=InnoDB;
create table Entity (entityID bigint(18) not null auto_increment, entityType integer not null, primary key (entityID)) type=InnoDB;
create table EntityAttribute (id bigint(18) not null auto_increment, attributeType integer not null, attributeValueID bigint(18) not null, ENTITYID bigint(18), primary key (id)) type=InnoDB;
create table Language (id bigint(18) not null auto_increment, name varchar(100) not null, locale varchar(20), primary key (id)) type=InnoDB;
create table Operation (id bigint(18) not null auto_increment, rate varchar(100), name varchar(100) not null, SERVICEID bigint(18), primary key (id)) type=InnoDB;
create table ParentAssetVersionSummary (id bigint(18) not null auto_increment, recommendOrder bigint(18), publishDate datetime, parentAssetId bigint(18), publishFlag varchar(255), newArrivalFlag bigint(18), recommendFlag bigint(18), saleFlag bigint(18), downloadTime bigint(18), saleStart datetime, saleEnd datetime, lowestPrice double precision, priceEqualFlg bigint(18), sampleFlag bigint(18), primary key (id), unique (parentAssetId)) type=InnoDB;
create table Platform (id bigint(18) not null auto_increment, name varchar(100) not null unique, description text, primary key (id)) type=InnoDB;
create table PurchaseHistory (id bigint(18) not null auto_increment, userid varchar(255), purchaseDate datetime, paidPrice numeric(19,2), transactionid varchar(255), status varchar(255), currency varchar(255), ASSETID bigint(18) not null, primary key (id)) type=InnoDB;
create table RestrictedType (id bigint(18) not null auto_increment, type varchar(50) not null unique, primary key (id)) type=InnoDB;
create table RoleCategory (id bigint(18) not null auto_increment, createDate datetime, roleName varchar(255) not null unique, displayName varchar(255), description text, primary key (id)) type=InnoDB;
create table ScreenShots (id bigint(18) not null auto_increment, createDate datetime, mediaType varchar(255), storeLocation varchar(255), description varchar(255), sequence bigint(18), VERSIONID bigint(18), ASSETID bigint(18) not null, primary key (id)) type=InnoDB;
create table Service (id bigint(18) not null auto_increment, docUrl text, sdkUrl text, serviceid text, brokerServiceName varchar(255), accessInterface varchar(255), brokerServiceAuthType varchar(255), brokerServiceUrl text, name varchar(100) not null unique, type varchar(100), description text, primary key (id)) type=InnoDB;
create table ServiceSubscription (id bigint(18) not null auto_increment, createDate datetime, userid varchar(128), APIKEYID bigint(18) not null, SERVICEID bigint(18), primary key (id)) type=InnoDB;
create table ShoppingCart (id bigint(18) not null auto_increment, createDate datetime, userid varchar(255), itemPrice numeric(19,2), status varchar(255), currency varchar(255), ASSETID bigint(18) not null, primary key (id)) type=InnoDB;
create table Status (id bigint(18) not null auto_increment, type bigint(18), displayName varchar(255), status varchar(30) not null unique, primary key (id)) type=InnoDB;
create table SubscriberProfile (userId varchar(255) not null, entityId bigint(18) not null, ownerTesterUserId varchar(255), msisdn varchar(255) not null, clientTesterFlag bigint(18), clientOwnerProviderId bigint(18), displayName varchar(255), primary key (userId), unique (entityId)) type=InnoDB;
create table SystemConfig (id bigint(18) not null auto_increment, configKey varchar(255), value varchar(255), primary key (id)) type=InnoDB;
create table Tag (id bigint(18) not null auto_increment, name varchar(100) not null unique, description text, primary key (id)) type=InnoDB;
create table UserDownloadHistory (id bigint(18) not null auto_increment, userid varchar(255), downloadDate datetime not null, version varchar(255), STATUS text not null, ASSETID bigint(18) not null, primary key (id)) type=InnoDB;
create table UserLifecycleAction (id bigint(18) not null auto_increment, comments text, createDate datetime not null, event varchar(255), submitterid text, ownerid text, processStatus text, userid varchar(255) not null, preRole varchar(255), postRole varchar(255), description text, primary key (id)) type=InnoDB;
create table UserLifecycleActionHistory (id bigint(18) not null auto_increment, comments text, createDate datetime not null, event varchar(255), result varchar(255), submitterid text, ownerid text, processStatus text, userid varchar(255) not null, preRole varchar(255), postRole varchar(255), description text, primary key (id)) type=InnoDB;
create table UserProfile (userid varchar(128) not null, entityId bigint(18) not null, email text not null, createDate datetime, idcard varchar(50), gender varchar(10), cellphone varchar(50), company varchar(100), zip varchar(50), birthday datetime, firstname varchar(50), lastname varchar(50), preferRole varchar(50), verificationCode varchar(50), onlineTime bigint(18), disabled bit, disabledDate datetime, address text, password varchar(50), LANGUAGEID bigint(18), COUNTRYID bigint(18), primary key (userid), unique (entityId)) type=InnoDB;
create table UserRoleCategory (id bigint(18) not null auto_increment, USERID varchar(128) not null, ROLEID bigint(18) not null, primary key (id)) type=InnoDB;
alter table ASSETPROVIDER add index FKC60D18C1B8A7CE0D (STATUSID), add constraint FKC60D18C1B8A7CE0D foreign key (STATUSID) references Status (id);
alter table Asset add index FK3C9FAD0A3503EE7 (PARENTID), add constraint FK3C9FAD0A3503EE7 foreign key (PARENTID) references Asset (id);
alter table Asset add index FK3C9FAD0B8A7CE0D (STATUSID), add constraint FK3C9FAD0B8A7CE0D foreign key (STATUSID) references Status (id);
alter table Asset add index FK3C9FAD0A1DF8ACB (PROVIDERID), add constraint FK3C9FAD0A1DF8ACB foreign key (PROVIDERID) references ASSETPROVIDER (id);
alter table AssetBinaryVersion add index FKDC29CA7B8A7CE0D (STATUSID), add constraint FKDC29CA7B8A7CE0D foreign key (STATUSID) references Status (id);
alter table AssetBinaryVersion add index FKDC29CA73342A7AD (ASSETID), add constraint FKDC29CA73342A7AD foreign key (ASSETID) references Asset (id);
alter table AssetCategoryRelation add index FKAD99DC0AD92D48BD (CTGID), add constraint FKAD99DC0AD92D48BD foreign key (CTGID) references Category (id);
alter table AssetCategoryRelation add index FKAD99DC0AD936A508 (VERSIONID), add constraint FKAD99DC0AD936A508 foreign key (VERSIONID) references AssetBinaryVersion (id);
alter table AssetCategoryRelation add index FKAD99DC0A3342A7AD (ASSETID), add constraint FKAD99DC0A3342A7AD foreign key (ASSETID) references Asset (id);
alter table AssetLifecycleAction add index FK74B45D50246A2DB0 (PRESTATUSID), add constraint FK74B45D50246A2DB0 foreign key (PRESTATUSID) references Status (id);
alter table AssetLifecycleAction add index FK74B45D509189C6AD (POSTSTATUSID), add constraint FK74B45D509189C6AD foreign key (POSTSTATUSID) references Status (id);
alter table AssetLifecycleAction add index FK74B45D503342A7AD (ASSETID), add constraint FK74B45D503342A7AD foreign key (ASSETID) references Asset (id);
alter table AssetLifecycleAction add index FK74B45D508CD96137 (ASSETBINARYVERSIONID), add constraint FK74B45D508CD96137 foreign key (ASSETBINARYVERSIONID) references AssetBinaryVersion (id);
alter table AssetPlatformRelation add index FK39C0427FB643B74F (PLATFORMID), add constraint FK39C0427FB643B74F foreign key (PLATFORMID) references Platform (id);
alter table AssetPlatformRelation add index FK39C0427F3342A7AD (ASSETID), add constraint FK39C0427F3342A7AD foreign key (ASSETID) references Asset (id);
alter table AssetPrice add index FKF4EF0859D936A508 (VERSIONID), add constraint FKF4EF0859D936A508 foreign key (VERSIONID) references AssetBinaryVersion (id);
alter table AssetPrice add index FKF4EF08593342A7AD (ASSETID), add constraint FKF4EF08593342A7AD foreign key (ASSETID) references Asset (id);
alter table AssetRating add index FKAB713C8D3342A7AD (ASSETID), add constraint FKAB713C8D3342A7AD foreign key (ASSETID) references Asset (id);
alter table AssetRestrictionRelation add index FK83A9A0F81662ABAA (RESTRICTIONID), add constraint FK83A9A0F81662ABAA foreign key (RESTRICTIONID) references RestrictedType (id);
alter table AssetRestrictionRelation add index FK83A9A0F8D936A508 (VERSIONID), add constraint FK83A9A0F8D936A508 foreign key (VERSIONID) references AssetBinaryVersion (id);
alter table AssetRestrictionRelation add index FK83A9A0F83342A7AD (ASSETID), add constraint FK83A9A0F83342A7AD foreign key (ASSETID) references Asset (id);
alter table AssetTagRelation add index FKA5A1AEC6D936A508 (VERSIONID), add constraint FKA5A1AEC6D936A508 foreign key (VERSIONID) references AssetBinaryVersion (id);
alter table AssetTagRelation add index FKA5A1AEC67A465301 (TAGID), add constraint FKA5A1AEC67A465301 foreign key (TAGID) references Tag (id);
alter table AssetTagRelation add index FKA5A1AEC63342A7AD (ASSETID), add constraint FKA5A1AEC63342A7AD foreign key (ASSETID) references Asset (id);
alter table AttributeValueChar add index FK4882D94BF71AB0F1 (attributeID), add constraint FK4882D94BF71AB0F1 foreign key (attributeID) references Attribute (attributeID);
alter table AttributeValueDate add index FK488335A3F71AB0F1 (attributeID), add constraint FK488335A3F71AB0F1 foreign key (attributeID) references Attribute (attributeID);
alter table AttributeValueDomain add index FK354E7179F71AB0F1 (ATTRIBUTEID), add constraint FK354E7179F71AB0F1 foreign key (ATTRIBUTEID) references Attribute (attributeID);
alter table AttributeValueNumber add index FK46B377FEF71AB0F1 (attributeID), add constraint FK46B377FEF71AB0F1 foreign key (attributeID) references Attribute (attributeID);
alter table Category add index FK6DD211E198F7A51 (PARENTID), add constraint FK6DD211E198F7A51 foreign key (PARENTID) references Category (id);
alter table Comments add index FKDFF242143342A7AD (ASSETID), add constraint FKDFF242143342A7AD foreign key (ASSETID) references Asset (id);
alter table ContentProviderOperator add index FK456A502E90B890B6 (userid), add constraint FK456A502E90B890B6 foreign key (userid) references UserProfile (userid);
alter table ContentProviderOperator add index FK456A502ECA3789BB (ASSETPROVIDERID), add constraint FK456A502ECA3789BB foreign key (ASSETPROVIDERID) references ASSETPROVIDER (id);
alter table EntityAttribute add index FKE73D4DD9FF77F743 (ENTITYID), add constraint FKE73D4DD9FF77F743 foreign key (ENTITYID) references Entity (entityID);
alter table Operation add index FKDA8CF547B37B1D77 (SERVICEID), add constraint FKDA8CF547B37B1D77 foreign key (SERVICEID) references Service (id);
alter table PurchaseHistory add index FKBCC3BC333342A7AD (ASSETID), add constraint FKBCC3BC333342A7AD foreign key (ASSETID) references Asset (id);
alter table ScreenShots add index FK6029DF8DD936A508 (VERSIONID), add constraint FK6029DF8DD936A508 foreign key (VERSIONID) references AssetBinaryVersion (id);
alter table ScreenShots add index FK6029DF8D3342A7AD (ASSETID), add constraint FK6029DF8D3342A7AD foreign key (ASSETID) references Asset (id);
alter table ServiceSubscription add index FK8C43D452856DE953 (APIKEYID), add constraint FK8C43D452856DE953 foreign key (APIKEYID) references ApiKey (id);
alter table ServiceSubscription add index FK8C43D452B37B1D77 (SERVICEID), add constraint FK8C43D452B37B1D77 foreign key (SERVICEID) references Service (id);
alter table ShoppingCart add index FKABBC40C83342A7AD (ASSETID), add constraint FKABBC40C83342A7AD foreign key (ASSETID) references Asset (id);
alter table UserDownloadHistory add index FKF073AF813342A7AD (ASSETID), add constraint FKF073AF813342A7AD foreign key (ASSETID) references Asset (id);
alter table UserProfile add index FK3EFA133E65CEE7D9 (LANGUAGEID), add constraint FK3EFA133E65CEE7D9 foreign key (LANGUAGEID) references Language (id);
alter table UserProfile add index FK3EFA133ED2818AF9 (COUNTRYID), add constraint FK3EFA133ED2818AF9 foreign key (COUNTRYID) references Country (id);
alter table UserRoleCategory add index FK30C1F71F90B890B6 (USERID), add constraint FK30C1F71F90B890B6 foreign key (USERID) references UserProfile (userid);
alter table UserRoleCategory add index FK30C1F71F529686B3 (ROLEID), add constraint FK30C1F71F529686B3 foreign key (ROLEID) references RoleCategory (id);



create table ClientAppHandSetRelation (id bigint(18) not null auto_increment, HANDSETDEVICEID bigint(18), CLIENTAPPID bigint(18), primary key (id)) type=InnoDB;
create table ClientAppSetting (id bigint(18) not null auto_increment, clientName varchar(255), fileLocation varchar(255), updateDate datetime, version varchar(255), primary key (id)) type=InnoDB;
create table ClientApplication (id bigint(18) not null auto_increment, clientName varchar(255), fileLocation varchar(255), updateDate datetime, version varchar(255), primary key (id)) type=InnoDB;
create table ClientSettingAppRelation (id bigint(18) not null auto_increment, CLIENTAPPSETTINGID bigint(18) not null, CLIENTAPPID bigint(18) not null, primary key (id)) type=InnoDB;
create table ContentItem (id bigint(18) not null auto_increment, itemId varchar(255), assetId bigint(18), versionId bigint(18), lid varchar(255), itemNumber bigint(18), itemName varchar(255), itemPrice bigint(18), displayText varchar(255), endTime datetime, intervalDays bigint(18), operatorRefId varchar(255), priceType varchar(255), trialFlag bigint(18), assetExternalId varchar(255), synchronizeFlag varchar(255), priceId varchar(255), trialPeriodInDays bigint(18), startTime datetime, version varchar(255), currency varchar(255), itemCount bigint(18), primary key (id)) type=InnoDB;
create table DeviceFunction (id bigint(18) not null auto_increment, function varchar(255), primary key (id)) type=InnoDB;
create table HandSetDevice (id bigint(18) not null auto_increment, deviceName varchar(255), functionFilter varchar(255), resolutionFilter bigint(18), createTime datetime, displayName varchar(255), primary key (id)) type=InnoDB;
create table HandSetFunctionRelation (id bigint(18) not null auto_increment, HANDSETID bigint(18) not null, DEVICEFUNCTIONID bigint(18) not null, primary key (id)) type=InnoDB;
create table MimeType (id bigint(18) not null auto_increment, type varchar(255), subType varchar(255), HANDSETID bigint(18), primary key (id)) type=InnoDB;
create table PurchaseHistoryExtend (eventId varchar(255) not null, userId varchar(255), itemId bigint(18), assetId bigint(18), assetExternalId varchar(255), orderNo varchar(255), tempPaidResult varchar(255), reqconfirmResult varchar(255), reqconfirmDate datetime, msisdn varchar(255), paidPrice bigint(18), tempPaidDate datetime, imsi varchar(255), tempPaidDetailCode varchar(255), reqConfirmDetailCode varchar(255), roinStallStatus varchar(255), version varchar(255), status varchar(255), primary key (eventId)) type=InnoDB;
create table SBMAssetPrice (id bigint(18) not null auto_increment, itemId bigint(18), assetId bigint(18), versionId bigint(18), operatorRefId varchar(255), trialFlag bigint(18), trialPeriodInDays bigint(18), amount numeric(19,2), billingComment varchar(255), type varchar(255), currency varchar(255), primary key (id)) type=InnoDB;
create table SecurityToken (token varchar(255) not null, userId varchar(255), msisdn varchar(255), imsi varchar(255), expireTime datetime, lockFlag integer, provideId varchar(255), testClientFlag integer, primary key (token)) type=InnoDB;
alter table ClientAppHandSetRelation add index FK13D36379BECA904B (CLIENTAPPID), add constraint FK13D36379BECA904B foreign key (CLIENTAPPID) references ClientApplication (id);
alter table ClientAppHandSetRelation add index FK13D36379F4287E62 (HANDSETDEVICEID), add constraint FK13D36379F4287E62 foreign key (HANDSETDEVICEID) references HandSetDevice (id);
alter table ClientSettingAppRelation add index FK42CB4618BECA904B (CLIENTAPPID), add constraint FK42CB4618BECA904B foreign key (CLIENTAPPID) references ClientApplication (id);
alter table ClientSettingAppRelation add index FK42CB4618C8BED87A (CLIENTAPPSETTINGID), add constraint FK42CB4618C8BED87A foreign key (CLIENTAPPSETTINGID) references ClientAppSetting (id);
alter table HandSetFunctionRelation add index FK9FD14967190B5C82 (DEVICEFUNCTIONID), add constraint FK9FD14967190B5C82 foreign key (DEVICEFUNCTIONID) references DeviceFunction (id);
alter table HandSetFunctionRelation add index FK9FD14967AFAAC7CC (HANDSETID), add constraint FK9FD14967AFAAC7CC foreign key (HANDSETID) references HandSetDevice (id);
alter table MimeType add index FKB0E051AEAFAAC7CC (HANDSETID), add constraint FKB0E051AEAFAAC7CC foreign key (HANDSETID) references HandSetDevice (id);

CREATE TABLE `jbpm4_deployment` (
  `DBID_` bigint(18) NOT NULL,
  `NAME_` text,
  `TIMESTAMP_` bigint(18) DEFAULT NULL,
  `STATE_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_deployprop` (
  `DBID_` bigint(18) NOT NULL,
  `DEPLOYMENT_` bigint(18) DEFAULT NULL,
  `OBJNAME_` varchar(255) DEFAULT NULL,
  `KEY_` varchar(255) DEFAULT NULL,
  `STRINGVAL_` varchar(255) DEFAULT NULL,
  `LONGVAL_` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_DEPLPROP_DEPL` (`DEPLOYMENT_`),
  CONSTRAINT `FK_DEPLPROP_DEPL` FOREIGN KEY (`DEPLOYMENT_`) REFERENCES `jbpm4_deployment` (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_execution` (
  `DBID_` bigint(18) NOT NULL,
  `CLASS_` varchar(255) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `ACTIVITYNAME_` varchar(255) DEFAULT NULL,
  `PROCDEFID_` varchar(255) DEFAULT NULL,
  `HASVARS_` bit(1) DEFAULT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  `KEY_` varchar(255) DEFAULT NULL,
  `ID_` varchar(255) DEFAULT NULL,
  `STATE_` varchar(255) DEFAULT NULL,
  `SUSPHISTSTATE_` varchar(255) DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `HISACTINST_` bigint(18) DEFAULT NULL,
  `PARENT_` bigint(18) DEFAULT NULL,
  `INSTANCE_` bigint(18) DEFAULT NULL,
  `SUPEREXEC_` bigint(18) DEFAULT NULL,
  `SUBPROCINST_` bigint(18) DEFAULT NULL,
  `PARENT_IDX_` int(11) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  UNIQUE KEY `ID_` (`ID_`),
  KEY `FK_EXEC_PARENT` (`PARENT_`),
  KEY `FK_EXEC_SUBPI` (`SUBPROCINST_`),
  KEY `FK_EXEC_INSTANCE` (`INSTANCE_`),
  KEY `FK_EXEC_SUPEREXEC` (`SUPEREXEC_`),
  CONSTRAINT `FK_EXEC_SUPEREXEC` FOREIGN KEY (`SUPEREXEC_`) REFERENCES `jbpm4_execution` (`DBID_`),
  CONSTRAINT `FK_EXEC_INSTANCE` FOREIGN KEY (`INSTANCE_`) REFERENCES `jbpm4_execution` (`DBID_`),
  CONSTRAINT `FK_EXEC_PARENT` FOREIGN KEY (`PARENT_`) REFERENCES `jbpm4_execution` (`DBID_`),
  CONSTRAINT `FK_EXEC_SUBPI` FOREIGN KEY (`SUBPROCINST_`) REFERENCES `jbpm4_execution` (`DBID_`)
) type=InnoDB;



CREATE TABLE `jbpm4_hist_procinst` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `ID_` varchar(255) DEFAULT NULL,
  `PROCDEFID_` varchar(255) DEFAULT NULL,
  `KEY_` varchar(255) DEFAULT NULL,
  `START_` datetime DEFAULT NULL,
  `END_` datetime DEFAULT NULL,
  `DURATION_` bigint(18) DEFAULT NULL,
  `STATE_` varchar(255) DEFAULT NULL,
  `ENDACTIVITY_` varchar(255) DEFAULT NULL,
  `NEXTIDX_` int(11) DEFAULT NULL,
  PRIMARY KEY (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_hist_task` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `EXECUTION_` varchar(255) DEFAULT NULL,
  `OUTCOME_` varchar(255) DEFAULT NULL,
  `ASSIGNEE_` varchar(255) DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `STATE_` varchar(255) DEFAULT NULL,
  `CREATE_` datetime DEFAULT NULL,
  `END_` datetime DEFAULT NULL,
  `DURATION_` bigint(18) DEFAULT NULL,
  `NEXTIDX_` int(11) DEFAULT NULL,
  `SUPERTASK_` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_HSUPERT_SUB` (`SUPERTASK_`),
  CONSTRAINT `FK_HSUPERT_SUB` FOREIGN KEY (`SUPERTASK_`) REFERENCES `jbpm4_hist_task` (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_hist_var` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `PROCINSTID_` varchar(255) DEFAULT NULL,
  `EXECUTIONID_` varchar(255) DEFAULT NULL,
  `VARNAME_` varchar(255) DEFAULT NULL,
  `VALUE_` varchar(255) DEFAULT NULL,
  `HPROCI_` bigint(18) DEFAULT NULL,
  `HTASK_` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_HVAR_HPROCI` (`HPROCI_`),
  KEY `FK_HVAR_HTASK` (`HTASK_`),
  CONSTRAINT `FK_HVAR_HTASK` FOREIGN KEY (`HTASK_`) REFERENCES `jbpm4_hist_task` (`DBID_`),
  CONSTRAINT `FK_HVAR_HPROCI` FOREIGN KEY (`HPROCI_`) REFERENCES `jbpm4_hist_procinst` (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_id_group` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `ID_` varchar(255) DEFAULT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  `TYPE_` varchar(255) DEFAULT NULL,
  `PARENT_` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_GROUP_PARENT` (`PARENT_`),
  CONSTRAINT `FK_GROUP_PARENT` FOREIGN KEY (`PARENT_`) REFERENCES `jbpm4_id_group` (`DBID_`)
) type=InnoDB;



CREATE TABLE `jbpm4_id_user` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `ID_` varchar(255) DEFAULT NULL,
  `PASSWORD_` varchar(255) DEFAULT NULL,
  `GIVENNAME_` varchar(255) DEFAULT NULL,
  `FAMILYNAME_` varchar(255) DEFAULT NULL,
  `BUSINESSEMAIL_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_id_membership` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `USER_` bigint(18) DEFAULT NULL,
  `GROUP_` bigint(18) DEFAULT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_MEM_GROUP` (`GROUP_`),
  KEY `FK_MEM_USER` (`USER_`),
  CONSTRAINT `FK_MEM_USER` FOREIGN KEY (`USER_`) REFERENCES `jbpm4_id_user` (`DBID_`),
  CONSTRAINT `FK_MEM_GROUP` FOREIGN KEY (`GROUP_`) REFERENCES `jbpm4_id_group` (`DBID_`)
) type=InnoDB;


CREATE TABLE `jbpm4_lob` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `BLOB_VALUE_` LONGBLOB DEFAULT NULL,
  `DEPLOYMENT_` bigint(18) DEFAULT NULL,
  `NAME_` text,
  PRIMARY KEY (`DBID_`),
  KEY `FK_LOB_DEPLOYMENT` (`DEPLOYMENT_`),
  CONSTRAINT `FK_LOB_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_`) REFERENCES `jbpm4_deployment` (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_job` (
  `DBID_` bigint(18) NOT NULL,
  `CLASS_` varchar(255) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `DUEDATE_` datetime DEFAULT NULL,
  `STATE_` varchar(255) DEFAULT NULL,
  `ISEXCLUSIVE_` bit(1) DEFAULT NULL,
  `LOCKOWNER_` varchar(255) DEFAULT NULL,
  `LOCKEXPTIME_` datetime DEFAULT NULL,
  `EXCEPTION_` text,
  `RETRIES_` int(11) DEFAULT NULL,
  `PROCESSINSTANCE_` bigint(18) DEFAULT NULL,
  `EXECUTION_` bigint(18) DEFAULT NULL,
  `CFG_` bigint(18) DEFAULT NULL,
  `SIGNAL_` varchar(255) DEFAULT NULL,
  `EVENT_` varchar(255) DEFAULT NULL,
  `REPEAT_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_JOB_CFG` (`CFG_`),
  CONSTRAINT `FK_JOB_CFG` FOREIGN KEY (`CFG_`) REFERENCES `jbpm4_lob` (`DBID_`)
) type=InnoDB;


CREATE TABLE `jbpm4_property` (
  `KEY_` varchar(255) NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `VALUE_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`KEY_`)
) type=InnoDB;

CREATE TABLE `jbpm4_swimlane` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  `ASSIGNEE_` varchar(255) DEFAULT NULL,
  `EXECUTION_` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_SWIMLANE_EXEC` (`EXECUTION_`),
  CONSTRAINT `FK_SWIMLANE_EXEC` FOREIGN KEY (`EXECUTION_`) REFERENCES `jbpm4_execution` (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_task` (
  `DBID_` bigint(18) NOT NULL,
  `CLASS_` char(1) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  `DESCR_` text,
  `STATE_` varchar(255) DEFAULT NULL,
  `SUSPHISTSTATE_` varchar(255) DEFAULT NULL,
  `ASSIGNEE_` varchar(255) DEFAULT NULL,
  `FORM_` varchar(255) DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `CREATE_` datetime DEFAULT NULL,
  `DUEDATE_` datetime DEFAULT NULL,
  `PROGRESS_` int(11) DEFAULT NULL,
  `SIGNALLING_` bit(1) DEFAULT NULL,
  `EXECUTION_ID_` varchar(255) DEFAULT NULL,
  `ACTIVITY_NAME_` varchar(255) DEFAULT NULL,
  `HASVARS_` bit(1) DEFAULT NULL,
  `SUPERTASK_` bigint(18) DEFAULT NULL,
  `EXECUTION_` bigint(18) DEFAULT NULL,
  `PROCINST_` bigint(18) DEFAULT NULL,
  `SWIMLANE_` bigint(18) DEFAULT NULL,
  `TASKDEFNAME_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_TASK_SWIML` (`SWIMLANE_`),
  KEY `FK_TASK_SUPERTASK` (`SUPERTASK_`),
  CONSTRAINT `FK_TASK_SUPERTASK` FOREIGN KEY (`SUPERTASK_`) REFERENCES `jbpm4_task` (`DBID_`),
  CONSTRAINT `FK_TASK_SWIML` FOREIGN KEY (`SWIMLANE_`) REFERENCES `jbpm4_swimlane` (`DBID_`)
) type=InnoDB;

CREATE TABLE `jbpm4_participation` (
  `DBID_` bigint(18) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `GROUPID_` varchar(255) DEFAULT NULL,
  `USERID_` varchar(255) DEFAULT NULL,
  `TYPE_` varchar(255) DEFAULT NULL,
  `TASK_` bigint(18) DEFAULT NULL,
  `SWIMLANE_` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_PART_SWIMLANE` (`SWIMLANE_`),
  KEY `FK_PART_TASK` (`TASK_`),
  CONSTRAINT `FK_PART_TASK` FOREIGN KEY (`TASK_`) REFERENCES `jbpm4_task` (`DBID_`),
  CONSTRAINT `FK_PART_SWIMLANE` FOREIGN KEY (`SWIMLANE_`) REFERENCES `jbpm4_swimlane` (`DBID_`)
) type=InnoDB;



CREATE TABLE `jbpm4_variable` (
  `DBID_` bigint(18) NOT NULL,
  `CLASS_` varchar(255) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `KEY_` varchar(255) DEFAULT NULL,
  `CONVERTER_` varchar(255) DEFAULT NULL,
  `HIST_` bit(1) DEFAULT NULL,
  `EXECUTION_` bigint(18) DEFAULT NULL,
  `TASK_` bigint(18) DEFAULT NULL,
  `LOB_` bigint(18) DEFAULT NULL,
  `DATE_VALUE_` datetime DEFAULT NULL,
  `DOUBLE_VALUE_` double DEFAULT NULL,
  `CLASSNAME_` varchar(255) DEFAULT NULL,
  `LONG_VALUE_` bigint(18) DEFAULT NULL,
  `STRING_VALUE_` varchar(255) DEFAULT NULL,
  `TEXT_VALUE_` text,
  `EXESYS_` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_VAR_LOB` (`LOB_`),
  KEY `FK_VAR_EXECUTION` (`EXECUTION_`),
  KEY `FK_VAR_EXESYS` (`EXESYS_`),
  KEY `FK_VAR_TASK` (`TASK_`),
  CONSTRAINT `FK_VAR_TASK` FOREIGN KEY (`TASK_`) REFERENCES `jbpm4_task` (`DBID_`),
  CONSTRAINT `FK_VAR_EXECUTION` FOREIGN KEY (`EXECUTION_`) REFERENCES `jbpm4_execution` (`DBID_`),
  CONSTRAINT `FK_VAR_EXESYS` FOREIGN KEY (`EXESYS_`) REFERENCES `jbpm4_execution` (`DBID_`),
  CONSTRAINT `FK_VAR_LOB` FOREIGN KEY (`LOB_`) REFERENCES `jbpm4_lob` (`DBID_`)
);

CREATE TABLE `jbpm4_hist_actinst` (
  `DBID_` bigint(18) NOT NULL,
  `CLASS_` varchar(255) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `HPROCI_` bigint(18) DEFAULT NULL,
  `TYPE_` varchar(255) DEFAULT NULL,
  `EXECUTION_` varchar(255) DEFAULT NULL,
  `ACTIVITY_NAME_` varchar(255) DEFAULT NULL,
  `START_` datetime DEFAULT NULL,
  `END_` datetime DEFAULT NULL,
  `DURATION_` bigint(18) DEFAULT NULL,
  `TRANSITION_` varchar(255) DEFAULT NULL,
  `NEXTIDX_` int(11) DEFAULT NULL,
  `HTASK_` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_HACTI_HPROCI` (`HPROCI_`),
  KEY `FK_HTI_HTASK` (`HTASK_`),
  CONSTRAINT `FK_HTI_HTASK` FOREIGN KEY (`HTASK_`) REFERENCES `jbpm4_hist_task` (`DBID_`),
  CONSTRAINT `FK_HACTI_HPROCI` FOREIGN KEY (`HPROCI_`) REFERENCES `jbpm4_hist_procinst` (`DBID_`)
);

CREATE TABLE `jbpm4_hist_detail` (
  `DBID_` bigint(18) NOT NULL,
  `CLASS_` varchar(255) NOT NULL,
  `DBVERSION_` int(11) NOT NULL,
  `USERID_` varchar(255) DEFAULT NULL,
  `TIME_` datetime DEFAULT NULL,
  `HPROCI_` bigint(18) DEFAULT NULL,
  `HPROCIIDX_` int(11) DEFAULT NULL,
  `HACTI_` bigint(18) DEFAULT NULL,
  `HACTIIDX_` int(11) DEFAULT NULL,
  `HTASK_` bigint(18) DEFAULT NULL,
  `HTASKIDX_` int(11) DEFAULT NULL,
  `HVAR_` bigint(18) DEFAULT NULL,
  `HVARIDX_` int(11) DEFAULT NULL,
  `MESSAGE_` text,
  `OLD_STR_` varchar(255) DEFAULT NULL,
  `NEW_STR_` varchar(255) DEFAULT NULL,
  `OLD_INT_` int(11) DEFAULT NULL,
  `NEW_INT_` int(11) DEFAULT NULL,
  `OLD_TIME_` datetime DEFAULT NULL,
  `NEW_TIME_` datetime DEFAULT NULL,
  `PARENT_` bigint(18) DEFAULT NULL,
  `PARENT_IDX_` int(11) DEFAULT NULL,
  PRIMARY KEY (`DBID_`),
  KEY `FK_HDETAIL_HPROCI` (`HPROCI_`),
  KEY `FK_HDETAIL_HACTI` (`HACTI_`),
  KEY `FK_HDETAIL_HTASK` (`HTASK_`),
  KEY `FK_HDETAIL_HVAR` (`HVAR_`),
  CONSTRAINT `FK_HDETAIL_HVAR` FOREIGN KEY (`HVAR_`) REFERENCES `jbpm4_hist_var` (`DBID_`),
  CONSTRAINT `FK_HDETAIL_HACTI` FOREIGN KEY (`HACTI_`) REFERENCES `jbpm4_hist_actinst` (`DBID_`),
  CONSTRAINT `FK_HDETAIL_HPROCI` FOREIGN KEY (`HPROCI_`) REFERENCES `jbpm4_hist_procinst` (`DBID_`),
  CONSTRAINT `FK_HDETAIL_HTASK` FOREIGN KEY (`HTASK_`) REFERENCES `jbpm4_hist_task` (`DBID_`)
);

commit;