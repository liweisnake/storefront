create table ASSETPROVIDER (id number(19,0) not null, entityId number(19,0) not null, email varchar2(255 char), externalId varchar2(255 char), settlementRate double precision, commissionRate double precision, firstName varchar2(255 char), lastName varchar2(255 char), streetAddress varchar2(255 char), city varchar2(255 char), phone varchar2(255 char), contractExpireDate timestamp, homePage varchar2(255 char), contractStartDate timestamp, name varchar2(30 char) not null unique, priority number(10,0), country varchar2(255 char), locale varchar2(255 char), source varchar2(255 char), organization varchar2(255 char), STATUSID number(19,0), primary key (id), unique (entityId));
create table ApiKey (id number(19,0) not null, sgName varchar2(100 char) not null, sgPassword varchar2(100 char) not null, description varchar2(512 char), primary key (id));
create table Asset (id number(19,0) not null, entityId number(19,0) not null, currentVersion varchar2(255 char), authorid varchar2(255 char), createDate timestamp not null, updateDate timestamp, brief varchar2(4000 char), downloadCount number(19,0) not null, externalId varchar2(255 char), currentVersionId number(19,0), assetHomePage varchar2(255 char), thumbnailLocation varchar2(255 char), thumbnailMiddleLocation varchar2(255 char), thumbnailBigLocation varchar2(255 char), docUrl varchar2(255 char), averageUserRating double precision, recommendOrder number(19,0), recommendStartDate timestamp, recommendDueDate timestamp, publishDate timestamp, newArrivalDueDate timestamp, recommendUpdateDate timestamp, name varchar2(512 char), source varchar2(255 char), description varchar2(4000 char), STATUSID number(19,0) not null, PROVIDERID number(19,0), PARENTID number(19,0), primary key (id), unique (entityId));
create table AssetBinaryVersion (id number(19,0) not null, entityId number(19,0) not null, createDate timestamp not null, updateDate timestamp not null, brief varchar2(4000 char), externalId varchar2(255 char), thumbnailLocation varchar2(255 char), thumbnailMiddleLocation varchar2(255 char), thumbnailBigLocation varchar2(255 char), recommendOrder number(19,0), recommendStartDate timestamp, recommendDueDate timestamp, publishDate timestamp, newArrivalDueDate timestamp, recommendUpdateDate timestamp, expireDate timestamp, ownerAssetParentId number(19,0), name varchar2(512 char), location varchar2(255 char), fileName varchar2(255 char), fileSize number(19,2), version varchar2(255 char), description varchar2(4000 char), ASSETID number(19,0), STATUSID number(19,0), primary key (id), unique (entityId));
create table AssetCategoryRelation (id number(19,0) not null, CTGID number(19,0) not null, ASSETID number(19,0) not null, VERSIONID number(19,0), primary key (id));
create table AssetLifecycleAction (id number(19,0) not null, comments varchar2(512 char), createDate timestamp, completeDate timestamp, event varchar2(200 char), result varchar2(200 char), submitterid varchar2(512 char) not null, ownerid varchar2(512 char) not null, processStatus varchar2(512 char), commentType varchar2(255 char), notificationDate timestamp, description varchar2(512 char), ASSETID number(19,0) not null, POSTSTATUSID number(19,0), ASSETBINARYVERSIONID number(19,0), PRESTATUSID number(19,0), primary key (id));
create table AssetLifecycleActionHistory (id number(19,0) not null, comments varchar2(255 char), createDate timestamp, externalId varchar2(255 char), completeDate timestamp, event varchar2(255 char), result varchar2(255 char), postStatus varchar2(255 char), processStatus varchar2(255 char), commentType varchar2(255 char), notificationDate timestamp, submitterId varchar2(255 char), ownerId varchar2(255 char), assetBinaryVersionId number(19,0), assetId number(19,0), prestatus varchar2(255 char), source varchar2(255 char), version varchar2(255 char), description varchar2(512 char), primary key (id));
create table AssetLifecycleCommentType (id number(19,0) not null, commentType varchar2(255 char), commentTemplate varchar2(255 char), primary key (id));
create table AssetPlatformRelation (id number(19,0) not null, PLATFORMID number(19,0) not null, ASSETID number(19,0) not null, primary key (id));
create table AssetPrice (id number(19,0) not null, amount number(19,2), currency varchar2(255 char), ASSETID number(19,0) not null, VERSIONID number(19,0), primary key (id));
create table AssetRating (id number(19,0) not null, createDate timestamp not null, userid varchar2(128 char) not null, rating double precision not null, description varchar2(512 char), ASSETID number(19,0) not null, primary key (id));
create table AssetRestrictionRelation (id number(19,0) not null, VERSIONID number(19,0), ASSETID number(19,0) not null, RESTRICTIONID number(19,0) not null, primary key (id));
create table AssetTagRelation (id number(19,0) not null, VERSIONID number(19,0), ASSETID number(19,0) not null, TAGID number(19,0) not null, primary key (id));
create table Attribute (attributeID number(19,0) not null, attributeName varchar2(100 char) not null unique, attributeType number(10,0) not null, attributeDesc varchar2(225 char), primary key (attributeID));
create table AttributeValueChar (attributeValueID number(19,0) not null, entityId number(19,0) not null, value varchar2(2000 char) not null, attributeID number(19,0), primary key (attributeValueID));
create table AttributeValueDate (attributeValueID number(19,0) not null, entityId number(19,0) not null, value timestamp not null, attributeID number(19,0), primary key (attributeValueID));
create table AttributeValueDomain (id number(19,0) not null, attributeValueID number(19,0) not null, isDefault number(1,0) not null, ATTRIBUTEID number(19,0), primary key (id));
create table AttributeValueNumber (attributeValueID number(19,0) not null, entityId number(19,0) not null, value float not null, attributeID number(19,0), primary key (attributeValueID));
create table BinaryFile (id number(19,0) not null, fileLocation varchar2(255 char) not null unique, fileBinary blob not null, primary key (id));
create table Category (id number(19,0) not null, externalId varchar2(255 char), name varchar2(100 char) not null, displayName varchar2(255 char), source varchar2(255 char), description varchar2(512 char), PARENTID number(19,0), primary key (id));
create table Comments (id number(19,0) not null, createDate timestamp not null, updateDate timestamp, externalId varchar2(255 char), userid varchar2(128 char), assetVersion varchar2(255 char), content varchar2(1000 char), title varchar2(255 char), ASSETID number(19,0) not null, primary key (id));
create table CommentsSensorWord (id number(19,0) not null, updateDate timestamp, sensorWord varchar2(255 char) unique, operatorId varchar2(255 char), primary key (id));
create table ContentProviderOperator (id number(19,0) not null, userid varchar2(255 char), ASSETPROVIDERID number(19,0) not null, primary key (id));
create table Country (id number(19,0) not null, name varchar2(100 char) not null, primary key (id));
create table Entity (entityID number(19,0) not null, entityType number(10,0) not null, primary key (entityID));
create table EntityAttribute (id number(19,0) not null, attributeType number(10,0) not null, attributeValueID number(19,0) not null, ENTITYID number(19,0), primary key (id));
create table Language (id number(19,0) not null, name varchar2(100 char) not null, locale varchar2(20 char), primary key (id));
create table Operation (id number(19,0) not null, rate varchar2(100 char), name varchar2(100 char) not null, SERVICEID number(19,0), primary key (id));
create table ParentAssetVersionSummary (id number(19,0) not null, recommendOrder number(19,0), publishDate timestamp, parentAssetId number(19,0), publishFlag varchar2(255 char), newArrivalFlag number(19,0), recommendFlag number(19,0), saleFlag number(19,0), downloadTime number(19,0), saleStart timestamp, saleEnd timestamp, lowestPrice double precision, priceEqualFlg number(19,0), sampleFlag number(19,0), primary key (id), unique (parentAssetId));
create table Platform (id number(19,0) not null, name varchar2(100 char) not null unique, description varchar2(512 char), primary key (id));
create table PurchaseHistory (id number(19,0) not null, userid varchar2(255 char), purchaseDate timestamp, paidPrice number(19,2), transactionid varchar2(255 char), status varchar2(255 char), currency varchar2(255 char), ASSETID number(19,0) not null, primary key (id));
create table RestrictedType (id number(19,0) not null, type varchar2(50 char) not null unique, primary key (id));
create table RoleCategory (id number(19,0) not null, createDate timestamp, roleName varchar2(255 char) not null unique, displayName varchar2(255 char), description varchar2(512 char), primary key (id));
create table ScreenShots (id number(19,0) not null, createDate timestamp, mediaType varchar2(255 char), storeLocation varchar2(255 char), description varchar2(255 char), sequence number(19,0), VERSIONID number(19,0), ASSETID number(19,0) not null, primary key (id));
create table Service (id number(19,0) not null, docUrl varchar2(512 char), sdkUrl varchar2(512 char), serviceid varchar2(256 char), brokerServiceName varchar2(255 char), accessInterface varchar2(255 char), brokerServiceAuthType varchar2(255 char), brokerServiceUrl varchar2(2048 char), name varchar2(100 char) not null unique, type varchar2(100 char), description varchar2(512 char), primary key (id));
create table ServiceSubscription (id number(19,0) not null, createDate timestamp, userid varchar2(128 char), SERVICEID number(19,0), APIKEYID number(19,0) not null, primary key (id));
create table ShoppingCart (id number(19,0) not null, createDate timestamp, userid varchar2(255 char), itemPrice number(19,2), status varchar2(255 char), currency varchar2(255 char), ASSETID number(19,0) not null, primary key (id));
create table Status (id number(19,0) not null, type number(19,0), displayName varchar2(255 char), status varchar2(30 char) not null unique, primary key (id));
create table SubscriberProfile (userId varchar2(255 char) not null, entityId number(19,0) not null, ownerTesterUserId varchar2(255 char), msisdn varchar2(255 char) not null, clientTesterFlag number(19,0), clientOwnerProviderId number(19,0), displayName varchar2(255 char), primary key (userId), unique (entityId));
create table SystemConfig (id number(19,0) not null, configKey varchar2(255 char), value varchar2(255 char), primary key (id));
create table Tag (id number(19,0) not null, name varchar2(100 char) not null unique, description varchar2(512 char), primary key (id));
create table UserDownloadHistory (id number(19,0) not null, userid varchar2(255 char), downloadDate timestamp not null, version varchar2(255 char), STATUS varchar2(400 char) not null, ASSETID number(19,0) not null, primary key (id));
create table UserLifecycleAction (id number(19,0) not null, comments varchar2(512 char), createDate timestamp not null, event varchar2(255 char), submitterid varchar2(512 char), ownerid varchar2(512 char), processStatus varchar2(512 char), userid varchar2(255 char) not null, preRole varchar2(255 char), postRole varchar2(255 char), description varchar2(512 char), primary key (id));
create table UserLifecycleActionHistory (id number(19,0) not null, comments varchar2(512 char), createDate timestamp not null, event varchar2(255 char), result varchar2(255 char), submitterid varchar2(512 char), ownerid varchar2(512 char), processStatus varchar2(512 char), userid varchar2(255 char) not null, preRole varchar2(255 char), postRole varchar2(255 char), description varchar2(512 char), primary key (id));
create table UserProfile (userid varchar2(128 char) not null, entityId number(19,0) not null, email varchar2(256 char) not null, createDate timestamp, idcard varchar2(50 char), gender varchar2(10 char), cellphone varchar2(50 char), company varchar2(100 char), zip varchar2(50 char), birthday timestamp, firstname varchar2(50 char), lastname varchar2(50 char), preferRole varchar2(50 char), verificationCode varchar2(50 char), onlineTime number(19,0), disabled number(1,0), disabledDate timestamp, address varchar2(256 char), password varchar2(50 char), COUNTRYID number(19,0), LANGUAGEID number(19,0), primary key (userid), unique (entityId));
create table UserRoleCategory (id number(19,0) not null, USERID varchar2(128 char) not null, ROLEID number(19,0) not null, primary key (id));
alter table ASSETPROVIDER add constraint FKC60D18C1B8A7CE0D foreign key (STATUSID) references Status;
alter table Asset add constraint FK3C9FAD0A3503EE7 foreign key (PARENTID) references Asset;
alter table Asset add constraint FK3C9FAD0B8A7CE0D foreign key (STATUSID) references Status;
alter table Asset add constraint FK3C9FAD0A1DF8ACB foreign key (PROVIDERID) references ASSETPROVIDER;
alter table AssetBinaryVersion add constraint FKDC29CA7B8A7CE0D foreign key (STATUSID) references Status;
alter table AssetBinaryVersion add constraint FKDC29CA73342A7AD foreign key (ASSETID) references Asset;
alter table AssetCategoryRelation add constraint FKAD99DC0AD92D48BD foreign key (CTGID) references Category;
alter table AssetCategoryRelation add constraint FKAD99DC0AD936A508 foreign key (VERSIONID) references AssetBinaryVersion;
alter table AssetCategoryRelation add constraint FKAD99DC0A3342A7AD foreign key (ASSETID) references Asset;
alter table AssetLifecycleAction add constraint FK74B45D50246A2DB0 foreign key (PRESTATUSID) references Status;
alter table AssetLifecycleAction add constraint FK74B45D509189C6AD foreign key (POSTSTATUSID) references Status;
alter table AssetLifecycleAction add constraint FK74B45D508CD96137 foreign key (ASSETBINARYVERSIONID) references AssetBinaryVersion;
alter table AssetLifecycleAction add constraint FK74B45D503342A7AD foreign key (ASSETID) references Asset;
alter table AssetPlatformRelation add constraint FK39C0427FB643B74F foreign key (PLATFORMID) references Platform;
alter table AssetPlatformRelation add constraint FK39C0427F3342A7AD foreign key (ASSETID) references Asset;
alter table AssetPrice add constraint FKF4EF0859D936A508 foreign key (VERSIONID) references AssetBinaryVersion;
alter table AssetPrice add constraint FKF4EF08593342A7AD foreign key (ASSETID) references Asset;
alter table AssetRating add constraint FKAB713C8D3342A7AD foreign key (ASSETID) references Asset;
alter table AssetRestrictionRelation add constraint FK83A9A0F81662ABAA foreign key (RESTRICTIONID) references RestrictedType;
alter table AssetRestrictionRelation add constraint FK83A9A0F8D936A508 foreign key (VERSIONID) references AssetBinaryVersion;
alter table AssetRestrictionRelation add constraint FK83A9A0F83342A7AD foreign key (ASSETID) references Asset;
alter table AssetTagRelation add constraint FKA5A1AEC6D936A508 foreign key (VERSIONID) references AssetBinaryVersion;
alter table AssetTagRelation add constraint FKA5A1AEC67A465301 foreign key (TAGID) references Tag;
alter table AssetTagRelation add constraint FKA5A1AEC63342A7AD foreign key (ASSETID) references Asset;
alter table AttributeValueChar add constraint FK4882D94BF71AB0F1 foreign key (attributeID) references Attribute;
alter table AttributeValueDate add constraint FK488335A3F71AB0F1 foreign key (attributeID) references Attribute;
alter table AttributeValueDomain add constraint FK354E7179F71AB0F1 foreign key (ATTRIBUTEID) references Attribute;
alter table AttributeValueNumber add constraint FK46B377FEF71AB0F1 foreign key (attributeID) references Attribute;
alter table Category add constraint FK6DD211E198F7A51 foreign key (PARENTID) references Category;
alter table Comments add constraint FKDFF242143342A7AD foreign key (ASSETID) references Asset;
alter table ContentProviderOperator add constraint FK456A502E90B890B6 foreign key (userid) references UserProfile;
alter table ContentProviderOperator add constraint FK456A502ECA3789BB foreign key (ASSETPROVIDERID) references ASSETPROVIDER;
alter table EntityAttribute add constraint FKE73D4DD9FF77F743 foreign key (ENTITYID) references Entity;
alter table Operation add constraint FKDA8CF547B37B1D77 foreign key (SERVICEID) references Service;
alter table PurchaseHistory add constraint FKBCC3BC333342A7AD foreign key (ASSETID) references Asset;
alter table ScreenShots add constraint FK6029DF8DD936A508 foreign key (VERSIONID) references AssetBinaryVersion;
alter table ScreenShots add constraint FK6029DF8D3342A7AD foreign key (ASSETID) references Asset;
alter table ServiceSubscription add constraint FK8C43D452856DE953 foreign key (APIKEYID) references ApiKey;
alter table ServiceSubscription add constraint FK8C43D452B37B1D77 foreign key (SERVICEID) references Service;
alter table ShoppingCart add constraint FKABBC40C83342A7AD foreign key (ASSETID) references Asset;
alter table UserDownloadHistory add constraint FKF073AF813342A7AD foreign key (ASSETID) references Asset;
alter table UserProfile add constraint FK3EFA133E65CEE7D9 foreign key (LANGUAGEID) references Language;
alter table UserProfile add constraint FK3EFA133ED2818AF9 foreign key (COUNTRYID) references Country;
alter table UserRoleCategory add constraint FK30C1F71F90B890B6 foreign key (USERID) references UserProfile;
alter table UserRoleCategory add constraint FK30C1F71F529686B3 foreign key (ROLEID) references RoleCategory;
create sequence hibernate_sequence;