DELIMITER $$

DROP PROCEDURE IF EXISTS `storefront`.`recordProcessor` $$
CREATE PROCEDURE `storefront`.`recordProcessor` (IN asset_id LONG, IN publish_flag VARCHAR(50))
BEGIN


  DECLARE result_parentAssetId LONG;
  DECLARE result_publishFlag VARCHAR(50);
  DECLARE result_newArrivalFlag, result_recommendFlag, result_saleFlag, result_downloadTime INT;
  DECLARE result_publishDate,result_saleStart,result_saleEnd TIMESTAMP;
  DECLARE result_lowestPrice DECIMAL;
  DECLARE result_priceEqualFlag,result_recommendOrder,result_sampleFlag INT;

  DECLARE temp_count INT;

  DECLARE temp_str VARCHAR(100);

  /*
  Set parent asset id
  */
  SET result_parentAssetId = asset_id;

  /*
  Set publish flag
  */
  select attrchar.value into result_publishFlag from attribute attr, entityattribute ea, attributevaluechar attrchar, assetbinaryversion bv where attr.attributeName = 'publishflg' and attrchar.attributeID = attr.attributeID and ea.attributeValueID = attrchar.attributeValueID and ea.ENTITYID = bv.entityId and bv.ownerassetparentid = result_parentAssetId;

  /*
  Set new arrival flag
  */
  select count(*) from assetbinaryversion bv where bv.ownerassetparentid = result_parentAssetId and DATE(bv.newarrivalduedate) >= CURRENT_DATE();

  IF temp_count > 0 THEN
    SET result_newArrivalFlag = 1;
  ELSE
    SET result_newArrivalFlag = 0;
  END IF;

  /*
  Set recommend flag
  */
  select count(*) into temp_count from assetbinaryversion bv where bv.ownerassetparentid = result_parentAssetId and DATE(bv.recommendstartdate) < CURRENT_DATE() and CURRENT_DATE() <= DATE(bv.recommendduedate);

  IF temp_count > 0 THEN
    SET result_recommendFlag = 1;
  ELSE
    SET result_recommendFlag = 0;
  END IF;

  /*
  Set sale flag
  */
  select count(*) into temp_count from assetbinaryversion bv, attribute attr1, entityattribute ea1, attributevaluedate attrdate1,attribute attr2, entityattribute ea2, attributevaluedate attrdate2 where bv.ownerassetparentid = result_parentAssetId and attr1.attributeName = 'campaignstartdate' and attrdate1.attributeID = attr1.attributeID and ea1.attributeValueID = attrdate1.attributeValueID and ea1.ENTITYID = bv.entityId and attr2.attributeName = 'campaignenddate' and attrdate2.attributeID = attr2.attributeID and ea2.attributeValueID = attrdate2.attributeValueID and ea2.ENTITYID = bv.entityId and (DATE(attrdate1.value) <= CURRENT_DATE() or attrdate1.value is null) and (CURRENT_DATE() <= DATE(attrdate2.value) or attrdate2.value is null);

  IF temp_count > 0 THEN
    SET result_saleFlag = 1;
  ELSE
    SET result_saleFlag = 0;
  END IF;


  /*
  Set recommendorder & publishdate
  */
  select min(bv.recommendorder), max(bv.publishdate) into result_recommendOrder, result_publishDate from assetbinaryversion bv where bv.ownerassetparentid = result_parentAssetId;


  /*
  Set download time
  */
  select distinct count(*) from userdownloadhistory where userdownloadhistory.downloaddate + INTERVAL 7 DAY > Now() and userdownloadhistory.assetid in (select asset.id from asset where userdownloadhistory.assetid = 1 or userdownloadhistory.assetid = asset.id and asset.parentid = result_parentAssetId)select distinct count(*) from userdownloadhistory where userdownloadhistory.downloaddate + INTERVAL 7 DAY > Now() and userdownloadhistory.assetid in (select asset.id from asset where userdownloadhistory.assetid = result_parentAssetId or userdownloadhistory.assetid = asset.id and asset.parentid = result_parentAssetId)

  /*
  Set sale start
  */
  select max(attrdate.value) into result_saleStart from attribute attr, entityattribute ea, attributevaluechar attrdate, assetbinaryversion bv where attr.attributeName = 'campaignstartdate' and attrdate.attributeID = attr.attributeID and ea.attributeValueID = attrdate.attributeValueID and ea.ENTITYID = bv.entityId and bv.ownerassetparentid = result_parentAssetId;

  /*
  Set sale end
  */
  select min(attrdate.value) into result_saleEnd from attribute attr, entityattribute ea, attributevaluechar attrdate, assetbinaryversion bv where attr.attributeName = 'campaignenddate' and attrdate.attributeID = attr.attributeID and ea.attributeValueID = attrdate.attributeValueID and ea.ENTITYID = bv.entityId and bv.ownerassetparentid = result_parentAssetId;

  /*
  Set lowest price
  */

  select min(attrnumber.value) into result_lowestPrice from assetbinaryversion bv, attribute attr, entityattribute ea, attributevaluenumber attrnumber, attribute attr1, entityattribute ea1, attributevaluedate attrdate1, attribute attr2, entityattribute ea2, attributevaluedate attrdate2 where attrnumber.attributeID = attr.attributeID and ea.attributeValueID = attrnumber.attributeValueID and ea.ENTITYID = bv.entityId and bv.ownerassetparentid = result_parentAssetId and attr1.attributeName = 'campaignstartdate' and attrdate1.attributeID = attr1.attributeID and ea1.attributeValueID = attrdate1.attributeValueID and ea1.ENTITYID = bv.entityId and attr2.attributeName = 'campaignenddate' and attrdate2.attributeID = attr2.attributeID and ea2.attributeValueID = attrdate2.attributeValueID and ea2.ENTITYID = bv.entityId and ((DATE(attrdate1.value) <= CURRENT_DATE() or attrdate1.value is null) and (CURRENT_DATE() <= DATE(attrdate2.value) or attrdate2.value is null) and attr.attributeName = 'saleprice') or ((DATE(attrdate1.value) > CURRENT_DATE() or attrdate1.value is null) and (CURRENT_DATE() > DATE(attrdate2.value) or attrdate2.value is null) and attr.attributeName = 'price') group by attrnumber.value;

  /*
  Set price equal flag
  */
  select count(*) into temp_count from assetbinaryversion bv, attribute attr1, entityattribute ea1, attributevaluenumber attrnumber1, attribute attr2, entityattribute ea2, attributevaluenumber attrnumber2, attribute attr3, entityattribute ea3, attributevaluedate attrdate3, attribute attr4, entityattribute ea4, attributevaluedate attrdate4 where attr1.attributeName = temp_str and attrnumber1.attributeID = attr1.attributeID and ea1.attributeValueID = attrnumber1.attributeValueID and ea1.ENTITYID = bv.entityId and attr2.attributeName = temp_str and attrnumber2.attributeID = attr2.attributeID and ea2.attributeValueID = attrnumber2.attributeValueID and ea2.ENTITYID = bv.entityId and attrnumber1.value <> attrnumber2.value and bv.ownerassetparentid = result_parentAssetId and attr3.attributeName = 'campaignstartdate' and attrdate3.attributeID = attr3.attributeID and ea3.attributeValueID = attrdate3.attributeValueID and ea3.ENTITYID = bv.entityId and attr4.attributeName = 'campaignenddate' and attrdate4.attributeID = attr4.attributeID and ea4.attributeValueID = attrdate4.attributeValueID and ea4.ENTITYID = bv.entityId and ((DATE(attrdate3.value) <= CURRENT_DATE() or attrdate3.value is null) and (CURRENT_DATE() <= DATE(attrdate4.value) or attrdate4.value is null) and attr1.attributeName = 'saleprice') or (CURRENT_DATE() <= DATE(attrdate4.value) or attrdate4.value is null) and attr2.attributeName = 'saleprice') or ((DATE(attrdate3.value) > CURRENT_DATE() or attrdate3.value is null) and (CURRENT_DATE() > DATE(attrdate4.value) or attrdate4.value is null) and attr1.attributeName = 'price') or ((DATE(attrdate3.value) > CURRENT_DATE() or attrdate3.value is null) and (CURRENT_DATE() > DATE(attrdate4.value) or attrdate4.value is null) and attr2.attributeName = 'price');

  IF temp_count > 0 THEN
    SET result_priceEqualFlag = 0;
  ELSE
    SET result_priceEqualFlag = 1;
  END IF;

  /*
  Set sample flag
  */
  select distinct count(*) into temp_count from attribute attr, entityattribute ea, attributevaluechar attrchar, assetbinaryversion bv where attr.attributeName = 'sampledownloadurl' and attrchar.value is not null and attrchar.attributeID = attr.attributeID and ea.attributeValueID = attrchar.attributeValueID and ea.ENTITYID = bv.entityId and bv.ownerassetparentid = result_parentAssetId;

  IF temp_count > 0 THEN
    SET result_sampleFlag = 1;
  ELSE
    SET result_sampleFlag = 0;
  END IF;

  /*
  Insert or update
  */
  select count(*) into temp_count from parentassetversionsummary summary where summary.parentassetid = result_parentAssetId;

  IF temp_count > 0 THEN
    UPDATE parentassetversionsummary summary SET summary.publishflag = result_publishFlag, summary.newarrivalflag = result_newArrivalFlag, summary.recommendflag = result_recommendFlag, summary.saleflag = result_saleFlag, summary.downloadtime = result_downloadTime, summary.publishdate = result_publishDate, summary.salestart = result_saleStart, summary.saleend = result_saleEnd, summary.lowestprice = result_lowestPrice, summary.priceEqualFlg = result_priceEqualFlag, summary.recommendorder = result_recommendOrder, summary.sampleflag = result_sampleFlag WHERE summary.parentassetid=result_parentAssetId and summary.publishflg=publish_flag;
  ELSE
    INSERT INTO parentassetversionsummary(parentassetid, publishflag, newarrivalflag, recommendflag, saleflag, downloadtime, publishdate, salestart, saleend, lowestprice, priceequalflg, recommendorder, sampleflag) VALUES(result_parentAssetId, result_publishFlag, result_newArrivalFlag, result_recommendFlag, result_saleFlag, result_downloadTime, result_publishDate, result_saleStart, result_saleEnd, result_lowestPrice, result_priceEqualFlag, result_recommendOrder, result_sampleFlag);
  END IF;


END $$

DELIMITER ;