DELIMITER $$

DROP PROCEDURE IF EXISTS `storefront`.`updateVersionSummary` $$
CREATE PROCEDURE `storefront`.`updateVersionSummary` ()
BEGIN

DECLARE done INT;
DECLARE asset_id LONG;
DECLARE publish_flag VARCHAR(50);
DECLARE cur_asset CURSOR FOR SELECT id, publishflg FROM assetbinaryversion;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

OPEN cur_asset;


REPEAT
  FETCH cur_asset INTO asset_id, publish_flag;
  IF asset_id is not null THEN
    CALL recordProcessor(asset_id, publish_flag);
  END IF;
  UNTIL done
END REPEAT;

/*
WHILE NOT done
  FETCH cur_asset INTO asset_id;
  CALL recordProcessor(asset_id);
END WHILE;
*/
CLOSE cur_asset;

END $$

DELIMITER ;