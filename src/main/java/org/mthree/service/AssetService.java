package org.mthree.service;

import org.mthree.dto.Asset;
import java.util.List;

public interface AssetService {
    List<Asset> getAllAssets();

    Asset getAssetById(int id);

    Asset addNewAsset(Asset asset);

    Asset updateAsset(int id, Asset asset);

    void deleteAssetById(int id);
}