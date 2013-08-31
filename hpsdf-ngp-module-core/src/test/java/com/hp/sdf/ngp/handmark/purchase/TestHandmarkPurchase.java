package com.hp.sdf.ngp.handmark.purchase;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.common.constant.HandmarkConstant;
import com.hp.sdf.ngp.handmark.impl.DownloadConnectorHandmarkImpl;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.service.ApplicationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public final class TestHandmarkPurchase extends DBEnablerTestBase {

	@Resource
	DownloadConnectorHandmarkImpl downloadConnectorHandmark;

	@Resource
	private ApplicationService applicationService;

	@Test
	public void testPurchase() {
		String deviceSerial = "1001";

		Asset asset = new Asset();
		asset.setId(1L);

		AssetPrice assetPrice = new AssetPrice();
		assetPrice.setAmount(new BigDecimal(12));
		assetPrice.setCurrency("USD");
		assetPrice.setAsset(asset);
		applicationService.saveAssetPrice(assetPrice);

		try {
			applicationService.addAttribute(asset.getId(), EntityType.ASSET,
					HandmarkConstant.PRODUCT_ID, new Float("135593"));
		} catch (Throwable e) {
			e.printStackTrace();
		}

		try {
			applicationService.addAttribute(asset.getId(), EntityType.ASSET,
					HandmarkConstant.DEVICE_ID, new Float(601));
		} catch (Throwable e) {
			e.printStackTrace();
		}

		String handmarkBinaryUrl = downloadConnectorHandmark
				.retrievedownloadURI(asset.getId(),null, deviceSerial);

		Assert.assertTrue(StringUtils.isNotEmpty(handmarkBinaryUrl));
	}

	public String dataSetFileName() {
		return "/data_init.xml";
	}
}