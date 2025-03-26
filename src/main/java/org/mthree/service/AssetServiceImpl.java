package org.mthree.service;

import org.mthree.dao.AssetDao;
import org.mthree.dto.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetDao assetDao;

    public AssetServiceImpl(AssetDao assetDao) {
        this.assetDao = assetDao;
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetDao.getAllAssets();
    }

    @Override
    public Asset getAssetById(int id) {
        try {
            return assetDao.findAssetById(id);
        } catch (DataAccessException ex) {
            Asset asset = new Asset();
            asset.setDescription("Asset Not Found");
            asset.setValue(null);
            return asset;
        }
    }

    @Override
    public Asset addNewAsset(Asset asset) {
        if (asset == null) {
            return null;
        }
        if (asset.getUserId() <= 0 || asset.getValue() == null || asset.getValue().signum() < 0 ||
                asset.getDescription() == null || asset.getDescription().trim().isEmpty()) {
            asset.setDescription("Invalid input: user_id, value, or description missing/invalid");
            asset.setValue(null);
            return asset;
        }
        return assetDao.createNewAsset(asset);
    }

    @Override
    public Asset updateAsset(int id, Asset asset) {
        if (asset == null) {
            return null;
        }
        if (id != asset.getId()) {
            asset.setDescription("IDs do not match, asset not updated");
            return asset;
        }
        Asset existing = assetDao.findAssetById(id);
        if (existing == null) {
            asset.setDescription("Asset not found, update failed");
            return asset;
        }
        assetDao.updateAsset(asset);
        return asset;
    }

    @Override
    public void deleteAssetById(int id) {
        Asset asset = assetDao.findAssetById(id);
        if (asset != null) {
            assetDao.deleteAsset(id);
            System.out.println("Asset ID: " + id + " deleted");
        } else {
            System.out.println("Asset ID: " + id + " not found, deletion skipped");
        }
    }
}