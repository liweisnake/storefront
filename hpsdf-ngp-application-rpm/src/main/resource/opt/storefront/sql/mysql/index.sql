ALTER TABLE `ParentAssetVersionSummary` ADD INDEX publishFlag ( `publishFlag` );
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX newArrivalFlag ( `newArrivalFlag` );
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX recommendFlag ( `recommendFlag` );
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX saleFlag ( `saleFlag` );
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX downloadTime ( `downloadTime` );
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX saleStart ( `saleStart` );
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX saleEnd ( `saleEnd` );		
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX lowestPrice ( `lowestPrice` );		
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX priceEqualFlg ( `priceEqualFlg` );		
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX recommendOrder ( `recommendOrder` );		
ALTER TABLE `ParentAssetVersionSummary` ADD INDEX sampleFlag ( `sampleFlag` );

ALTER TABLE `attributevaluechar` ADD INDEX value ( `value`(255));
ALTER TABLE `attributevaluechar` ADD INDEX entityId ( `entityId` );
ALTER TABLE `attributevaluedate` ADD INDEX value ( `value` );
ALTER TABLE `attributevaluedate` ADD INDEX entityId ( `entityId` );
ALTER TABLE `attributevaluenumber` ADD INDEX value ( `value` );
ALTER TABLE `attributevaluenumber` ADD INDEX entityId ( `entityId` );

ALTER TABLE `assetbinaryversion` ADD INDEX name  ( `name`(255));
ALTER TABLE `assetbinaryversion` ADD INDEX externalId ( `externalId` );
ALTER TABLE `assetbinaryversion` ADD INDEX version ( `version` );
ALTER TABLE `assetbinaryversion` ADD INDEX updateDate ( `updateDate` );
ALTER TABLE `assetbinaryversion` ADD INDEX publishDate ( `publishDate` );
ALTER TABLE `assetbinaryversion` ADD INDEX expireDate ( `expireDate` );
ALTER TABLE `assetbinaryversion` ADD INDEX recommendOrder ( `recommendOrder` );
ALTER TABLE `assetbinaryversion` ADD INDEX recommendStartDate ( `recommendStartDate` );
ALTER TABLE `assetbinaryversion` ADD INDEX recommendDueDate ( `recommendDueDate` );
ALTER TABLE `assetbinaryversion` ADD INDEX ownerAssetParentId ( `ownerAssetParentId` );
ALTER TABLE `assetbinaryversion` ADD INDEX newArrivalDueDate ( `newArrivalDueDate` );

ALTER TABLE `asset` ADD INDEX name ( `name`(255));
ALTER TABLE `asset` ADD INDEX externalId ( `externalId` );
ALTER TABLE `asset` ADD INDEX currentVersionId ( `currentVersionId` );
ALTER TABLE `asset` ADD INDEX publishDate ( `publishDate` );

ALTER TABLE `asset` ADD INDEX recommendOrder ( `recommendOrder` );
ALTER TABLE `asset` ADD INDEX recommendStartDate ( `recommendStartDate` );
ALTER TABLE `asset` ADD INDEX recommendDueDate ( `recommendDueDate` );
ALTER TABLE `asset` ADD INDEX newArrivalDueDate ( `newArrivalDueDate` );

ALTER TABLE `purchasehistoryextend` ADD INDEX reqconfirmDate ( `reqconfirmDate` );

commit;
