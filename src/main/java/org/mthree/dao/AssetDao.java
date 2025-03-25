package org.mthree.dao;


import org.mthree.dto.Asset;
import java.util.List;

public interface AssetDao {

    Asset createNewAsset(Asset asset);

    List<Asset> getAllAssets();

    Asset findAssetById(int id);

    void updateAsset(Asset asset);

    void deleteAsset(int id);



}
