package ir.utopia.common;

public class CommonEnums {

	/**
	 * @author Arsalani
	 * used this enum in Asset and Inventory 
	 */
	public enum AssetType {
		ground,realEstate,medicalDevice,books,vehicle,utilitiesAndTools,assetInUsableSentence,
		cameraAndParaphernalia,furnishing,carpet,intangibleAsset;
	}
	
	/**
	 * @author Arsalani
	 * used this enum in Inventory and Asset
	 */
	public enum AssetStatus {
		noConfirmation,aminConfirm,aminReject,managerConfirm,managerReject;
	}
}
