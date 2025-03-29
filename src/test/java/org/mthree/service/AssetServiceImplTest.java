
package org.mthree.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mthree.dao.AssetDao;
import org.mthree.dto.Asset;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetDao assetDao;

    @InjectMocks
    private AssetServiceImpl assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAssets() {
        Asset asset1 = new Asset(1, 101, "Stock", new BigDecimal("1000"));
        Asset asset2 = new Asset(2, 102, "Bond", new BigDecimal("500"));
        List<Asset> assets = Arrays.asList(asset1, asset2);

        when(assetDao.getAllAssets()).thenReturn(assets);

        List<Asset> result = assetService.getAllAssets();

        assertEquals(2, result.size());
        assertEquals("Stock", result.get(0).getDescription());
        verify(assetDao, times(1)).getAllAssets();
    }

    @Test
    void testGetAssetById_Found() {
        Asset asset = new Asset(1, 101, "Stock", new BigDecimal("1000"));
        when(assetDao.findAssetById(1)).thenReturn(asset);

        Asset result = assetService.getAssetById(1);

        assertNotNull(result);
        assertEquals("Stock", result.getDescription());
        verify(assetDao, times(1)).findAssetById(1);
    }

    @Test
    void testGetAssetById_NotFound() {
        int assetId = 999; // Non-existent asset ID

        when(assetDao.findAssetById(assetId)).thenThrow(new DataAccessException("Asset Not Found") {});

        Asset result = assetService.getAssetById(assetId);

        assertNotNull(result);
        assertEquals("Asset Not Found", result.getDescription());
        assertNull(result.getValue()); // Value should be null as per service implementation
    }


    @Test
    void testAddNewAsset_Valid() {
        Asset asset = new Asset(0, 101, "Stock", new BigDecimal("1000"));
        when(assetDao.createNewAsset(asset)).thenReturn(asset);

        Asset result = assetService.addNewAsset(asset);

        assertNotNull(result);
        assertEquals("Stock", result.getDescription());
        verify(assetDao, times(1)).createNewAsset(asset);
    }

    @Test
    void testAddNewAsset_Invalid() {
        Asset invalidAsset = new Asset(0, 0, "", null);

        Asset result = assetService.addNewAsset(invalidAsset);

        assertEquals("Invalid input: user_id, value, or description missing/invalid", result.getDescription());
        verify(assetDao, never()).createNewAsset(any());
    }
}